package com.fungsiutama;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import koneksi.Daftar_String;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class List_DataSppd extends AppCompatActivity {
	private static final String TAG = "DaftarSPTSPPD";
	private ListView listView;
	private List<Daftar_String> list;
    private ProgressDialog loading;
	private TextView txt_jumlah_sppd;
	private TextView txt_sppd_selesai;
	private TextView txt_jumlah_laporan;
	private int totalSppd = 0;
	private int sppdSelesai = 0;
	private int sppdLaporan = 0;
	String transfer_nip = null;
	String nipLokal, totalsppd;
	RelativeLayout laylistrecent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list_datasppd);

		laylistrecent 	= findViewById(R.id.laylistrecent);
		listView 		= findViewById(R.id.list_daftar_sppd);
		list 			= new ArrayList<>();

		txt_jumlah_sppd     = findViewById(R.id.txt_jumlah_sppd);
		txt_sppd_selesai    = findViewById(R.id.txt_sppd_selesai);
		txt_jumlah_laporan  = findViewById(R.id.txt_jumlah_laporan);

		Bundle b = getIntent().getExtras();

		if (b != null) {
			transfer_nip = b.getString("transfer_nip");
		}
		nipLokal = transfer_nip;

		if (!terkoneksi_roaming(List_DataSppd.this)) {
			String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			showPesanKoneksi(pesan);
		}else{
			new Load_Data().execute();
		}


	}

	private void showPesanSnackbar(String message) {
		View rootView = findViewById(android.R.id.content);
		Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
				.setAction("OK", v -> {})
				.show();
	}

	public void postingAllSPPD(View view) {
		if (totalsppd.equals("0")){
			showPesanSnackbar("Tidak ada data sppd.");
		}else{
			String pesan = "Pastikan laporan sudah dibuat dan\nber status ✅ [SUDAH]\n\nData yang sudah dikirim tidak ditampilkan lagi di list.";
			showPesanPostingAll(pesan);
		}
	}

	@SuppressLint("StaticFieldLeak")
    private class Load_Data extends AsyncTask<Void, Void, String> {

		Java_Connection jc = new Java_Connection();

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(List_DataSppd.this);
			loading.setMessage("Sedang memuat data sppd...");
			loading.setIndeterminate(false);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(Void... voids) {

			String nip = nipLokal.trim();

			try {
				String nipEncoded = URLEncoder.encode(nip, "UTF-8");

				String urlList = Koneksi.list_sptsppd
						+ "?nip_pegawai=" + nipEncoded;

				String urlPosting = Koneksi.count_sptsppd
						+ "?nip_pegawai=" + nipEncoded;

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

					String responseList = jc.sendGetRequest(urlList);
					Log.e("RAW_RESPONSE", responseList);
					String responseStatistik = jc.sendGetRequest(urlPosting);

					if (responseList == null) {
						return "Server tidak merespon";
					}

					if (responseStatistik != null) {
						proses_pengambilan_data_statistik(responseStatistik);
					}

					list  = proses_pengambilan_data(responseList);
				}

			} catch (Exception e) {
				Log.e("Load_Database", "ERROR", e);
				return e.toString();
			}

			return null;
		}

		@RequiresApi(api = Build.VERSION_CODES.O)
        @Override
		protected void onPostExecute(String result) {
			loading.dismiss();
			txt_jumlah_sppd.setText(String.valueOf(totalSppd));
			txt_sppd_selesai.setText(String.valueOf(sppdSelesai));
			txt_jumlah_laporan.setText(String.valueOf(sppdLaporan));

			if ("SERVER_NULL".equals(result)) {
				Toast.makeText(List_DataSppd.this,
						"⚠ Server tidak merespon atau diblok firewall",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result != null) {
				Toast.makeText(List_DataSppd.this,
						"⚠ Error : " + result,
						Toast.LENGTH_LONG).show();
				return;
			}

			if (list == null || list.isEmpty()) {
				showPesanSnackbar("❌ Tidak ditemukan data SPPD!");
				return;
			}
			menampilkan_nama_pegawai();
		}
	}

	private List<Daftar_String> proses_pengambilan_data(String response) {

		List<Daftar_String> list = new ArrayList<>();

		try {
			JSONObject json = new JSONObject(response);

			if (!json.optBoolean("success")) {
				Log.e("LOAD", "Success false");
				return list;
			}

			JSONArray array = json.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {

				JSONObject obj = array.getJSONObject(i);

				Daftar_String item = new Daftar_String();
				item.setnomor_urut(obj.optString("nomor_urut"));
				item.setid_sppd(obj.optString("id_sppd"));
				item.setid_spt(obj.optString("id_spt"));
				item.setnomor_SPT(obj.optString("nomor_spt"));
				item.setnomor_SPPD(obj.optString("nomor_surat_sppd"));
				item.settgl_surat_spt_masuk(obj.optString("tgl_surat_masuk"));
				item.setstatus_laporan_petugas(obj.optString("status_laporan_petugas"));
				item.setstatus_riil(obj.optString("status_riil"));
				item.setstatus_rincian(obj.optString("status_rincian_biaya"));
				item.setsts_postingan(obj.optString("status_post"));
				item.setlama_perj(obj.optString("lama_perj"));
				item.settgl_brngkt(obj.optString("tgl_brngkt"));
				item.settgl_kembali(obj.optString("tgl_kembali"));
				item.setwaktu_dibuat(obj.optString("waktu_aktivitas"));
				item.setnip(obj.optString("nip"));
				item.setnama_pegawai(obj.optString("nama_pegawai"));
				item.settempat_tujuan(obj.optString("tempat_tujuan"));
				item.settempat_brngkt(obj.optString("tempat_brngkt"));
				item.setmaksud_perj(obj.optString("maksud_perj"));
				item.setsurat_masuk_dari(obj.optString("surat_masuk_dari"));

				list.add(item);
			}
			totalsppd = String.valueOf(json.optInt("total"));
			Log.e("LOAD_TOTAL", "TOTAL = " + json.optInt("total"));

		} catch (JSONException e) {
			Log.e("LOAD", "Parsing error", e);
		}

		Log.e("LOAD", "JUMLAH DATA = " + list.size());

		return list;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    private void menampilkan_nama_pegawai() {
        AdapterSPPD adaptersppd = new AdapterSPPD(List_DataSppd.this, list, this::handleMenuClick);
		listView.setAdapter(adaptersppd);
	}

	private void proses_pengambilan_data_statistik(String response) {
		Log.e("STATISTIK", response);
		try {
			JSONObject json = new JSONObject(response);
			if (!json.optBoolean("success")) {
				return;
			}

			JSONObject statistik = json.getJSONObject("statistik");

			totalSppd     = statistik.optInt("total_sppd", 0);
			sppdSelesai   = statistik.optInt("sppd_selesai", 0);
			sppdLaporan   = statistik.optInt("sppd_laporan", 0);
//			String note = statistik.optString("note", "-");

		} catch (JSONException e) {
			Log.e("STATISTIK", "Parsing error", e);
		}
	}

	public void refreshSPPD(View view) {
		finish();
		startActivity(getIntent());
	}

	private boolean terkoneksi_roaming(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();

	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    private void handleMenuClick(Daftar_String selectedList, int menuId) {

		switch (menuId) {

			case 1:
				prosesLaporan(selectedList);
				break;

			case 2:
				prosesRincian(selectedList);
				break;

			case 3:
				prosesRiil(selectedList);
				break;

			case 4:
				downloadSPT(selectedList);
				break;

			case 5:
				downloadSPPD(selectedList);
				break;

			case 6:
				prosesPosting(selectedList);
				break;
		}
	}

	private void prosesLaporan(Daftar_String selectedList) {

		String cek_laporanperjalanan = selectedList.getstatus_laporan_petugas();

		if (cek_laporanperjalanan.isEmpty()|| cek_laporanperjalanan.contains("null")) {
			String pesan = "Ada kesalahan pada status laporan perjalanan dinas";
			showPesanSnackbar(pesan);
		} else if (cek_laporanperjalanan.contains("SUDAH")) {
			String pesan2 = "Laporan perjalanan dinas sudah dibuat\nLakukan perubahan laporan ?";
			showPesanLaporan(pesan2, selectedList);
		} else {

			Intent i = new Intent(this, Edit_LaporanPerjalanan.class);
			i.putExtra("nomor_spt", selectedList.getnomor_SPT());
			i.putExtra("nip", selectedList.getnip());
			i.putExtra("nama_pegawai", selectedList.getnama_pegawai());
			i.putExtra("lama_perj", selectedList.getlama_perj());
			i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
			i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
			i.putExtra("tempat_tujuan",selectedList.gettempat_tujuan());
			i.putExtra("surat_masuk_dari",selectedList.getsurat_masuk_dari());
			i.putExtra("maksud_perj", selectedList.getmaksud_perj());
			i.putExtra("tgl_surat_masuk",selectedList.gettgl_surat_spt_masuk());
			i.putExtra("status_laporan_petugas",selectedList.getstatus_laporan_petugas());

			startActivity(i);
		}
	}
	private void prosesRincian(Daftar_String selectedList) {

		String cek_status_rincian = selectedList.getstatus_rincian();
		String cek_laporanperjalanan = selectedList.getstatus_laporan_petugas();
		if (cek_laporanperjalanan.isEmpty()||cek_laporanperjalanan.contains("null")) {
			String pesan = "Ada kesalahan pada status laporan perjalanan dinas\nHubungi Admin!";
			showPesanSnackbar(pesan);
		}else if (cek_status_rincian.isEmpty()||cek_status_rincian.contains("null")) {
			String pesan = "Ada kesalahan pada status perincian biaya\nHubungi Admin!";
			showPesanSnackbar(pesan);
		}else if ((cek_status_rincian.contains("SUDAH"))) {
			String pesan = "Perincian biaya + bukti sudah dibuat\nLakukan perubahan data?";
			showPesanRincian(pesan, selectedList);
		}else{
			Intent i ;
			i = new Intent(List_DataSppd.this,
					List_DataRincian.class);
			i.putExtra("nomor_surat_sppd", selectedList.getnomor_SPPD());
			i.putExtra("nip", selectedList.getnip());
			i.putExtra("tgl_surat_masuk",selectedList.gettgl_surat_spt_masuk());
			startActivity(i);
		}
	}
	private void prosesRiil(Daftar_String selectedList){
		String cek_laporanperjalanan = selectedList.getstatus_laporan_petugas();
		String cek_riil = selectedList.getstatus_riil();
		if (cek_laporanperjalanan.isEmpty()||cek_laporanperjalanan.contains("null")) {
			String pesan = "Ada kesalahan pada status laporan perjalanan dinas\nHubungi Admin!";
			showPesanSnackbar(pesan);
		}else if (cek_riil.isEmpty()||cek_riil.contains("null")) {
			String pesan = "Ada kesalahan pada status perincian biaya riil\nHubungi Admin!";
			showPesanSnackbar(pesan);
		}else if (cek_riil.contains("SUDAH")) {
			String pesan = "Perincian biaya riil sudah dibuat\nLakukan perubahan data?";
			showPesanRiil(pesan, selectedList);
		}else{
			Intent i ;
			i = new Intent(List_DataSppd.this, List_DataRiil.class);
			i.putExtra("nip", selectedList.getnip());
			i.putExtra("nomor_surat_sppd", selectedList.getnomor_SPPD());
			i.putExtra("tgl_surat_masuk",selectedList.gettgl_surat_spt_masuk());
			startActivity(i);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    private void downloadSPT(Daftar_String selectedList){
		HashMap<String, String> map = new HashMap<>();
		map.put("id_spt", selectedList.getid_spt());
		map.put("id_sppd", selectedList.getid_sppd());
		map.put("nomor_spt", selectedList.getnomor_SPT());
		map.put("nomor_sppd", selectedList.getnomor_SPPD());
		download(map, 1); // 2 = SPT
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    private void downloadSPPD(Daftar_String selectedList) {

		HashMap<String, String> map = new HashMap<>();
		map.put("id_spt", selectedList.getid_spt());
		map.put("id_sppd", selectedList.getid_sppd());
		map.put("nomor_spt", selectedList.getnomor_SPT());
		map.put("nomor_sppd", selectedList.getnomor_SPPD());
		download(map, 2); // 3 = SPPD
	}
	private void prosesPosting(Daftar_String selectedList){
		String cek_status_lapsppd = selectedList.getstatus_laporan_petugas();
		String cek_status_rincian = selectedList.getstatus_rincian();
		String cek_status_riil = selectedList.getstatus_riil();
		String get_spt = selectedList.getnomor_SPT();

		if ((cek_status_lapsppd.contains("SUDAH"))&&(cek_status_rincian.contains("BELUM"))&&(cek_status_riil.contains("BELUM"))) {
			String pesan = "SPT : "+get_spt+"\nStatus Laporan : "+cek_status_lapsppd+"\nStatus Rincian : "+cek_status_rincian+
					"\nStatus Riil : "+cek_status_riil+"\nLanjut selesai / posting ?";
			showPesanPosting(pesan, selectedList);
		} else if (cek_status_lapsppd.contains("BELUM")) {
			showPesanSnackbar("SPT : "+get_spt+"\nLaporan perjalanan dinas belum dibuat!");
		} else {
			String pesan = "Data Sudah Lengkap, Selesaikan / Posting Data ?";
			showPesanPosting(pesan, selectedList);
		}
	}
	private void showPesanKoneksi(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Warning")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Ulangi",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							startActivity(getIntent());
						});
				builder.setNegativeButton("Kembali",
				(dialog, id) -> {
					dialog.dismiss();
					finish();
					List_DataSppd.this.finish();
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showPesanLaporan(String message,Daftar_String selectedList) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Warning")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("✅ Edit Data",
						(dialog, id) -> {
							dialog.dismiss();
							Intent i ;
							i = new Intent(List_DataSppd.this, Edit_LaporanPerjalanan.class);
							i.putExtra("id_spt", selectedList.getid_spt());
							i.putExtra("nomor_spt", selectedList.getnomor_SPT());
							i.putExtra("nip", selectedList.getnip());
							i.putExtra("nama_pegawai", selectedList.getnama_pegawai());
							i.putExtra("lama_perj", selectedList.getlama_perj());
							i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
							i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
							i.putExtra("tempat_tujuan",selectedList.gettempat_tujuan());
							i.putExtra("surat_masuk_dari",selectedList.getsurat_masuk_dari());
							i.putExtra("maksud_perj", selectedList.getmaksud_perj());
							i.putExtra("tgl_surat_masuk",selectedList.gettgl_surat_spt_masuk());
							i.putExtra("status_laporan_petugas",selectedList.getstatus_laporan_petugas());
							startActivity(i);

						});
		builder.setNegativeButton(R.string.batal_alert,
				(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showPesanRincian(String message,Daftar_String selectedList) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Warning")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("✅ Edit Data Rincian + Bukti",
						(dialog, id) -> {
							dialog.dismiss();
							Intent i ;
							i = new Intent(List_DataSppd.this, List_DataRincian.class);
							i.putExtra("nomor_surat_sppd", selectedList.getnomor_SPPD());
							i.putExtra("nip", selectedList.getnip());
							i.putExtra("tgl_surat_masuk",selectedList.gettgl_surat_spt_masuk());
							startActivity(i);
						});
		builder.setNegativeButton(R.string.batal_alert,
				(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showPesanRiil(String message,Daftar_String selectedList) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Warning")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("✅ Edit Data Riil",
						(dialog, id) -> {
							dialog.dismiss();
							Intent i ;
							i = new Intent(List_DataSppd.this, List_DataRiil.class);
							i.putExtra("nip", selectedList.getnip());
							i.putExtra("nomor_surat_sppd", selectedList.getnomor_SPPD());
							i.putExtra("tgl_surat_masuk",selectedList.gettgl_surat_spt_masuk());
							startActivity(i);
						});
		builder.setNegativeButton(R.string.batal_alert,
				(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showPesanPosting(String message,Daftar_String selectedList) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Warning")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setNegativeButton(R.string.batal_alert,
						(dialog, id) -> dialog.dismiss())
				.setPositiveButton("✅ Kirim",
						(dialog, id) -> {
							String no_sppd 	= selectedList.getnomor_SPPD();
							String nip 		= selectedList.getnip();
							new Posting_SPPD(no_sppd, nip).execute();
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void showPesanPostingAll(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Warning")
				.setCancelable(true)
				.setIcon(R.drawable.ic_warning_black)
				.setNegativeButton(R.string.batal_alert,
						(dialog, id) -> dialog.dismiss())
				.setPositiveButton("✅ Kirim / Posting",
						(dialog, id) -> new PostingSemua_SPPD().execute());
		AlertDialog alert = builder.create();
		alert.show();
	}
	@SuppressLint("StaticFieldLeak")
	private class Posting_SPPD extends AsyncTask<Void, Void, String> {

		private ProgressDialog loading;
		private final Java_Connection jc = new Java_Connection();

		private final String no_sppd;
		private final String nip;

		Posting_SPPD(String no_sppd, String nip) {
			this.no_sppd = no_sppd;
			this.nip = nip;
		}

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(List_DataSppd.this);
			loading.setMessage("Sedang Dalam Proses Posting...");
			loading.setIndeterminate(false);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(Void... voids) {

			try {

				HashMap<String, String> data = new HashMap<>();
				data.put("no_sppd", no_sppd);
				data.put("nip", nip);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

					String response = jc.sendPostRequest(
							Koneksi.postingSPTSPPD,
							data
					);

					if (response == null) {
						return "SERVER_NULL";
					}

					Log.e(TAG, response);

					JSONObject obj = new JSONObject(response);
					String status = obj.getString("status");
					String message = obj.getString("message");

					if (!status.equalsIgnoreCase("success")) {
						return message; // kirim pesan error ke onPostExecute
					}

				}

			} catch (Exception e) {
				Log.e(TAG, "ERROR ", e);
				return e.toString();
			}

			return null; // SUCCESS
		}

		@Override
		protected void onPostExecute(String result) {

			if (loading != null && loading.isShowing()) {
				loading.dismiss();
			}

			if ("SERVER_NULL".equals(result)) {
				Toast.makeText(List_DataSppd.this,
						"Server tidak merespon atau diblok firewall",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result != null) {
				Toast.makeText(List_DataSppd.this,
						"Posting gagal: " + result,
						Toast.LENGTH_LONG).show();
				return;
			}

			Toast.makeText(List_DataSppd.this,
					"Posting berhasil",
					Toast.LENGTH_LONG).show();

			// Lebih aman daripada finish()+startActivity()
			recreate();
		}
	}

	@SuppressLint("StaticFieldLeak")
	private class PostingSemua_SPPD extends AsyncTask<Void, Void, String> {

		Java_Connection jc = new Java_Connection();

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(List_DataSppd.this);
			loading.setMessage("Sedang mengirim semua data SPPD...");
			loading.setCancelable(false);
			loading.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {

				String nip = nipLokal.trim();
				String nipEncoded = URLEncoder.encode(nip, "UTF-8");

				String url = Koneksi.posting_AllSPTSPPD
						+ "?nip_pegawai=" + nipEncoded;

				String response = jc.sendGetRequest(url);

				if (response == null) {
					return "SERVER_NULL";
				}

				return response;

			} catch (Exception e) {
				Log.e("POST_ALL_SPPD", "ERROR", e);
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String result) {

			loading.dismiss();

			if ("SERVER_NULL".equals(result)) {
				Toast.makeText(List_DataSppd.this,
						"Server tidak merespon",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result == null) {
				Toast.makeText(List_DataSppd.this,
						"Terjadi kesalahan tidak diketahui",
						Toast.LENGTH_LONG).show();
				return;
			}

			try {

				JSONObject obj = new JSONObject(result);
				boolean success = obj.getBoolean("success");
				String message = obj.getString("message");

				Toast.makeText(List_DataSppd.this,
						message,
						Toast.LENGTH_LONG).show();

				if (success) {
					// kalau mau refresh list
					recreate();
				}

			} catch (JSONException e) {
				Toast.makeText(List_DataSppd.this,
						"Format respon salah",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void download(HashMap<String, String> d, int code) {

		String idSpt 	= d.get("id_spt");
		String idSppd 	= d.get("id_sppd");
		String noSpt 	= d.get("nomor_spt");
		String noSppd 	= d.get("nomor_sppd");

//		String status;
		String url;
		String folderName;
		String filePrefix;
		String safeName;

		// 🔥 TENTUKAN PARAMETER BERDASARKAN CODE
		try {
			switch (code) {
				case 1: // SPT
					url = Koneksi.downloadSPT
							+ "?id_spt=" + URLEncoder.encode(idSpt, "UTF-8");
					folderName = "SPT";
					filePrefix = "SPT";
					safeName    = Objects.requireNonNull(noSpt).replace("/", "_");
					break;

				case 2: // SPPD
					url = Koneksi.downloadSPPD
							+ "?id_sppd=" + URLEncoder.encode(idSppd, "UTF-8");
					folderName = "SPPD";
					filePrefix = "SPPD";
					safeName   = Objects.requireNonNull(noSppd).replace("/", "_");
					break;

				default:
					showPesanSnackbar("Kode download tidak dikenal");
					return;
			}

		} catch (Exception e) {
			showPesanSnackbar("Parameter download tidak valid");
			return;
		}

		// 📁 FOLDER
		File folder = new File(
				Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_DOWNLOADS
				),
				"eSPPD/" + folderName
		);
		if (!folder.exists()) folder.mkdirs();

		// 🔥 NAMA FILE AMAN
		File pdf = new File(folder, filePrefix + "_" + safeName + ".pdf");

		// 🧠 CACHE
		if (pdf.exists()) {
			showPesanSnackbar("File sudah ada!");
			openPdf(pdf);
			return;
		}

		// ⏳ PROGRESS
		showProgress("Mengunduh file " + filePrefix);

		String finalUrl = url;
		new Thread(() -> {

			Java_Connection jc = new Java_Connection();

			boolean sukses = jc.downloadFileWithProgress(
					finalUrl,
					pdf,
					progress -> runOnUiThread(() ->
							loading.setProgress(progress)
					)
			);

			runOnUiThread(() -> {
				hideProgress();
				if (sukses) {
					showPesanSnackbar(filePrefix + " berhasil diunduh");
					openPdf(pdf);
				} else {
					showPesanSnackbar("Download gagal");
				}
			});

		}).start();
	}
	private void openPdf(File file) {

		Uri uri = FileProvider.getUriForFile(
				this,
				getPackageName() + ".provider",
				file
		);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/pdf");
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		try {
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this,
					"Tidak ada aplikasi pembuka PDF",
					Toast.LENGTH_LONG).show();
		}
	}

	private void showProgress(String title) {
		loading = new ProgressDialog(this);
		loading.setTitle(title);
		loading.setMessage("Mengunduh file...");
		loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		loading.setIndeterminate(false);
		loading.setCancelable(false);
		loading.show();
	}

	private void hideProgress() {
		if (loading != null && loading.isShowing()) {
			loading.dismiss();
		}
	}
}

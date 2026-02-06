package com.e_sppd_rssm;

import static koneksi.Koneksi.posting_url;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import koneksi.Daftar_String;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class Daftar_Laporan_Per_Petugas extends AppCompatActivity {
	private static final String TAG = "DaftarPetugas";
	private ListView listView, list_notif;
//	private Koneksi Koneksi_Server;
	private List<Daftar_String> list, listpost;
	private List_Daftar_SPT_per_nip adapter;
	public List_Informasi informasi;
	private TextView nip_lokal;
	private ProgressDialog loading;
	public ImageView bantuan;
	private Daftar_String selectedList;
	private static final int progress_bar_type_spt 	= 0; 
	private static final int progress_bar_type_sppd = 1;
	String transfer_nip = null;
	RelativeLayout laylistrecent;
	Daftar_String mhs, jml ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_tampil_detail_datasppd);
		nip_lokal 		= findViewById(R.id.nip_lokal);
		laylistrecent 	= findViewById(R.id.laylistrecent);
		//---------------
		listView 		= findViewById(R.id.list_daftar_sppd);
		list 			= new ArrayList<>();
		//---------------
		list_notif 		= findViewById(R.id.list_notif);
		listpost 		= new ArrayList<>();
		bantuan 		= findViewById(R.id.ImageView_Help);

		bantuan.setOnClickListener(v -> {
			Intent i;
			i = new Intent(Daftar_Laporan_Per_Petugas.this, Tampil_Bantuan.class);
			startActivity(i);
		});

		Bundle b = getIntent().getExtras();

		if (b != null) {
			transfer_nip = b.getString("transfer_nip");
		}
		nip_lokal.setText(transfer_nip);
		if (!terkoneksi_roaming(Daftar_Laporan_Per_Petugas.this)) {
			String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			showAlert(pesan);
		}else{
			new Load_Data().execute();
		}
	}

	@SuppressLint("StaticFieldLeak")
    private class Load_Data extends AsyncTask<Void, Void, String> {

		Java_Connection jc = new Java_Connection();

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(Daftar_Laporan_Per_Petugas.this);
			loading.setMessage("Sedang Memuat...");
			loading.setIndeterminate(false);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(Void... voids) {

			String nip = nip_lokal.getText().toString().trim();

			try {
				String nipEncoded = URLEncoder.encode(nip, "UTF-8");

				String urlList = Koneksi.list_sptsppd
						+ "?nip_pegawai=" + nipEncoded;

				String urlPosting = Koneksi.count_sptsppd
						+ "?nip_pegawai=" + nipEncoded;

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

					String responseList = jc.sendGetRequest(urlList);
					String responsePosting = jc.sendGetRequest(urlPosting);

					if (responseList == null || responsePosting == null) {
						Log.e("Load_Database", "RESPON NULL DARI SERVER");
						return "SERVER_NULL";
					}

					listpost = proses_pengambilan_data_jmlh_postingan(responsePosting);
					list     = proses_pengambilan_data(responseList);
				}

			} catch (Exception e) {
				Log.e("Load_Database", "ERROR", e);
				return e.toString();
			}

			return null; // sukses
		}

		@Override
		protected void onPostExecute(String result) {
			loading.dismiss();

			Log.e("Load_Database", "RESULT = " + result);

			if ("SERVER_NULL".equals(result)) {
				showAlert("Server tidak merespon atau diblok firewall");
				return;
			}

			if (result != null) {
				showAlert("Gagal Sinkronisasi Data\n" + result);
				return;
			}

			menampilkan_nama_pegawai();

			String pesanawal =
					"Untuk Pembuatan\n" +
							"1. Laporan Perjalanan Dinas,\n" +
							"2. Perincian Biaya, dan\n" +
							"3. Pengeluaran Riil\n\n" +
							"**Pilih Salah Satu Daftar SPT & SPPD berikut ini, untuk menampilkan MENU PILIHAN";

			info_pesan(pesanawal);
		}
	}


	public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}

	private List<Daftar_String> proses_pengambilan_data(String response) {
		List<Daftar_String> list_Daftar_String = new ArrayList<>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampil_daftar_sppd");
			Log.d(TAG, "proses_pengambilan_data: " + jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				mhs = new Daftar_String();
				mhs.setid_sppd(obj.getString("id_sppd"));
				mhs.setid_spt(obj.getString("id_spt"));
				mhs.setnomor_SPT(obj.getString("nomor_spt"));
				mhs.setnomor_SPPD(obj.getString("nomor_surat_sppd"));
				mhs.setnip(obj.getString("nip"));
				mhs.setnama_pegawai(obj.getString("nama_pegawai"));
				mhs.setjabatan(obj.getString("jabatan"));
				mhs.setgolongan(obj.getString("golongan"));
				mhs.setbiaya_perj(obj.getString("biaya_perj"));
				mhs.setmaksud_perj(obj.getString("maksud_perj"));
				mhs.setalat_angkutan(obj.getString("alat_angkutan"));
				mhs.settempat_brngkt(obj.getString("tempat_brngkt"));
				mhs.settempat_tujuan(obj.getString("tempat_tujuan"));
				mhs.setlama_perj(obj.getString("lama_perj"));
				mhs.settgl_brngkt(obj.getString("tgl_brngkt"));
				mhs.settgl_kembali(obj.getString("tgl_kembali"));
				mhs.settambh_pengikut1(obj.getString("tambh_pengikut1"));
				mhs.settambh_pengikut2(obj.getString("tambh_pengikut2"));
				mhs.settambh_pengikut3(obj.getString("tambh_pengikut3"));
				mhs.settambh_pengikut4(obj.getString("tambh_pengikut4"));
				mhs.settambh_pengikut5(obj.getString("tambh_pengikut5"));
				mhs.settgl_aktivitas(obj.getString("tanggal_aktivitas"));
				mhs.setjam_aktivitas(obj.getString("waktu_aktivitas"));
				mhs.setsurat_masuk_dari(obj.getString("surat_masuk_dari"));
				mhs.settgl_surat_spt_masuk(obj.getString("tgl_surat_masuk"));
				mhs.setakun_anggaran(obj.getString("akun_pembebanan_anggaran"));
				mhs.setstatus_laporan_petugas(obj.getString("status_laporan_petugas"));
				mhs.setstatus_riil(obj.getString("status_riil"));
				mhs.setstatus_rincian(obj.getString("status_rincian_biaya"));
				mhs.setsts_postingan(obj.getString("status_post"));
				//KHUSUS PENGAMBILAN LAPORAN 
				mhs.setnip_pembuatlaporanperj(obj.getString("nip_pembuatlaporanperj"));
				mhs.setnomor_spt_laporanperj(obj.getString("nomor_spt_laporanperj"));
				mhs.sethasil_pertemuan(obj.getString("hasil_pertemuan"));
				mhs.setmasalah(obj.getString("masalah"));
				mhs.setsaran(obj.getString("saran"));
				mhs.setlain_lain(obj.getString("lain_lain"));
				mhs.settgl_pembuatan_laporan(obj.getString("tgl_pembuatan_laporan"));
				mhs.setnomor_urut(obj.getString("nomor_urut"));

				list_Daftar_String.add(mhs);
			}
		} catch (JSONException e) {

			if (e.getMessage() != null) {
				Log.d("proses_pengambilan_data", e.getMessage());
			}else{
				Toast.makeText(Daftar_Laporan_Per_Petugas.this,
						"Gagal Mengambil Data", Toast.LENGTH_LONG).show();
			}

		}
		return list_Daftar_String;
	}

	private List<Daftar_String> proses_pengambilan_data_jmlh_postingan(String response) {
		List<Daftar_String> list_Daftar_String = new ArrayList<>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampil_jmlh_blmpostingan");
			Log.d(TAG, "proses_pengambilan_data_jmlh_postingan: " + jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				jml = new Daftar_String();				
				jml.setjml_stspost(obj.getString("jml_stspost")); //AMBIL POSTINGAN JUMLAHNYA
				jml.setjml_nomor_surat_sppd(obj.getString("jml_nomor_surat_sppd")); 
				jml.setnote(obj.getString("note"));
				jml.setjml_terposting(obj.getString("jml_terposting"));
				list_Daftar_String.add(jml);
			}
		} catch (JSONException e) {

			if (e.getMessage() != null) {
				Log.d(TAG, e.getMessage());
			} else {
				Toast.makeText(Daftar_Laporan_Per_Petugas.this,
						"Gagal Mengambil Data", Toast.LENGTH_LONG).show();
			}

		}
		return list_Daftar_String;
	}
	
//	private void tampil_jml_post() {
//		if (!connected(Daftar_Laporan_Per_Petugas.this)) {
//			String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
//			showAlert(pesan);
//		} else {
//			informasi = new List_Informasi(getApplicationContext(), listpost);
//			list_notif.setAdapter(informasi);
		//}
//	}
	
	private void menampilkan_nama_pegawai() {
//		if (!connected(Daftar_Laporan_Per_Petugas.this)) {
//			String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
//			showAlert(pesan);
//		} else {
			informasi = new List_Informasi(getApplicationContext(), listpost);
			list_notif.setAdapter(informasi);

			adapter = new List_Daftar_SPT_per_nip(getApplicationContext(), list);						
			listView.setAdapter(adapter);
			listView.setOnItemClickListener((parent, view, pos, id) -> {
				// TODO Auto-generated method stub
				selectedList = (Daftar_String) adapter.getItem(pos);
				tampil_pilihan_menu();
				Snackbar.make(view, "Jangan Lupa ( Posting SPT & SPPD ),\nyang sudah terselesaikan ya....", Snackbar.LENGTH_LONG)
						.setAction("Snackbar", null).show();
			});
		//}
	}

	private boolean terkoneksi_roaming(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();

	}

	private void tampil_pilihan_menu() {
		final Dialog dialog2 = new Dialog(this);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.dialog_pilihan_daftar_petugas);
		//String cek_nip_pembuat_lap 						= selectedList.getnip_pembuatlaporanperj();

		CardView btn_gotoinput_lap_perj_dinas 				= dialog2.findViewById(R.id.btn_gotoinput_lap_perj_dinas);
		CardView btn_gotocek_spt 							= dialog2.findViewById(R.id.btn_gotocek_spt);
		CardView btn_gotocek_sppd 							= dialog2.findViewById(R.id.btn_gotocek_sppd);
		CardView btn_gotoinputdaftar_pengeluaran_rincian 	= dialog2.findViewById(R.id.btn_gotoinputdaftar_pengeluaran_rincian);
		CardView btn_gotoinputdaftar_pengeluaran_riil 		= dialog2.findViewById(R.id.btn_gotoinputdaftar_pengeluaran_riil);
		CardView btn_posting 								= dialog2.findViewById(R.id.btn_posting);

		dialog2.show();

		//.setMagicButtonClickListener(new View.OnClickListener() {
		btn_gotoinput_lap_perj_dinas.setOnClickListener(v -> {
				String cek_laporanperjalanan 	= selectedList.getstatus_laporan_petugas();
				String cek_nip_pembuat_lap 		= selectedList.getnip_pembuatlaporanperj();
				String cek_nip_login			= selectedList.getnip();

				if (cek_laporanperjalanan.isEmpty()||cek_laporanperjalanan.contains("null")) {
					//dialog2.dismiss();
					String pesan = "Telah Terjadi Kesalahan Status Laporan Perjalanan Dinas Anda\nSegera Hubungi Admin !!!";
					showAlert_Khusus_Lap(pesan);
				}else if (cek_laporanperjalanan.contains("SUDAH")) {

					if (cek_nip_pembuat_lap.contains(cek_nip_login)){
						dialog2.dismiss();
						String pesan2 = "Anda Sudah Membuat Laporan Perjalanan Dinas\nEdit Data Laporan Perjalanan Dinas ?";
						ALert_Edit_Laporan_Perjalanan_Dinas(pesan2);
					}else{
						dialog2.dismiss();
						String pesan = "Laporan Perjalanan Dinas Sudah di Buat oleh \n"+"NIP: "+cek_nip_pembuat_lap;
						showAlert_Khusus_Lap(pesan);
					}
				}else{
				dialog2.dismiss();
				Daftar_Laporan_Per_Petugas.this.finish();
				finish();
				Intent i;
				i = new Intent(Daftar_Laporan_Per_Petugas.this,
						Pembuatan_Lap_Setelah_Perj_Dinas.class);
				i.putExtra("id_spt", selectedList.getid_spt());
				i.putExtra("nomor_spt", selectedList.getnomor_SPT());
				i.putExtra("nomor_surat_sppd", selectedList
						.getnomor_SPPD());
				i.putExtra("nip", selectedList.getnip());
				i.putExtra("nama_pegawai", selectedList.getnama_pegawai());
				i.putExtra("jabatan", selectedList.getjabatan());
				i.putExtra("golongan", selectedList.getgolongan());
				i.putExtra("biaya_perj", selectedList.getbiaya_perj());
				i.putExtra("maksud_perj", selectedList.getmaksud_perj());
				i.putExtra("alat_angkutan",
						selectedList.getalat_angkutan());
				i.putExtra("tempat_brngkt",
						selectedList.gettempat_brngkt());
				i.putExtra("tempat_tujuan",
						selectedList.gettempat_tujuan());
				i.putExtra("lama_perj", selectedList.getlama_perj());
				i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
				i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
				i.putExtra("tambh_pengikut1",
						selectedList.gettambh_pengikut1());
				i.putExtra("tambh_pengikut2",
						selectedList.gettambh_pengikut2());
				i.putExtra("tambh_pengikut3",
						selectedList.gettambh_pengikut3());
				i.putExtra("tambh_pengikut4",
						selectedList.gettambh_pengikut4());
				i.putExtra("tambh_pengikut5",
						selectedList.gettambh_pengikut5());
				i.putExtra("tanggal_aktivitas",
						selectedList.gettgl_aktivitas());
				i.putExtra("waktu_aktivitas",
						selectedList.getjam_aktivitas());
				i.putExtra("akun_pembebanan_anggaran",
						selectedList.getakun_anggaran());
				i.putExtra("surat_masuk_dari",
						selectedList.getsurat_masuk_dari());
				i.putExtra("tgl_surat_masuk",
						selectedList.gettgl_surat_spt_masuk());
				i.putExtra("status_laporan_petugas",
						selectedList.getstatus_laporan_petugas());
				//------------------------
				i.putExtra("nip_pembuatlaporanperj",
						selectedList.getnip_pembuatlaporanperj());
				i.putExtra("nomor_spt_laporanperj",
						selectedList.getnomor_spt_laporanperj());
				i.putExtra("hasil_pertemuan",
						selectedList.gethasil_pertemuan());
				i.putExtra("masalah",
						selectedList.getmasalah());
				i.putExtra("saran",
						selectedList.getsaran());
				i.putExtra("lain_lain",
						selectedList.getlain_lain());
				i.putExtra("tgl_pembuatan_laporan",
						selectedList.gettgl_pembuatan_laporan());


				startActivity(i);

				}

			});

		btn_gotocek_spt.setOnClickListener(v -> {
			dialog2.dismiss();
			if (!terkoneksi_roaming(Daftar_Laporan_Per_Petugas.this)) {
				String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
				showAlert(pesan);
			} else {
				String id_spt = selectedList.getid_spt();
				String Cek = String.valueOf(id_spt);
				try {

					new DownloadFile_SPT().execute(Koneksi.download_spt + "?id_spt=" + URLEncoder.encode(Cek, "UTF-8"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btn_gotocek_sppd.setOnClickListener(v -> {
			dialog2.dismiss();
			if (!terkoneksi_roaming(Daftar_Laporan_Per_Petugas.this)) {
				String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
				showAlert(pesan);
			} else {
				String id_sppd = selectedList.getid_sppd();
				String Cek = String.valueOf(id_sppd);

				try {

					new DownloadFile_SPPD().execute(Koneksi.download_sppd + "?id_sppd=" + URLEncoder.encode(Cek, "UTF-8"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btn_gotoinputdaftar_pengeluaran_rincian
				.setOnClickListener(v -> {
					String cek_status_rincian = selectedList.getstatus_rincian();
					String cek_laporanperjalanan = selectedList.getstatus_laporan_petugas();
					if (cek_laporanperjalanan.isEmpty()||cek_laporanperjalanan.contains("null")) {
						//dialog2.dismiss();
						String pesan = "Telah Terjadi Kesalahan Status Laporan Perjalanan Dinas Anda\nSegera Hubungi Admin !!!";
						showAlert_Khusus_Lap(pesan);
					}else if (cek_laporanperjalanan.contains("BELUM")) {
						String pesan = "Silahkan Anda Buat Laporan Perjalanan Dinas Terlebih Dahulu";
						showAlert_Khusus_Lap(pesan);
					}else if (cek_status_rincian.isEmpty()||cek_status_rincian.contains("null")) {
							//dialog2.dismiss();
						String pesan = "Telah Terjadi Kesalahan Status Perincian Biaya Anda\nSegera Hubungi Admin !!!";
						showAlert_Khusus_Lap(pesan);

					}else if (cek_status_rincian.contains("BELUM")) {
						dialog2.dismiss();
						finish();
						Daftar_Laporan_Per_Petugas.this.finish();

					Intent i;
					i = new Intent(Daftar_Laporan_Per_Petugas.this,
							Laporan_Rincian_Biaya_Perj_Dinas.class);
					i.putExtra("id_spt", selectedList.getid_spt());
					i.putExtra("nomor_spt", selectedList.getnomor_SPT());
					i.putExtra("nomor_surat_sppd", selectedList.getnomor_SPPD());
					i.putExtra("nip", selectedList.getnip());
					i.putExtra("nama_pegawai", selectedList.getnama_pegawai());
					i.putExtra("jabatan", selectedList.getjabatan());
					i.putExtra("golongan", selectedList.getgolongan());
					i.putExtra("biaya_perj", selectedList.getbiaya_perj());
					i.putExtra("maksud_perj", selectedList.getmaksud_perj());
					i.putExtra("alat_angkutan",
							selectedList.getalat_angkutan());
					i.putExtra("tempat_brngkt",
							selectedList.gettempat_brngkt());
					i.putExtra("tempat_tujuan",
							selectedList.gettempat_tujuan());
					i.putExtra("lama_perj", selectedList.getlama_perj());
					i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
					i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
					i.putExtra("tambh_pengikut1",
							selectedList.gettambh_pengikut1());
					i.putExtra("tambh_pengikut2",
							selectedList.gettambh_pengikut2());
					i.putExtra("tambh_pengikut3",
							selectedList.gettambh_pengikut3());
					i.putExtra("tambh_pengikut4",
							selectedList.gettambh_pengikut4());
					i.putExtra("tambh_pengikut5",
							selectedList.gettambh_pengikut5());
					i.putExtra("tanggal_aktivitas",
							selectedList.gettgl_aktivitas());
					i.putExtra("waktu_aktivitas",
							selectedList.getjam_aktivitas());
					i.putExtra("akun_pembebanan_anggaran",
							selectedList.getakun_anggaran());
					i.putExtra("surat_masuk_dari",
							selectedList.getsurat_masuk_dari());
					i.putExtra("tgl_surat_masuk",
							selectedList.gettgl_surat_spt_masuk());
					i.putExtra("status_laporan_petugas",
							selectedList.getstatus_laporan_petugas());
					i.putExtra("status_rincian_biaya", selectedList.getstatus_rincian());
					i.putExtra("status_riil", selectedList.getstatus_riil());
					startActivity(i);

					}else{
						dialog2.dismiss();
						String pesan = "Anda Sudah Membuat Perincian Biaya\nEdit Laporan Perincian Biaya ?";
						ALert_Edit_Laporan_Rincian(pesan);
					}

				});

		btn_gotoinputdaftar_pengeluaran_riil.setOnClickListener(v -> {
			String cek_laporanperjalanan = selectedList.getstatus_laporan_petugas();
			String cek_riil = selectedList.getstatus_riil();
			if (cek_laporanperjalanan.isEmpty()||cek_laporanperjalanan.contains("null")) {
				//dialog2.dismiss();
				String pesan = "Telah Terjadi Kesalahan Status Laporan Perjalanan Dinas Anda\nSegera Hubungi Admin !!!";
				showAlert_Khusus_Lap(pesan);
			}else if (cek_laporanperjalanan.contains("BELUM")) {
				String pesan = "Silahkan Anda Buat Laporan Perjalanan Dinas Terlebih Dahulu";
				showAlert_Khusus_Lap(pesan);
			}else if (cek_riil.isEmpty()||cek_riil.contains("null")) {
				//dialog2.dismiss();
				String pesan = "Telah Terjadi Kesalahan Status Perincian Biaya Riil Anda\nSegera Hubungi Admin !!!";
				showAlert_Khusus_Lap(pesan);
			}else if (cek_riil.contains("SUDAH")) {
				dialog2.dismiss();
				String pesan = "Anda Sudah Membuat Laporan Pengeluaran Riil\nEdit Laporan Pengeluaran Riil ?";
				ALert_Edit_Laporan_Riil(pesan);
			}else{
				dialog2.dismiss();
				finish();
			Daftar_Laporan_Per_Petugas.this.finish();

//			Intent i ;
//			i = new Intent(Daftar_Laporan_Per_Petugas.this,
//					Laporan_Riil.class);
//			i.putExtra("id_spt", selectedList.getid_spt());
//			i.putExtra("nomor_spt", selectedList.getnomor_SPT());
//			i.putExtra("nomor_surat_sppd", selectedList.getnomor_SPPD());
//			i.putExtra("nip", selectedList.getnip());
//			i.putExtra("nama_pegawai", selectedList.getnama_pegawai());
//			i.putExtra("jabatan", selectedList.getjabatan());
//			i.putExtra("golongan", selectedList.getgolongan());
//			i.putExtra("biaya_perj", selectedList.getbiaya_perj());
//			i.putExtra("maksud_perj", selectedList.getmaksud_perj());
//			i.putExtra("alat_angkutan", selectedList.getalat_angkutan());
//			i.putExtra("tempat_brngkt",
//					selectedList.gettempat_brngkt());
//			i.putExtra("tempat_tujuan",
//					selectedList.gettempat_tujuan());
//			i.putExtra("lama_perj", selectedList.getlama_perj());
//			i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
//			i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
//			i.putExtra("tambh_pengikut1",
//					selectedList.gettambh_pengikut1());
//			i.putExtra("tambh_pengikut2",
//					selectedList.gettambh_pengikut2());
//			i.putExtra("tambh_pengikut3",
//					selectedList.gettambh_pengikut3());
//			i.putExtra("tambh_pengikut4",
//					selectedList.gettambh_pengikut4());
//			i.putExtra("tambh_pengikut5",
//					selectedList.gettambh_pengikut5());
//			i.putExtra("tanggal_aktivitas",
//					selectedList.gettgl_aktivitas());
//			i.putExtra("waktu_aktivitas",
//					selectedList.getjam_aktivitas());
//			i.putExtra("akun_pembebanan_anggaran",
//					selectedList.getakun_anggaran());
//			i.putExtra("surat_masuk_dari",
//					selectedList.getsurat_masuk_dari());
//			i.putExtra("tgl_surat_masuk",
//					selectedList.gettgl_surat_spt_masuk());
//			i.putExtra("status_laporan_petugas",
//					selectedList.getstatus_laporan_petugas());
//			i.putExtra("status_rincian_biaya", selectedList.getstatus_rincian());
//			i.putExtra("status_riil", selectedList.getstatus_riil());
//			startActivity(i);


			}
		});

		btn_posting.setOnClickListener(v -> {
			String cek_status_lap_rincian = selectedList
					.getstatus_laporan_petugas();
			String cek_status_rincian = selectedList.getstatus_rincian();
			String cek_status_riil = selectedList.getstatus_riil();
//			String no_sppd = selectedList.getnomor_SPPD();
//			String nip = selectedList.getnip();
//			String id_sppd = selectedList.getid_sppd();
//			String Cek = String.valueOf(id_sppd);

			if (cek_status_lap_rincian.contains("BELUM")) {
				Toast.makeText(Daftar_Laporan_Per_Petugas.this,
						"Anda Belum Membuat Laporan Perjalanan Dinas",
						Toast.LENGTH_LONG).show();
			} else if (cek_status_rincian.contains("BELUM")
					&& cek_status_riil.contains("BELUM")) {
				String pesan = "Anda Belum Membuat Laporan Perincian Biaya dan Laporan Pengeluaran Riil";
				alert_posting(pesan);
			} else if (cek_status_rincian.contains("BELUM")) {
				String pesan = "Anda Belum Membuat Laporan Perincian Biaya";
				alert_posting(pesan);

			} else if (cek_status_riil.contains("BELUM")) {
				String pesan = "Anda Belum Membuat Laporan Pengeluaran Biaya Riil";
				alert_posting(pesan);

			} else {
				String pesan = "Data Sudah Lengkap, Posting Data ?";
				alert_posting(pesan);
				//ngeposting(no_sppd, nip);
			}

		});
	}

	private void alert_posting(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setNegativeButton("Batal",
						(dialog, id) -> dialog.dismiss())
				.setPositiveButton("Tetap Posting",
						(dialog, id) -> {
							String no_sppd 	= selectedList.getnomor_SPPD();
							String nip 		= selectedList.getnip();
							ngeposting(no_sppd, nip);
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Peringatan")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Ulangi",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							startActivity(getIntent());

						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void info_pesan(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Mengerti",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showAlert_Khusus_Lap(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Ok",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void ALert_Edit_Laporan_Perjalanan_Dinas(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Edit Data",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							Daftar_Laporan_Per_Petugas.this.finish();
							Intent i ;
							i = new Intent(Daftar_Laporan_Per_Petugas.this,
									Pembuatan_Lap_Setelah_Perj_Dinas.class);
							i.putExtra("id_spt", selectedList.getid_spt()
									);
							i.putExtra("nomor_spt", selectedList.getnomor_SPT()
									);
							i.putExtra("nomor_surat_sppd", selectedList
									.getnomor_SPPD());
							i.putExtra("nip", selectedList.getnip());
							i.putExtra("nama_pegawai", selectedList
									.getnama_pegawai());
							i.putExtra("jabatan", selectedList.getjabatan()
									);
							i.putExtra("golongan", selectedList.getgolongan()
									);
							i.putExtra("biaya_perj", selectedList.getbiaya_perj()
									);
							i.putExtra("maksud_perj", selectedList.getmaksud_perj());
							i.putExtra("alat_angkutan",
									selectedList.getalat_angkutan());
							i.putExtra("tempat_brngkt",
									selectedList.gettempat_brngkt());
							i.putExtra("tempat_tujuan",
									selectedList.gettempat_tujuan());
							i.putExtra("lama_perj", selectedList.getlama_perj());
							i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
							i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
							i.putExtra("tambh_pengikut1",
									selectedList.gettambh_pengikut1());
							i.putExtra("tambh_pengikut2",
									selectedList.gettambh_pengikut2());
							i.putExtra("tambh_pengikut3",
									selectedList.gettambh_pengikut3());
							i.putExtra("tambh_pengikut4",
									selectedList.gettambh_pengikut4());
							i.putExtra("tambh_pengikut5",
									selectedList.gettambh_pengikut5());
							i.putExtra("tanggal_aktivitas",
									selectedList.gettgl_aktivitas());
							i.putExtra("waktu_aktivitas",
									selectedList.getjam_aktivitas());
							i.putExtra("akun_pembebanan_anggaran",
									selectedList.getakun_anggaran());
							i.putExtra("surat_masuk_dari",
									selectedList.getsurat_masuk_dari());
							i.putExtra("tgl_surat_masuk",
									selectedList.gettgl_surat_spt_masuk());
							i.putExtra("status_laporan_petugas",
									selectedList.getstatus_laporan_petugas());
							//------------------------
							i.putExtra("nip_pembuatlaporanperj",
									selectedList.getnip_pembuatlaporanperj());
							i.putExtra("nomor_spt_laporanperj",
									selectedList.getnomor_spt_laporanperj());
							i.putExtra("hasil_pertemuan",
									selectedList.gethasil_pertemuan());
							i.putExtra("masalah",
									selectedList.getmasalah());
							i.putExtra("saran",
									selectedList.getsaran());
							i.putExtra("lain_lain",
									selectedList.getlain_lain());
							i.putExtra("tgl_pembuatan_laporan",
									selectedList.gettgl_pembuatan_laporan());


							startActivity(i);

						});
		builder.setNegativeButton("Tidak",
				(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void ALert_Edit_Laporan_Rincian(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Edit Data",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							Daftar_Laporan_Per_Petugas.this.finish();
							Intent i ;
							i = new Intent(Daftar_Laporan_Per_Petugas.this,
									Edit_Laporan_Rincian_Biaya.class);
							i.putExtra("id_spt", selectedList.getid_spt()
									);
							i.putExtra("nomor_spt", selectedList.getnomor_SPT()
									);
							i.putExtra("nomor_surat_sppd", selectedList
									.getnomor_SPPD());
							i.putExtra("nip", selectedList.getnip());
							i.putExtra("nama_pegawai", selectedList
									.getnama_pegawai());
							i.putExtra("jabatan", selectedList.getjabatan()
									);
							i.putExtra("golongan", selectedList.getgolongan()
									);
							i.putExtra("biaya_perj", selectedList.getbiaya_perj()
									);
							i.putExtra("maksud_perj", selectedList.getmaksud_perj());
							i.putExtra("alat_angkutan",
									selectedList.getalat_angkutan());
							i.putExtra("tempat_brngkt",
									selectedList.gettempat_brngkt());
							i.putExtra("tempat_tujuan",
									selectedList.gettempat_tujuan());
							i.putExtra("lama_perj", selectedList.getlama_perj());
							i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
							i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
							i.putExtra("tambh_pengikut1",
									selectedList.gettambh_pengikut1());
							i.putExtra("tambh_pengikut2",
									selectedList.gettambh_pengikut2());
							i.putExtra("tambh_pengikut3",
									selectedList.gettambh_pengikut3());
							i.putExtra("tambh_pengikut4",
									selectedList.gettambh_pengikut4());
							i.putExtra("tambh_pengikut5",
									selectedList.gettambh_pengikut5());
							i.putExtra("tanggal_aktivitas",
									selectedList.gettgl_aktivitas());
							i.putExtra("waktu_aktivitas",
									selectedList.getjam_aktivitas());
							i.putExtra("akun_pembebanan_anggaran",
									selectedList.getakun_anggaran());
							i.putExtra("surat_masuk_dari",
									selectedList.getsurat_masuk_dari());
							i.putExtra("tgl_surat_masuk",
									selectedList.gettgl_surat_spt_masuk());
							i.putExtra("status_laporan_petugas",
									selectedList.getstatus_laporan_petugas());
							i.putExtra("status_rincian_biaya", selectedList.getstatus_rincian());
							//------------------------
							i.putExtra("nip_pembuatlaporanperj",
									selectedList.getnip_pembuatlaporanperj());
							i.putExtra("nomor_spt_laporanperj",
									selectedList.getnomor_spt_laporanperj());
							i.putExtra("hasil_pertemuan",
									selectedList.gethasil_pertemuan());
							i.putExtra("masalah",
									selectedList.getmasalah());
							i.putExtra("saran",
									selectedList.getsaran());
							i.putExtra("lain_lain",
									selectedList.getlain_lain());
							i.putExtra("tgl_pembuatan_laporan",
									selectedList.gettgl_pembuatan_laporan());


							startActivity(i);

						});
		builder.setNegativeButton("Tidak",
				(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void ALert_Edit_Laporan_Riil(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Edit Data",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							Daftar_Laporan_Per_Petugas.this.finish();
							Intent i ;
							i = new Intent(Daftar_Laporan_Per_Petugas.this,
									Edit_Laporan_Riil.class);
							i.putExtra("id_spt", selectedList.getid_spt()
									);
							i.putExtra("nomor_spt", selectedList.getnomor_SPT()
									);
							i.putExtra("nomor_surat_sppd", selectedList
									.getnomor_SPPD());
							i.putExtra("nip", selectedList.getnip());
							i.putExtra("nama_pegawai", selectedList
									.getnama_pegawai());
							i.putExtra("jabatan", selectedList.getjabatan()
									);
							i.putExtra("golongan", selectedList.getgolongan()
									);
							i.putExtra("biaya_perj", selectedList.getbiaya_perj()
									);
							i.putExtra("maksud_perj", selectedList.getmaksud_perj());
							i.putExtra("alat_angkutan",
									selectedList.getalat_angkutan());
							i.putExtra("tempat_brngkt",
									selectedList.gettempat_brngkt());
							i.putExtra("tempat_tujuan",
									selectedList.gettempat_tujuan());
							i.putExtra("lama_perj", selectedList.getlama_perj());
							i.putExtra("tgl_brngkt", selectedList.gettgl_brngkt());
							i.putExtra("tgl_kembali", selectedList.gettgl_kembali());
							i.putExtra("tambh_pengikut1",
									selectedList.gettambh_pengikut1());
							i.putExtra("tambh_pengikut2",
									selectedList.gettambh_pengikut2());
							i.putExtra("tambh_pengikut3",
									selectedList.gettambh_pengikut3());
							i.putExtra("tambh_pengikut4",
									selectedList.gettambh_pengikut4());
							i.putExtra("tambh_pengikut5",
									selectedList.gettambh_pengikut5());
							i.putExtra("tanggal_aktivitas",
									selectedList.gettgl_aktivitas());
							i.putExtra("waktu_aktivitas",
									selectedList.getjam_aktivitas());
							i.putExtra("akun_pembebanan_anggaran",
									selectedList.getakun_anggaran());
							i.putExtra("surat_masuk_dari",
									selectedList.getsurat_masuk_dari());
							i.putExtra("tgl_surat_masuk",
									selectedList.gettgl_surat_spt_masuk());
							i.putExtra("status_laporan_petugas",
									selectedList.getstatus_laporan_petugas());
							//------------------------
							i.putExtra("nip_pembuatlaporanperj",
									selectedList.getnip_pembuatlaporanperj());
							i.putExtra("nomor_spt_laporanperj",
									selectedList.getnomor_spt_laporanperj());
							i.putExtra("hasil_pertemuan",
									selectedList.gethasil_pertemuan());
							i.putExtra("masalah",
									selectedList.getmasalah());
							i.putExtra("saran",
									selectedList.getsaran());
							i.putExtra("lain_lain",
									selectedList.getlain_lain());
							i.putExtra("tgl_pembuatan_laporan",
									selectedList.gettgl_pembuatan_laporan());


							startActivity(i);

						});
		builder.setNegativeButton("Tidak",
				(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showprogress_download(String a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(a)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Ok",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							startActivity(getIntent());
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void ngeposting(String no_sppd, String nip) {

		@SuppressLint("StaticFieldLeak")
		class posting extends AsyncTask<String, Void, String> {

			private ProgressDialog tampilloading;
			private Java_Connection ruc = new Java_Connection();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				tampilloading = ProgressDialog.show(
						Daftar_Laporan_Per_Petugas.this, "", "Sedang Dalam Proses Posting\nTunggu Sebentar...",
						true, true);
                tampilloading.setCancelable(false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				tampilloading.dismiss();
				//Toast.makeText(Daftar_Laporan_Per_Petugas.this,
					//	"Posting Sukses", Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> data = new HashMap<>();
				data.put("no_sppd", params[0]);
				data.put("nip", params[1]);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return ruc.sendPostRequest(posting_url, data);
                }

                return null;
            }
		}

		posting ru = new posting();
		ru.execute(no_sppd, nip);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		String id_sppd = selectedList.getid_sppd();
		String id_spt = selectedList.getid_spt();
		switch (id) {

		case progress_bar_type_spt:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh SPT...\nNama File : laporan_surat_perintah_tugas_"
					+ id_spt + ".pdf");
			loading.setIndeterminate(false);
			loading.setMax(100);
			loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			loading.setCancelable(false);
			loading.show();
			return loading;

		case progress_bar_type_sppd:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh SPPD...\nNama File : laporan_sppd_" + id_sppd + ".pdf");
			loading.setIndeterminate(false);
			loading.setMax(100);
			loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			loading.setCancelable(false);
			loading.show();
			return loading;
		default:

			return null;
		}
	}

	@SuppressLint("StaticFieldLeak")
	class DownloadFile_SPT extends AsyncTask<String, String, String>

	{
		@Override
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type_spt);
			
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			String id_spt = selectedList.getid_spt();

			try {

				URL url = new URL(f_url[0]);

				URLConnection connection = url.openConnection();
				connection.connect();

				int lenghOfFile = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				@SuppressLint("SdCardPath") OutputStream output = new FileOutputStream(
						"/sdcard/Download/laporan_surat_perintah_tugas_" + id_spt + ".pdf");
				byte[] data = new byte[1024];
				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("Error Download SPT: ", e.getMessage());
				//String a = "Download Gagal"+e.getMessage();
				//showAlert(a);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			loading.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		@SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url) {
			dismissDialog(progress_bar_type_spt);
			String pesan = "Download Berhasil\nSilahkan BUKA di FOLDER DOWNLOAD";
			showprogress_download(pesan);
		}
	}

	@SuppressLint({"SdCardPath", "StaticFieldLeak"})
	class DownloadFile_SPPD extends AsyncTask<String, String, String>

	{
		@Override
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type_sppd);
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			String id_sppd = selectedList.getid_sppd();

			try {

				// KURANG GETS POST

				URL url = new URL(f_url[0]);

				URLConnection connection = url.openConnection();
				connection.connect();

				int lenghOfFile = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				OutputStream output = new FileOutputStream(
						"/sdcard/Download/laporan_sppd_" + id_sppd + ".pdf");
				byte[] data = new byte[1024];
				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("Error Download SPPD : ", e.getMessage());
//				String a = "Download Gagal"+e.getMessage();
//				showAlert(a);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			loading.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		@SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url) {
			dismissDialog(progress_bar_type_sppd);
			String pesan = "Download Berhasil\nSilahkan BUKA di FOLDER DOWNLOAD";
			showprogress_download(pesan);
			
			// String imagePath =
			// Environment.getExternalStorageDirectory().toString() +
			// "/prints_spt.pdf";
			// laylistrecent.setBackgroundDrawable(Drawable.createFromPath(imagePath));
		}
	}
	public void kembali_activity(View view){
		super.onBackPressed();
	}
}

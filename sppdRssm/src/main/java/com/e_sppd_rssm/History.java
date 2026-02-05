package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e_sppd.rssm.R;

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
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import koneksi.Daftar_String;
import koneksi.Koneksi;

//import koneksi.JSONParser;

public class History extends AppCompatActivity {
	private static final String TAG = "History";
	private ListView listView;
	private Koneksi Koneksi_Server;
	private List<Daftar_String> list;
	private List_History_SPT_per_nip adapter;
	private TextView nip_lokal;
	private ProgressDialog loading;
	private Daftar_String selectedList;

	private static final int progress_bar_type_spt 		= 0;
	private static final int progress_bar_type_sppd 	= 1;
	private static final int progress_bar_type_lap_perj = 2;
	private static final int progress_bar_type_rincian 	= 3;
	private static final int progress_bar_type_riil 	= 4;

	RelativeLayout laylistrecent;
	String pesan = "Download Berhasil\nSilahkan BUKA di FOLDER DOWNLOAD";
	// NOTE :
	// History.java dan Daftar Laporan Perjalana Dinas.java saling berhubungan
	// pada layout listview
	public ImageView bantuan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_tampil_detail_history);
		nip_lokal 		= findViewById(R.id.nip_lokal);
		laylistrecent 	= findViewById(R.id.laylistrecent);
		bantuan 		= findViewById(R.id.bantuan_history);
		TextView info_button5	= findViewById(R.id.info_button5);
		Bundle b = getIntent().getExtras();
		String transfer_nip = null;
		if (b != null) {
			transfer_nip = b.getString("transfer_nip");
		}
		nip_lokal.setText(transfer_nip);
		Koneksi_Server = new Koneksi();
		listView = findViewById(R.id.list_history);

		list = new ArrayList<>();
		new Tampil_history().execute();

		bantuan.setOnClickListener(v -> {
		//String pesan = "Sipp Mantap";
		//showAlert(pesan);
			Intent i;
			i = new Intent(History.this,
					Tampil_Bantuan.class);
			startActivity(i);
		});
		String welcome = "Pilih Salah Satu Daftar Dibawah Ini, Untuk menampilkan MENU PILIHAN";
		String welcome2 = "Data yang ditampilkan di HISTORY adalah Data SPT dan SPPD beserta isinya yang sudah Anda Posting di Halaman Daftar SPT dan SPPD";
		info_pesan(welcome2);
		info_button5.setText(welcome);
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
	@SuppressLint("StaticFieldLeak")
	private class Tampil_history extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(History.this);
			loading.setMessage("Loading ...");
			loading.setIndeterminate(false);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(String... params) {

			/* Mengirimkan request ke server dan memproses JSON response */
			String nip_pegawai = nip_lokal.getText().toString().trim();
			//String Cek = String.valueOf(nip_pegawai);
			String url;

			// Super PENTINGGGGGGGGGGGGGGGGGGGGGG PUOOLLLLLLLLLL
			// Barokallah Alhamdulilah KETEMU CARANE MENGATASI
			// DATA NIP YANG BERSIFAT VARCHAR DENGAN SPASI BANYAK
			try {

				url = Koneksi_Server
						.sendGetRequest(Koneksi.tampil_daftar_sppd_per_nip_HISTORY
								+ "?nip_pegawai="
								+ URLEncoder.encode(nip_pegawai, "UTF-8"));

				list = proses_pengambilan_data(url);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			loading.dismiss();
			runOnUiThread(History.this::menampilkan_nama_pegawai);
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
			Log.d(TAG, "data lengt: " + jsonArray.length());
			Daftar_String mhs;
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
				mhs.setstatus_laporan_petugas(obj
						.getString("status_laporan_petugas"));
				mhs.setstatus_riil(obj.getString("status_riil"));
				mhs.setstatus_rincian(obj.getString("status_rincian_biaya"));
				mhs.setsts_postingan(obj.getString("status_post"));

				list_Daftar_String.add(mhs);
			}
		} catch (JSONException e) {

			if (e.getMessage() != null) {
				Log.d(TAG, e.getMessage());
			} else {
				Toast.makeText(History.this, "Gagal Mengambil Data",
						Toast.LENGTH_LONG).show();
			}

		}
		return list_Daftar_String;
	}

	private void menampilkan_nama_pegawai() {
		if (!terkoneksi(History.this)) {
			Toast.makeText(History.this, "Not Connected", Toast.LENGTH_LONG)
					.show();
		} else {
			adapter = new List_History_SPT_per_nip(getApplicationContext(),
					list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener((parent, view, pos, id) -> {
				// TODO Auto-generated method stub
				selectedList = (Daftar_String) adapter.getItem(pos);
				tampil_pilihan_menu_download();
			});
		}
	}

	private boolean terkoneksi(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		// Toast.makeText(getApplication(), "Koneksi Internet Tersedia",
		// Toast.LENGTH_SHORT).show();
		return netInfo != null && netInfo.isConnectedOrConnecting();

	}

	private void tampil_pilihan_menu_download() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pilihan_download);

		CardView btn_download_lap_perj 	= dialog.findViewById(R.id.btn_download_lap_perj);
		CardView btn_download_spt 		= dialog.findViewById(R.id.btn_download_spt);
		CardView btn_download_sppd 		= dialog.findViewById(R.id.btn_download_sppd);
		CardView btn_download_rincian 	= dialog.findViewById(R.id.btn_download_rincian);
		CardView btn_download_riil 		= dialog.findViewById(R.id.btn_download_riil);

		dialog.show();

		btn_download_lap_perj.setOnClickListener(v -> {
			dialog.dismiss();
			String no_spt 			= selectedList.getnomor_SPT();
			String nip 				= selectedList.getnip();
			String cek_status_lap 	= selectedList.getstatus_laporan_petugas();
			String Cek 				= String.valueOf(no_spt);
			String Cek_Nip 			= String.valueOf(nip);

			if (cek_status_lap.contains("BELUM")) {
				Toast.makeText(
						History.this,
						"Download Gagal \nAnda Tidak Punya Laporan Perjalanan Dinas",
						Toast.LENGTH_LONG).show();
			} else {
				try {
					//new DownloadFile_SPT().execute(Koneksi.download_lap_perj + "?no_spt=" + URLEncoder.encode(Cek, "UTF-8") + "&&" + "nip=" + URLEncoder.encode(Cek_Nip, "UTF-8"));
					new DownloadFile_Laporan_perj().execute(Koneksi.download_lap_perj + "?no_spt="
									+ URLEncoder.encode(Cek, "UTF-8")
									+ "&&" + "nip="
									+ URLEncoder.encode(Cek_Nip, "UTF-8"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		// + "?no_hp=" + tampil_aturidkaryawan + "&&"
		// + "tanggal=" + tanggal);

		btn_download_spt.setOnClickListener(v -> {
			dialog.dismiss();
			String id_spt = selectedList.getid_spt();
			String Cek = String.valueOf(id_spt);
			try {
				new DownloadFile_SPT().execute(Koneksi.download_spt + "?id_spt=" + URLEncoder.encode(Cek, "UTF-8"));

				//new DownloadFile_SPT().execute(download_spt + "?id_spt=" + URLEncoder.encode(Cek, "UTF-8"));

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		btn_download_sppd.setOnClickListener(v -> {
			dialog.dismiss();
			String id_sppd = selectedList.getid_sppd();
			String Cek = String.valueOf(id_sppd);

			try {

				new DownloadFile_SPPD().execute(Koneksi.download_sppd + "?id_sppd="
						+ URLEncoder.encode(Cek, "UTF-8"));

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		btn_download_rincian.setOnClickListener(v -> {
			String cek_status_lap_rincian = selectedList
					.getstatus_rincian();
			String id_sppd = selectedList.getid_sppd();
			String Cek = String.valueOf(id_sppd);

			if (cek_status_lap_rincian.contains("BELUM")) {
				Toast.makeText(
						History.this,
						"Download Gagal \nAnda Tidak Punya Laporan Perincian Biaya",
						Toast.LENGTH_LONG).show();
			}else{

				// Toast.makeText(History.this,
				// "Maaf, Masih dalam Tahap Pengembangan \n\nSilahkan Beri Feedback Anda Mengenai Aplikasi e-SPPD"
				// ,Toast.LENGTH_LONG).show();
				try {

					new DownloadFile_Rincian_Biaya()
							.execute(Koneksi.download_rincian + "?id_sppd="
									+ URLEncoder.encode(Cek, "UTF-8"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		btn_download_riil.setOnClickListener(v -> {
			String cek_status_lap_riil = selectedList.getstatus_riil();
			String id_sppd = selectedList.getid_sppd();
			String Cek = String.valueOf(id_sppd);

			if (cek_status_lap_riil.contains("BELUM")) {
				Toast.makeText(
						History.this,
						"Download Gagal \nAnda Tidak Punya Laporan Pengeluaran Riil",
						Toast.LENGTH_LONG).show();
			} else {

				// Toast.makeText(History.this,
				// "Maaf, Masih dalam Tahap Pengembangan \n\nSilahkan Beri Feedback Anda Mengenai Aplikasi e-SPPD"
				// ,Toast.LENGTH_LONG).show();
				try {

					new DownloadFile_Pengeluaran_Riil()
							.execute(Koneksi.download_riil + "?id_sppd="
									+ URLEncoder.encode(Cek, "UTF-8"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(
							History.this,
							"Maaf, Anda Gagal Mendownload Laporan Pengeluaran Riil",
							Toast.LENGTH_LONG).show();
				}
			}

		});

	}

	@Override
	public Dialog onCreateDialog(int id) {
		String id_spt = selectedList.getid_spt();
		String id_sppd = selectedList.getid_sppd();
		switch (id) {
		case progress_bar_type_lap_perj:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh Lap. Perjalanan Dinas ...\nNama File : laporan_perjalanan_dinas_"+ id_spt + ".pdf");
			loading.setIndeterminate(false);
			loading.setMax(100);
			loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			loading.setCancelable(false);
			loading.show();
			return loading;

		case progress_bar_type_spt:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh SPT ...\nNama File : laporan_surat_perintah_tugas_"+ id_spt + ".pdf");
			loading.setIndeterminate(false);
			loading.setMax(100);
			loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			loading.setCancelable(false);
			loading.show();
			return loading;

		case progress_bar_type_sppd:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh SPPD ...\nNama File : laporan_sppd_"+ id_sppd + ".pdf");
			loading.setIndeterminate(false);
			loading.setMax(100);
			loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			loading.setCancelable(false);
			loading.show();
			return loading;

		case progress_bar_type_rincian:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh Lap. Rincian Biaya ...\nNama File : laporan_rincian_biaya_"+ id_sppd + ".pdf");
			loading.setIndeterminate(false);
			loading.setMax(100);
			loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			loading.setCancelable(false);
			loading.show();
			return loading;

		case progress_bar_type_riil:
			loading = new ProgressDialog(this);
			loading.setMessage("Sedang Mengunduh Lap. Rincian Riil ...\nNama File : laporan_pengeluaran_riil_"+ id_sppd + ".pdf");
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

	@SuppressLint({"SdCardPath", "StaticFieldLeak"})
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

				OutputStream output = new FileOutputStream(
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
			String ambil_id = selectedList.getid_spt();
			showAlert(pesan+"\nNama File : laporan_surat_perintah_tugas_"+ambil_id+".pdf");
			//finish();
			//startActivity(getIntent());
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
			String ambil_id = selectedList.getid_sppd();
			showAlert(pesan+"\nNama File : laporan_sppd_"+ambil_id+".pdf");
			//finish();
			//startActivity(getIntent());
			// String imagePath =
			// Environment.getExternalStorageDirectory().toString() +
			// "/prints_spt.pdf";
			// laylistrecent.setBackgroundDrawable(Drawable.createFromPath(imagePath));
		}
	}

	@SuppressLint({"SdCardPath", "StaticFieldLeak"})
	class DownloadFile_Laporan_perj extends AsyncTask<String, String, String>

	{
		@Override
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type_lap_perj);
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			String ambil_sptnnip = selectedList.getid_spt();

			try {

				// KURANG GETS POST

				URL url = new URL(f_url[0]);

				URLConnection connection = url.openConnection();
				connection.connect();

				int lenghOfFile = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				OutputStream output = new FileOutputStream(
						"/sdcard/Download/laporan_perjalanan_dinas_"
								+ ambil_sptnnip + ".pdf");
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
				Log.e("Er Down Perj:", e.getMessage());
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
			dismissDialog(progress_bar_type_lap_perj);
			String ambil_sptnnip = selectedList.getid_spt();
			showAlert(pesan+"\nNama File : laporan_perjalanan_dinas_"+ambil_sptnnip+".pdf");
			//finish();
			//startActivity(getIntent());
		}
	}

	@SuppressLint({"SdCardPath", "StaticFieldLeak"})
	class DownloadFile_Rincian_Biaya extends AsyncTask<String, String, String>

	{
		@Override
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type_rincian);
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
						"/sdcard/Download/laporan_rincian_biaya_" + id_sppd
								+ ".pdf");
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
				Log.e("Er Down Rincian: ", e.getMessage());
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
			dismissDialog(progress_bar_type_rincian);
			String ambil_id = selectedList.getid_sppd();
			showAlert(pesan+"\nNama File : laporan_rincian_biaya_"+ambil_id+".pdf");
		}
	}

	@SuppressLint({"SdCardPath", "StaticFieldLeak"})
	class DownloadFile_Pengeluaran_Riil extends
			AsyncTask<String, String, String>

	{
		@Override
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type_riil);
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
						"/sdcard/Download/laporan_pengeluaran_riil_" + id_sppd
								+ ".pdf");
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
				Log.e("Err Down Riil:", e.getMessage());

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
			dismissDialog(progress_bar_type_riil);
			String ambil_id = selectedList.getid_sppd();
			showAlert(pesan+"\nNama File : laporan_pengeluaran_riil_"+ambil_id+".pdf");
			//finish();
			//startActivity(getIntent());
			// String imagePath =
			// Environment.getExternalStorageDirectory().toString() +
			// "/prints_spt.pdf";
			// laylistrecent.setBackgroundDrawable(Drawable.createFromPath(imagePath));
		}
	}


	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Successfully")
				.setCancelable(false)
				.setIcon(R.drawable.ic_check_circle_black_24dp)
				.setPositiveButton("Ok",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							startActivity(getIntent());
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	public void kembali_activity(View view){
		super.onBackPressed();
	}
}

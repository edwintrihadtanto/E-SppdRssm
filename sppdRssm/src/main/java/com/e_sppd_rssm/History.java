package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import koneksi.Java_Connection;
import koneksi.Koneksi;

public class History extends AppCompatActivity {
	private static final String TAG = "History";
	ListView listHistory;
	ArrayList<HashMap<String, String>> dataHistory;
	ProgressDialog progressDialog;
	RelativeLayout laylistrecent;
	String nippegawai;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_tampil_detail_history);
		laylistrecent 	= findViewById(R.id.laylistrecent);
		listHistory 	= findViewById(R.id.list_history);
		dataHistory     = new ArrayList<>();

		Bundle b = getIntent().getExtras();
		if (b != null) {
			nippegawai = b.getString("transfer_nip");
		}

		new Tampil_history().execute();
		showErrorSnackbar("Pilih salah satu history untuk mengunduh laporan.");
	}

	private void showErrorSnackbar(String message) {
		View rootView = findViewById(android.R.id.content);
		Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
				.setAction("OK", v -> {})
				.show();
	}
	private boolean isInternetAvailable() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			return ni != null && ni.isConnected();
		}
		return false;
	}
	@SuppressLint("StaticFieldLeak")
    private class Tampil_history extends AsyncTask<Void, Void, String> {
		ProgressDialog loading;
		Java_Connection jc = new Java_Connection();
		String errorMessage = null;
		@Override
		protected void onPreExecute() {

			if (!isInternetAvailable()) {
				errorMessage = "❌ Koneksi internet tidak tersedia";
				cancel(true);
				return;
			}

			loading = ProgressDialog.show(
					History.this,
					"",
					"Mengambil rincian biaya riil...",
					false
			);
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {
				String nip = nippegawai.trim();

				if (nip.trim().isEmpty()) {
					errorMessage = "NIP Kosong";
					return null;
				}

				String url = Koneksi.list_history
						+ "?nip_pegawai="
						+ URLEncoder.encode(nip, "UTF-8");

				return jc.sendGetRequest(url);

			} catch (NullPointerException e) {
				errorMessage = "❌ Data tidak lengkap (NullPointer)";
				return null;

			} catch (Exception e) {
				errorMessage = "❌ Gagal terhubung ke server";
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {

			if (loading != null && loading.isShowing()) {
				loading.dismiss();
			}

			if (result != null) {
				parsingJSON(result);
				setAdapter();
			} else {
				showErrorSnackbar(errorMessage);
			}
		}
	}
	private void tampilkanError(String pesan) {

		if (pesan == null) {
			pesan = "Terjadi kesalahan tidak diketahui";
		}

		Toast.makeText(
				History.this,
				pesan,
				Toast.LENGTH_LONG
		).show();
	}
	private void parsingJSON(String json) {

		try {
			if (json == null) {
				tampilkanError("Respon server kosong");
				return;
			}

			JSONObject obj = new JSONObject(json);

			// ❌ backend balikin error
			if (!obj.has("tampilkan_data")) {
				tampilkanError("Format respon tidak sesuai");
				return;
			}

			JSONArray arr = obj.getJSONArray("tampilkan_data");

			if (arr == null || arr.length() == 0) {
				tampilkanError("Data History tidak ditemukan!");
				return;
			}

			dataHistory.clear();

			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);

				HashMap<String, String> map = new HashMap<>();
				map.put("nomor_urut", o.optString("nomor_urut", ""));
				map.put("id_sppd", o.optString("id_sppd", ""));
				map.put("id_spt", o.optString("id_spt", ""));
				map.put("nomor_spt", o.optString("nomor_spt", ""));
				map.put("nomor_surat_sppd", o.optString("nomor_surat_sppd", ""));
				map.put("nip", o.optString("nip", ""));
				map.put("biaya_perj", o.optString("biaya_perj", ""));
				map.put("tgl_brngkt", o.optString("tgl_brngkt", ""));
				map.put("tgl_kembali", o.optString("tgl_kembali", ""));
				map.put("lama_perj", o.optString("lama_perj", ""));
				map.put("tgl_surat_masuk", o.optString("tgl_surat_masuk", ""));
				map.put("status_laporan_petugas", o.optString("status_laporan_petugas", ""));
				map.put("status_riil", o.optString("status_riil", ""));
				map.put("status_rincian_biaya", o.optString("status_rincian_biaya", ""));
				map.put("status_post", o.optString("status_post", ""));

				dataHistory.add(map);
			}

		} catch (Exception e) {
			tampilkanError("Gagal membaca data server");
		}
	}
	private void setAdapter() {

		listHistory.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return dataHistory == null ? 0 : dataHistory.size();
			}

			@Override
			public Object getItem(int i) {
				return dataHistory.get(i);
			}

			@Override
			public long getItemId(int i) {
				return i;
			}

			@RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
			@Override
			public View getView(int i, View v, ViewGroup parent) {

				if (v == null) {
					v = getLayoutInflater()
							.inflate(R.layout.row_list_detail_history, parent, false);
				}
				TextView no       	  = v.findViewById(R.id.txtNo);
				TextView nospt  	  = v.findViewById(R.id.txtNospt);
				TextView nosppd    	  = v.findViewById(R.id.txtNosppd);
				TextView tanggal      = v.findViewById(R.id.txtTanggal);
				TextView lama 		  = v.findViewById(R.id.txtLama);

				TextView badgeLap     = v.findViewById(R.id.badgeLap);
				TextView badgeRincian = v.findViewById(R.id.badgeRincian);
				TextView badgeRiil    = v.findViewById(R.id.badgeRiil);

				HashMap<String, String> d = dataHistory.get(i);

				no.setText(d.get("nomor_urut"));
				nospt.setText("SPT " + d.get("nomor_spt"));
				nosppd.setText("SPPD " + d.get("nomor_surat_sppd"));

				tanggal.setText(d.get("tgl_brngkt") + " → " + d.get("tgl_kembali"));
				lama.setText(d.get("lama_perj") + " hari");

				setBadge(badgeLap, "Laporan", d.get("status_laporan_petugas"));
				setBadge(badgeRincian, "Rincian", d.get("status_rincian_biaya"));
				setBadge(badgeRiil, "Riil", d.get("status_riil"));

				RelativeLayout rootRow = v.findViewById(R.id.rootRow);
				ImageView btnDownload = v.findViewById(R.id.btnDownload);

				rootRow.setOnClickListener(view -> {
					Toast.makeText(History.this,
							"Klik ikon ⬇ untuk mengunduh dokumen",
							Toast.LENGTH_SHORT).show();
				});

				// klik icon download
				btnDownload.setOnClickListener(view -> {

					String[] menu = {
							"Download Laporan",
							"Download SPT",
							"Download SPPD",
							"Download Rincian Biaya",
							"Download Pengeluaran Riil"
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
					builder.setTitle("Pilih dokumen yang akan diunduh");
					builder.setItems(menu, (dialog, which) -> {

						switch (which) {
							case 0:
								download(d, 1); // Laporan
								break;
							case 1:
								download(d, 2); // SPT
								break;
							case 2:
								download(d, 3); // SPPD
								break;
							case 3:
								download(d, 4); // Rincian
								break;
							case 4:
								download(d, 5); // Riil
								break;
						}
					});

					builder.show();
				});

				return v;
			}
		});
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    private void download(HashMap<String, String> d, int code) {

		String idSpt 	= d.get("id_spt");
		String idSppd 	= d.get("id_sppd");
		String noSpt 	= d.get("nomor_spt");
		String nip   	= d.get("nip");

		String status;
		String url;
		String folderName;
		String filePrefix;

		// 🔥 TENTUKAN PARAMETER BERDASARKAN CODE
		try {
			switch (code) {

				case 1: // LAPORAN
					status = d.get("status_laporan_petugas");
					if (status == null || status.contains("BELUM")) {
						toast("Anda belum memiliki Laporan Perjalanan Dinas");
						return;
					}
					url = Koneksi.download_lap_perj
							+ "?no_spt=" + URLEncoder.encode(noSpt, "UTF-8")
							+ "&&nip=" + URLEncoder.encode(nip, "UTF-8");
					folderName = "Laporan";
					filePrefix = "Laporan";
					break;

				case 2: // SPT
					url = Koneksi.download_spt
							+ "?id_spt=" + URLEncoder.encode(idSpt, "UTF-8");
					folderName = "SPT";
					filePrefix = "SPT";
					break;

				case 3: // SPPD
					url = Koneksi.download_sppd
							+ "?id_sppd=" + URLEncoder.encode(idSppd, "UTF-8");
					folderName = "SPPD";
					filePrefix = "SPPD";
					break;

				case 4: // RINCIAN
					url = Koneksi.download_rincian
							+ "?id_sppd=" + URLEncoder.encode(idSppd, "UTF-8");
					folderName = "Rincian";
					filePrefix = "Rincian";
					break;

				case 5: // RIIL
					url = Koneksi.download_riil
							+ "?id_sppd=" + URLEncoder.encode(idSppd, "UTF-8");
					folderName = "Riil";
					filePrefix = "Riil";
					break;

				default:
					toast("Kode download tidak dikenal");
					return;
			}

			// 🔒 URL FINAL
//			url = url
//					+ "?no_spt=" + URLEncoder.encode(noSpt, "UTF-8")
//					+ "&&nip=" + URLEncoder.encode(nip, "UTF-8");

		} catch (Exception e) {
			toast("Parameter download tidak valid");
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
		String safeName = noSpt.replace("/", "_");
		File pdf = new File(folder, filePrefix + "_" + safeName + ".pdf");

		// 🧠 CACHE
		if (pdf.exists()) {
			toast("File sudah ada");
			openPdf(pdf);
			return;
		}

		// ⏳ PROGRESS
		showProgress("Mengunduh " + filePrefix);

		String finalUrl = url;
		new Thread(() -> {

			Java_Connection jc = new Java_Connection();

			boolean sukses = jc.downloadFileWithProgress(
					finalUrl,
					pdf,
					progress -> runOnUiThread(() ->
							progressDialog.setProgress(progress)
					)
			);

			runOnUiThread(() -> {
				hideProgress();
				if (sukses) {
					toast(filePrefix + " berhasil diunduh");
					openPdf(pdf);
				} else {
					toast("Download gagal");
				}
			});

		}).start();
	}

	@SuppressLint("SetTextI18n")
    private void setBadge(TextView tv, String label, String status) {

		if (status == null || status.trim().isEmpty()) {
			status = "BELUM";
		}

		// 🔴 INI KUNCI: teks + konteks
		tv.setText(label + " : " + status);

		if ("SUDAH".equalsIgnoreCase(status)) {
			tv.setBackgroundResource(R.drawable.badge_sudah);
		} else {
			tv.setBackgroundResource(R.drawable.badge_belum);
		}

		tv.setVisibility(View.VISIBLE);
	}

	public void refreshhistory(View view) {
		finish();
		startActivity(getIntent());
	}

	private void showProgress(String title) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(title);
		progressDialog.setMessage("Mengunduh file...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	private void hideProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
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

	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

}

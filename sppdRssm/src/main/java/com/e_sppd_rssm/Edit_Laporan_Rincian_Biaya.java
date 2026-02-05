package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.e_sppd.rssm.BuildConfig;
import com.e_sppd.rssm.R;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import koneksi.Daftar_String;
import koneksi.JSONParser;
import koneksi.Koneksi;
import koneksi.Progres_Sistem;
import koneksi.Progres_Sistem.ProgressListener;

public class Edit_Laporan_Rincian_Biaya extends AppCompatActivity {
	private static final String TAG = "Edit_Rincian_Biaya";
	private TextView edit_lamp_sppd, status_laporan_petugas, status_riil, 
	status_rincian_biaya, edit_tgl_lamp, ambil_nip_pop, nippegwdit, txt_presentasi, txt_upload, path, untuk_cek_keluar;
	private EditText edit_dialog_uraian, edit_textjml_riil;
	public Button btn_tambh_uraian_upload, btn_upload_image, btn_camera, btn_refresh;
	private ListView list_edit_rincian;
	private List<Daftar_String> list;
	private ProgressDialog loading;
	public ImageView ImageView_Help, imgPreview, btn_bantuan;
	private Daftar_String selectedList;
	private List_Edit_Rincian adapter;
	private Koneksi Koneksi_Server;
	private ProgressBar progressBar;

	View vedit;
	//private static final String hapus_data_per_uraian 		= "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/hapus_file/hapus_data_per_uraian.php";

	private static final String TAG_SUKSES 					= "berhasil";
	private static final String TAG_PESAN 					= "tampilkan_pesan";
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
	private static final int GALERY_CAPTURE_IMAGE_REQUEST_CODE = 2;
	private static final int MEDIA_TYPE_IMAGE = 1;
	public final int REQUEST_CAMERA = 0;

	JSONParser classJsonParser = new JSONParser();
	private Uri fileUri;	
	long totalSize = 0;
	public String ambil_lokasi_path = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_rincian_biaya_dinas);
		edit_lamp_sppd 			= findViewById(R.id.edit_lamp_sppd);
		status_laporan_petugas 	= findViewById(R.id.status_laporan_petugas);
		status_riil 			= findViewById(R.id.status_riil);
		status_rincian_biaya	= findViewById(R.id.status_rincian_biaya);
		edit_tgl_lamp			= findViewById(R.id.edit_tgl_lamp);
		nippegwdit				= findViewById(R.id.nippegwdit);
		btn_tambh_uraian_upload = findViewById(R.id.btn_tambh_uraian_upload);
		list_edit_rincian		= findViewById(R.id.list_edit_rincian);
//		ImageView_Help			= findViewById(R.id.ImageView_Help);
		txt_presentasi			= findViewById(R.id.txt_presentasi);
		progressBar				= findViewById(R.id.progressBar);
		btn_refresh 			= findViewById(R.id.btn_refresh_sppd);
		untuk_cek_keluar		= findViewById(R.id.untuk_cek_keluar);
		txt_upload 				= findViewById(R.id.txt_upload);
		vedit 					= findViewById(R.id.vedit);
		sembunyikan_progressupload();
		Tampil_data();

		btn_tambh_uraian_upload.setOnClickListener(v -> {
			// TODO Auto-generated method stub
			dialog_pop_up_tambah_uraian();
		});
		Koneksi_Server = new Koneksi();
		list 		= new ArrayList<Daftar_String>();
		new MainActivityAsync().execute();

		btn_bantuan =  findViewById(R.id.btn_bantuan);
        btn_bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* String pesan = "Sipp Mantap"; showAlert(pesan); */
                Intent i = null;
                i = new Intent(Edit_Laporan_Rincian_Biaya.this,
                        Tampil_Bantuan.class);
                startActivity(i);
            }
        });
	}
	public void sembunyikan_progressupload() {
		txt_presentasi.setVisibility(View.GONE);
		txt_upload.setVisibility(View.GONE);
		progressBar.setVisibility(View.GONE);
		vedit.setVisibility(View.GONE);

	}
	public void tampilkan_progressupload() {
		txt_presentasi.setVisibility(View.VISIBLE);
		txt_upload.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		vedit.setVisibility(View.VISIBLE);

	}
	public void Tampil_data() {
		// DIBAWAH INI DIAMBIL DARI DAFTAR LAPORAN
		String nip_pegawai					= getIntent().getStringExtra("nip");
		/* String nama 			= getIntent().getStringExtra("nama_pegawai"); String jab 				= getIntent().getStringExtra("jabatan");*/
		String nomor_sppd 					= getIntent().getStringExtra("nomor_surat_sppd");
		String tgl_surat_masuk 				= getIntent().getStringExtra("tgl_surat_masuk");
		String ambil_status_laporan_petugas = getIntent().getStringExtra("status_laporan_petugas");
		String ambil_status_riil 			= getIntent().getStringExtra("status_riil");
		String ambil_status_rincian 		= getIntent().getStringExtra("status_rincian_biaya");

		nippegwdit.setText(nip_pegawai);
		edit_lamp_sppd.setText(nomor_sppd);
		edit_tgl_lamp.setText(tgl_surat_masuk);
		status_laporan_petugas.setText(ambil_status_laporan_petugas);
		status_riil.setText(ambil_status_riil);
		status_rincian_biaya.setText(ambil_status_rincian);
	}
	
	@SuppressLint("SetTextI18n")
	private void dialog_pop_up_tambah_uraian() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up_perincian_riil);
		edit_dialog_uraian 	= dialog.findViewById(R.id.edit_dialog_uraian);
		edit_textjml_riil 	= dialog.findViewById(R.id.edit_textjml_riil);
		btn_upload_image 	= dialog.findViewById(R.id.btn_upload_image);
		btn_camera 			= dialog.findViewById(R.id.btn_camera);
		imgPreview 			= dialog.findViewById(R.id.imgPreview);
		TextView judul 		= dialog.findViewById(R.id.judul);
	
		Button btn_cncl 	= dialog.findViewById(R.id.btn_cncl);
		Button btn_simpan 	= dialog.findViewById(R.id.btn_simpan);

		path = dialog.findViewById(R.id.path);
		
		judul.setText("Pengisian Uraian Rincian Biaya");
		dialog.show();

		btn_upload_image.setOnClickListener(v -> {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.putExtra("image for", "1");
			startActivityForResult(Intent.createChooser(intent,
					"Pilih Gambar Bukti Transaksi"), GALERY_CAPTURE_IMAGE_REQUEST_CODE);
			btn_upload_image.setText("Ambil Gambar Dari Galery");
			btn_camera.setText("Ambil Gambar Dari Kamera");
			imgPreview.setVisibility(View.GONE);
			path.setText("");
		});
		btn_camera.setOnClickListener(v -> {
			// capture picture
			btn_upload_image.setText("Ambil Gambar Dari Galery");
			btn_camera.setText("Ambil Gambar Dari Kamera");
			imgPreview.setVisibility(View.GONE);
			path.setText("");
			captureImage();
		});

		btn_simpan.setOnClickListener(v -> {

			if (edit_dialog_uraian.getText().toString().isEmpty()) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this,
						"Data Wajib Diisi", Toast.LENGTH_SHORT).show();
			} else if (edit_textjml_riil.getText().toString().isEmpty()) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this,
						"Data Wajib Diisi", Toast.LENGTH_SHORT).show();
			} else if (path.getText().toString().isEmpty()) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this,
						"Anda Belum Melakukan Upload Bukti",
						Toast.LENGTH_SHORT).show();
				// PROSES SIMPAN DATA APABILA TIDAK UPLOAD FOTO
			} else {
				if (btn_upload_image.getText().toString()
						.contains("Ambil Gambar Dari Galery")) {
					dialog.dismiss();

					new UploadFileToServer().execute(); //DIAMBIL DARI BTN CAMERA
				} else if (btn_camera.getText().toString()
						.contains("Ambil Gambar Dari Kamera")) {

					dialog.dismiss();
					new UploadFileToServer().execute(); //DIAMBIL DARI BTN GALERY


				}

			}
		});

		btn_cncl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Maaf, Perangkat Smartphone Anda Tidak Mendukung",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}

	}
	//BERIKUT PROSES KETIKA AMBIL GAMBAR / KAMERA
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case GALERY_CAPTURE_IMAGE_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
					imgPreview.setVisibility(View.VISIBLE);
					final Uri uri1 = data.getData();
					final String path1;
					//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						path1 = FileUtils.getPath(this, uri1);

						btn_upload_image.setText(path1);
						path.setText(path1);

						BitmapFactory.Options options = new BitmapFactory.Options();
						// downsizing image as it throws OutOfMemory Exception for larger
						// images
						options.inSampleSize = 4;
						final Bitmap bitmap = BitmapFactory.decodeFile(path1, options);

						imgPreview.setImageBitmap(bitmap);
					}

				}
				break;
			case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
				if (resultCode == RESULT_OK) {

					// successfully captured the image
					// launching upload activity
					 //launchUploadActivity(true);


					final String nm_dir = fileUri.getPath();// BERFUNGSI MENGAMBIL
					// LOKASI PATH
					path.setText(nm_dir);
					path.setVisibility(View.VISIBLE);
					btn_camera.setText(nm_dir);
					imgPreview.setVisibility(View.VISIBLE);
					previewCapturedImage();
				} else if (resultCode == RESULT_CANCELED) {

					// user cancelled Image capture
					Toast.makeText(getApplicationContext(),
							"Batal Mengambil Gambar", Toast.LENGTH_SHORT).show();

				} else {
					// failed to capture image
					Toast.makeText(getApplicationContext(),
							"Maaf, Gagal Mengambil Gambar", Toast.LENGTH_SHORT)
							.show();
				}
				break;

		}
	}
	

	/**
	 * Uploading the file to server
	 */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			progressBar.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Making progress bar visible
			//progressBar.setVisibility(View.VISIBLE);
			//txt_presentasi.setVisibility(View.VISIBLE);
			tampilkan_progressupload();
			btn_refresh.setEnabled(false);
			btn_tambh_uraian_upload.setEnabled(false);
			untuk_cek_keluar.setText("1");
			// updating progress bar value
			progressBar.setProgress(progress[0]);

			// updating percentage value
			txt_presentasi.setText(String.valueOf(progress[0]) + "%");
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings({ "deprecation", "resource" })
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Koneksi.FILE_UPLOAD_URL);

			try {
				Progres_Sistem entity = new Progres_Sistem(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});
				ambil_lokasi_path = path.getText().toString();
				File sourceFile = new File(ambil_lokasi_path);
				String nosppd 	= edit_lamp_sppd.getText().toString();
				String ambilnip = nippegwdit.getText().toString().trim();
				String uraian 	= edit_dialog_uraian.getText().toString().trim();
				String jml 		= edit_textjml_riil.getText().toString().trim();
				//String tgl_buat = tgl_pembuatan_rincian.getText().toString().trim();
				
				// Adding file data to http body				
				entity.addPart("nosppd"		, new StringBody(nosppd));
				entity.addPart("ambilnip"	, new StringBody(ambilnip));
				entity.addPart("uraian"		, new StringBody(uraian));
				entity.addPart("jml"		, new StringBody(jml));
				//entity.addPart("tgl_buat"	, new StringBody(tgl_buat));
				entity.addPart("image"		, new FileBody(sourceFile));
				// Extra parameters if you want to pass to server
				
				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Respon Dari Server ::: " + "Error");

			// showing the server response in an alert dialog
			informasi_progressbar(result);

			super.onPostExecute(result);
		}

	}
	
	private void previewCapturedImage() {
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);
			//final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(fileUri.getPath())));
			imgPreview.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	private void captureImage() {
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////		fileUri = getOutputMediaFileUri();
////		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
////
////		// start the image capture Intent
////		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//fileUri = getOutputMediaFileUri();
		fileUri = getOutputMediaFileUri();
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	
	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri() {
		//return Uri.fromFile(getOutputMediaFile(type));
		return FileProvider.getUriForFile(Edit_Laporan_Rincian_Biaya.this,
				BuildConfig.APPLICATION_ID + ".provider",
				Objects.requireNonNull(getOutputMediaFile()));
	}

	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}
	
	private boolean isDeviceSupportCamera() {
		// this device has a camera
		// no camera on this device
		return getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA);
	}
	
	private static File getOutputMediaFile() {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Koneksi.IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "Error Buat Dir " + Koneksi.IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		//if (type == MEDIA_TYPE_IMAGE) {
//			mediaFile = new File(mediaStorageDir.getPath() + File.separator
//					+ "Bukti_" + timeStamp + ".jpg");
			mediaFile = new File(mediaStorageDir.getPath() + "Bukti_123" + ".jpg");
			// } else if (type == MEDIA_TYPE_VIDEO) {
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator
			// + "VID_" + timeStamp + ".mp4");
//		} else {
//			return null;
//		}

		return mediaFile;
	}

	private List<Daftar_String> ambil_editan_rincian(String response) {
		List<Daftar_String> list_Daftar_String = new ArrayList<Daftar_String>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampilkan_saja");
			Log.d(TAG, "data lengt: " + jsonArray.length());
			Daftar_String edit_rincian = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				edit_rincian = new Daftar_String();				
				edit_rincian.setid_rincian(obj.getString("id_rincian")); //AMBIL POSTINGAN JUMLAHNYA
				edit_rincian.seturaian_rincian(obj.getString("rincian_biaya")); 
				edit_rincian.setjml_rincian(obj.getString("jumlah")); 
				edit_rincian.setbukti_rincian(obj.getString("bukti_image")); 
				list_Daftar_String.add(edit_rincian);
				
			}
		} catch (JSONException e) {

			if (e.getMessage() != null) {
				Log.d(TAG, e.getMessage());
			} else {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this,
						"Gagal Mengambil Data", Toast.LENGTH_LONG).show();
			}

		}
		return list_Daftar_String;
	}
	
	@SuppressLint("StaticFieldLeak")
	private class MainActivityAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(Edit_Laporan_Rincian_Biaya.this);
			loading.setMessage("Loading . . .");
			loading.setIndeterminate(false);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(String... params) {

			/* Mengirimkan request ke server dan memproses JSON response */
			String nomor_sppd 	= getIntent().getStringExtra("nomor_surat_sppd");
			String Cek 			= String.valueOf(nomor_sppd);
			String url;

			try {

				url = Koneksi_Server
						.sendGetRequest(Koneksi.tampil_daftar_edit_rincian
								+ "?nomor_surat_sppd="
								+ URLEncoder.encode(Cek, "UTF-8"));				
				list 		= ambil_editan_rincian(url);
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			loading.dismiss();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					menampilkan_nama_pegawai();				
				}
			});
		}

	}
	
	private void menampilkan_nama_pegawai() {
		if (!terkoneksi(Edit_Laporan_Rincian_Biaya.this)) {
			String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			showAlert(pesan);
		} else {
			
			adapter = new List_Edit_Rincian(getApplicationContext(), list);						
			list_edit_rincian.setAdapter(adapter);		
			adapter.notifyDataSetChanged();
			//list_edit_rincian.notifyAll();
			list_edit_rincian.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					selectedList = (Daftar_String) adapter.getItem(pos);
					tampil_pilihan_menu();
					
				}
			});
		}
	}

	private boolean terkoneksi(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				
				.setPositiveButton("Coba Lagi",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								finish();
								startActivity(getIntent());
								
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}
	
	private void tampil_pilihan_menu() {
		final Dialog dialog2 = new Dialog(this);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog2.setContentView(R.layout.dialog_pilihan_edit);
		dialog2.closeOptionsMenu();
		
		Button btn_editrincian_riil 	= dialog2.findViewById(R.id.btn_editrincian_riil);
		Button btn_hapus_rincian_riil 	= dialog2.findViewById(R.id.btn_hapus_rincian_riil);

		dialog2.show();
		btn_editrincian_riil.setOnClickListener(v -> {
			dialog2.dismiss();
			dialog_pop_up_edit_uraian();
		});
		btn_hapus_rincian_riil.setOnClickListener(v -> {
			dialog2.dismiss();
			pertanyaan_hapus();
		});
		
	}
	private void dialog_pop_up_edit_uraian() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up_perincian_riil);
		String id_rincian 	= selectedList.getid_rincian();		
		String ambil_uraian = selectedList.geturaian_rincian();
		String jml_uraian 	= selectedList.getjml_rincian();
	//	String bukti 		= selectedList.getbukti_rincian();
		
		edit_dialog_uraian 	=  dialog.findViewById(R.id.edit_dialog_uraian);
		edit_textjml_riil 	=  dialog.findViewById(R.id.edit_textjml_riil);		
		btn_upload_image 	=  dialog.findViewById(R.id.btn_upload_image);
		btn_camera 			=  dialog.findViewById(R.id.btn_camera);
		imgPreview 			=  dialog.findViewById(R.id.imgPreview);
		TextView judul 		=  dialog.findViewById(R.id.judul);
		TextView atau 		=  dialog.findViewById(R.id.atau);
		ambil_nip_pop		=  dialog.findViewById(R.id.ambil_nip_pop);
		
		Button btn_cncl 	=  dialog.findViewById(R.id.btn_cncl);
		Button btn_simpan 	=  dialog.findViewById(R.id.btn_simpan);
		btn_simpan.setText("Update Data");
		btn_upload_image.setVisibility(View.GONE);
		btn_camera.setVisibility(View.GONE);
		imgPreview.setVisibility(View.GONE);
		atau.setVisibility(View.GONE);
		judul.setText("Edit Uraian Rincian Biaya");
		
		ambil_nip_pop.setText(id_rincian); //ID RINCIAN URAIAN
		edit_dialog_uraian.setText(ambil_uraian);
		edit_textjml_riil.setText(jml_uraian);
		
		dialog.show();
		btn_simpan.setOnClickListener(v -> {

			if (edit_dialog_uraian.getText().toString().isEmpty()) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this,
						"Uraian Rincian Belum Diisi", Toast.LENGTH_SHORT).show();
			} else if (edit_textjml_riil.getText().toString().isEmpty()) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this,
						"Jumlah Rincian Belum Diisi", Toast.LENGTH_SHORT).show();
			} else {
				dialog.dismiss();
				new Update_Rincian().execute();
			}
		});

		btn_cncl.setOnClickListener(v -> dialog.dismiss());
	}
	
	private void pertanyaan_hapus() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String id = selectedList.getid_rincian();
		String ambil_uraian = selectedList.geturaian_rincian();
		String jml_uraian 	= selectedList.getjml_rincian();
		
		ad.setTitle("Informasi");
		ad.setMessage("ID : "+id+"\nRincian : " + ambil_uraian + "\nJumlah : Rp. "+ jml_uraian +"\n\n" +
				"Apakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				new Hapus_Rincian_Per_Id().execute();				
			}
		});
		ad.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ad.show();
	}
	
	public class Hapus_Rincian_Per_Id extends AsyncTask<String, String, String> {
		ProgressDialog pd ;
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			
			pd = new ProgressDialog(Edit_Laporan_Rincian_Biaya.this);
			pd.setMessage("Hapus Uraian !!!");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;

			String id_rincian 	= selectedList.getid_rincian();
			String ambil_uraian = selectedList.geturaian_rincian();
			String no_sppd 		= edit_lamp_sppd.getText().toString();

			try {

				List<NameValuePair> oio = new ArrayList<NameValuePair>();
				oio.add(new BasicNameValuePair("no_sppd", no_sppd));
				oio.add(new BasicNameValuePair("id_rincian", id_rincian));
				oio.add(new BasicNameValuePair("uraian", ambil_uraian));

				Log.d("Start Goo !!!", "Majuuu");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.hapus_data_per_uraian, "POST", oio);
				Log.d("Coba login", jsonObjectNya.toString());
				
				jikaSukses = jsonObjectNya.getInt(TAG_SUKSES);
				if (jikaSukses == 1) {
					Log.d("Hapus Sukses!", jsonObjectNya.toString());
					
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					Log.d("Hapus Gagal!",jsonObjectNya.getString(TAG_PESAN));
					
					return jsonObjectNya.getString(TAG_PESAN);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String urlFileNya) {
			pd.dismiss();
			
			if (urlFileNya != null) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this, urlFileNya, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}else{
				finish();
				startActivity(getIntent());
			}

		}

	}

	public class Update_Rincian extends AsyncTask<String, String, String> {
		ProgressDialog pd ;
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			
			pd = new ProgressDialog(Edit_Laporan_Rincian_Biaya.this);
			pd.setMessage("Update Rincian Biaya ...!!!");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;
						
			String uraian 	  = edit_dialog_uraian.getText().toString();
			String jml 		  = edit_textjml_riil.getText().toString();
			String no_sppd 	  = edit_lamp_sppd.getText().toString();
			String id_rincian = ambil_nip_pop.getText().toString();
			String nip_pegawai= nippegwdit.getText().toString();
			try {

				List<NameValuePair> oio = new ArrayList<NameValuePair>();
				oio.add(new BasicNameValuePair("nomor_surat_sppd", no_sppd));
				oio.add(new BasicNameValuePair("uraian", uraian));
				oio.add(new BasicNameValuePair("jml", jml));
				oio.add(new BasicNameValuePair("id_rincian", id_rincian));
				oio.add(new BasicNameValuePair("nip", nip_pegawai));
				
				Log.d("Start Goo !!!", "Majuuu");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.update_rincian_biaya, "POST", oio);
				Log.d("Coba login", jsonObjectNya.toString());
				
				jikaSukses = jsonObjectNya.getInt(TAG_SUKSES);
				if (jikaSukses == 1) {
					Log.d("Edit Sukses!", jsonObjectNya.toString());
					
					return jsonObjectNya.getString(TAG_PESAN);
				} else{
					Log.d("Edit Gagal!",jsonObjectNya.getString(TAG_PESAN));
					
					return jsonObjectNya.getString(TAG_PESAN);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String urlFileNya) {
			pd.dismiss();
			
			if (urlFileNya != null) {
				Toast.makeText(Edit_Laporan_Rincian_Biaya.this, urlFileNya, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}
			

		}

	}
	@Override
	public void onBackPressed() {
		String persen = untuk_cek_keluar.getText().toString();
		
		
		if (persen.contains("1")){
			String pesan = "Masih Dalam Proses Upload\nTunggu Sebentar ...!!!";
			informasi(pesan);
		}else{
			finish();
			Edit_Laporan_Rincian_Biaya.this.finish();
		}
		
	}
	private void informasi(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							
								
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void informasi_progressbar(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								finish();
								startActivity(getIntent());
								
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}


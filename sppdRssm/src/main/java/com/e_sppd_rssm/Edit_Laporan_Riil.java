package com.e_sppd_rssm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import koneksi.Daftar_String;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class Edit_Laporan_Riil extends AppCompatActivity {
	private static final String TAG = "Edit_Laporan_Riil";
	private TextView nip, nama, jab, status_rincian_biaya, status_riil, ambil_nip_pop;
	private EditText edit_dialog_uraian, edit_textjml_riil;
	public Button btn_tambh_uraian_riil, btn_refresh;
	private ListView list_edit_riil;
	private List<Daftar_String> list;
	private ProgressDialog loading;
	private ImageView img_bantuan_edit_riil;
	private Daftar_String selectedList;
	private List_Edit_Riil adapter;
	private Koneksi Koneksi_Server;
	private static final String TAG_SUKSES 	= "berhasil";
	private static final String TAG_PESAN 	= "tampilkan_pesan";
    String ambil_sppd, ambil_tgl_sppd;

    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_rincian_riil);
		nip 	=  findViewById(R.id.edit_nip_pembuat);
		nama 	=  findViewById(R.id.edit_nama_pembuat);
		jab 	=  findViewById(R.id.edit_jab_pembuat);
		
		status_rincian_biaya	=  findViewById(R.id.status_rincian_biaya);
		status_riil				=  findViewById(R.id.status_riil);
		
		btn_tambh_uraian_riil 	=  findViewById(R.id.btn_tambh_uraian_riil);
		
		list_edit_riil			=  findViewById(R.id.list_edit_riil);
		img_bantuan_edit_riil	=  findViewById(R.id.img_bantuan_edit_riil);
		
		btn_refresh 			=  findViewById(R.id.btn_refresh_editriil);
		
		
		Tampil_data();
		
		btn_tambh_uraian_riil.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            dialog_pop_up_tambah_riil();
        });
		
		Koneksi_Server = new Koneksi();
		list 		= new ArrayList<Daftar_String>();
		
		new MainActivityAsync().execute();
		img_bantuan_edit_riil.setOnClickListener(v -> {
            //String pesan = "Sipp Mantap";
            //showAlert(pesan);
            Intent i = null;
            i = new Intent(Edit_Laporan_Riil.this,
                    Tampil_Bantuan.class);
            startActivity(i);
        });
	}
	public void Tampil_data() {
		// DIBAWAH INI DIAMBIL DARI DAFTAR LAPORAN
		String ambil_nip = getIntent().getStringExtra("nip");
		String ambil_nama = getIntent().getStringExtra("nama_pegawai");
		String ambil_jab = getIntent().getStringExtra("jabatan");
		ambil_sppd = getIntent().getStringExtra("nomor_surat_sppd");
		ambil_tgl_sppd = getIntent().getStringExtra("tgl_surat_masuk");
		nama.setText(ambil_nama);
		nip.setText(ambil_nip);
		jab.setText(ambil_jab);
		
	}
		
	private void dialog_pop_up_tambah_riil() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up_perincian_riil);		
	
		edit_dialog_uraian 			= (EditText) dialog.findViewById(R.id.edit_dialog_uraian);
		edit_textjml_riil 			= (EditText) dialog.findViewById(R.id.edit_textjml_riil);		
		Button btn_upload_image 	=  dialog.findViewById(R.id.btn_upload_image);
		Button btn_camera 			=  dialog.findViewById(R.id.btn_camera);
		
		TextView judul 				=  dialog.findViewById(R.id.judul);
		TextView atau 				=  dialog.findViewById(R.id.atau);
		ambil_nip_pop				=  dialog.findViewById(R.id.ambil_nip_pop);
		
		Button btn_cncl 	=  dialog.findViewById(R.id.btn_cncl);
		Button btn_simpan 	=  dialog.findViewById(R.id.btn_simpan);
		btn_simpan.setText("Simpan Data");
		btn_upload_image.setVisibility(View.GONE);
		btn_camera.setVisibility(View.GONE);
		
		atau.setVisibility(View.GONE);				
		judul.setText("Tambah Uraian Pengeluaran Riil");
				
		
		dialog.show();
		btn_simpan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (edit_dialog_uraian.getText().toString().isEmpty()) {
					Toast.makeText(Edit_Laporan_Riil.this,
							"Uraian Riil Belum Diisi", Toast.LENGTH_SHORT).show();
				} else if (edit_textjml_riil.getText().toString().isEmpty()) {
					Toast.makeText(Edit_Laporan_Riil.this,
							"Jumlah Riil Belum Diisi", Toast.LENGTH_SHORT).show();
				} else {				
					dialog.dismiss();					
					new Tambah_Uraian_Baru().execute();
				}
			}
		});

		btn_cncl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});	
	}
	private List<Daftar_String> ambil_editan_riil(String response) {
		List<Daftar_String> list_Daftar_String = new ArrayList<Daftar_String>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampilkan_saja");
			Log.d(TAG, "data lengt: " + jsonArray.length());
			Daftar_String edit_riil = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				edit_riil = new Daftar_String();				
				edit_riil.setid_riil(obj.getString("id_riil")); //AMBIL POSTINGAN JUMLAHNYA
				edit_riil.seturaian_riil(obj.getString("uraian_daftar_riil")); 
				edit_riil.setjml_riil(obj.getString("jumlah_riil")); 				
				list_Daftar_String.add(edit_riil);
				
			}
		} catch (JSONException e) {

			if (e.getMessage() != null) {
				Log.d(TAG, e.getMessage());
			} else {
				Toast.makeText(Edit_Laporan_Riil.this,
						"Gagal Mengambil Data", Toast.LENGTH_LONG).show();
			}

		}
		return list_Daftar_String;
	}
	
	private class MainActivityAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			loading = new ProgressDialog(Edit_Laporan_Riil.this);
			loading.setMessage("Loading . . .");
			loading.setIndeterminate(false);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(String... params) {

			/*Mengirimkan request ke server dan memproses JSON response*/
			String nomor_sppd 	= getIntent().getStringExtra("nomor_surat_sppd");
			String Cek 			= String.valueOf(nomor_sppd);
			String url;

			try {

				url = Koneksi_Server
						.sendGetRequest(Koneksi.tampil_daftar_edit_riil
								+ "?nomor_surat_sppd="
								+ URLEncoder.encode(Cek, "UTF-8"));				
				list 		= ambil_editan_riil(url);
			
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
		if (!terkoneksi(Edit_Laporan_Riil.this)) {
			String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			showAlert(pesan);
			return;
		} else {
			
			adapter = new List_Edit_Riil(getApplicationContext(), list);						
			list_edit_riil.setAdapter(adapter);		
			//adapter.notifyDataSetChanged();
			//list_edit_rincian.notifyAll();
			list_edit_riil.setOnItemClickListener(new OnItemClickListener() {

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
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {			
			return true;
		}
		return false;

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
		
		Button btn_editrincian_riil 	=  dialog2.findViewById(R.id.btn_editrincian_riil);
		Button btn_hapus_rincian_riil 	=  dialog2.findViewById(R.id.btn_hapus_rincian_riil);		

		dialog2.show();
		btn_editrincian_riil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				dialog2.dismiss();
				dialog_pop_up_edit_riil();
			}				
		});
		btn_hapus_rincian_riil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				dialog2.dismiss();
				pertanyaan_hapus();
			}				
		});
		
	}
	
	
	private void pertanyaan_hapus() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String id 			= selectedList.getid_riil();
		String ambil_uraian = selectedList.geturaian_riil();
		String jml_uraian 	= selectedList.getjml_riil();
		
		ad.setTitle("Informasi");
		ad.setMessage("ID : "+id+"\nUraian : " + ambil_uraian + "\nJumlah : Rp. "+ jml_uraian +"\n\n" +
				"Apakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				new Hapus_Riil_Per_Id().execute();				
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
	
	/*public class Hapus_Riil_Per_Id extends AsyncTask<String, String, String> {
		ProgressDialog pd ;
			JSONParser classJsonParser = new JSONParser();
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			
			pd = new ProgressDialog(Edit_Laporan_Riil.this);
			pd.setMessage("Hapus Uraian !!!");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;

			String id_riil 		= selectedList.getid_riil();
			String ambil_uraian = selectedList.geturaian_riil();
			//String no_sppd 		= edit_lamp_sppd.getText().toString();

			try {

				List<NameValuePair> oio = new ArrayList<NameValuePair>();
				//oio.add(new BasicNameValuePair("no_sppd", no_sppd));
				oio.add(new BasicNameValuePair("id_riil", id_riil));
				oio.add(new BasicNameValuePair("uraian_daftar_riil", ambil_uraian));

				Log.d("Start Goo !!!", "Majuuu");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.hapus_data_per_riil, "POST", oio);
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
				Toast.makeText(Edit_Laporan_Riil.this, urlFileNya, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}else{
				finish();
				startActivity(getIntent());
			}

		}

	}*/

	public class Hapus_Riil_Per_Id extends AsyncTask<String, String, String> {

		ProgressDialog pd;
		Java_Connection jc = new Java_Connection();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = new ProgressDialog(Edit_Laporan_Riil.this);
			pd.setMessage("Hapus Uraian !!!");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(String... args) {

			String id_riil = selectedList.getid_riil();
			String ambil_uraian = selectedList.geturaian_riil();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("id_riil", id_riil);
				params.put("uraian_daftar_riil", ambil_uraian);

				Log.d("HAPUS_RIIL", "POST DATA = " + params.toString());

				String response = jc.sendPostRequest(
						Koneksi.hapus_data_per_riil,
						params
				);

				if (response == null || response.isEmpty()) {
					Log.e("HAPUS_RIIL", "Response NULL / kosong");
					return null;
				}

				Log.d("HAPUS_RIIL", "RAW RESPONSE = " + response);

				JSONObject json = new JSONObject(response);

				int sukses = json.getInt(TAG_SUKSES);
				String pesan = json.getString(TAG_PESAN);

				if (sukses == 1) {
					Log.d("HAPUS_RIIL", "SUKSES: " + pesan);
				} else {
					Log.e("HAPUS_RIIL", "GAGAL: " + pesan);
				}

				return pesan;

			} catch (Exception e) {
				Log.e("HAPUS_RIIL", "EXCEPTION", e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			pd.dismiss();

			if (hasil != null) {
				Toast.makeText(Edit_Laporan_Riil.this, hasil, Toast.LENGTH_LONG).show();
			}

			finish();
			startActivity(getIntent());
		}
	}


	private void dialog_pop_up_edit_riil() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up_perincian_riil);
		String id_riil 		= selectedList.getid_riil();
		String ambil_uraian = selectedList.geturaian_riil();
		String jml_riil 	= selectedList.getjml_riil();
	
		edit_dialog_uraian 	= (EditText) dialog.findViewById(R.id.edit_dialog_uraian);
		edit_textjml_riil 	= (EditText) dialog.findViewById(R.id.edit_textjml_riil);		
		Button btn_upload_image 	=  dialog.findViewById(R.id.btn_upload_image);
		Button btn_camera 			=  dialog.findViewById(R.id.btn_camera);
		
		TextView judul 		=  dialog.findViewById(R.id.judul);
		TextView atau 		=  dialog.findViewById(R.id.atau);
		ambil_nip_pop		=  dialog.findViewById(R.id.ambil_nip_pop);
		
		Button btn_cncl 	=  dialog.findViewById(R.id.btn_cncl);
		Button btn_simpan 	=  dialog.findViewById(R.id.btn_simpan);
		btn_simpan.setText("Update Data");
		btn_upload_image.setVisibility(View.GONE);
		btn_camera.setVisibility(View.GONE);
		ambil_nip_pop.setVisibility(View.VISIBLE);
		atau.setVisibility(View.GONE);				
		judul.setText("Edit Uraian Pengeluaran Riil");
		
		ambil_nip_pop.setText(id_riil); //ID RINCIAN URAIAN
		
		edit_dialog_uraian.setText(ambil_uraian);
		edit_textjml_riil.setText(jml_riil);
		
		dialog.show();
		btn_simpan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (edit_dialog_uraian.getText().toString().isEmpty()) {
					Toast.makeText(Edit_Laporan_Riil.this,
							"Uraian Riil Belum Diisi", Toast.LENGTH_SHORT).show();
				} else if (edit_textjml_riil.getText().toString().isEmpty()) {
					Toast.makeText(Edit_Laporan_Riil.this,
							"Jumlah Riil Belum Diisi", Toast.LENGTH_SHORT).show();
				} else {				
					dialog.dismiss();					
					new Update_Riil().execute();
				}
			}
		});

		btn_cncl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});	
	}
	
	/*public class Update_Riil extends AsyncTask<String, String, String> {
		ProgressDialog pd ;
		@Override
		protected void onPreExecute() {

			super.onPreExecute();

			pd = new ProgressDialog(Edit_Laporan_Riil.this);
			pd.setMessage("Update Uraian Riil ...!!!");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;

			String id_riil 				  = ambil_nip_pop.getText().toString();
			String uraian_daftar_riil 	  = edit_dialog_uraian.getText().toString();
			String jumlah_riil 		  	  = edit_textjml_riil.getText().toString();

			try {

				List<NameValuePair> oio = new ArrayList<NameValuePair>();
				oio.add(new BasicNameValuePair("id_riil", id_riil));
				oio.add(new BasicNameValuePair("uraian_daftar_riil", uraian_daftar_riil));
				oio.add(new BasicNameValuePair("jumlah_riil", jumlah_riil));

				Log.d("Start Goo !!!", "Majuuu");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.update_riil, "POST", oio);
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
			}catch (Exception e) {
                e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String urlFileNya) {
			pd.dismiss();

			if (urlFileNya != null) {
				Toast.makeText(Edit_Laporan_Riil.this, urlFileNya, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}


		}

	}*/
	public class Update_Riil extends AsyncTask<String, String, String> {

		ProgressDialog pd;
		Java_Connection jc = new Java_Connection();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = new ProgressDialog(Edit_Laporan_Riil.this);
			pd.setMessage("Update Uraian Riil ...!!!");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(String... args) {

			String id_riil = ambil_nip_pop.getText().toString();
			String uraian_daftar_riil = edit_dialog_uraian.getText().toString();
			String jumlah_riil = edit_textjml_riil.getText().toString();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("id_riil", id_riil);
				params.put("uraian_daftar_riil", uraian_daftar_riil);
				params.put("jumlah_riil", jumlah_riil);

				Log.d("UPDATE_RIIL", "POST DATA = " + params.toString());

				String response = jc.sendPostRequest(
						Koneksi.update_riil,
						params
				);

				if (response == null || response.isEmpty()) {
					Log.e("UPDATE_RIIL", "Response NULL / kosong");
					return null;
				}

				Log.d("UPDATE_RIIL", "RAW RESPONSE = " + response);

				JSONObject json = new JSONObject(response);

				int sukses = json.getInt(TAG_SUKSES);
				String pesan = json.getString(TAG_PESAN);

				if (sukses == 1) {
					Log.d("UPDATE_RIIL", "SUKSES: " + pesan);
				} else {
					Log.e("UPDATE_RIIL", "GAGAL: " + pesan);
				}

				return pesan;

			} catch (Exception e) {
				Log.e("UPDATE_RIIL", "EXCEPTION", e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			pd.dismiss();

			if (hasil != null) {
				Toast.makeText(Edit_Laporan_Riil.this, hasil, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}
		}
	}

	/*public class Tambah_Uraian_Baru extends AsyncTask<String, String, String> {
		ProgressDialog pd ;
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			
			pd = new ProgressDialog(Edit_Laporan_Riil.this);
			pd.setMessage("Sedang Dalam Proses Simpan Data...");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;
		
			String nip_riil 			  = nip.getText().toString();
			String uraian_daftar_riil 	  = edit_dialog_uraian.getText().toString();
			String jumlah_riil 		  	  = edit_textjml_riil.getText().toString();

			try {

				List<NameValuePair> oio = new ArrayList<NameValuePair>();
                oio.add(new BasicNameValuePair("nomor_sppd", ambil_sppd));
                oio.add(new BasicNameValuePair("nip", nip_riil));
				oio.add(new BasicNameValuePair("uraian_daftar_riil", uraian_daftar_riil));
				oio.add(new BasicNameValuePair("jumlah_riil", jumlah_riil));
											
				Log.d("Start Goo !!!", "Majuuu");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.tambah_uraian_riil, "POST", oio);
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
				Toast.makeText(Edit_Laporan_Riil.this, urlFileNya, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}
			

		}

	}*/

	public class Tambah_Uraian_Baru extends AsyncTask<String, String, String> {

		ProgressDialog pd;
		Java_Connection jc = new Java_Connection();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pd = new ProgressDialog(Edit_Laporan_Riil.this);
			pd.setMessage("Sedang Dalam Proses Simpan Data...");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(String... args) {

			String nip_riil = nip.getText().toString();
			String uraian_daftar_riil = edit_dialog_uraian.getText().toString();
			String jumlah_riil = edit_textjml_riil.getText().toString();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("nomor_sppd", ambil_sppd);
				params.put("nip", nip_riil);
				params.put("uraian_daftar_riil", uraian_daftar_riil);
				params.put("jumlah_riil", jumlah_riil);

				Log.d("TAMBAH_RIIL", "POST DATA = " + params.toString());

				String response = jc.sendPostRequest(
						Koneksi.tambah_uraian_riil,
						params
				);

				if (response == null || response.isEmpty()) {
					Log.e("TAMBAH_RIIL", "Response NULL / kosong");
					return null;
				}

				Log.d("TAMBAH_RIIL", "RAW RESPONSE = " + response);

				JSONObject json = new JSONObject(response);

				int sukses = json.getInt(TAG_SUKSES);
				String pesan = json.getString(TAG_PESAN);

				if (sukses == 1) {
					Log.d("TAMBAH_RIIL", "SUKSES: " + pesan);
				} else {
					Log.e("TAMBAH_RIIL", "GAGAL: " + pesan);
				}

				return pesan;

			} catch (Exception e) {
				Log.e("TAMBAH_RIIL", "EXCEPTION", e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			pd.dismiss();

			if (hasil != null) {
				Toast.makeText(Edit_Laporan_Riil.this, hasil, Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}
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

}


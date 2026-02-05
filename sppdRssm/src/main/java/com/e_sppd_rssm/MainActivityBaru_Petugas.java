package com.e_sppd_rssm;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e_sppd.rssm.BuildConfig;
import com.e_sppd.rssm.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

//import koneksi.JSONParser;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class MainActivityBaru_Petugas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final Handler handler = new Handler();
    private final static String TAG = "Informasi_error";
//    JSONParser classJsonParser = new JSONParser();
    ProgressDialog loading_cek, loading_fcm;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    Fragment fragment = null;
    SharedPreferences sharedpreferences;
    String nip, nama_pegawai, jabatan, golongan, unit, password, email;
    RelativeLayout frame_loading_utama;
    ImageView gmbar_loading_utama, img_refresh;
    private ProgressDialog progresdialog;
    boolean doubleBackToExitPressedOnce = false;

    TextView tgl_utama, jam_utama, menuutama_nippetugas, menuutama_namapetugas, menuutama_jabpetugas, menuutama_unitpetugas, tvToken1, menuutama_version;
    public static final String TAG_NIP          = "nip";
    public static final String TAG_NAMA_PEGAWAI = "nama_pegawai";
    public final static String TAG_JABATAN 		= "jabatan";
    public final static String TAG_GOLONGAN 	= "golongan";
    public final static String TAG_UNIT 		= "unit";
    public final static String TAG_PASSWORD 	= "password";
    public final static String TAG_EMAIL 		= "email";
    private static final String TAG_SUKSES 		= "berhasil";
    private static final String TAG_PESAN 		= "tampilkan_pesan";
    private static final String Info_Pesan 		= "message";
    private static final String STATUSSUKSES 	= "sukses";
    private static final String STATUSPESANTOKEN 	= "pesan";
    private static final String TAG_VERSI           = "versi";
    private static final int progress_DOWNLOAD 	= 0;
    String get_pesan;
    String cek_versi_apk = BuildConfig.VERSION_NAME;

    public String token_lama;
    public  static final int RequestPermissionCode_StorageCamera  = 11 ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_petugas);
        handler.postDelayed(runnable, 1000);
        Permission_AksesCameradanStorage();
        Log.e("info versi bawaan ", BuildConfig.VERSION_NAME);
        gmbar_loading_utama     = findViewById(R.id.gmbar_loading_utama);
        frame_loading_utama     = findViewById(R.id.frame_loading_utama);

        menuutama_nippetugas    = findViewById(R.id.menuutama_nippetugas);
        menuutama_namapetugas   = findViewById(R.id.menuutama_namapetugas);
        menuutama_jabpetugas    = findViewById(R.id.menuutama_jabpetugas);
        menuutama_unitpetugas   = findViewById(R.id.menuutama_unitpetugas);
        menuutama_version       = findViewById(R.id.menuutama_version);
        tgl_utama               = findViewById(R.id.tgl_utama);
        jam_utama               = findViewById(R.id.jam_utama);
        tvToken1                = findViewById(R.id.token_utama);
        img_refresh             = findViewById(R.id.btn_refresh_utama);

        sharedpreferences   = getSharedPreferences(Login_Activity.my_shared_preferences, Context.MODE_PRIVATE);
        nip                 = getIntent().getStringExtra(TAG_NIP);
        nama_pegawai        = getIntent().getStringExtra(TAG_NAMA_PEGAWAI);
//        cek_versi_apk       = getIntent().getStringExtra(TAG_VERSI);
        jabatan             = getIntent().getStringExtra(TAG_JABATAN);
        golongan            = getIntent().getStringExtra(TAG_GOLONGAN);
        unit                = getIntent().getStringExtra(TAG_UNIT);
        password            = getIntent().getStringExtra(TAG_PASSWORD);
        email               = getIntent().getStringExtra(TAG_EMAIL);
        if (!terkoneksi_roaming(MainActivityBaru_Petugas.this)) {
            String a = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
            info_tak_ada_koneksi(a);
        }else{
            //new FCM_TOKEN().execute();
            fcm();
        }

        menuutama_nippetugas.setText(nip);
        menuutama_namapetugas.setText(nama_pegawai);
        menuutama_jabpetugas.setText(jabatan +" ( "+ golongan +" )");
        menuutama_unitpetugas.setText(unit);
        menuutama_version.setText(cek_versi_apk+"RSSM");
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // tampilan default awal ketika aplikasii dijalankan
        if (savedInstanceState == null) {
            fragment = new Welcome();
            callFragment(fragment);
        }

        Glide.with(MainActivityBaru_Petugas.this)
                // LOAD URL DARI LOKAL DRAWABLE
                .load(R.drawable.loading_ring)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_refresh);
    }

    private boolean terkoneksi_roaming(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
    public void fcm(){
        String tok = tvToken1.getText().toString();
        Log.i("TOKEN_ESPPD", tok);
        if (tvToken1.getText().toString().isEmpty()){
            token_lama = FirebaseInstanceId.getInstance().getToken();
            tvToken1.setText(token_lama);
            Log.i("TOKEN_ESPPD", token_lama);
            new GETTOKEN().execute();
        }
    }

    /*@SuppressLint("StaticFieldLeak")
    public class FCM_TOKEN extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            loading_fcm = new ProgressDialog(MainActivityBaru_Petugas.this);
//            loading_fcm.setMessage("Loading Cek Notifikasi... !!!");
//            loading_fcm.setIndeterminate(false);
//            loading_fcm.setCancelable(false);
//            loading_fcm.show();
//            loading_fcm.hide();
            loading_tampil();
        }

        @Override
        protected String doInBackground(String... args) {
            int jikaSukses;
            String responseString;
            String fcm_token  = tvToken1.getText().toString();
            String nip_peg    = menuutama_nippetugas.getText().toString();
            Log.i("Info", fcm_token);
            Log.i("Info", nip_peg);
//            notifikasi(fcm_token);
//            notifikasi(nip_peg);
            try {

                List<NameValuePair> tokenlist = new ArrayList<>();
                tokenlist.add(new BasicNameValuePair("fcm_token", fcm_token));
                tokenlist.add(new BasicNameValuePair("nip_pegawai", nip_peg));

                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.FCM_TOKEN, "POST", tokenlist);

                // priksa log jawaban dari JSON
                //Log.i("Pesan JSON Token", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(STATUSSUKSES);

                if (jikaSukses == 1) {
                    //Log.i("Pesan Token", jsonObjectNya.getString(STATUSPESANTOKEN));
                    return jsonObjectNya.getString(STATUSPESANTOKEN);
                }else{
                    //Log.i("Pesan Token Gagal:",jsonObjectNya.getString(STATUSPESANTOKEN));
                    return jsonObjectNya.getString(STATUSPESANTOKEN);
                }
            } catch (JSONException e) {
                responseString = e.toString();
            } catch (Exception e){
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String a) {
            //loading_fcm.dismiss();
            loading_sembunyi();
            //notifikasi(a+"\n"+tvToken1.getText().toString());
            //Toast.makeText(MainActivityBaru_Petugas.this, a+"\n"+tvToken1.getText().toString(), LENGTH_LONG).show();
            makeText(MainActivityBaru_Petugas.this, a, LENGTH_LONG).show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class GETTOKEN extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//            loading_fcm = new ProgressDialog(MainActivityBaru_Petugas.this);
//            loading_fcm.setMessage("Loading Cek Notifikasi... !!!");
//            loading_fcm.setIndeterminate(false);
//            loading_fcm.setCancelable(false);
//            loading_fcm.show();
//            loading_fcm.hide();
			loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {
			int jikaSukses;

            String fcm_token  = tvToken1.getText().toString();
            String nip_peg    = menuutama_nippetugas.getText().toString();
            Log.i("Info", fcm_token);
            Log.i("Info", nip_peg);

			String responseString;
			try {

				List<NameValuePair> versi = new ArrayList<>();
				versi.add(new BasicNameValuePair("fcm_token", fcm_token));
                versi.add(new BasicNameValuePair("nip_pegawai", nip_peg));
				Log.i("Proses Cek Token!", "dimulai");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.FCM_TOKEN, "POST", versi);

				Log.i("Proses Method Token", jsonObjectNya.toString());
				jikaSukses = jsonObjectNya.getInt(STATUSSUKSES);
                Log.i("INFO TOKEN", jsonObjectNya.getString(STATUSPESANTOKEN));
                return jsonObjectNya.getString(STATUSPESANTOKEN);
			} catch (JSONException e) {
				responseString = e.toString();
			} catch (Exception e){
				responseString = e.toString();
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String code) {
//            loading_fcm.dismiss();
            loading_sembunyi();
//            notifikasi(a+"\n"+tvToken1.getText().toString());
//            Toast.makeText(MainActivityBaru_Petugas.this, a+"\n"+tvToken1.getText().toString(), LENGTH_LONG).show();
//            makeText(MainActivityBaru_Petugas.this, code, LENGTH_LONG).show();
            Snackbar.make(findViewById(R.id.myCoordinatorLayout), code, Snackbar.LENGTH_SHORT).show();
        }

	}
*/

    public class GETTOKEN extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_tampil();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            String fcm_token = tvToken1.getText().toString().trim();
            String nip_peg   = menuutama_nippetugas.getText().toString().trim();

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("fcm_token", fcm_token);
                params.put("nip_pegawai", nip_peg);

                Log.i("FCM", "Kirim token ke server");

                Java_Connection jc = new Java_Connection();
                String response = jc.sendPostRequest(
                        Koneksi.FCM_TOKEN,
                        params
                );

                if (response == null) {
                    return "Gagal mengirim token ke server";
                }

                Log.i("FCM", "RESPON = " + response);

                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt(STATUSSUKSES);

                return jsonObject.getString(STATUSPESANTOKEN);

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String pesan) {
            loading_sembunyi();

            if (pesan != null) {
                Snackbar.make(
                        findViewById(R.id.myCoordinatorLayout),
                        pesan,
                        Snackbar.LENGTH_SHORT
                ).show();
                new Cek_Versi_APK().execute();
            }
        }
    }

//    @SuppressLint("StaticFieldLeak")
    /*public class Cek_Versi_APK extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading_cek = new ProgressDialog(MainActivityBaru_Petugas.this);
//            loading.setMessage("Loading Cek Versi... !!!");
            loading_cek.setIndeterminate(false);
            loading_cek.setCancelable(false);
//            loading.show();
            loading_tampil();
        }

        @Override
        protected String doInBackground(String... args) {
            int jikaSukses;

            //String Kirim_Cek_Versi_APK = cek_versi_apk.getText().toString();
            String responseString;
            try {

                List<NameValuePair> namaDanPassword = new ArrayList<>();
                namaDanPassword.add(new BasicNameValuePair("kirim_versi", cek_versi_apk));

                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.CEK_VERSI, "POST", namaDanPassword);

                // priksa log jawaban dari JSON
                Log.d("Pesan Versi:", jsonObjectNya.toString());

                jikaSukses = jsonObjectNya.getInt(TAG_SUKSES);

                if (jikaSukses == 1) {

                    Log.d("Versi Benar", jsonObjectNya.getString(TAG_PESAN));

                    return jsonObjectNya.getString(TAG_PESAN);

                } else {
                    //String Info_versi = jsonObjectNya.getString(TAG_VERSI_BARU);
                    Log.d("Perlu Versi Baru",jsonObjectNya.getString(TAG_PESAN));
                    get_pesan = jsonObjectNya.getString(Info_Pesan);
                    //menuutama_namapetugas.setText(get_pesan);
                    return jsonObjectNya.getString(TAG_PESAN);

                }
            } catch (JSONException e) {
                responseString = e.toString();
            } catch (Exception e){
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            // kalau sudah selesai di gunakan, matikanlah
            // progressbar_nya dengan metode dismiss();
            loading_cek.dismiss();
            loading_sembunyi();
            if (urlFileNya != null) {
                if (urlFileNya.contains("Silahkan")){
                    download_informasi_versi(urlFileNya);
                }else if (urlFileNya.contains("Maintenance")){
                   // String pesan = "Sedang Dalam Maintenance Sistem ... !!!\nCobalah Beberapa Menit Lagi...";
                    info_maintenance(urlFileNya);
                }else if (urlFileNya.contains("Wajib")) {
                    info_download(get_pesan);
                }else if (urlFileNya.contains("Sunnah")) {
                    info_download(urlFileNya);
                }
                fcm();
            }else{
                String a = "Sambungan Internet Terputus.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
                showAlert(a);
            }

        }

    }*/

    public class Cek_Versi_APK extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading_cek = new ProgressDialog(MainActivityBaru_Petugas.this);
            loading_cek.setIndeterminate(false);
            loading_cek.setCancelable(false);
            loading_tampil();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            try {

                HashMap<String, String> params = new HashMap<>();
                if (cek_versi_apk == null) {
                    cek_versi_apk = "";
                }
                params.put("versi_apk", cek_versi_apk);

                Java_Connection jc = new Java_Connection();
                String response = jc.sendPostRequest(
                        Koneksi.CEK_VERSI,
                        params
                );
                Log.d("DEBUG_VERSI", "versi apk = " + cek_versi_apk);
                if (response == null) {
                    return "ERROR_CONNECTION";
                }

                Log.d("Pesan Versi:", response);

                JSONObject jsonObject = new JSONObject(response);
                int jikaSukses = jsonObject.getInt(TAG_SUKSES);

                if (jikaSukses == 1) {
                    // versi benar
                    return jsonObject.getString(TAG_PESAN);
                } else {
                    // perlu versi baru / maintenance / info
                    get_pesan = jsonObject.optString(Info_Pesan);
                    return jsonObject.getString(TAG_PESAN);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String hasil) {
            if ("ERROR_CONNECTION".equals(hasil)) {
                showAlert(
                        "Gagal terhubung ke server.\n" +
                                "Server tidak merespon atau sedang bermasalah"
                );
                return;
            }

            if (loading_cek != null && loading_cek.isShowing()) {
                loading_cek.dismiss();
            }
            loading_sembunyi();

            if (hasil != null) {

                if (hasil.contains("Silahkan")) {
                    download_informasi_versi(hasil);

                } else if (hasil.contains("Maintenance")) {
                    info_maintenance(hasil);

                } else if (hasil.contains("Wajib")) {
                    info_download(get_pesan);

                } else if (hasil.contains("Sunnah")) {
                    info_download(hasil);
                }

                // tetap jalan
//                fcm();

            } else {
                String pesan =
                        "Sambungan Internet Terputus.\n" +
                                "Pastikan Wi-Fi atau Data Seluler aktif, lalu coba lagi";
                showAlert(pesan);
            }
        }
    }

    private void download_informasi_versi(String pesan) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Informasi");
        ad.setMessage(pesan);
        ad.setCancelable(false);
        ad.setIcon(R.drawable.ic_info_outline_24dp);
        ad.setPositiveButton("Download", (dialog, which) -> {
            dialog.dismiss();
            //e-Sppd.v"+kirim_versi+".apk
            //String kirim_versi = cek_versi_apk.getText().toString();
            //String kirim_versi = "1.3.2";
            String Cek = cek_versi_apk;
            try {

                new Download_Aplikasi().execute(Koneksi.download_apk + "e-Sppd.v"
                       + URLEncoder.encode(Cek, "UTF-8")+".apk");

            } catch (Exception ex) {
                // TODO Auto-generated catch block
                //ex.getMessage();
                ex.printStackTrace();

            }

        });
        ad.setNegativeButton("Nanti ",
                (dialog, id) -> dialog.dismiss());
        ad.setNeutralButton("Keluar ",
                (dialog, id) -> {
                    dialog.dismiss();
                    MainActivityBaru_Petugas.this.finish();
                    finish();

                });

        ad.show();
    }

    private void notifikasi(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Pesan Masuk")
                .setCancelable(false)
                .setIcon(R.drawable.ic_file_download_black)
                .setNeutralButton("Terima Kasih",
                        (dialog, id) -> {
                            // Intent in = null;
                            dialog.dismiss();

                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void info_download(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Tersedia Versi Terbaru !!!")
                .setCancelable(false)
                .setIcon(R.drawable.ic_file_download_black)
                .setPositiveButton("Download",
                        (dialog, id) -> {
                            dialog.dismiss();
                            //e-Sppd.v"+kirim_versi+".apk
                            //String kirim_versi = cek_versi_apk.getText().toString();
                            //String kirim_versi = "1.3.2";
                            String Cek = cek_versi_apk;
                            try {
                                  new Download_Aplikasi().execute(Koneksi.download_apk + "e-Sppd.v" + URLEncoder.encode(Cek, "UTF-8")+".apk");
                            } catch (Exception ex) {
                                // TODO Auto-generated catch block
                                ex.printStackTrace();
                            }
                        })
                .setNegativeButton("Nanti",
                        (dialog, id) -> {
                            dialog.dismiss();
                            MainActivityBaru_Petugas.this.finish();
                            finish();

                        })
                .setNeutralButton("Masuk Website",
                        (dialog, id) -> {
                           // Intent in = null;
                            dialog.dismiss();
                            Intent in = new Intent();
                            in.setAction(Intent.ACTION_VIEW);
                            in.addCategory(Intent.CATEGORY_BROWSABLE);
                            in.setData(Uri.parse(Koneksi.URL_WEBSITE));
                            startActivity(in);

                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class Download_Aplikasi extends AsyncTask<String, String, String>

    {
        @Override
        @SuppressWarnings("deprecation")
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_DOWNLOAD);

        }

        @SuppressLint("SdCardPath")
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String responseString = null; 											// 1
            //String kirim_versi = cek_versi_apk.getText().toString();
            try {

                URL url = new URL(f_url[0]);

                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                @SuppressLint("SdCardPath") OutputStream output = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    output = Files.newOutputStream(Paths.get("/sdcard/Download/e-Sppd.v" + cek_versi_apk + ".apk"));
                }
                byte[] data = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghOfFile));
                    assert output != null;
                    output.write(data, 0, count);
                }
                assert output != null;
                output.flush();
                output.close();
                input.close();
            } catch (ClientProtocolException e) { 	// 2
                responseString = e.toString();		//
            } catch (IOException e) {				//
                responseString = e.toString();		//3
            }										//
            return responseString;					//4
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            progresdialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        @SuppressWarnings("deprecation")
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_DOWNLOAD);
            Log.e(TAG, "Respon Dari Server ::: " + "Error");

            String pesan1 = "java.net.SocketException: recvfrom failed: ETIMEDOUT (Connection timed out)";
            String pesan2 = "java.net.UnknownHostException: Unable to resolve host";

            if (file_url != null){
                if (file_url.contains(pesan2)){
                    String info_pesan1 = "Download File E-SPPD Terhenti\nTidak Ada Koneksi Internet\n" +
                            "Pastikan Wi-fi atau Data Seluler aktif dan lancar, lalu coba lagi";
                    showAlert(info_pesan1);
                }else if (file_url.contains(pesan1)){
                    String info_pesan1 = "Download File E-SPPD Terhenti\nKoneksi Sambungan Terputus\n" +
                            "Pastikan Wi-fi atau Data Seluler aktif dan lancar, lalu coba lagi";
                    showAlert(info_pesan1);
                }else{
                    refresh();
                }

                //}else if (file_url.equalsIgnoreCase(pesan1)){
                //	String info_pesan2 = "Koneksi Sambungan Terputus\n" +
                //			"Pastikan Wi-fi atau Data Seluler aktif dan lancar, lalu coba lagi";
                //	showAlert(info_pesan2);
            }else{
                String info_pesan3 = "Download Berhasil !!!\nSilahkan Install File E-SPPD V."+cek_versi_apk+" Pada Folder Downloads";
                info_selesai_download(info_pesan3);
            }
            super.onPostExecute(file_url);
        }
    }

    @Override
    public Dialog onCreateDialog(int id) {
        //String kirim_versi = cek_versi_apk.getText().toString();
        if (id == progress_DOWNLOAD) {
            progresdialog = new ProgressDialog(this);
            progresdialog.setMessage("Downloading file...\ne-Sppd.v" + cek_versi_apk + ".apk");
            progresdialog.setIndeterminate(false);
            progresdialog.setMax(100);
            progresdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progresdialog.setCancelable(false);
            progresdialog.show();
            return progresdialog;
        }
        return null;
    }
    private void info_maintenance(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Informasi")
                .setCancelable(false)
                .setIcon(R.drawable.ic_info_outline_24dp)
                .setPositiveButton("Ok",
                        (dialog, id) -> {
                            dialog.dismiss();
                            MainActivityBaru_Petugas.this.finish();
                            finish();

                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void info_selesai_download(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Informasi")
                .setCancelable(false)
                .setIcon(R.drawable.ic_info_outline_24dp)
                .setPositiveButton("Ok",
                        (dialog, id) -> {
                            dialog.dismiss();
                            MainActivityBaru_Petugas.this.finish();
                            finish();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void info_tak_ada_koneksi(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Peringatan")
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton("Ok",
                        (dialog, id) -> {
                            dialog.dismiss();
                            refresh();
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
                .setPositiveButton("Coba Lagi",
                        (dialog, id) -> {
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        })

                .setNeutralButton("Keluar",
                        (dialog, id) -> {
                            dialog.dismiss();
                            MainActivityBaru_Petugas.this.finish();
                            finish();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Untuk memanggil layout dari menu yang dipilih
        if (id == R.id.menu1) {
            finish();
            startActivity(getIntent());
            makeText(MainActivityBaru_Petugas.this, "Selamat Datang\n"+nama_pegawai,
                           LENGTH_LONG).show();

         //   new Activity_Gambar().onDestroyView();
        //    fragment = new Activity_Gambar();
         //   callFragment(fragment);
        } else if (id == R.id.menu2) {
            Intent in;

            in = new Intent(MainActivityBaru_Petugas.this, Daftar_Laporan_Per_Petugas.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);
        } else if (id == R.id.menu3) {
            Intent in;

            in = new Intent(MainActivityBaru_Petugas.this, History.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);

        } else if (id == R.id.menu4) {
            Intent in;

            in = new Intent(MainActivityBaru_Petugas.this, Profil.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            bun.putString("transfer_nama_pegawai", nama_pegawai);
            bun.putString("transfer_jabatan", jabatan);
            bun.putString("transfer_golongan", golongan);
            bun.putString("transfer_unit", unit);
            bun.putString("transfer_password", password);
            bun.putString("email", email);
            in.putExtras(bun);
            startActivity(in);

        } else if (id == R.id.menu5) {

            //String nip3 = ambil_nip.getText().toString();
           // String versi = cek_versi_apk.getText().toString();
          //  MainActivityBaru_Petugas.this.finish();
           // finish();
            Intent in;

            in = new Intent(MainActivityBaru_Petugas.this, Tentang_Aplikasi.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);
        } else if (id == R.id.menu6) {
            infodialogback();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // untuk mengganti isi kontainer menu yang dipiih
    private void callFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            makeText(this, "Klik Tombol Kembali Dua Kali (2x) Untuk Keluar Dari Aplikasi E-SPPD", LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }
    private void infodialogback() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Informasi");
        ad.setIcon(R.drawable.ic_lock_black);
        ad.setMessage("Anda Akan Keluar Dari Session Aplikasi E-SPPD dan Kembali ke Menu Login ?");
        ad.setPositiveButton("Keluar", (dialog, which) -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Login_Activity.session_status_level2, false);
            editor.putString(TAG_NIP, null);
            editor.putString(TAG_NAMA_PEGAWAI, null);
            editor.putString(TAG_JABATAN, null);
            editor.putString(TAG_GOLONGAN, null);
            editor.putString(TAG_PASSWORD, null);
            editor.putString(TAG_VERSI, null);
            editor.apply();
            Intent intent = new Intent(MainActivityBaru_Petugas.this, Login_Activity.class);
            finish();
            startActivity(intent);
        });

        ad.setNeutralButton("Batal", (dialog, which) -> dialog.dismiss());
        ad.show();
    }

    private final Runnable runnable = new Runnable() {

        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Calendar c1 = Calendar.getInstance();

            SimpleDateFormat hariini    = new SimpleDateFormat("EEEE");
            SimpleDateFormat tgl_skrng  = new SimpleDateFormat("d MMM yyyy");
            SimpleDateFormat jam_skrng  = new SimpleDateFormat("HH:mm:s");
            // SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
            String strdate_tgl = tgl_skrng.format(c1.getTime());
            String strdate_jam = jam_skrng.format(c1.getTime());
            String dayOfTheWeek = hariini.format(c1.getTime());

            tgl_utama.setText(dayOfTheWeek+", "+strdate_tgl);
            jam_utama.setText(strdate_jam);

            handler.postDelayed(this, 1000);
        }

    };
    public void refresh(View view) {
        finish();
        startActivity(getIntent());
    }
    public void menu(View view) {
        drawer.openDrawer(GravityCompat.START);
    }
    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    public void loading_sembunyi() {
        frame_loading_utama.setVisibility(View.GONE);
    }

    public void loading_tampil() {
        frame_loading_utama.setVisibility(View.VISIBLE);
        Glide.with(MainActivityBaru_Petugas.this)
                // LOAD URL DARI LOKAL DRAWABLE
                .load(R.drawable.loading_ring)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(gmbar_loading_utama);
    }

    public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {

        if (RC == RequestPermissionCode_StorageCamera) {
            if ((PResult.length > 0) && (PResult[0] == PackageManager.PERMISSION_DENIED)) {

                makeText(MainActivityBaru_Petugas.this, "Diperlukan Ijin Mengakses Lokasi Penyimpanan dan Kamera !!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Permission_AksesCameradanStorage(){

        if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivityBaru_Petugas.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) &&
                (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityBaru_Petugas.this, Manifest.permission.CAMERA))) {

            makeText(MainActivityBaru_Petugas.this, "Diperlukan Ijin Mengakses Penyimpanan  dan Kamera !!!", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(MainActivityBaru_Petugas.this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
        } else {
            ActivityCompat.requestPermissions(MainActivityBaru_Petugas.this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
        }
    }

    /*public void showImage(View view)
    {
    ViewFlipper viewFlipper = findViewById(R.id.viewFlippermainactivity);
    viewFlipper.setFlipInterval(2500);
    viewFlipper.startFlipping();

    Cursor cursor = db.rawQuery(Query_Select_All ,  null);
    int i = 1;
    if(cursor.getCount() != 0)
        while (cursor.moveToNext())
        {
            Cursor cursor2 = db.rawQuery("select * from imageColumns where id = "+i , null);
            String path = cursor2.getString(cursor2.getColumnIndex(imageColumnName));
            File imageFile = new File(path);
            Bitmap bitmapImage = BitmapFactory.decodeFile(imageFile.toString());
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmapImage);
            viewFlipper.addView(imageView);
            i++;
        }
    }*/
}

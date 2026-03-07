package com.fungsiutama;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.e_sppd.rssm.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import koneksi.Java_Connection;
import koneksi.Koneksi;

public class MainActivityUtama extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final Handler handler = new Handler();
    private final static String TAG = "MainActivity";
    private ProgressDialog downloaddiMAIN;
    private String progressdownload = "";
    private DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    Fragment fragment = null;
    SharedPreferences sharedpreferences;
    String nip, nama_pegawai, jabatan, golongan, unit, password, email;
    RelativeLayout frame_loading_utama;
    ImageView gmbar_loading_utama, img_refresh;
//    private ProgressDialog progresdialog;
    boolean doubleBackToExitPressedOnce = false;

    TextView tgl_utama, jam_utama, menuutama_nippetugas, menuutama_namapetugas, menuutama_jabpetugas, menuutama_unitpetugas, tvToken1, menuutama_version;
    public static final String TAG_NIP          = "nip";
    public static final String TAG_NAMA_PEGAWAI = "nama_pegawai";
    public final static String TAG_JABATAN 		= "jabatan";
    public final static String TAG_GOLONGAN 	= "golongan";
    public final static String TAG_UNIT 		= "unit";
    public final static String TAG_PASSWORD 	= "password";
    public final static String TAG_EMAIL 		= "email";
//    private static final String STATUSSUKSES 	= "sukses";
    private static final String STATUSPESANTOKEN 	= "pesan";
    private static final String TAG_VERSI           = "versi";
//    private static final int progress_DOWNLOAD 	= 0;

    private static final String TAG_VERSICODE 		= "code";
    private static final String TAG_VERSIPESAN_CEK	= "pesan";
    private static final String TAG_VERSIWARNING	= "warning";
    private static final String TAG_VERSIBARU	    = "versiygbaru";
    private static final String TAG_LINK	        = "link";
    String cek_versi_apk = null;
    public String pesanversi, warningversi, versiygbaru, linkupdate;
    public String token_lama;
    public  static final int RequestPermissionCode_StorageCamera  = 11 ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_main_petugas);
        try {
            cek_versi_apk = getPackageManager()
                    .getPackageInfo(getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        handler.postDelayed(runnable, 1000);
        Permission_AksesCameradanStorage();
//        Log.e("info versi bawaan ", BuildConfig.VERSION_NAME);
        Log.e("TAG_VERSI", Objects.requireNonNull(getIntent().getStringExtra(TAG_VERSI)));
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

        if (savedInstanceState == null) {
            fragment = new Welcome();
            callFragment(fragment);
        }

        if (!terkoneksi_roaming(MainActivityUtama.this)) {
            String a = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
            info_tak_ada_koneksi(a);
        }else{
            //new FCM_TOKEN().execute();
            fcm();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return;
                }

                if (doubleBackToExitPressedOnce) {
                    setEnabled(false);   // penting agar tidak loop
                    getOnBackPressedDispatcher().onBackPressed();
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(MainActivityUtama.this,
                        "Tekan tombol kembali [2x] untuk keluar aplikasi.",
                        Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(
                        () -> doubleBackToExitPressedOnce = false,
                        2000
                );
            }
        });
    }

    private boolean terkoneksi_roaming(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
    public void fcm() {

        String tok = tvToken1.getText().toString();
        Log.i("TOKEN_ESPPD", tok);

        if (tok.isEmpty()) {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN_ESPPD", "Fetching FCM token failed", task.getException());
                            return;
                        }

                        String tokenBaru = task.getResult();
                        tvToken1.setText(tokenBaru);
                        Log.i("TOKEN_ESPPD", tokenBaru);

                        new GETTOKEN().execute();
                    });
        }

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{
                    Manifest.permission.POST_NOTIFICATIONS
            }, 1);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class GETTOKEN extends AsyncTask<Void, Void, String> {
        private String fcm_token;
        private String nip_peg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_tampil();

            nip_peg   = menuutama_nippetugas.getText().toString().trim();
            fcm_token = tvToken1.getText().toString().trim();
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("fcm_token", fcm_token);
                params.put("nip_pegawai", nip_peg);

                Log.i("FCM", String.valueOf(params));

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
//                int status = jsonObject.getInt(STATUSSUKSES);

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

    @SuppressLint("StaticFieldLeak")
    public class Cek_Versi_APK extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_tampil();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String versiApk = cek_versi_apk.trim();
            Java_Connection jc = new Java_Connection();

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("versi_apk", versiApk);
                Log.i("Proses Cek Versi!", versiApk);
                String response = jc.sendPostRequest(
                        Koneksi.CEK_VERSI,
                        params
                );

                if (response == null) {
                    return "0"; // gagal / tidak ada respon
                }

                Log.d("CEK_VERSI", "RESPON = " + response);

                JSONObject json = new JSONObject(response);
                int code = json.getInt(TAG_VERSICODE);

                pesanversi      = json.optString(TAG_VERSIPESAN_CEK, "");
                warningversi    = json.optString(TAG_VERSIWARNING, "");
                versiygbaru     = json.optString(TAG_VERSIBARU, "");
                linkupdate      = json.optString(TAG_LINK, "");

                return String.valueOf(code);

            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String code) {
            loading_sembunyi();

            switch (code) {
                case "1": // versi terbaru

                    break;

                case "405": // info
                    jikainfo(pesanversi);
                    break;

                case "404": // maintenance
                    jikamaintenance(pesanversi);
                    break;

                case "405404": // info + maintenance
                    jikamaintenancedaninfo(pesanversi);
                    break;

                case "101": // wajib update
                    info_download(pesanversi, warningversi, linkupdate);
                    break;

                default:
                    Toast.makeText(
                            getApplicationContext(),
                            "Gagal cek versi",
                            Toast.LENGTH_LONG
                    ).show();
                    break;
            }
        }
    }

    //dialog untuk cek versi
    private void jikainfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(warningversi)
                .setCancelable(false)
                .setIcon(R.drawable.ic_info_outline_24dp)
                .setPositiveButton("Ok",
                        (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void jikamaintenance(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(warningversi)
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton("Terima Kasih",
                        (dialog, id) -> {
                            dialog.dismiss();
                            finish();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void jikamaintenancedaninfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(warningversi)
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton("Terima Kasih",
                        (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
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

    private void info_download(String message, String warningversi, String linkupdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(warningversi)
                .setCancelable(false)
                .setIcon(R.drawable.ic_download)
//                .setPositiveButton("✅ Download",
//                        (dialog, id) -> {
//                            dialog.dismiss();
//                            try {
//                                  new down_apk().execute(Koneksi.download_apk + "e-Sppd.v" + URLEncoder.encode(versiygbaru, "UTF-8")+".apk");
//                            } catch (Exception ex) {
//                                // TODO Auto-generated catch block
//                                ex.printStackTrace();
//                            }
//                        })
                .setPositiveButton("✅ Update",
                        (dialog, id) -> {
                            // Intent in = null;
                            dialog.dismiss();
                            Intent in = new Intent();
                            in.setAction(Intent.ACTION_VIEW);
                            in.addCategory(Intent.CATEGORY_BROWSABLE);
                            in.setData(Uri.parse(linkupdate));
                            startActivity(in);
                        })
                .setNegativeButton("❌ Nanti",
                        (dialog, id) -> {
                            dialog.dismiss();
                            MainActivityUtama.this.finish();
                            finish();
                        });
//                .setNeutralButton("Masuk Website",
//                        (dialog, id) -> {
//                           // Intent in = null;
//                            dialog.dismiss();
//                            Intent in = new Intent();
//                            in.setAction(Intent.ACTION_VIEW);
//                            in.addCategory(Intent.CATEGORY_BROWSABLE);
//                            in.setData(Uri.parse(Koneksi.URL_WEBSITE));
//                            startActivity(in);
//                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @SuppressLint("StaticFieldLeak")
    private class down_apk extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            downloaddiMAIN = new ProgressDialog(MainActivityUtama.this);
            downloaddiMAIN.setTitle("Download Aplikasi");
            downloaddiMAIN.setMessage("Sedang mengunduh...");
            downloaddiMAIN.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            downloaddiMAIN.setIndeterminate(false);
            downloaddiMAIN.setMax(100);
            downloaddiMAIN.setCancelable(false);
            downloaddiMAIN.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            String error = null;
            int count;

            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lengthOfFile = connection.getContentLength();

                // ===== MediaStore.Files (Universal) =====
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME,
                        "e-Sppd.v" + versiygbaru + ".apk");
                values.put(MediaStore.MediaColumns.MIME_TYPE,
                        "application/vnd.android.package-archive");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    values.put("relative_path", Environment.DIRECTORY_DOWNLOADS);
                }

                Uri fileUri = getContentResolver().insert(
                        MediaStore.Files.getContentUri("external"), values);

                if (fileUri == null) {
                    return "Gagal membuat file Download";
                }

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = getContentResolver().openOutputStream(fileUri);

                if (output == null) {
                    return "Gagal membuka OutputStream";
                }

                byte[] data = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    int progress = (int) ((total * 100) / lengthOfFile);
                    publishProgress(progress);
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                error = e.toString();
            }

            return error;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (downloaddiMAIN != null && downloaddiMAIN.isShowing()) {
                downloaddiMAIN.setProgress(progress[0]);
            }
            progressdownload = String.valueOf(progress[0]);
            Log.e(TAG, "Progress Download: " + progress[0] + "%");
        }

        @Override
        protected void onPostExecute(String error) {

            if (downloaddiMAIN != null && downloaddiMAIN.isShowing()) {
                downloaddiMAIN.dismiss();
                downloaddiMAIN = null;
            }

            if (error == null && "100".equals(progressdownload)) {

                String pesan =
                        "Download E-SPPD V" + versiygbaru + " berhasil.\n\n" +
                                "File tersimpan di folder Download.\n" +
                                "Silakan install ulang aplikasi.";

                showprogress_download(pesan);

            } else if (error != null) {

                if (error.contains("UnknownHost") ||
                        error.contains("ETIMEDOUT") ||
                        error.contains("SSLException")) {

                    showAlert(
                            "Tidak ada koneksi internet.\n" +
                                    "Periksa Wi-Fi atau data seluler lalu coba lagi."
                    );

                } else if (error.contains("Permission denied")) {

                    Toast.makeText(
                            MainActivityUtama.this,
                            "Izin penyimpanan diperlukan.\nAktifkan di pengaturan aplikasi.",
                            Toast.LENGTH_LONG
                    ).show();

                } else {
                    showAlert("Download gagal:\n" + error);
                }

            } else {
                showAlert("Download tidak selesai");
            }

            super.onPostExecute(error);
        }
    }

    private void showprogress_download(String a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(a)
                .setTitle("Status")
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black)
                .setPositiveButton("Ok",
                        (dialog, id) -> {
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());
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
                            MainActivityUtama.this.finish();
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
            makeText(MainActivityUtama.this, "Selamat Datang "+nama_pegawai,
                           LENGTH_LONG).show();

            /*new Activity_Gambar().onDestroyView();
            fragment = new Activity_Gambar();
            callFragment(fragment);*/
        } else if (id == R.id.menu2) {
            Intent in;

            in = new Intent(MainActivityUtama.this, List_DataSppd.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);
        } else if (id == R.id.menu3) {
            Intent in;

            in = new Intent(MainActivityUtama.this, History.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);

        } else if (id == R.id.menu4) {
            Intent in;

            in = new Intent(MainActivityUtama.this, Profil.class);

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
            Intent in;

            in = new Intent(MainActivityUtama.this, Tentang_Aplikasi.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);
        } else if (id == R.id.menu6) {
            infodialogback();
        } else if (id == R.id.menu7) {
            Intent in;

            in = new Intent(MainActivityUtama.this, List_Notif.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            in.putExtras(bun);
            startActivity(in);
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    private void infodialogback() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Warning");
        ad.setIcon(R.drawable.ic_lock_open_black);
        ad.setMessage("Keluar dari Sesi Login E-SPPD ?");
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
            Intent intent = new Intent(MainActivityUtama.this, Login_Activity.class);
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
//        Glide.with(MainActivityUtama.this)
//                // LOAD URL DARI LOKAL DRAWABLE
//                .load(R.drawable.loading_blue)
//                .asGif()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(gmbar_loading_utama);

        Glide.with(MainActivityUtama.this)
                .load(R.drawable.loading_blue)
                .into(gmbar_loading_utama);
    }

    public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode_StorageCamera) {
            if ((PResult.length > 0) && (PResult[0] == PackageManager.PERMISSION_DENIED)) {

                makeText(MainActivityUtama.this, "Diperlukan ijin akses lokasi penyimpanan data dan akses galery!", LENGTH_LONG).show();
            }
        }
    }

    public void Permission_AksesCameradanStorage(){

        if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivityUtama.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) &&
                (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityUtama.this, Manifest.permission.CAMERA))) {

            makeText(MainActivityUtama.this, "Diperlukan ijin akses lokasi penyimpanan data dan akses galery!", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(MainActivityUtama.this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
        } else {
            ActivityCompat.requestPermissions(MainActivityUtama.this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
        }
    }

}

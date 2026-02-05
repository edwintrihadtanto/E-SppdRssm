package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.e_sppd.rssm.R;
import com.google.android.material.navigation.NavigationView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivityBaru_Admin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final Handler handler = new Handler();
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences sharedpreferences;
    String nip, nama_pegawai, cek_versi_apk;
    private TextView tgl_utama, jam_utama, menuutama_nippetugas, menuutama_namapetugas;
    public static final String TAG_NIP          = "nip";
    public static final String TAG_NAMA_PEGAWAI = "nama_pegawai";
    public static final String VERSI            = "versi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.postDelayed(runnable, 1000);

        menuutama_nippetugas    = findViewById(R.id.menuutama_nippetugas);
        menuutama_namapetugas   = findViewById(R.id.menuutama_namapetugas);
        tgl_utama               = findViewById(R.id.tgl_utama);
        jam_utama               = findViewById(R.id.jam_utama);

        sharedpreferences   = getSharedPreferences(Login_Activity.my_shared_preferences, Context.MODE_PRIVATE);
        nip                 = getIntent().getStringExtra(TAG_NIP);
        nama_pegawai        = getIntent().getStringExtra(TAG_NAMA_PEGAWAI);
        cek_versi_apk       = getIntent().getStringExtra(VERSI);

       // Intent intent       = getIntent();

        menuutama_nippetugas.setText(nip);
        menuutama_namapetugas.setText(nama_pegawai);

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
            // Intent(getActivity(), UmurActivity.class);
            //  getActivity().startActivity(intent);
          //  fragment = new Login_Activity();
         //   callFragment(fragment);
            //Toast.makeText(MainActivityBaru_Admin.this, "Yess... Sukses",
             //       Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Untuk memanggil layout dari menu yang dipilih
        if (id == R.id.menu1) {
         //   new Activity_Gambar().onDestroyView();
        //    fragment = new Activity_Gambar();
         //   callFragment(fragment);
        } else if (id == R.id.menu2) {
            Intent in = null;

            in = new Intent(MainActivityBaru_Admin.this, Sppd.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);
        } else if (id == R.id.menu3) {
            Intent in = null;

            in = new Intent(MainActivityBaru_Admin.this, Pembuatan_SPT.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);

        } else if (id == R.id.menu4) {
            Intent in = null;

            in = new Intent(MainActivityBaru_Admin.this, Profil.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            bun.putString("transfer_nama_pegawai", nama_pegawai);
            in.putExtras(bun);
            startActivity(in);

        } else if (id == R.id.menu5) {

            //String nip3 = ambil_nip.getText().toString();
           // String versi = cek_versi_apk.getText().toString();
            Intent in = null;

            in = new Intent(MainActivityBaru_Admin.this, Tentang_Aplikasi.class);

            Bundle bun = new Bundle();
            bun.putString("transfer_nip", nip);
            bun.putString("versi", cek_versi_apk);
            in.putExtras(bun);
            startActivity(in);
        } else if (id == R.id.menu6) {
            infodialogback();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //infodialogback();
            super.onBackPressed();
        }
    }
    private void infodialogback() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Informasi");
        ad.setIcon(R.drawable.icon_logout);
        ad.setMessage("LogOut e-SPPD ?");
        ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login_Activity.session_status_level1, false);
                editor.putString(TAG_NIP, null);
                editor.putString(TAG_NAMA_PEGAWAI, null);
                editor.putString(VERSI, null);
                editor.apply();

                Intent intent = new Intent(MainActivityBaru_Admin.this, Login_Activity.class);
                finish();
                startActivity(intent);

            }
        });
        ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    private final Runnable runnable = new Runnable() {

        @SuppressLint("SimpleDateFormat")
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Calendar c1 = Calendar.getInstance();

            SimpleDateFormat tgl_skrng = new SimpleDateFormat("d MMM yyyy");
            SimpleDateFormat jam_skrng = new SimpleDateFormat("HH:m:s");
            // SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
            String strdate_tgl = tgl_skrng.format(c1.getTime());
            String strdate_jam = jam_skrng.format(c1.getTime());

            tgl_utama.setText(strdate_tgl);
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
}

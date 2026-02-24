package com.fungsiutama;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import koneksi.Daftar_String;

public class List_Notif extends AppCompatActivity {
    private ListView listView;
    private List<Daftar_String> list;
    private TextView jumlahNotif;
    private TextView terbaca;
    private TextView belumterbaca;
    String transfer_nip = null;
    String nipLokal;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_notif);

        listView 		= findViewById(R.id.list_daftar_notif);
        list 			= new ArrayList<>();

        jumlahNotif     = findViewById(R.id.txt_jumlah_notif);
        terbaca         = findViewById(R.id.txt_notif_terbca);
        belumterbaca    = findViewById(R.id.txt_notif_belum);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            transfer_nip = b.getString("transfer_nip");
        }
        nipLokal = transfer_nip;

        if (!terkoneksi_roaming(List_Notif.this)) {
            String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
            showPesanKoneksi(pesan);
        }else{
//            new List_DataSppd.Load_Data().execute();
        }
    }
    private boolean terkoneksi_roaming(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

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
                    List_Notif.this.finish();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showPesanSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", v -> {})
                .show();
    }

    public void refreshNotif(View view){
        String pesan = "❌ Tidak ada notifikasi masuk.";
        showPesanSnackbar(pesan);
    }
}

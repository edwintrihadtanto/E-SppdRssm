package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.e_sppd.rssm.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import kamera.FileUtil;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class Edit_Rincian_BiayaRiil extends AppCompatActivity {
    EditText edtRincianRiil, edtJumlahRiil;
    TextView txtTanggalRiil;

    Button btnSimpan, btnHapus;
    String id_riil, nosppd, nip, pembuat, mode;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_edit_rincian_biaya_riil);

        edtRincianRiil = findViewById(R.id.edtRincian);
        edtJumlahRiil  = findViewById(R.id.edtJumlah);
        txtTanggalRiil = findViewById(R.id.txtTanggal);

        btnSimpan  = findViewById(R.id.btn_simpanriil);
        btnHapus   = findViewById(R.id.btn_hapusriil);
        Intent i = getIntent();

        mode        = i.getStringExtra("mode");
        if (mode == null) mode = "edit"; // default
        nosppd      = i.getStringExtra("nosppd");
        nip         = i.getStringExtra("nippegawai");

        if (mode.equals("tambah")) {

            edtRincianRiil.setText("");
            edtJumlahRiil.setText("");
            txtTanggalRiil.setText("");

            btnHapus.setVisibility(View.GONE);

        } else {
            id_riil  = i.getStringExtra("id_rincian");
            edtRincianRiil.setText(i.getStringExtra("rincian"));
            edtJumlahRiil.setText(i.getStringExtra("jumlah"));
            txtTanggalRiil.setText(i.getStringExtra("tgl"));
            pembuat = i.getStringExtra("pembuat");
            if (pembuat.equals("1")){
                btnHapus.setVisibility(View.GONE);
                btnSimpan.setVisibility(View.GONE);
            }
        }

        btnSimpan.setOnClickListener(v -> {
            if (mode.equals("tambah")) {
                new SimpanBiayaRiil(Edit_Rincian_BiayaRiil.this).execute();
            } else {
                new UpdateRiil(Edit_Rincian_BiayaRiil.this).execute();
            }
        });

        btnHapus.setOnClickListener((View v) -> new AlertDialog.Builder(Edit_Rincian_BiayaRiil.this)
                .setTitle("Hapus Rincian")
                .setMessage("Yakin ingin menghapus rincian ini?")
                .setPositiveButton("Hapus", (d, w) -> new HapusRiil(Edit_Rincian_BiayaRiil.this).execute())
                .setNegativeButton("Batal", null)
                .show());
    }
    @SuppressLint("StaticFieldLeak")
    public class UpdateRiil extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        Java_Connection jc = new Java_Connection();
        Activity activity;

        public UpdateRiil(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(activity);
            pd.setMessage("Menyimpan perubahan...");
            pd.setCancelable(false);
            pd.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id_rincian", id_riil);
                params.put("uraian", edtRincianRiil.getText().toString());
                params.put("jml", edtJumlahRiil.getText().toString());
                params.put("nomor_surat_sppd", nosppd);
                params.put("nip", nip);

                String response = jc.sendPostRequest(
                        Koneksi.update_rincian_biaya,
                        params
                );

                if (response == null) return null;

                JSONObject json = new JSONObject(response);
                return json.getString("pesan");

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String hasil) {
            pd.dismiss();

            if (hasil != null) {
                Toast.makeText(activity, hasil, Toast.LENGTH_LONG).show();
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class HapusRiil extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        Java_Connection jc = new Java_Connection();
        Activity activity;

        public HapusRiil(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(activity);
            pd.setMessage("Menghapus rincian...");
            pd.setCancelable(false);
            pd.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id_rincian", id_riil);

                String res = jc.sendPostRequest(
                        Koneksi.hapus_rincian_biaya,
                        params
                );

                if (res == null) return null;

                JSONObject json = new JSONObject(res);
                return json.getString("pesan");

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String hasil) {
            pd.dismiss();

            if (hasil != null) {
                Toast.makeText(activity, hasil, Toast.LENGTH_LONG).show();
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SimpanBiayaRiil extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        Java_Connection jc = new Java_Connection();
        Activity activity;

        public SimpanBiayaRiil(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(activity);
            pd.setMessage("Menyimpan rincian...");
            pd.setCancelable(false);
            pd.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("nosppd", nosppd);
                params.put("nip", nip);
                params.put("uraian", edtRincianRiil.getText().toString());
                params.put("jml", edtJumlahRiil.getText().toString());

                String res = jc.sendPostRequest(
                        Koneksi.simpan_rincian_biaya,
                        params
                );

                if (res == null) return null;

                JSONObject json = new JSONObject(res);
                return json.getString("pesan");

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String hasil) {
            pd.dismiss();
            if (hasil != null) {
                Toast.makeText(activity, hasil, Toast.LENGTH_LONG).show();
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
    }
}


package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import koneksi.Java_Connection;
import koneksi.Koneksi;


public class List_DataRincian extends AppCompatActivity {

    TextView nippegwdit, edit_lamp_sppd, edit_tgl_lamp;
    ListView listRincianBiaya;
    ArrayList<HashMap<String, String>> dataRincian;
    Button btnRefresh, btnTambah;
    Animation anim_hilang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_datarincian);
        anim_hilang     = AnimationUtils.loadAnimation(this, R.anim.anim_menghilang);
        nippegwdit      = findViewById(R.id.nippegwdit);
        edit_lamp_sppd  = findViewById(R.id.edit_lamp_sppd);
        edit_tgl_lamp   = findViewById(R.id.edit_tgl_lamp);
        btnRefresh      = findViewById(R.id.btn_refresh);
        btnTambah       = findViewById(R.id.btnTambahRincian);
        listRincianBiaya = findViewById(R.id.listRincianBiaya);
        dataRincian     = new ArrayList<>();

        Tampil_data();
        new AmbilRincianAsync().execute();

        btnRefresh.setOnClickListener(v -> {
            v.startAnimation(anim_hilang);
            new AmbilRincianAsync().execute();
        });

        btnTambah.setOnClickListener(v -> {
            v.startAnimation(anim_hilang);
            Intent i = new Intent(List_DataRincian.this, Edit_Rincian_Biaya.class);

            i.putExtra("mode", "tambah");
            i.putExtra("nosppd", edit_lamp_sppd.getText().toString());
            i.putExtra("nippegawai", nippegwdit.getText().toString());

            startActivityForResult(i, 100);
        });
    }

    private void showPesanSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", v -> {})
                .show();
    }
    public void Tampil_data() {

        Intent i = getIntent();

        nippegwdit.setText(i.getStringExtra("nip"));
        edit_lamp_sppd.setText(i.getStringExtra("nomor_surat_sppd"));
        edit_tgl_lamp.setText(i.getStringExtra("tgl_surat_masuk"));
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

    private void tampilkanError(String pesan) {

        if (pesan == null) {
            pesan = "Terjadi kesalahan tidak diketahui";
        }

        Toast.makeText(
                List_DataRincian.this,
                pesan,
                Toast.LENGTH_LONG
        ).show();
    }

    @SuppressLint("StaticFieldLeak")
    private class AmbilRincianAsync extends AsyncTask<Void, Void, String> {

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
                    List_DataRincian.this,
                    "",
                    "Mengambil rincian biaya...",
                    false
            );
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            try {
                String nomor = edit_lamp_sppd.getText().toString();

                if (nomor.trim().isEmpty()) {
                    errorMessage = "Nomor SPPD kosong";
                    return null;
                }

                String url = Koneksi.list_datarincian
                        + "?nomor_surat_sppd="
                        + URLEncoder.encode(nomor, "UTF-8");

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
                tampilkanError(errorMessage);
            }
        }

        @Override
        protected void onCancelled() {
            if (loading != null && loading.isShowing()) {
                loading.dismiss();
            }
            tampilkanError(errorMessage);
        }
    }

    private void parsingJSON(String json) {

        try {
            if (json == null) {
                tampilkanError("Respon server kosong");
                return;
            }

            JSONObject obj = new JSONObject(json);

            String message  = obj.optString("message", "");
            boolean status  = obj.optBoolean("status", false);
            String info     = obj.optString("info", "");

            // ❌ backend balikin error
            if (!obj.has("tampilkan_saja")) {
                tampilkanError("Format respon tidak sesuai");
                return;
            }

            JSONArray arr = obj.getJSONArray("tampilkan_saja");
            showPesanSnackbar(info);
            tampilkanError(message);

            if (arr == null || arr.length() == 0) {
                dataRincian.clear();
                return;
            }
            Log.d("MESSAGE_BACKEND", message);
            Log.d("RESPON_JSON", json);

            dataRincian.clear();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                HashMap<String, String> map = new HashMap<>();
                map.put("nomorurut", o.optString("nomor_urut", ""));
                map.put("id_rincian", o.optString("id_rincian", ""));
                map.put("no_sppd", o.optString("nomor_surat_sppd", ""));
                map.put("nip", o.optString("nip", ""));
                map.put("rincian", o.optString("rincian_biaya", ""));
                map.put("jumlah", o.optString("jumlah", ""));
                map.put("tgl", o.optString("tgl_pembuatan_rincian", ""));
                map.put("bukti", o.optString("bukti_image", ""));
                map.put("pembuat", o.optString("pembuat", ""));
                map.put("judul_pembuat", o.optString("judul_pembuat", ""));

                dataRincian.add(map);
            }

        } catch (Exception e) {
            tampilkanError("Gagal membaca data server");
            e.printStackTrace();
        }
    }
    private void setAdapter() {

        listRincianBiaya.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return dataRincian.size();
            }

            @Override
            public Object getItem(int i) {
                return dataRincian.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int i, View v, ViewGroup parent) {

                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.item_rincian_biaya, parent, false);
                }
                TextView nomorurut  = v.findViewById(R.id.txtNomorUrut);
                TextView rincian    = v.findViewById(R.id.txtRincianBiaya);
                TextView jumlah     = v.findViewById(R.id.txtJumlah);
                TextView tanggal    = v.findViewById(R.id.txtTanggal);
                TextView infopembuat= v.findViewById(R.id.txtinfopembuat);

                HashMap<String, String> d = dataRincian.get(i);
                nomorurut.setText(d.get("nomorurut"));
                rincian.setText(d.get("rincian"));
                jumlah.setText("Rp. " + d.get("jumlah"));
                tanggal.setText(d.get("tgl"));
                if (Objects.equals(d.get("pembuat"), "0")){
                    infopembuat.setVisibility(View.GONE);
                }else{
                    infopembuat.setVisibility(View.VISIBLE);
                    infopembuat.setText(d.get("judul_pembuat"));
                }
                return v;
            }
        });

        // 🔥 KLIK ITEM
        listRincianBiaya.setOnItemClickListener((parent, view, position, id) -> {

            HashMap<String, String> d = dataRincian.get(position);

            Intent i = new Intent(List_DataRincian.this, Edit_Rincian_Biaya.class);
            i.putExtra("nosppd", d.get("no_sppd"));
            i.putExtra("nippegawai", d.get("nip"));
            i.putExtra("id_rincian", d.get("id_rincian"));
            i.putExtra("rincian", d.get("rincian"));
            i.putExtra("jumlah", d.get("jumlah"));
            i.putExtra("tgl", d.get("tgl"));
            i.putExtra("bukti", d.get("bukti"));
            i.putExtra("pembuat", d.get("pembuat"));
//            startActivity(i);
            startActivityForResult(i, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            // 🔄 refresh list otomatis
            dataRincian.clear();
            new AmbilRincianAsync().execute();
        }
    }
}

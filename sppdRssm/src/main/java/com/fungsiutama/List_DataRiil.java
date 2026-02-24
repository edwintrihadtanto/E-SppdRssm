package com.fungsiutama;

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

public class List_DataRiil extends AppCompatActivity {
    TextView nippeg_riil, nosppd_riil, tglsppd_riil;
    ListView listRiilBiaya;
    ArrayList<HashMap<String, String>> dataRiil;
    Button btnRefresh, btnTambah;
    Animation anim_hilang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_datariil);
        anim_hilang     = AnimationUtils.loadAnimation(this, R.anim.anim_menghilang);
        nippeg_riil     = findViewById(R.id.nippeg_riil);
        nosppd_riil     = findViewById(R.id.nosppd_riil);
        tglsppd_riil    = findViewById(R.id.tglsppd_riil);
        btnRefresh      = findViewById(R.id.btn_refresh);
        btnTambah       = findViewById(R.id.btnTambahRiil);
        listRiilBiaya   = findViewById(R.id.listRiilBiaya);

        dataRiil        = new ArrayList<>();

        Tampil_dataRiil();
        new AmbilRiilAsync().execute();

        btnRefresh.setOnClickListener(v -> {
            v.startAnimation(anim_hilang);
            new AmbilRiilAsync().execute();
        });

        btnTambah.setOnClickListener(v -> {
            v.startAnimation(anim_hilang);
            Intent i = new Intent(List_DataRiil.this, Edit_Rincian_BiayaRiil.class);

            i.putExtra("mode", "tambah");
            i.putExtra("nosppd", nosppd_riil.getText().toString());
            i.putExtra("nippegawai", nippeg_riil.getText().toString());

            startActivityForResult(i, 100);
        });
    }
    private void showPesanSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", v -> {})
                .show();
    }
    public void Tampil_dataRiil() {

        Intent i = getIntent();

        nippeg_riil.setText(i.getStringExtra("nip"));
        nosppd_riil.setText(i.getStringExtra("nomor_surat_sppd"));
        tglsppd_riil.setText(i.getStringExtra("tgl_surat_masuk"));
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
    private class AmbilRiilAsync extends AsyncTask<Void, Void, String> {

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
                    List_DataRiil.this,
                    "",
                    "Mengambil rincian biaya riil...",
                    false
            );
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            try {
                String nomor = nosppd_riil.getText().toString();

                if (nomor.trim().isEmpty()) {
                    errorMessage = "Nomor SPPD Kosong";
                    return null;
                }

                String url = Koneksi.list_datariil
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
            if (!obj.has("tampilkan_data")) {
                tampilkanError("Format respon tidak sesuai");
                return;
            }

            JSONArray arr = obj.getJSONArray("tampilkan_data");
            showPesanSnackbar(info);
            tampilkanError(message);

            if (arr == null || arr.length() == 0) {
                dataRiil.clear();
                /*tampilkanError("Data Riil tidak ditemukan");*/
                return;
            }

            dataRiil.clear();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                HashMap<String, String> map = new HashMap<>();
                map.put("nomorurut", o.optString("nomor_urut", ""));
                map.put("id_riil", o.optString("id_riil", ""));
                map.put("no_sppd", o.optString("nomor_surat_sppd", ""));
                map.put("nip", o.optString("nip", ""));
                map.put("rincian", o.optString("uraian_daftar_riil", ""));
                map.put("jumlah", o.optString("jumlah_riil", ""));
                map.put("tgl", o.optString("tgl_pembuatan", ""));
                map.put("pembuat", o.optString("pembuat", ""));
                map.put("judul_pembuat", o.optString("judul_pembuat", ""));

                dataRiil.add(map);
            }

        } catch (Exception e) {
            tampilkanError("Gagal membaca data server");
        }
    }
    private void setAdapter() {

        listRiilBiaya.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return dataRiil.size();
            }

            @Override
            public Object getItem(int i) {
                return dataRiil.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int i, View v, ViewGroup parent) {

                if (v == null) {
                    v = getLayoutInflater()
                            .inflate(R.layout.item_rincian_biaya, parent, false);
                }
                TextView nomorurut  = v.findViewById(R.id.txtNomorUrut);
                TextView rincian    = v.findViewById(R.id.txtRincianBiaya);
                TextView jumlah     = v.findViewById(R.id.txtJumlah);
                TextView tanggal    = v.findViewById(R.id.txtTanggal);
                TextView infopembuat= v.findViewById(R.id.txtinfopembuat);

                HashMap<String, String> d = dataRiil.get(i);
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
        listRiilBiaya.setOnItemClickListener((parent, view, position, id) -> {

            HashMap<String, String> d = dataRiil.get(position);

            Intent i = new Intent(List_DataRiil.this, Edit_Rincian_BiayaRiil.class);
            i.putExtra("nosppd", d.get("no_sppd"));
            i.putExtra("nippegawai", d.get("nip"));
            i.putExtra("id_riil", d.get("id_riil"));
            i.putExtra("rincian", d.get("rincian"));
            i.putExtra("jumlah", d.get("jumlah"));
            i.putExtra("tgl", d.get("tgl"));
            i.putExtra("pembuat", d.get("pembuat"));
//            startActivity(i);
            startActivityForResult(i, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            dataRiil.clear();
            new AmbilRiilAsync().execute();
        }
    }

    private void tampilkanError(String pesan) {

        if (pesan == null) {
            pesan = "Terjadi kesalahan tidak diketahui";
        }

        Toast.makeText(
                List_DataRiil.this,
                pesan,
                Toast.LENGTH_LONG
        ).show();
    }

}

package com.fungsiutama;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

import kamera.FileUtil;
import koneksi.Java_Connection;
import koneksi.Koneksi;
public class Edit_Rincian_Biaya extends AppCompatActivity {
    EditText edtRincian, edtJumlah;
    TextView txtTanggal;
    ImageView imgBukti;
    Uri imageUri;
    File imageFile;
    static final int REQ_GALERI = 10;
    static final int REQ_KAMERA = 20;
    Button btnSimpan, btnHapus, btnPilih;
    String id_rincian, nosppd, nip, pembuat, mode;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_edit_rincian_biaya);

        edtRincian = findViewById(R.id.edtRincian);
        edtJumlah  = findViewById(R.id.edtJumlah);
        txtTanggal = findViewById(R.id.txtTanggal);
        imgBukti   = findViewById(R.id.imgBukti);
        btnSimpan  = findViewById(R.id.btn_simpanrincian);
        btnHapus   = findViewById(R.id.btn_hapusrincian);
        btnPilih   = findViewById(R.id.btn_pilihgambar);
        Intent i = getIntent();

        mode        = i.getStringExtra("mode");
        if (mode == null) mode = "edit"; // default
        nosppd      = i.getStringExtra("nosppd");
        nip         = i.getStringExtra("nippegawai");

        if (mode.equals("tambah")) {

            edtRincian.setText("");
            edtJumlah.setText("");
            txtTanggal.setText("");

            imgBukti.setImageResource(R.drawable.no_upload);

            btnHapus.setVisibility(View.GONE);

        } else {
            id_rincian  = i.getStringExtra("id_rincian");
            edtRincian.setText(i.getStringExtra("rincian"));
            edtJumlah.setText(i.getStringExtra("jumlah"));
            txtTanggal.setText(i.getStringExtra("tgl"));
            pembuat = i.getStringExtra("pembuat");
            String namaFile = i.getStringExtra("bukti");
            /*Toast.makeText(this, namaFile, Toast.LENGTH_LONG).show();*/
            if (pembuat.equals("1")){
                btnHapus.setVisibility(View.GONE);
                btnSimpan.setVisibility(View.GONE);
                btnPilih.setVisibility(View.GONE);
                imgBukti.setImageResource(R.drawable.no_bukti);
            }

            if (!Objects.requireNonNull(namaFile).isEmpty()) {
                String urlImage = Koneksi.FolderUpload + namaFile;
                new DownloadImageTask(imgBukti).execute(urlImage);
            }
        }

        btnSimpan.setOnClickListener(v -> {
            if (mode.equals("tambah")) {
                new SimpanRincianBiaya(Edit_Rincian_Biaya.this).execute();
            } else {
                new UpdateRincian(Edit_Rincian_Biaya.this).execute();
            }
        });

        btnHapus.setOnClickListener((View v) -> new AlertDialog.Builder(Edit_Rincian_Biaya.this)
                .setTitle("Hapus Rincian")
                .setMessage("Yakin ingin menghapus rincian ini?")
                .setPositiveButton("Hapus", (d, w) -> new HapusRincian(Edit_Rincian_Biaya.this).execute())
                .setNegativeButton("Batal", null)
                .show());

        btnPilih.setOnClickListener(v -> pilihGambar());
    }
    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @SuppressLint("StaticFieldLeak")
        private final ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.connect();

                // ⛔ FILE TIDAK ADA / FORBIDDEN
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream in = conn.getInputStream();
                return BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // 🔁 FALLBACK IMAGE
                imageView.setImageResource(R.drawable.img_not_found);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class UpdateRincian extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        Java_Connection jc = new Java_Connection();
        Activity activity;
        String uraian, jumlah;
        public UpdateRincian(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(activity);
            pd.setMessage("Menyimpan perubahan rincian...");
            pd.setCancelable(false);
            pd.show();

            uraian = edtRincian.getText().toString();
            jumlah = edtJumlah.getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = null;
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id_rincian", id_rincian);
                params.put("uraian", uraian);
                params.put("jml", jumlah);
                params.put("nomor_surat_sppd", nosppd);
                params.put("nip", nip);

                if (imageFile != null && imageFile.exists()) {
                    response = jc.sendMultipart(
                            Koneksi.update_rincian_biaya,
                            params,
                            imageFile,
                            "bukti"
                    );
                } else {
                    response = jc.sendPostRequest(
                            Koneksi.update_rincian_biaya,
                            params
                    );
                }

                if (response == null) return "false|Server tidak merespon";

                JSONObject json = new JSONObject(response.trim());
                boolean status = json.getBoolean("status");
                String pesan = json.getString("pesan");

                return status + "|" + pesan;

            } catch (Exception e) {
                Log.e("JSON_ERROR_UPDATE", "RAW RESPONSE = " + response);
                e.printStackTrace();
                return "false|Response tidak valid dari server";
            }
        }

        @Override
        protected void onPostExecute(String hasil) {
            pd.dismiss();

            if (hasil != null) {

                String[] parts = hasil.split("\\|", 2);
                boolean status = Boolean.parseBoolean(parts[0]);
                String pesan = parts[1];

                Toast.makeText(activity, pesan, Toast.LENGTH_LONG).show();

                if (status) {
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class HapusRincian extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        Java_Connection jc = new Java_Connection();
        Activity activity;

        public HapusRincian(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(activity);
            pd.setMessage("Menghapus rincian...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("id_rincian", id_rincian);

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
                activity.finish(); // ⬅️ balik ke list
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SimpanRincianBiaya extends AsyncTask<Void, Void, String> {

        ProgressDialog pd;
        Java_Connection jc = new Java_Connection();
        Activity activity;
        String uraian, jumlah;
        public SimpanRincianBiaya(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(activity);
            pd.setMessage("Menyimpan rincian biaya.");
            pd.setCancelable(false);
            pd.show();

            uraian = edtRincian.getText().toString();
            jumlah = edtJumlah.getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = null;

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("nosppd", nosppd);
                params.put("nip", nip);
                params.put("uraian", uraian);
                params.put("jml", jumlah);

                res = jc.sendMultipart(
                        Koneksi.simpan_rincian_biaya,
                        params,
                        imageFile,
                        "bukti"
                );

                if (res == null) return "Server tidak merespon";

                JSONObject json = new JSONObject(res.trim());
                boolean status = json.getBoolean("status");
                String pesan = json.getString("pesan");

                return status + "|" + pesan;

            } catch (Exception e) {
                Log.e("JSON_ERROR", "RAW RESPONSE = " + res);
                e.printStackTrace();
                return "Response tidak valid dari server";
            }
        }

        @Override
        protected void onPostExecute(String hasil) {
            pd.dismiss();
            if (hasil != null) {

                String[] parts = hasil.split("\\|", 2);
                boolean status = Boolean.parseBoolean(parts[0]);
                String pesan = parts[1];

                Toast.makeText(activity, pesan, Toast.LENGTH_LONG).show();

                if (status) {
                    activity.setResult(Activity.RESULT_OK);
                    activity.finish();
                }
            }

        }
    }

    private void pilihGambar() {
        String[] opsi = {"Kamera", "Galeri"};

        new AlertDialog.Builder(this)
                .setTitle("Pilih Gambar")
                .setItems(opsi, (d, i) -> {
                    if (i == 0) bukaKamera();
                    else bukaGaleri();
                }).show();
    }

    private void bukaGaleri() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, REQ_GALERI);
    }
    private void bukaKamera() {

        File dir = new File(
                getFilesDir(),
                "Bukti Upload SPPD"
        );

        if (!dir.exists()) dir.mkdirs();

        File photoFile = new File(
                dir,
                "bukti_" + System.currentTimeMillis() + ".jpg"
        );

        imageFile = photoFile;

        imageUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                photoFile
        );

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, REQ_KAMERA);
    }


    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

        if (res != RESULT_OK) return;

        try {

            if (req == REQ_GALERI && data != null) {

                imageUri = data.getData();
                if (imageUri == null) return;

            } else if (req == REQ_KAMERA) {

                // data == null itu NORMAL
                if (imageUri == null) {
                    Toast.makeText(this, "URI kamera kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // 🔥 SATU JALUR UNTUK KAMERA & GALERI
            imageFile = FileUtil.compressAndRotate(
                    this,
                    imageUri,
                    1600,   // resolusi lebih tajam
                    500     // limit 500 KB
            );

            /*imgBukti.setImageURI(Uri.fromFile(imageFile));*/
            imgBukti.setImageDrawable(null);   // clear dulu
            imgBukti.invalidate();
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imgBukti.setImageBitmap(bitmap);

            Log.d("IMG_PATH", imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show();
        }
    }
}


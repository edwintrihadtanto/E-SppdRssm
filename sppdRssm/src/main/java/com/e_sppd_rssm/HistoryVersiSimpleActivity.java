package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.e_sppd.rssm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import koneksi.Java_Connection;
import koneksi.Koneksi;

public class HistoryVersiSimpleActivity extends AppCompatActivity {
    LinearLayout container;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_versi_simple);

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.history_versi);
        container = findViewById(R.id.container_history);
        progressBar = findViewById(R.id.progressBar_history);

        loadHistory();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadHistory() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                // Gunakan koneksi custom
                Java_Connection conn = new Java_Connection();
                String response = conn.sendGetRequest(Koneksi.history_versi);

                if (response == null || response.isEmpty()) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Gagal memuat data history versi", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                JSONObject json = new JSONObject(response);
                if (!json.optBoolean("success", false)) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                JSONArray historyArray = json.getJSONArray("history");
                Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_regular);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        for (int i = 0; i < historyArray.length(); i++) {
                            JSONObject item = historyArray.getJSONObject(i);

                            LinearLayout card = getLayout();

                            TextView versi = new TextView(this);
                            versi.setText("Versi: " + item.getString("versi"));
                            versi.setTextSize(16);
                            versi.setTextColor(R.color.hitam);
                            versi.setTypeface(typeface, Typeface.BOLD);
                            card.addView(versi);

                            TextView tanggal = new TextView(this);
                            tanggal.setText("Tanggal Rilis: " + item.getString("tanggal_rilis"));
                            tanggal.setTextSize(14);
                            tanggal.setTextColor(R.color.hitam);
                            tanggal.setTypeface(typeface);
                            card.addView(tanggal);

                            TextView fitur = new TextView(this);
                            fitur.setText("Fitur: " + item.getString("fitur"));
                            fitur.setTextSize(14);
                            fitur.setTextColor(R.color.hitam);
                            fitur.setTypeface(typeface);
                            card.addView(fitur);

                            /*container.addView(card);*/
                            addCardWithFadeIn(card);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Terjadi kesalahan saat parsing JSON", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Gagal memuat data history versi", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private LinearLayout getLayout() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setPadding(24, 24, 24, 24);
        card.setBackgroundResource(R.drawable.card_background);
        return card;
    }

    private LinearLayout getLinearLayout() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setPadding(24, 24, 24, 24);
        card.setBackgroundResource(R.drawable.card_background);
        return card;
    }
    private void addCardWithFadeIn(LinearLayout card) {
        // Set initial alpha ke 0 (invisible)
        card.setAlpha(0f);
        container.addView(card);

        // Animate alpha ke 1 (fade-in)
        card.animate()
                .alpha(1f)
                .setDuration(400) // durasi 400ms
                .setStartDelay(container.getChildCount() * 100L) // stagger animasi tiap card
                .start();
    }

}

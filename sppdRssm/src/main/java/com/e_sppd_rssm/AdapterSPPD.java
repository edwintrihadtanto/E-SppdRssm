package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.e_sppd.rssm.R;

import java.util.List;

import koneksi.Daftar_String;

public class AdapterSPPD extends BaseAdapter {
    private final Context context;
    private final List<Daftar_String> list;
    private final OnMenuClickListener listener;

    public AdapterSPPD(Context context, List<Daftar_String> list,
                       OnMenuClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
//        Log.e("ADAPTER", "Constructor dipanggil, size = " + list.size());
    }
    @Override
    public int getCount() {
//        Log.e("ADAPTER", "getCount dipanggil, size = " + list.size());
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {

        TextView penomoran;

        TextView nomor_spt;
        TextView nomor_sppd;

        TextView tgl_berangkattiba;
        TextView lama_perjalanan;
        TextView status_lap;
        TextView status_biaya;
        TextView status_riil;
        TextView waktu_dibuat;
        View  rootRow;
        CardView btnEdit;

    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("ADAPTER", "getView dipanggil posisi = " + position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.row_list_detail_datasppd, parent, false);

            holder = new ViewHolder();
            holder.rootRow = convertView.findViewById(R.id.rootRowSPPD);
            holder.penomoran = convertView.findViewById(R.id.penomoran);
            holder.nomor_spt = convertView.findViewById(R.id.menampilkan_nomor_spt);
            holder.nomor_sppd = convertView.findViewById(R.id.menampilkan_nomor_sppd);

            holder.tgl_berangkattiba = convertView.findViewById(R.id.text_tanggal);
            holder.lama_perjalanan = convertView.findViewById(R.id.text_lama_perjalanan);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);

            holder.status_lap = convertView.findViewById(R.id.status_posting_laporan);
            holder.status_biaya = convertView.findViewById(R.id.status_posting_biaya);
            holder.status_riil = convertView.findViewById(R.id.status_posting_riil);
            holder.waktu_dibuat = convertView.findViewById(R.id.waktu_dibuat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Daftar_String data = list.get(position);
        holder.rootRow.setOnClickListener(view -> Toast.makeText(context.getApplicationContext(),
                "Klik ikon [✎] untuk melihat daftar menu.",
                Toast.LENGTH_SHORT).show());
        holder.penomoran.setText(data.getnomor_urut());
        holder.nomor_spt.setText(data.getnomor_SPT());
        holder.nomor_sppd.setText(data.getnomor_SPPD());

        holder.tgl_berangkattiba.setText(data.gettgl_brngkt() +" → "+ data.gettgl_kembali());
        holder.lama_perjalanan.setText(data.getlama_perj() + " Hari");
        holder.waktu_dibuat.setText(data.getwaktu_dibuat());
        setBadge(holder.status_lap, "Laporan", data.getstatus_laporan_petugas());
        setBadge(holder.status_biaya, "Rincian", data.getstatus_rincian());
        setBadge(holder.status_riil, "Riil", data.getstatus_riil());

        holder.btnEdit.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(context, v);

            popup.getMenu().add(0, 1, 0, "1. Form Laporan Perjalanan Dinas");
            popup.getMenu().add(0, 2, 1, "2. Form Perincian Biaya dan Bukti");
            popup.getMenu().add(0, 3, 2, "3. Form Perincian Biaya Riil");
            popup.getMenu().add(0, 4, 3, "4. Download SPT");
            popup.getMenu().add(0, 5, 4, "5. Download SPPD");
            popup.getMenu().add(0, 6, 5, "6. Selesai / Posting");

            popup.setOnMenuItemClickListener(item -> {

                if (listener != null) {
                    listener.onMenuClick(data, item.getItemId());
                }

                return true;
            });
            popup.show();
        });

        return convertView;
    }

    @SuppressLint("SetTextI18n")
    private void setBadge(TextView tv, String label, String status) {

        if (status == null || status.trim().isEmpty()) {
            status = "BELUM";
        }

        // 🔴 INI KUNCI: teks + konteks
        tv.setText(label + " : " + status);

        if ("SUDAH".equalsIgnoreCase(status)) {
            tv.setBackgroundResource(R.drawable.badge_sudah);
        } else {
            tv.setBackgroundResource(R.drawable.badge_belum);
        }

        tv.setVisibility(View.VISIBLE);
    }

    public interface OnMenuClickListener {
        void onMenuClick(Daftar_String data, int menuId);
    }

}

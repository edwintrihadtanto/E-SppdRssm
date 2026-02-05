package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.e_sppd.rssm.R;

import java.util.ArrayList;
import java.util.List;

import koneksi.Daftar_String;

@SuppressLint("DefaultLocale")
public class List_Daftar_SPT_per_nip extends BaseAdapter implements Filterable {

	private Context context;
	private List<Daftar_String> list, filterd;
	private CheckBox centangdaftar;
	ListDataPegawai_Filter filter ;
	List_Daftar_SPT_per_nip(Context context, List<Daftar_String> list) {
		this.context = context;
		this.list = list;
		this.filterd = this.list;
	}

	@Override
	public int getCount() {
		return filterd.size();
	}

	@Override
	public Object getItem(int position) {
		return filterd.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({"InflateParams", "ResourceAsColor", "SetTextI18n"})
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.row_list_detail_datasppd,
					null);
		}
		
		Daftar_String mhs = filterd.get(position);
		// RelativeLayout pengatur_layout =
		// (RelativeLayout)convertView.findViewById(R.id.pengatur_layout);

//		ImageView status_posting_merah = convertView.findViewById(R.id.status_posting);
//		ImageView status_posting_hijau = convertView.findViewById(R.id.status_posting2);
//
//		ImageView status_posting_merah1 = convertView.findViewById(R.id.status_posting1);
//		ImageView status_posting_hijau1 = convertView.findViewById(R.id.status_posting12);
//
//		ImageView status_posting_merah2 = convertView.findViewById(R.id.status_posting3);
//		ImageView status_posting_hijau2 = convertView.findViewById(R.id.status_posting31);

		TextView penomoran 				= convertView.findViewById(R.id.penomoran);

		TextView text_sts_post 			= convertView.findViewById(R.id.text_sts_post);
		ImageView img_post 				= convertView.findViewById(R.id.img_post);

		TextView menampilkan_nomor_spt 	= convertView.findViewById(R.id.menampilkan_nomor_spt);
		TextView menampilkan_nomor_sppd = convertView.findViewById(R.id.menampilkan_nomor_sppd);

		TextView text_tgl_keberangkatan = convertView.findViewById(R.id.text_tgl_keberangkatan);
		TextView text_tgl_tiba 			= convertView.findViewById(R.id.text_tgl_tiba);
		TextView text_lama_perjalanan 	= convertView.findViewById(R.id.text_lama_perjalanan);

		ImageView img_posting_lap 		= convertView.findViewById(R.id.img_posting_laporan);
		ImageView img_posting_biaya		= convertView.findViewById(R.id.img_posting_biaya);
		ImageView img_posting_riil 		= convertView.findViewById(R.id.img_posting_riil);
		TextView status_lap_perj 		= convertView.findViewById(R.id.status_posting_laporan);
		TextView status_rincian 		= convertView.findViewById(R.id.status_posting_biaya);
		TextView status_riil 			= convertView.findViewById(R.id.status_posting_riil);

		text_sts_post.setText(mhs.getsts_postingan());
		menampilkan_nomor_spt.setText(mhs.getnomor_SPT());
		menampilkan_nomor_sppd.setText(mhs.getnomor_SPPD());
		text_tgl_keberangkatan.setText(mhs.gettgl_brngkt());
		text_tgl_tiba.setText(mhs.gettgl_kembali());
		text_lama_perjalanan.setText(mhs.getlama_perj() + " Hari Perjalanan");
		status_lap_perj.setText(mhs.getstatus_laporan_petugas());
		status_rincian.setText(mhs.getstatus_rincian());
		status_riil.setText(mhs.getstatus_riil());
		penomoran.setText(mhs.getnomor_urut());

		if (text_sts_post.getText().toString().contains("0")){
			text_sts_post.setText("BELUM POSTING");
			img_post.setBackgroundResource(R.drawable.merah);
		}else{
			text_sts_post.setText("No Match");
			img_post.setBackgroundResource(R.drawable.merah);
		}

		String cek_status_lap 		= mhs.getstatus_laporan_petugas();
		String cek_status_rincian 	= mhs.getstatus_rincian();
		String cek_status_riil 		= mhs.getstatus_riil();

//		View a = convertView.findViewById(R.id.ko);
//		View a2 = convertView.findViewById(R.id.ko2);

		if (cek_status_lap.contains("SUDAH")) {
			img_posting_lap.setBackgroundResource(R.drawable.hijau);
		} else {
			img_posting_lap.setBackgroundResource(R.drawable.merah);
		}

		if (cek_status_rincian.contains("SUDAH")) {
			img_posting_biaya.setBackgroundResource(R.drawable.hijau);
		} else {
			img_posting_biaya.setBackgroundResource(R.drawable.merah);
		}

		if (cek_status_riil.contains("SUDAH")) {
			img_posting_riil.setBackgroundResource(R.drawable.hijau);
		} else {
			img_posting_riil.setBackgroundResource(R.drawable.merah);
		}

		centangdaftar = convertView.findViewById(R.id.centangdaftar);
		centangdaftar.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (!isChecked) {
				// show password
				centangdaftar.setChecked(false);
				//edit_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
			} else {
				centangdaftar.setChecked(true);
				// hide password
				//edit_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			}
		});


		return convertView;
	}

	@Override
	public Filter getFilter() {

		filter = new ListDataPegawai_Filter();
		return filter;
	}

	/** Class filter untuk melakukan filter (pencarian) */

	private class ListDataPegawai_Filter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<Daftar_String> filteredData = new ArrayList<>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();

			for (Daftar_String mhs : list) {

				if ((mhs.gettgl_aktivitas().contains(filterString))) {
					filteredData.add(mhs);
				}
			}
			result.count = filteredData.size();
			result.values = filteredData;
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			filterd = (List<Daftar_String>) results.values;
			notifyDataSetChanged();
		}

	}

}

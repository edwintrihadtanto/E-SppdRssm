package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.e_sppd.rssm.R;

import java.util.ArrayList;
import java.util.List;

import koneksi.Daftar_String;
@SuppressLint("DefaultLocale")
public class List_menampilkan_data_spt extends BaseAdapter implements
		Filterable {
	private Context context;
	private List<Daftar_String> list, filterd;

	public List_menampilkan_data_spt(Context context, List<Daftar_String> list) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(
					R.layout.row_tampil_activity_sppd_berdasarkan_spt, null);

		}
		Daftar_String mhs = filterd.get(position);

		TextView nomor = (TextView) convertView
				.findViewById(R.id.row_nomorspt_pegawai_spt);
		nomor.setText(mhs.getnomor_SPT());

		TextView nama = (TextView) convertView
				.findViewById(R.id.row_nama_pegawai_spt);
		nama.setText(mhs.getnama_pegawai());

		TextView gol = (TextView) convertView
				.findViewById(R.id.row_gol_pegawai_spt);
		gol.setText(mhs.getgolongan());

		TextView jab = (TextView) convertView
				.findViewById(R.id.row_jab_pegawai_spt);
		jab.setText(mhs.getjabatan());

		return convertView;
	}

	@Override
	public Filter getFilter() {

		ListDataPegawai_Filter filter = new ListDataPegawai_Filter();
		return filter;
	}

	/** Class filter untuk melakukan filter (pencarian) */

	private class ListDataPegawai_Filter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			List<Daftar_String> filteredData = new ArrayList<Daftar_String>();
			FilterResults result = new FilterResults();
			String filterString = constraint.toString().toLowerCase();

			for (Daftar_String mhs : list) {

				if ((mhs.getnip().toString().contains(filterString))
						|| (mhs.getnomor_SPT().toLowerCase()
								.contains(filterString))
						|| (mhs.getnama_pegawai().toLowerCase()
								.contains(filterString))) {
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

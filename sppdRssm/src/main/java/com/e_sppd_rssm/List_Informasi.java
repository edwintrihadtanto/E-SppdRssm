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
public class List_Informasi extends BaseAdapter implements Filterable {

	private Context context;
	private List<Daftar_String> listpost, filterd;
	ListDataPegawai_Filter filter ;

	List_Informasi(Context context, List<Daftar_String> listpost) {
		this.context = context;
		this.listpost = listpost;
		this.filterd = this.listpost;
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
			convertView = inflater.inflate(R.layout.row_informasi,
					null);

		}
		
		Daftar_String mhs = filterd.get(position);
		
		TextView jmlh_sppd = convertView.findViewById(R.id.jmlh_sppd);
		jmlh_sppd.setText(mhs.getjml_nomor_surat_sppd());

		TextView jmlh_posting = convertView.findViewById(R.id.jmlh_posting);
		jmlh_posting.setText(mhs.getjml_terposting());
		
		TextView note = convertView.findViewById(R.id.note);
		note.setText(mhs.getnote());

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

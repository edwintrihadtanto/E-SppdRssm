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
public class List_Edit_Rincian extends BaseAdapter implements Filterable {

	private Context context;
	private List<Daftar_String> listpost, filterd;

	public List_Edit_Rincian(Context context, List<Daftar_String> listpost) {
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
			convertView = inflater.inflate(R.layout.row_edit_rincian,
					null);

		}
		
		Daftar_String mhs = filterd.get(position);
		
		TextView textid_rincian 			= (TextView) convertView.findViewById(R.id.textid_rincian);
		textid_rincian.setText(mhs.getid_rincian());
		
		TextView textnomor_sppd 			= (TextView) convertView.findViewById(R.id.textnomor_sppd);
		textnomor_sppd.setText(mhs.getnomor_SPPD());
		
		TextView texteditrincian_biaya 		= (TextView) convertView.findViewById(R.id.texteditrincian_biaya);
		texteditrincian_biaya.setText(mhs.geturaian_rincian());
		
		TextView texteditjmlrincian_biaya 	= (TextView) convertView.findViewById(R.id.texteditjmlrincian_biaya);
		texteditjmlrincian_biaya.setText(mhs.getjml_rincian());
		
	//	ImageView gambar_bukti 				= (ImageView) convertView.findViewById(R.id.gambar_bukti);
	//	gambar_bukti.setBackgroundDrawable(mhs.getjml_rincian());
		
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

			for (Daftar_String mhs : listpost) {

				//if ((mhs.gettgl_aktivitas().toString().contains(filterString))) {
				//	filteredData.add(mhs);
				//}
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

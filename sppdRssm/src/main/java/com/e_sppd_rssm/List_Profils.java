package com.e_sppd_rssm;

import java.util.ArrayList;
import java.util.List;

import koneksi.Daftar_String;
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
@SuppressLint("DefaultLocale")
public class List_Profils extends BaseAdapter implements Filterable {

	private Context context;
	private List<Daftar_String> list, filterd;

	public List_Profils(Context context, List<Daftar_String> list) {
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
			convertView = inflater.inflate(R.layout.row_list_nama_peg, null);

		}
		Daftar_String mhs = filterd.get(position);

		TextView nip = (TextView) convertView.findViewById(R.id.row_nipprofil);
		nip.setText(mhs.getnip());

		TextView nama = (TextView) convertView
				.findViewById(R.id.row_namapegprofil);
		nama.setText(mhs.getnama_pegawai());

		TextView jab = (TextView) convertView.findViewById(R.id.row_jabatanprofil);
		jab.setText(mhs.getjabatan());
		jab.setVisibility(View.GONE);
		
		TextView gol = (TextView) convertView.findViewById(R.id.row_golonganprofil);
		gol.setText(mhs.getgolongan());
		gol.setVisibility(View.GONE);
		
		TextView pass = (TextView) convertView.findViewById(R.id.row_reg_passprofil);
		pass.setText(mhs.getpass());
		pass.setVisibility(View.GONE);
		
		TextView text_jabatan 	= (TextView) convertView.findViewById(R.id.text_jabatan);
		text_jabatan.setVisibility(View.GONE);
		TextView text_golongan	= (TextView) convertView.findViewById(R.id.text_golongan);
		text_golongan.setVisibility(View.GONE);
		TextView text_pass		= (TextView) convertView.findViewById(R.id.text_pass);
		text_pass.setVisibility(View.GONE);
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

				if ((mhs.gettgl_aktivitas().toString().contains(filterString))) {
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

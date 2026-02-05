//TIDAK DIAPAKAI
package com.e_sppd_rssm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;

public class Detail_Daftar_Laporan_Sppd extends AppCompatActivity {
	private TextView detail_nip, detail_PdanG, detail_JaborIns,
			detail_BiayaPerjDinas, detail_Tujuan, detail_JnsKendaraan,
			detail_AwalBerangkat, detail_AkhirBerangkat, detail_lamaperj,
			detail_TglBerangkat, detail_TglKembali, pengikut1, pengikut2,
			pengikut3, pengikut4, pengikut5, tgl1, jam1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_daftar_sppd);
		detail_nip = (TextView) findViewById(R.id.detail_nip);
		detail_PdanG = (TextView) findViewById(R.id.detail_PdanG);
		detail_JaborIns = (TextView) findViewById(R.id.detail_JaborIns);
		detail_BiayaPerjDinas = (TextView) findViewById(R.id.detail_BiayaPerjDinas);
		detail_Tujuan = (TextView) findViewById(R.id.detail_Tujuan);
		detail_JnsKendaraan = (TextView) findViewById(R.id.detail_JnsKendaraan);
		detail_AwalBerangkat = (TextView) findViewById(R.id.detail_AwalBerangkat);
		detail_AkhirBerangkat = (TextView) findViewById(R.id.detail_AkhirBerangkat);
		detail_lamaperj = (TextView) findViewById(R.id.detail_lamaperj);
		detail_TglBerangkat = (TextView) findViewById(R.id.detail_TglBerangkat);
		detail_TglKembali = (TextView) findViewById(R.id.detail_TglKembali);
		pengikut1 = (TextView) findViewById(R.id.pengikut1);
		pengikut2 = (TextView) findViewById(R.id.pengikut2);
		pengikut3 = (TextView) findViewById(R.id.pengikut3);
		pengikut4 = (TextView) findViewById(R.id.pengikut4);
		pengikut5 = (TextView) findViewById(R.id.pengikut5);
		tgl1 = (TextView) findViewById(R.id.tgl1);
		jam1 = (TextView) findViewById(R.id.jam1);

		Tampil_data();
	}

	public void Tampil_data() {
		pengikut1.setEnabled(false);
		pengikut2.setEnabled(false);
		pengikut3.setEnabled(false);
		pengikut4.setEnabled(false);
		pengikut5.setEnabled(false);

		// String nip = getIntent().getStringExtra("nip");
		String nomor_spt = getIntent().getStringExtra("nomor_spt");
		String nama = getIntent().getStringExtra("nama_pegawai");
		String pdang = getIntent().getStringExtra("jabatan");
		String jaborins = getIntent().getStringExtra("golongan");
		String biaya_perj = getIntent().getStringExtra("biaya_perj");
		String maksud_perj = getIntent().getStringExtra("maksud_perj");
		String alat_angkutan = getIntent().getStringExtra("alat_angkutan");
		String tempat_brngkt = getIntent().getStringExtra("tempat_brngkt");
		String tempat_tujuan = getIntent().getStringExtra("tempat_tujuan");
		String lama_perj = getIntent().getStringExtra("lama_perj");
		String tgl_brngkt = getIntent().getStringExtra("tgl_brngkt");
		String tgl_kembali = getIntent().getStringExtra("tgl_kembali");
		String tambh_pengikut1 = getIntent().getStringExtra("tambh_pengikut1");
		String tambh_pengikut2 = getIntent().getStringExtra("tambh_pengikut2");
		String tambh_pengikut3 = getIntent().getStringExtra("tambh_pengikut3");
		String tambh_pengikut4 = getIntent().getStringExtra("tambh_pengikut4");
		String tambh_pengikut5 = getIntent().getStringExtra("tambh_pengikut5");
		String tanggal_aktivitas = getIntent().getStringExtra(
				"tanggal_aktivitas");
		String waktu_aktivitas = getIntent().getStringExtra("waktu_aktivitas");

		detail_nip.setText(nomor_spt);
		detail_PdanG.setText(pdang);
		detail_JaborIns.setText(jaborins);
		detail_BiayaPerjDinas.setText(biaya_perj);
		detail_Tujuan.setText(maksud_perj);
		detail_JnsKendaraan.setText(alat_angkutan);
		detail_AwalBerangkat.setText(tempat_brngkt);
		detail_AkhirBerangkat.setText(tempat_tujuan);
		detail_lamaperj.setText(lama_perj);
		detail_TglBerangkat.setText(tgl_brngkt);
		detail_TglKembali.setText(tgl_kembali);

		pengikut1.setText(tambh_pengikut1);
		pengikut2.setText(tambh_pengikut2);
		pengikut3.setText(tambh_pengikut3);
		pengikut4.setText(tambh_pengikut4);
		pengikut5.setText(tambh_pengikut5);
		tgl1.setText(tanggal_aktivitas);
		jam1.setText(waktu_aktivitas);
	}
}

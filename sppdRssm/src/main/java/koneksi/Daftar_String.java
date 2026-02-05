package koneksi;

public class Daftar_String {
	private String nip;
	private String nama_pegawai;
	private String jabatan;
	private String golongan;
	private String pass;

	private String biaya_perj;
	private String maksud_perj;
	private String alat_angkutan;
	private String tempat_brngkt;
	private String tempat_tujuan;
	private String lama_perj;
	private String tgl_brngkt;
	private String tgl_kembali;
	private String tambh_pengikut1;
	private String tambh_pengikut2;
	private String tambh_pengikut3;
	private String tambh_pengikut4;
	private String tambh_pengikut5;

	private String tgl_aktivitas;
	private String jam_aktivitas;
	private String nomor_SPT;
	private String nomor_SPPD;
	private String jml_petugas;
	private String lokasi_dikeluarkan;
	private String tgl_dikeluarkan;
	private String akun_anggaran;
	private String surat_masuk_dari;
	private String tgl_surat_spt_masuk;
	private String id_spt;
	private String status_laporan_petugas;
	private String id_sppd;
	private String status_riil;
	private String status_rincian;
	private String informasi;
	private String jml_nomor_surat_sppd;
	private String jml_stspost;
	private String note;
	
	private String nip_pembuatlaporanperj;
	private String nomor_spt_laporanperj;
	private String hasil_pertemuan;
	private String masalah;
	private String saran;
	private String lain_lain;
	private String tgl_pembuatan_laporan;
	
	private String id_rincian;
	private String uraian_rincian;
	private String jml_rincian;
	private String bukti_rincian;

	private String id_riil;
	private String uraian_riil;
	private String jml_riil;
	
	private String sts_postingan;
	private String jml_terposting;
	private String nomor_urut;
	public Daftar_String() {
		super();
	}

	public Daftar_String(String nip, String nama_pegawai, String jabatan,
			String golongan, String pass, String biaya_perj,
			String maksud_perj, String alat_angkutan, String tempat_brngkt,
			String tempat_tujuan, String lama_perj, String tgl_brngkt,
			String tgl_kembali, String tambh_pengikut1, String tambh_pengikut2,
			String tambh_pengikut3, String tambh_pengikut4,
			String tambh_pengikut5, String tgl_aktivitas, String jam_aktivitas,
			String nomor_SPT, String nomor_SPPD, String jml_petugas,
			String tgl_dikeluarkan, String lokasi_dikeluarkan,
			String akun_anggaran, String surat_masuk_dari,
			String tgl_surat_spt_masuk, String id_spt,
			String status_laporan_petugas, String id_sppd, String status_riil,
			String status_rincian, String informasi, String jml_nomor_surat_sppd, String jml_stspost,
			String note, String nip_pembuatlaporanperj, String nomor_spt_laporanperj, String hasil_pertemuan, 
			String masalah, String saran, String lain_lain, String tgl_pembuatan_laporan, String uraian_rincian,
			String jml_rincian, String bukti_rincian, String uraian_riil, String jml_riil, String id_rincian, String id_riil,
			String sts_postingan, String jml_terposting, String nomor_urut){
		super();

		this.nip 					= nip;
		this.nama_pegawai 			= nama_pegawai;
		this.jabatan 				= jabatan;
		this.golongan 				= golongan;
		this.pass 					= pass;

		this.biaya_perj 			= biaya_perj;
		this.maksud_perj 			= maksud_perj;
		this.alat_angkutan 			= alat_angkutan;
		this.tempat_brngkt 			= tempat_brngkt;
		this.tempat_tujuan 			= tempat_tujuan;
		this.lama_perj 				= lama_perj;
		this.tgl_brngkt 			= tgl_brngkt;
		this.tgl_kembali 			= tgl_kembali;
		this.tambh_pengikut1 		= tambh_pengikut1;
		this.tambh_pengikut2 		= tambh_pengikut2;
		this.tambh_pengikut3 		= tambh_pengikut3;
		this.tambh_pengikut4 		= tambh_pengikut4;
		this.tambh_pengikut5 		= tambh_pengikut5;
		this.tgl_aktivitas 			= tgl_aktivitas;
		this.jam_aktivitas 			= jam_aktivitas;
		this.nomor_SPT 				= nomor_SPT;
		this.nomor_SPPD 			= nomor_SPPD;
		this.jml_petugas 			= jml_petugas;
		this.tgl_dikeluarkan 		= tgl_dikeluarkan;
		this.lokasi_dikeluarkan 	= lokasi_dikeluarkan;
		this.akun_anggaran 			= akun_anggaran;
		this.surat_masuk_dari 		= surat_masuk_dari;
		this.tgl_surat_spt_masuk 	= tgl_surat_spt_masuk;
		this.id_spt 				= id_spt;
		this.status_laporan_petugas = status_laporan_petugas;
		this.id_sppd 				= id_sppd;
		this.status_riil 			= status_riil;
		this.informasi 				= informasi;
		this.jml_nomor_surat_sppd 	= jml_nomor_surat_sppd;
		this.jml_stspost 			= jml_stspost;
		this.note 					= note;
		
		this.nip_pembuatlaporanperj = nip_pembuatlaporanperj;
		this.nomor_spt_laporanperj 	= nomor_spt_laporanperj;
		this.hasil_pertemuan 		= hasil_pertemuan;
		this.masalah 				= masalah;
		this.saran 					= saran;
		this.lain_lain 				= lain_lain;
		this.tgl_pembuatan_laporan 	= tgl_pembuatan_laporan;
			
		this.id_rincian 			= id_rincian;
		this.uraian_rincian 		= uraian_rincian;
		this.jml_rincian 			= jml_rincian;
		this.bukti_rincian 			= bukti_rincian;
		
		this.id_riil 				= id_riil;
		this.uraian_riil 			= uraian_riil;
		this.jml_riil 				= jml_riil;
		
		this.sts_postingan 				= sts_postingan;
		this.jml_terposting 			= jml_terposting;
		this.nomor_urut 				= nomor_urut;
	}

	// -----------------
	public String getnip() {
		return nip;
	}

	public void setnip(String nip) {
		this.nip = nip;
	}

	// -----------------
	public String getnama_pegawai() {
		return nama_pegawai;
	}

	public void setnama_pegawai(String nama_pegawai) {
		this.nama_pegawai = nama_pegawai;
	}

	// -----------------
	public String getjabatan() {
		return jabatan;
	}

	public void setjabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	// -----------------
	public String getgolongan() {
		return golongan;
	}

	public void setgolongan(String golongan) {
		this.golongan = golongan;
	}

	// -----------------
	public String getpass() {
		return pass;
	}

	public void setpass(String pass) {
		this.pass = pass;
	}

	// -----------------
	public String getbiaya_perj() {
		return biaya_perj;
	}

	public void setbiaya_perj(String biaya_perj) {
		this.biaya_perj = biaya_perj;
	}

	// -----------------
	public String getmaksud_perj() {
		return maksud_perj;
	}

	public void setmaksud_perj(String maksud_perj) {
		this.maksud_perj = maksud_perj;
	}

	// -----------------
	public String getalat_angkutan() {
		return alat_angkutan;
	}

	public void setalat_angkutan(String alat_angkutan) {
		this.alat_angkutan = alat_angkutan;
	}

	// -----------------
	public String gettempat_brngkt() {
		return tempat_brngkt;
	}

	public void settempat_brngkt(String tempat_brngkt) {
		this.tempat_brngkt = tempat_brngkt;
	}

	// -----------------
	public String gettempat_tujuan() {
		return tempat_tujuan;
	}

	public void settempat_tujuan(String tempat_tujuan) {
		this.tempat_tujuan = tempat_tujuan;
	}

	// -----------------
	public String getlama_perj() {
		return lama_perj;
	}

	public void setlama_perj(String lama_perj) {
		this.lama_perj = lama_perj;
	}

	// -----------------
	public String gettgl_brngkt() {
		return tgl_brngkt;
	}

	public void settgl_brngkt(String tgl_brngkt) {
		this.tgl_brngkt = tgl_brngkt;
	}

	// -----------------
	public String gettgl_kembali() {
		return tgl_kembali;
	}

	public void settgl_kembali(String tgl_kembali) {
		this.tgl_kembali = tgl_kembali;
	}

	// -----------------
	public String gettambh_pengikut1() {
		return tambh_pengikut1;
	}

	public void settambh_pengikut1(String tambh_pengikut1) {
		this.tambh_pengikut1 = tambh_pengikut1;
	}

	// -----------------
	public String gettambh_pengikut2() {
		return tambh_pengikut2;
	}

	public void settambh_pengikut2(String tambh_pengikut2) {
		this.tambh_pengikut2 = tambh_pengikut2;
	}

	// -----------------
	public String gettambh_pengikut3() {
		return tambh_pengikut3;
	}

	public void settambh_pengikut3(String tambh_pengikut3) {
		this.tambh_pengikut3 = tambh_pengikut3;
	}

	// -----------------
	public String gettambh_pengikut4() {
		return tambh_pengikut4;
	}

	public void settambh_pengikut4(String tambh_pengikut4) {
		this.tambh_pengikut4 = tambh_pengikut4;
	}

	// -----------------
	public String gettambh_pengikut5() {
		return tambh_pengikut5;
	}

	public void settambh_pengikut5(String tambh_pengikut5) {
		this.tambh_pengikut5 = tambh_pengikut5;
	}

	// -----------------
	public String gettgl_aktivitas() {
		return tgl_aktivitas;
	}

	public void settgl_aktivitas(String tgl_aktivitas) {
		this.tgl_aktivitas = tgl_aktivitas;
	}

	// -----------------
	public String getjam_aktivitas() {
		return jam_aktivitas;
	}

	public void setjam_aktivitas(String jam_aktivitas) {
		this.jam_aktivitas = jam_aktivitas;
	}

	// -----------------
	public String getnomor_SPT() {
		return nomor_SPT;
	}

	public void setnomor_SPT(String nomor_SPT) {
		this.nomor_SPT = nomor_SPT;
	}

	// -----------------
	public String getnomor_SPPD() {
		return nomor_SPPD;
	}

	public void setnomor_SPPD(String nomor_SPPD) {
		this.nomor_SPPD = nomor_SPPD;
	}

	// -----------------
	public String getjml_petugas() {
		return jml_petugas;
	}

	public void setjml_petugas(String jml_petugas) {
		this.jml_petugas = jml_petugas;
	}

	// -----------------
	public String getlokasi_dikeluarkan() {
		return lokasi_dikeluarkan;
	}

	public void setlokasi_dikeluarkan(String lokasi_dikeluarkan) {
		this.lokasi_dikeluarkan = lokasi_dikeluarkan;
	}

	// -----------------
	public String gettgl_dikeluarkan() {
		return tgl_dikeluarkan;
	}

	public void settgl_dikeluarkan(String tgl_dikeluarkan) {
		this.tgl_dikeluarkan = tgl_dikeluarkan;
	}

	// -----------------
	public String getakun_anggaran() {
		return akun_anggaran;
	}

	public void setakun_anggaran(String akun_anggaran) {
		this.akun_anggaran = akun_anggaran;
	}

	// -----------------
	public String getsurat_masuk_dari() {
		return surat_masuk_dari;
	}

	public void setsurat_masuk_dari(String surat_masuk_dari) {
		this.surat_masuk_dari = surat_masuk_dari;
	}

	// -----------------
	public String gettgl_surat_spt_masuk() {
		return tgl_surat_spt_masuk;
	}

	public void settgl_surat_spt_masuk(String tgl_surat_spt_masuk) {
		this.tgl_surat_spt_masuk = tgl_surat_spt_masuk;
	}

	// -----------------
	public String getid_spt() {
		return id_spt;
	}

	public void setid_spt(String id_spt) {
		this.id_spt = id_spt;
	}

	// -----------------
	public String getstatus_laporan_petugas() {
		return status_laporan_petugas;
	}

	public void setstatus_laporan_petugas(String status_laporan_petugas) {
		this.status_laporan_petugas = status_laporan_petugas;
	}

	// -----------------
	public String getid_sppd() {
		return id_sppd;
	}

	public void setid_sppd(String id_sppd) {
		this.id_sppd = id_sppd;
	}

	// -----------------

	public String getstatus_riil() {
		return status_riil;
	}

	public void setstatus_riil(String status_riil) {
		this.status_riil = status_riil;
	}

	// -----------------
	public String getstatus_rincian() {
		return status_rincian;
	}

	public void setstatus_rincian(String status_rincian) {
		this.status_rincian = status_rincian;
	}
	// -----------------
	public String getinformasi() {
		return informasi;
	}

	public void setinformasi(String informasi) {
		this.informasi = informasi;
	}
	// -----------------
		
	public String getjml_nomor_surat_sppd() {
		return jml_nomor_surat_sppd;
	}

	public void setjml_nomor_surat_sppd(String jml_nomor_surat_sppd) {
		this.jml_nomor_surat_sppd = jml_nomor_surat_sppd;
	}
	// -----------------
	public String getjml_stspost() {
		return jml_stspost;
	}

	public void setjml_stspost(String jml_stspost) {
		this.jml_stspost = jml_stspost;
	}
	//------------------
	public String getjml_terposting() {
		return jml_terposting;
	}

	public void setjml_terposting(String jml_terposting) {
		this.jml_terposting = jml_terposting;
	}
	// -----------------
	public String getnote() {
		return note;
	}

	public void setnote(String note) {
		this.note = note;
	}
	// -----------------
	public String getnip_pembuatlaporanperj() {
		return nip_pembuatlaporanperj;
	}

	public void setnip_pembuatlaporanperj(String nip_pembuatlaporanperj) {
		this.nip_pembuatlaporanperj = nip_pembuatlaporanperj;
	}
	// -----------------
	public String getnomor_spt_laporanperj() {
		return nomor_spt_laporanperj;
	}

	public void setnomor_spt_laporanperj(String nomor_spt_laporanperj) {
		this.nomor_spt_laporanperj = nomor_spt_laporanperj;
	}
	// -----------------
	public String gethasil_pertemuan() {
		return hasil_pertemuan;
	}

	public void sethasil_pertemuan(String hasil_pertemuan) {
		this.hasil_pertemuan = hasil_pertemuan;
	}
	// -----------------
	public String getmasalah() {
		return masalah;
	}

	public void setmasalah(String masalah) {
		this.masalah = masalah;
	}
	// -----------------
	public String getsaran() {
		return saran;
	}

	public void setsaran(String saran) {
		this.saran = saran;
	}
	// -----------------
	public String getlain_lain() {
		return lain_lain;
	}

	public void setlain_lain(String lain_lain) {
		this.lain_lain = lain_lain;
	}
	// -----------------
	public String gettgl_pembuatan_laporan() {
		return tgl_pembuatan_laporan;
	}

	public void settgl_pembuatan_laporan(String tgl_pembuatan_laporan) {
		this.tgl_pembuatan_laporan = tgl_pembuatan_laporan;
	}
	// -----------------
	public String getid_rincian() {
		return id_rincian;
	}

	public void setid_rincian(String id_rincian) {
		this.id_rincian = id_rincian;
	}
	// -----------------
	public String geturaian_rincian() {
		return uraian_rincian;
	}

	public void seturaian_rincian(String uraian_rincian) {
		this.uraian_rincian = uraian_rincian;
	}
	// -----------------
	public String getjml_rincian() {
		return jml_rincian;
	}

	public void setjml_rincian(String jml_rincian) {
		this.jml_rincian = jml_rincian;
	}
	// -----------------
	public String getbukti_rincian() {
		return bukti_rincian;
	}

	public void setbukti_rincian(String bukti_rincian) {
		this.bukti_rincian = bukti_rincian;
	}
	// -----------------
	public String getid_riil() {
		return id_riil;
	}

	public void setid_riil(String id_riil) {
		this.id_riil = id_riil;
	}
	// -----------------
	public String geturaian_riil() {
		return uraian_riil;
	}

	public void seturaian_riil(String uraian_riil) {
		this.uraian_riil = uraian_riil;
	}
	// -----------------
	public String getjml_riil() {
		return jml_riil;
	}

	public void setjml_riil(String jml_riil) {
		this.jml_riil = jml_riil;
	}
	// -----------------
	public String getsts_postingan() {
		return sts_postingan;
	}

	public void setsts_postingan(String sts_postingan) {
		this.sts_postingan = sts_postingan;
	}
	// -----------------
	public String getnomor_urut() {
		return nomor_urut;
	}

	public void setnomor_urut(String nomor_urut) {
		this.nomor_urut = nomor_urut;
	}
	//------------------
}
package koneksi;


/** @noinspection ALL*/
public class Koneksi {
	private final static String TAG = "KoneksiESPPD";
	public static final String URL_WEBSITE = "https://apprssm.rssoedono.jatimprov.go.id/esppd/";
    //private static final String URL_SERVER = "https://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/";
	private static final String URL_SERVER = "https://apprssm.rssoedono.jatimprov.go.id/esppd/sppd_rssm_apk/";
	private static final String URL_SERVERNEW = "https://apprssm.rssoedono.jatimprov.go.id/esppd/sppd_rssm_apk/app/";
//	private static final String URL_SERVERNEW = "https://180.250.155.77/esppd_webbase/mobile/";
//	private static final String URL_SERVERNEW = "https://apprssm.rssoedono.jatimprov.go.id/esppd_webbase/mobile/";
//	public static final String tampil_data_nip 						= "tampil_data_nip.php";
//	public static final String tampil_data_nip_spt 					= "tampil_data_nip_spt.php";
//	public static final String tampil_data_nip_berdasarkan_spt 		= "tampil_data_nip_berdasarkan_spt.php";
//	public static final String profil_pegawai 						= "profil_pegawai.php";
//	public static final String search_nama_pegawai 					= "search_nama_pegawai.php";
//	public static final String simpan_data_sppdbaru 				= "simpan_data_sppdbaru.php";
//	public static final String tampil_daftar_sppd_per_nip 			= "tampil_daftar_sppd_per_nip.php/";
//	public static final String tampil_daftar_sppd_per_nip_HISTORY 	= "tampil_daftar_sppd_per_nip_HISTORY.php";
//	public static final String tampil_daftar_sppd_per_nip_POSTING 	= "tampil_daftar_sppd_per_nip_POSTING.php";
	//public static final String download_spt 						= "print_spt2.php";
//	public static final String tampil_daftar_edit_rincian 			= "tampil_daftar_edit_rincian.php";
//	public static final String tampil_daftar_edit_riil 				= "tampil_daftar_edit_riil.php";
//	public static final String IMAGE_DIRECTORY_NAME 				= "../Bukti E-SPPD";

//	public static final String hapus_data_per_riil					= URL_SERVER + "hapus_file/hapus_data_per_riil.php";
//	public static final String update_riil							= URL_SERVER + "update_riil.php";
//	public static final String tambah_uraian_riil					= URL_SERVER + "tambah_uraian_riil.php";
//	public static final String update_data_laporan_petugas 			= URL_SERVER + "update_lap_perj.php";

	//public static final String LINK_UNTUK_LOGIN 					= URL_SERVER + "login_nya.php";
	//public static final String CEK_VERSI 							= URL_SERVER + "cek_versi.php";

//	public static final String simpan_pass 							= URL_SERVER + "registrasi_pass_baru.php";
//	public static final String simpan_update_data_rincian 			= URL_SERVER + "simpan_data_rincian_biaya.php";
//	public static final String hapus_data_rincian					= URL_SERVER + "hapus_file/hapus_rincian.php";
//	public static final String hapus_data_per_uraian			    = URL_SERVER + "hapus_file/hapus_data_per_uraian.php";
//  public static final String hapus_data_per_uraian_laporan	    = URL_SERVER + "hapus_file/hapus_data_per_uraian_laporan.php";
//  public static final String update_rincian_biaya 		        = URL_SERVER + "update_rincian_biaya.php";
//    public static final String posting_url                          = URL_SERVER + "posting_url.php";

//	public static final String download_spt 		                = URL_SERVER + "prints/print_spt.php";
//	public static final String download_sppd 	                    = URL_SERVER + "prints/print_sppd.php";
//	public static final String download_lap_perj					= URL_SERVER + "prints/print_laporan_perj_dinas.php";
//	public static final String download_rincian 					= URL_SERVER + "prints/print_rincian.php";
//	public static final String download_riil 						= URL_SERVER + "prints/print_riil.php";

	//ALAMAT BARU
	public static final String LINK_UNTUK_LOGIN_TES					= URL_SERVERNEW + "loginapk.php/";
	public static final String LINK_PENCARIAN						= URL_SERVERNEW + "cek_data_register.php/";
	public static final String simpan_pass_baru						= URL_SERVERNEW + "sv_pass_baru.php/";
	public static final String CEK_VERSI 							= URL_SERVERNEW + "cek_versi.php/";
	public static final String FCM_TOKEN 							= URL_SERVERNEW + "token/get_token.php/";
	public static final String update_pass 							= URL_SERVERNEW + "profil/update_pass.php/";
	public static final String update_email							= URL_SERVERNEW + "profil/update_email.php/";
	public static final String update_unit							= URL_SERVERNEW + "profil/update_unit.php/";
	public static final String loading_profil						= URL_SERVERNEW + "profil/loading_profil.php/";
	public static final String simpan_kritik 						= URL_SERVERNEW + "tentang/simpan_kritik.php/";

	public static final String list_history 						= URL_SERVERNEW + "sptsppd/list_history.php";
	public static final String list_sptsppd 						= URL_SERVERNEW + "sptsppd/list_sptsppd.php";
	public static final String count_sptsppd 						= URL_SERVERNEW + "sptsppd/count_sptsppd.php";
	public static final String download_apk 						= URL_WEBSITE + "Download_Apk/";

	/*LAPORAN PERJALANAN*/
	public static final String load_data_laporan_petugas 			= URL_SERVERNEW + "laporan/loadData.php/";
	public static final String simpanupdate_laporan_petugas 		= URL_SERVERNEW + "laporan/simpan_lap_perj.php/";
	public static final String hapus_laporan_petugas 				= URL_SERVERNEW + "laporan/hapus_lap_perj.php/";

	/*RINCIAN BIAYA*/
	public static final String list_datarincian 					= URL_SERVERNEW + "rincian/list_datarincian.php";
	public static final String update_rincian_biaya 		        = URL_SERVERNEW + "rincian/update_rincian_biaya.php/";
	public static final String hapus_rincian_biaya 		        	= URL_SERVERNEW + "rincian/hapus_rincian_biaya.php/";
	public static final String simpan_rincian_biaya 		        = URL_SERVERNEW + "rincian/simpan_rincian_biaya.php/";

	/*RINCIAN BIAYA RIIL*/
	public static final String list_datariil 						= URL_SERVERNEW + "riil/list_datariil.php";
	public static final String update_riil_biaya 		        	= URL_SERVERNEW + "riil/update_riil_biaya.php/";
	public static final String hapus_riil_biaya 		        	= URL_SERVERNEW + "riil/hapus_riil_biaya.php/";
	public static final String simpan_riil_biaya 		        	= URL_SERVERNEW + "riil/simpan_riil_biaya.php/";
//	public static final String FILE_UPLOAD_URL 						= URL_SERVER + "temp/Upload_Terbaru.php/";
	public static final String history_versi 		        		= URL_SERVERNEW + "history_versi.php/";
	public static final String FolderUpload 						= URL_SERVERNEW + "upload/";
	public static final String tutorial 							= URL_WEBSITE + "Download_Apk/tutorial.pdf";

	/*DOWNLOAD LAPORAN*/
	public static final String downloadLapPerj						= URL_WEBSITE + "do_prints/Cetak_Laporan_Perj_Dinas.php";
	public static final String downloadSPT							= URL_WEBSITE + "do_prints/Cetak_SPT.php";
	public static final String downloadSPPD							= URL_WEBSITE + "do_prints/Cetak_SPPD.php";
	public static final String downloadRincian						= URL_WEBSITE + "do_prints/Cetak_Rincian_Biaya.php";
	public static final String downloadRiil							= URL_WEBSITE + "do_prints/Cetak_Riil.php";
	public static final String downloadKwitansi						= URL_WEBSITE + "do_prints/Cetak_Kwitansi.php";
	public static final String downloadDokumen						= URL_WEBSITE + "do_prints/Cetak_Dokument.php";
	public static final String downloadMenginap						= URL_WEBSITE + "do_prints/Cetak_Pernyataan.php";
	public static final String downloadTidakMenginap				= URL_WEBSITE + "do_prints/Cetak_PernyataanTidak.php";

	public static final String postingSPTSPPD                       = URL_SERVERNEW + "postingSPTSPPD.php/";
	public static final String posting_AllSPTSPPD                   = URL_SERVERNEW + "postingSemua.php";
	public Koneksi() {
		super();
	}
}

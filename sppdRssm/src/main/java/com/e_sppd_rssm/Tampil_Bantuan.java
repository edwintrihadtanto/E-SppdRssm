package com.e_sppd_rssm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;

import bantuan_tutorial.Tutorial_Bantuan_Data_Tidak_Tampil;


public class Tampil_Bantuan extends AppCompatActivity {
	
	private Button Button1, Button2, Button3, Button4, Button5, Button6, Button7, Button8;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
	
		Button1 = (Button) findViewById(R.id.Button1); //Data Tidak Bisa Tampil
		Button2 = (Button) findViewById(R.id.Button2); //Tidak Bisa Posting
		Button3 = (Button) findViewById(R.id.Button3); //Cara-Cara Posting
		Button4 = (Button) findViewById(R.id.Button4); //Menampilkan Menu Pilihan
		Button5 = (Button) findViewById(R.id.Button5); //Cara Regristrasi
		Button6 = (Button) findViewById(R.id.Button6); //Cara Ganti Password
		Button7 = (Button) findViewById(R.id.Button7); //Lupa Password
		Button8 = (Button) findViewById(R.id.Button8); //Edit Laporan
		
		Button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "Data Tidak Bisa Tampil";
				Intent i = null;
				i = new Intent(Tampil_Bantuan.this,
						Tutorial_Bantuan_Data_Tidak_Tampil.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("pesan", text);
				i.putExtras(Bundle);
				startActivity(i);			
			}
			
		});	
		Button2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String text = "Tidak Bisa Posting";
						Intent i = null;
						i = new Intent(Tampil_Bantuan.this,
								Tutorial_Bantuan_Data_Tidak_Tampil.class);
						Bundle Bundle = new Bundle();
						Bundle.putString("pesan", text);
						i.putExtras(Bundle);
						startActivity(i);				
					}
				});	
		Button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "Cara-Cara Posting";
				Intent i = null;
				i = new Intent(Tampil_Bantuan.this,
						Tutorial_Bantuan_Data_Tidak_Tampil.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("pesan", text);
				i.putExtras(Bundle);
				startActivity(i);			
			}
		});	
		Button4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "Menampilkan Menu Pilihan";
				Intent i = null;
				i = new Intent(Tampil_Bantuan.this,
						Tutorial_Bantuan_Data_Tidak_Tampil.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("pesan", text);
				i.putExtras(Bundle);
				startActivity(i);			
			}
		});	
		Button5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "Cara Regristrasi";
				Intent i = null;
				i = new Intent(Tampil_Bantuan.this,
						Tutorial_Bantuan_Data_Tidak_Tampil.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("pesan", text);
				i.putExtras(Bundle);
				startActivity(i);			
			}
		});	
		Button6.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "Cara Ganti Password";
				Intent i = null;
				i = new Intent(Tampil_Bantuan.this,
						Tutorial_Bantuan_Data_Tidak_Tampil.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("pesan", text);
				i.putExtras(Bundle);
				startActivity(i);			
			}
		});	
		Button7.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "Lupa Password";
				Intent i = null;
				i = new Intent(Tampil_Bantuan.this,
						Tutorial_Bantuan_Data_Tidak_Tampil.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("pesan", text);
				i.putExtras(Bundle);
				startActivity(i);			
			}
		});	
	}
	
}

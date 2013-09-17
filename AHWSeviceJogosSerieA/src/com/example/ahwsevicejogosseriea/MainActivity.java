package com.example.ahwsevicejogosseriea;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void iniciar(View v){
		Intent i = new Intent(this, LeJogos.class);
		startService(i);
	}
	
	
}

package com.example.mybogopicgen;
//This file is adapted from: https://github.com/abramhindle/BogoPicGen
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setBogoPic();
		ImageButton ib = (ImageButton)findViewById(R.id.imageButton1);
		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setBogoPic();
			}
		});
		Button acceptButton = (Button)findViewById(R.id.button1);
		acceptButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processIntent(true);
			}
		});
		Button cancelButton = (Button)findViewById(R.id.button2);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processIntent(false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private Bitmap newBMP;
	private void setBogoPic(){
		Toast.makeText(this, "Generating Photo", Toast.LENGTH_LONG).show();
		newBMP = BogoPicGen.generateBitmap(400,  400);
		ImageButton ib = (ImageButton) findViewById(R.id.imageButton1);
		ib.setImageBitmap(newBMP);
	}
	private void processIntent(boolean okPressed) {
		Intent intent = getIntent();
		if (intent != null) {
			try {
				if (intent.getExtras() != null) {    
					if (okPressed) {
						Uri uri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
						OutputStream out = new FileOutputStream(new File(uri.getPath()));
						newBMP.compress(Bitmap.CompressFormat.JPEG, 75, out);
						out.close();
						setResult(RESULT_OK);
					}//if (okPressed).
					else{//cancel is pressed:
						Toast.makeText(this, "Photo Cancelled!", Toast.LENGTH_LONG).show();
						setResult(RESULT_CANCELED);
						finish();
					}
				} else {
					Toast.makeText(this, "Photo Cancelled: No Reciever?", Toast.LENGTH_LONG).show();
					setResult(RESULT_CANCELED);
				}
			} catch (FileNotFoundException e) {
				Toast.makeText(this, "Couldn't Find File to Write to?", Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);    	
			} catch (IOException e) {
				Toast.makeText(this, "Couldn't Write File!", Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
			}
		}//if (intent != null).
		finish();
	}	
}

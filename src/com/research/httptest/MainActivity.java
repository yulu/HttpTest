package com.research.httptest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.research.httptest.util.AsyncClientRunner;
import com.research.httptest.util.AsyncClientRunner.RequestListener;
import com.research.httptest.util.Client;

public class MainActivity extends Activity implements RequestListener{
	private TextView mParamsView;
	private TextView mImageView;
	private TextView mResponseView;
	private Button mButtonParams;
	private Button mButtonImage;
	
	private Client client;
	private AsyncClientRunner asyncrunner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mParamsView = (TextView)findViewById(R.id.param_detail);
		mImageView = (TextView)findViewById(R.id.image_detail);
		mResponseView = (TextView)findViewById(R.id.response_detail);
		mButtonParams = (Button)findViewById(R.id.param_button);
		mButtonImage = (Button)findViewById(R.id.image_button);
		
		client = Client.getInstance();
		asyncrunner = new AsyncClientRunner(client);
		
		mButtonParams.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	UploadParams();
            }
        });
		
		mButtonImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	UploadImage();
            }
        });

	}

	private void UploadParams(){
		String[] identifier = {"identification", "password"};
		String[] value = {"admin", "admin"};
		
		String output = "["+identifier[0] + ":" + value[0] + "\n" +
						identifier[1] + ":" + value[1] + "]";
		mParamsView.setText(output);
		
		Bundle params = new Bundle();
		params.putString(identifier[0], value[0]);
		params.putString(identifier[1], value[1]);
		
		asyncrunner.request(this, 
				AppConfig.URL, 
				params, 
				Client.HTTPMETHOD_POST, 
				null, 
				this, 
				"upload params");
	}
	
	private void UploadImage(){
		String filepath = Environment.getExternalStorageDirectory()+ "/starbucks.jpg";
		String[] identifier = {"file", "longitude", "latitude", "location_name", "object_name"};
		String[] value = {filepath, 
				"100.0", "100.0", "location1", "object1"};
		
		String output = "["+identifier[0] + ":" + value[0] + "\n" +
				identifier[1] + ":" + value[1] + "\n" +
				identifier[2] + ":" + value[2] + "\n" +
				identifier[3] + ":" + value[3] + "\n" +
				identifier[4] + ":" + value[4] + "]";
		
		mImageView.setText(output);
		
		Bundle params = new Bundle();
		params.putString(identifier[0], value[0]);
		params.putFloat(identifier[1], Float.valueOf(value[1]));
		params.putFloat(identifier[2], Float.valueOf(value[2]));
		params.putString(identifier[3], value[3]);
		params.putString(identifier[4], value[4]);
		
		asyncrunner.request(this, 
				AppConfig.URL, 
				params, 
				Client.HTTPMETHOD_POST, 
				"file", 
				this, 
				"upload image");
	}

	@Override
	public void onComplete(String response, Object state) {
		final String display = response;
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				mResponseView.setText(display);		
			}
		});
	}

	@Override
	public void onComplete(byte[] response, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		
	}
}

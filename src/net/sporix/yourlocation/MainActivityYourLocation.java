package net.sporix.yourlocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityYourLocation extends Activity {
	
	private TextView txtMobNumber;
	private TextView txtLat;
	private TextView txtLong;
	private Button btnInsert;
	
	private ProgressDialog dialog = null;
	
	private String llat;
	private String llong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        
        txtMobNumber = (TextView)this.findViewById(R.id.txtMobNumber);
        txtLat = (TextView)this.findViewById(R.id.txtLat);
        txtLong = (TextView)this.findViewById(R.id.txtLong);
        btnInsert = (Button)this.findViewById(R.id.btnInsert);
        
        
        btnInsert.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		        String number = tm.getLine1Number();
		        
		        txtMobNumber.setText("01711082652");
		        
		        
		        /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		        double longitude = location.getLongitude();
		        double latitude = location.getLatitude();*/
		        
		        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		        LocationListener mlocListener = new MyLocationListener();

		        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		      
		        
		        //txtLat.setText(String.valueOf(latitude));
		        //txtLong.setText(String.valueOf(longitude));
		        
		        Log.v("mobile", "number: "+number);
		        
		        
		        number = String.valueOf("01711082652");
		        
		        //http://sporix.net/mobileapp/location/insert.php?mobile=1711090909&lat=3.4&lon=33.3
		        new RequestTask().execute("http://sporix.net/mobileapp/location/insert.php?mobile="+number+"&lat="+llat+"&lon="+llong);
				
		       
			}
		});
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.layout_main, menu);
        return true;
    }
    
    
    public class MyLocationListener implements LocationListener
    {

    	public MyLocationListener()
    	{
    		
    	}

    	public void onLocationChanged(Location loc) {
    		// TODO Auto-generated method stub
    		loc.getLatitude();

    		loc.getLongitude();

    		String Text = "My current location is: " +

    		"Latitud =  " + loc.getLatitude() +

    		"Longitud = " + loc.getLongitude();

    		//Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
    		
    		
    		txtLat.setText(String.valueOf(loc.getLatitude()));
	        txtLong.setText(String.valueOf(loc.getLongitude()));
	        
	        llat = String.valueOf(loc.getLatitude());
	        llong = String.valueOf(loc.getLongitude());
    		
    	}

    	public void onProviderDisabled(String arg0) {
    		// TODO Auto-generated method stub
    		Toast.makeText( getApplicationContext(),

    				"Gps Disabled",

    				Toast.LENGTH_SHORT ).show();

    	}

    	public void onProviderEnabled(String arg0) {
    		// TODO Auto-generated method stub
    		Toast.makeText( getApplicationContext(),

    				"Gps Enabled",

    				Toast.LENGTH_SHORT).show();
    	}

    	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    		// TODO Auto-generated method stub

    	}

    }/* End of UseGps Activity */

    
    
    private class RequestTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            
            
           
            
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                    
                    Log.v("output", "output: "+responseString);
                    
                   
                } 
                
               else{
                    //Closes the connection.
            	  
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                    
                   
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }
        
        @Override    
        protected void onPreExecute() 
        {       
            super.onPreExecute();
            setDialog();
        }
        
        
        
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            
            if(dialog != null)
            {
            	dialog.dismiss();
            	dialog = null;
            }
            
            //Do anything with response..
        }
    }
    
    private void setDialog()
	{
		if(dialog == null)
		{
			dialog = new ProgressDialog(this);
	        dialog.setMessage("Loading, please wait");
	        dialog.show();
	    }
	}
    
}

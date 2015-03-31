package com.example.locationupadate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.WebView;

public class SecondActivity extends Activity implements OnClickListener {

	WebView myBrowser;
	private ImageButton speak_btn;
	String value;
	private EditText edt_txt;
	private RadioGroup radioGroup;
	boolean walk = false, rooo = false;
	DBActivity dbActivity;
	
	int roo_stop_numnber;
	String stringUrl;
	String IntentMsg2;

	protected static final int RESULT_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wbview);
		dbActivity = new DBActivity(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			IntentMsg2 = extras.getString("key");
			IntentMsg2 = IntentMsg2.replace(" ", "");
			value =IntentMsg2;
		}
		Log.e("key", value);

		// myBrowser.addJavascriptInterface(this, "ADFunction");
		myBrowser = (WebView) findViewById(R.id.webView1);
		

		// showDest = (TextView) findViewById(R.id.textView2);
		speak_btn = (ImageButton) findViewById(R.id.imageButton1);
		edt_txt = (EditText) findViewById(R.id.editText1);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		
		


		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				if (checkedId == R.id.wlk_mode) {
					value =IntentMsg2;
					myBrowser.loadUrl("file:///android_asset/simplemap.html");
					myBrowser.getSettings().setJavaScriptEnabled(true);
					
					String dest = edt_txt.getText().toString()
							.replaceAll(" ", "+");
					 stringUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="
							+ dest + ",+AKRON,+OH&sensor=true_or_false";
					Log.e("stringUrl", stringUrl);
			
					new WalkLoadpage().execute(stringUrl);
				}

				if (checkedId == R.id.roobus_mde) {
					//
					value =IntentMsg2;
					myBrowser.loadUrl("file:///android_asset/complexmap.html");
					myBrowser.getSettings().setJavaScriptEnabled(true);
					
					
					Log.e("mode","enter the radio button");
					String dest = edt_txt.getText().toString()
							.replaceAll(" ", "+");
					 stringUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="
							+ dest + ",+AKRON,+OH&sensor=true_or_false";
					Log.e("stringUrl", stringUrl);

	                new RooBusPickup().execute(stringUrl);
          
				}
			}
		});

		
		speak_btn.setOnClickListener(this);

	}

	@Override
	@SuppressLint("JavascriptInterface")
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.imageButton1:

			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

			try {
				radioGroup.clearCheck();
				startActivityForResult(intent, RESULT_SPEECH);
				edt_txt.setText("");

				// myJavaScriptInterface
			} catch (ActivityNotFoundException a) {
				a.printStackTrace();
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				edt_txt.setText(text.get(0));

			}
			break;
		}

		}
	}

	
	public class WalkLoadpage extends AsyncTask<String, String, String>

	{

		String long_lat;

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			
			

			StringBuilder response = new StringBuilder();

			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection httpconn = (HttpURLConnection) url
						.openConnection();
				Log.e("httpconn", httpconn.toString());
				if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {

					BufferedReader input = new BufferedReader(
							new InputStreamReader(httpconn.getInputStream()),
							8192);
					String strLine = null;

					while ((strLine = input.readLine()) != null) {
						response.append(strLine);
					}
					input.close();
				}

				String jsonOutput = response.toString();

				JSONObject jsonObject = new JSONObject(jsonOutput);
				JSONArray results_array = jsonObject.getJSONArray("results");
				JSONObject Jobject = results_array.getJSONObject(0);
				JSONObject geo = Jobject.getJSONObject("geometry");
				JSONObject location = geo.getJSONObject("location");

				String latandlong = location.toString().replaceAll("[{}:\"]",
						"");
				latandlong = latandlong.replaceAll("[a-z]", "");

				String[] dest_long_lat = latandlong.split(",");
				// System.out.println("The lat values are :"+long_lat[1]);
				// System.out.println("The long values are :"+long_lat[0]);

				long_lat = value.toString() + ":" + dest_long_lat[1] + ","
						+ dest_long_lat[0];
				Log.e("long_lat", long_lat);

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("e.printStackTrace()", e.toString());

			}

			return null;

		}

		@Override
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog after the Music file was downloaded

			myBrowser.loadUrl("javascript:Call2walk(\"" + long_lat + "\")");

		}

	}
	
	
	public class RooBusPickup extends AsyncTask<String, String, String>

	{

		Map<Integer, String> table_data;
		Map<Integer, Integer> route_num;
		String msg;
		
		
		private void select_data() {

			SQLiteDatabase db = dbActivity.getWritableDatabase();

			table_data = new HashMap<Integer, String>();
			route_num = new HashMap<Integer, Integer>();
			String Linking_route;

			try {
			String selectQuery = "SELECT  LAT,LONG,ROUTE_NO FROM  WEST_ROUTE";
			Log.e("selQuery", selectQuery);
			Cursor cursor = db.rawQuery(selectQuery, null);

			int count = 1;
			if (cursor.moveToFirst()) {
			do {
			table_data.put(
					count,
					cursor.getString(cursor.getColumnIndex("LAT"))
							+ ","
							+ cursor.getString(cursor
									.getColumnIndex("LONG")));
			route_num.put(count, cursor.getInt(cursor.getColumnIndex("ROUTE_NO")));

			count++;
			} while (cursor.moveToNext());
			}
			// Message.message(this, " " + table_data);
			} catch (SQLException e) {
			// Message.message(this, " " + e);
			e.printStackTrace();

			}

			}


		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			
			Log.e("eneter the Async","started working");
			
			StringBuilder response = new StringBuilder();

			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection httpconn = (HttpURLConnection) url
						.openConnection();
				//Log.e("httpconn", httpconn.toString());
				if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {

					BufferedReader input = new BufferedReader(
							new InputStreamReader(httpconn.getInputStream()),
							8192);
					String strLine = null;

					while ((strLine = input.readLine()) != null) {
						response.append(strLine);
					}
					input.close();
				}

				String jsonOutput = response.toString();

				JSONObject jsonObject = new JSONObject(jsonOutput);
				JSONArray results_array = jsonObject.getJSONArray("results");
				JSONObject Jobject = results_array.getJSONObject(0);
				JSONObject geo = Jobject.getJSONObject("geometry");
				JSONObject location = geo.getJSONObject("location");

				String latandlong = location.toString().replaceAll("[{}:\"]",
						"");
				latandlong = latandlong.replaceAll("[a-z]", "");

				String[] dest_long_lat = latandlong.split(",");
				String[] stndg_long_lat =value.split(",");
				
				String orig_dest_long_lat[] = latandlong.split(",");
				
				orig_dest_long_lat[0]=dest_long_lat[1];
				orig_dest_long_lat[1]=dest_long_lat[0];
				Log.e("error","error in herer");
				
				Log.e("stndg_long_lat",stndg_long_lat[0]+", "+stndg_long_lat[1]);
				Log.e("dest_long_lat" ,orig_dest_long_lat[0]+" ,"+orig_dest_long_lat[1]);
				
				// System.out.println("The lat values are :"+long_lat[1]);
				// System.out.println("The long values are :"+long_lat[0]);

				/*long_lat = value.toString() + ":" + dest_long_lat[1] + ","
						+ dest_long_lat[0];
				Log.e("long_lat", long_lat);*/
				
				Log.e("Async","started fucntion calls");
				
				select_data();
				
				Log.e("Async1","after selected data ");
			
				ComparePath p1 =min_location(stndg_long_lat,"stdg");
				Log.e("Async2","p1");
				ComparePath p2 =min_location(orig_dest_long_lat,"dest");
				Log.e("Async3","p2");
				
				//removing the routes
				
				
				
				msg ="Route 1 :"+p1.getRoute_no()+" and Route 2 :"+p2.getRoute_no();
				Log.e("final routes ",msg);
				
				//Linking_route =value;
			 
			  if(p1.getRoute_no() > p2.getRoute_no())
			  {
				for (int i=p1.getRoute_no() ;i<=table_data.size();i++)
				{
					value =value+":"+table_data.get(i);
					
				}
				
				for (int j=1;j<=p2.getRoute_no();j++)
						{
					      value =value+":"+table_data.get(j);
						}
			  }
			  
			  if(p1.getRoute_no() < p2.getRoute_no())
			  {
				
				  for (int i=p1.getRoute_no() ;i<=p2.getRoute_no();i++)
					{
						value =value+":"+table_data.get(i);
					}
  
			  }
			  
			  value =value+":"+dest_long_lat[1]+","+dest_long_lat[0];
				
				
				Log.e("the routes",value);
				
				

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("e.printStackTrace()", e.toString());

			}

			return null;

		}

		private ComparePath min_location(String[]  long_lat, String msg) {
			
        double lat1 =	Double.parseDouble(long_lat[0]);
        double lng1 =	Double.parseDouble(long_lat[1]);
        double dist_to_bus =Double.MAX_VALUE;
        int flag_value =0;
        
        Log.e("p1","In the logic before loop");
        for (int i = 1; i <= table_data.size(); i++) {
        	//Log.e("the Map data",table_data.toString());
            String table_map_Data[] =table_data.get(i).split(",");
            double lat2 =Double.parseDouble(table_map_Data[0]); 
            double lng2 =Double.parseDouble(table_map_Data[1]); 
            Log.e("lat and long",lat2+" ,"+lng2);
        	double earthRadius = 6371; //kilometers
            double dLat = Math.toRadians(lat2-lat1);
            double dLng = Math.toRadians(lng2-lng1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                       Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                       Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            float dist = (float) (earthRadius * c);
            
            Log.e("dist and route num ",dist +": ,"+route_num.get(i));
            
			if (dist < dist_to_bus) {
				dist_to_bus = dist;
				flag_value =route_num.get(i);
			}
		}
        
        Log.e("p11","after  loop");
        
        Log.e("flag_value",flag_value+" ");
		
        ComparePath path = new ComparePath(msg);
        String table_map_Data[] =table_data.get(flag_value).split(",");
        path.setLat(Double.parseDouble(table_map_Data[0]));
        path.setLng(Double.parseDouble(table_map_Data[1]));
        path.setRoute_no(route_num.get(flag_value));
       
			
        return path;
        
		}


		@Override
		protected void onPostExecute(String file_url) {
			// Dismiss the dialog after the Music file was downloaded

			
			myBrowser.loadUrl("javascript:Call2Roo(\"" + value + "\")");
			
		

		}

	}


	
	

}

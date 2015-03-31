package com.example.locationupadate;

import android.database.sqlite.SQLiteOpenHelper;

public class DBActivity extends SQLiteOpenHelper {

	private static final String Database_Name = "AkronRooBusSchedule.db";
	private static final String create_tab_bus_table = "CREATE TABLE BUS_TABLE ( BUS_NAME TEXT PRIMARY KEY NOT NULL , BUS_ST_TIME TEXT NOT NULL , BUS_END_TIME TEXT NOT NULL , WEEK_DAY INTEGER NOT NULL,TIME_FRAME INTEGER NOT NULL);";
	private static final String create_west_route = "CREATE TABLE WEST_ROUTE ( LOCATION_NAME TEXT PRIMARY KEY NOT NULL , LAT REAL NOT NULL , LONG REAL NOT NULL , ROUTE_NO INTEGER NOT NULL);";
	private static final int DATABASE_VERSION = 2;
	private Context context;

	DBActivity(Context context) {
		super(context, Database_Name, null, DATABASE_VERSION);
		this.context = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		try {
			db.execSQL(create_tab_bus_table);
			db.execSQL(create_west_route);
			// Log.d("tables", "tables created");
			insert_data(db,"BUS_TABLE","roo_bus.txt");
			insert_data(db,"WEST_ROUTE","WEST_ROUTE.txt");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insert_data(SQLiteDatabase db, String table_name, String txt_file ) {
		
		try {
			// SQLiteDatabase sqllitedb =dbActivity.getWritableDatabase();
			BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(txt_file)));
			// storing the value of length of the first line of maze
			String curr_line =br.readLine();
			String column_data[] = curr_line.split(",");	
			while ((curr_line = br.readLine()) != null) {
				String row_data[] = curr_line.split(",");
				ContentValues values = new ContentValues();
				for (int i=0;i<column_data.length;i++)
				{
					values.put(column_data[i], row_data[i]);
				}
				long id = db.insert(table_name, null, values);
				if (id < 0) {
					Toast.makeText(context, "messed upsomewhere", Toast.LENGTH_LONG).show();
					Log.e("id","messed up somewhere");
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			Toast.makeText(context, " terrible exception", Toast.LENGTH_LONG).show();
		} 
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		try {
			db.execSQL("DROP TABLE IF EXISTS BUS_TABLE ");
			db.execSQL("DROP TABLE IF EXISTS WEST_ROUTE");
			// create new tables
			onCreate(db);

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

}

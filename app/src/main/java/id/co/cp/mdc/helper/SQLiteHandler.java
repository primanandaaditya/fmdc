/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package id.co.cp.mdc.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.cp.mdc.app.AppConfig;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 17;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT, pass TEXT, ztsid TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
		String CREATE_FARMER_TABLE = "CREATE TABLE " + "farm" + "("
				+ "famid" + " TEXT PRIMARY KEY," + "famnm" + " TEXT,"
				+ "famad" + " TEXT, longi TEXT, latit TEXT" + ")";
		db.execSQL(CREATE_FARMER_TABLE);
		String CREATE_LHK_TABLE ="CREATE TABLE lhkh (aid INTEGER, ztsid TEXT, zfmid TEXT, lhkcd TEXT," +
				" recdt TEXT, quant TEXT, longi TEXT, latit TEXT, PRIMARY KEY (aid) )";
		db.execSQL(CREATE_LHK_TABLE);
		String CREAKE_MASTER_AREA = "CREATE TABLE MsArea (area_id TEXT, area TEXT, PRIMARY KEY (area_id))";
		db.execSQL(CREAKE_MASTER_AREA);
		String CREAKE_MASTER_BRANCH = "CREATE TABLE MsBranch (area_id TEXT, branch_id TEXT,branch TEXT, " +
				"PRIMARY KEY (area_id, branch_id))";
		db.execSQL(CREAKE_MASTER_BRANCH);
		String CREAKE_MASTER_TS = "CREATE TABLE MsTs (area_id TEXT, branch_id TEXT, ts_id TEXT, ts TEXT, " +
				"PRIMARY KEY (area_id, branch_id, ts_id))";
		db.execSQL(CREAKE_MASTER_TS);
		String CREATE_MASTER_FARM = "CREATE TABLE MsFarm (area_id TEXT, branch_id TEXT, ts_id TEXT, farmer_id TEXT, name TEXT," +
				"address TEXT, PRIMARY KEY (area_id, branch_id, ts_id, farmer_id))";
		db.execSQL(CREATE_MASTER_FARM);

		String CREATE_TABLE_IMAGE = "CREATE TABLE Image(latit TEXT, longi TEXT, ztsid TEXT, zfmid TEXT, mobile TEXT, type TEXT, zuser TEXT, clien TEXT, image TEXT, descr TEXT, token TEXT, primary key (zfmid))";
		db.execSQL(CREATE_TABLE_IMAGE);

		String CREATE_CHECK_IN_TABLE = "CREATE TABLE tsci (pid INTEGER, ztsid TEXT, zfmid TEXT, descr TEXT, pic TEXT, longi TEXT," +
				" latit TEXT, PRIMARY KEY (pid) )";
//		db.execSQL(CREATE_CHECK_IN_TABLE);
		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + "farm");
		db.execSQL("DROP TABLE IF EXISTS " + "lhkh");
		db.execSQL("DROP TABLE IF EXISTS " + "MsArea");
		db.execSQL("DROP TABLE IF EXISTS " + "MsBranch");
		db.execSQL("DROP TABLE IF EXISTS " + "MsTs");
		db.execSQL("DROP TABLE IF EXISTS " + "MsFarm");
		db.execSQL("DROP TABLE IF EXISTS " + "tsci");
		db.execSQL("DROP TABLE IF EXISTS " + "Image");
		Log.d(TAG, "Database tables drop");

		// Create tables again
		onCreate(db);
	}

	public void addImage(String latit, String longi, String ztsid, String zfmid, String mobile, String type, String zuser, String clien, String image, String descr, String token) {
		SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS Image(latit TEXT, longi TEXT, ztsid TEXT, zfmid TEXT, mobile TEXT, type TEXT, zuser TEXT, clien TEXT, image TEXT, descr TEXT, token TEXT, primary key (zfmid))";
		db.execSQL(CREATE_TABLE_IMAGE);

		ContentValues values = new ContentValues();
		values.put("latit", latit);
		values.put("longi", longi);
		values.put("ztsid", ztsid);
		values.put("zfmid", zfmid);
		values.put("mobile", mobile);
		values.put("type", type);
		values.put("zuser", zuser);
		values.put("clien", clien);
		values.put("image", image);
		values.put("descr", descr);
		values.put("token", token);

		long id = db.insert("Image", null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New image inserted into sqlite: " + id);
	}

	public void DropImage(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM Image");
	}

	public HashMap<String, String> getImage() {
		HashMap<String, String> image = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM Image";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			image.put("latit", cursor.getString(0));
			image.put("longi", cursor.getString(1));
			image.put("ztsid", cursor.getString(2));
			image.put("zfmid", cursor.getString(3));
			image.put("mobile", cursor.getString(4));
			image.put("type", cursor.getString(5));
			image.put("zuser", cursor.getString(6));
			image.put("clien", cursor.getString(7));
			image.put("image", cursor.getString(8));
			image.put("descr", cursor.getString(9));
			image.put("token", cursor.getString(10));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching Image from Sqlite: " + image.toString());

		return image;
	}

	public List<image> getAllImage() {
		List<image> ImageList = new ArrayList<image>();
		// Select All Query
		String selectQuery = "SELECT  * FROM Image";
		String CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS Image (latit TEXT, longi TEXT, ztsid TEXT, zfmid TEXT, mobile TEXT, type TEXT, zuser TEXT, clien TEXT, image TEXT, descr TEXT, token TEXT, primary key (zfmid))";

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(CREATE_IMAGE_TABLE);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				image image = new image();
				image.setLatit(cursor.getString(cursor.getColumnIndex("latit")));
				image.setLongi(cursor.getString(cursor.getColumnIndex("longi")));
				image.setZtsid(cursor.getString(cursor.getColumnIndex("ztsid")));
				image.setZfmid(cursor.getString(cursor.getColumnIndex("zfmid")));
				image.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
				image.setType(cursor.getString(cursor.getColumnIndex("type")));
				image.setZuser(cursor.getString(cursor.getColumnIndex("zuser")));
				image.setClien(cursor.getString(cursor.getColumnIndex("clien")));
				image.setImage(cursor.getString(cursor.getColumnIndex("image")));
				image.setDescr(cursor.getString(cursor.getColumnIndex("descr")));
				image.setToken(cursor.getString(cursor.getColumnIndex("token")));
				// Adding contact to list
				ImageList.add(image);
			} while (cursor.moveToNext());
		}

		// return contact list
		return ImageList;
	}

	public void addTsci(String ztsid, String zfmid, String descr, String pic, String longi, String latit) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("ztsid", ztsid);
		values.put("zfmid", zfmid);
		values.put("descr", descr);
		values.put("pic",pic);
		values.put("longi",longi);
		values.put("latit", latit);

		// Inserting Row
		long id = db.insert("tsci", null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New tsci inserted into sqlite: " + id);
	}
	public void addUser(String name, String email, String uid, String created_at, String password, String ztsid) {
		SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT, pass TEXT, ztsid TEXT" + ")";
		db.delete(TABLE_USER, null, null);
		db.execSQL("DROP TABLE " + TABLE_USER);
		db.execSQL(CREATE_LOGIN_TABLE);

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At
		values.put("pass", password); // Created At
		values.put("ztsid", ztsid); // Created At

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}
	public void addMsArea(String area_id, String area) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("area_id", area_id); // area_id
		values.put("area", area); // area
		// Inserting Row
		long id = db.insert("MsArea", null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New MsArea inserted into sqlite: " + id);
	}
	public void addJsonMsArea(JSONArray jarr){
		for (int i=0;i<jarr.length();i++){
			try {
				JSONObject jobj = jarr.getJSONObject(i);
				String area_id = jobj.getString("area_id");
				String area = jobj.getString("area");
				addMsArea(area_id, area);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void addMsBranch(String area_id, String branch_id, String branch) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("area_id", area_id); // area_id
		values.put("branch_id", branch_id); // area
		values.put("branch", branch); // area
		// Inserting Row
		long id = db.insert("MsBranch", null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New MsBranch inserted into sqlite: " + id);
	}
	public void addJsonMsBranch(JSONArray jarr){
		for (int i=0;i<jarr.length();i++){
			try {
				JSONObject jobj = jarr.getJSONObject(i);
				String area_id = jobj.getString("area_id");
				String branch_id = jobj.getString("branch_id");
				String branch = jobj.getString("branch");
				addMsBranch(area_id, branch_id, branch);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void addMsTs(String area_id, String branch_id, String ts_id, String ts) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("area_id", area_id); // area_id
		values.put("branch_id", branch_id); // area
		values.put("ts_id", ts_id); // area
		values.put("ts", ts); // area
		// Inserting Row
		try {
			long id = db.insert("MsTs", null, values);
			Log.d(TAG, "New MsTs inserted into sqlite: " + id);
		}
		catch (SQLException e){
			Log.d(TAG, e.toString());
		}
		db.close(); // Closing database connection

	}
	public void addJsonMsTs(JSONArray jarr){
		for (int i=0;i<jarr.length();i++){
			try {
				JSONObject jobj = jarr.getJSONObject(i);
				String area_id = jobj.getString("area_id");
				String branch_id = jobj.getString("branch_id");
				String ts_id = jobj.getString("ts_id");
				String ts = jobj.getString("ts");
				addMsTs(area_id, branch_id, ts_id, ts);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void addMsFarm(String area_id, String branch_id, String ts_id, String farmer_id, String name, String address) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("area_id", area_id); // area_id
		values.put("branch_id", branch_id); // area
		values.put("ts_id", ts_id); // area
		values.put("farmer_id", farmer_id);
		values.put("name", name);
		values.put("address", address);
		// Inserting Row
		long id = db.insert("MsFarm", null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New MsFarm inserted into sqlite: " + id);
	}
	public void addJsonMsFarm(JSONArray jarr){
		for (int i=0;i<jarr.length();i++){
			try {
				JSONObject jobj = jarr.getJSONObject(i);
				String area_id = jobj.getString("area_id");
				String branch_id = jobj.getString("branch_id");
				String ts_id = jobj.getString("ts_id");
				String farmer_id = jobj.getString("farmer_id");
				String name = jobj.getString("name");
				String address = jobj.getString("address");
				addMsFarm(area_id,branch_id,ts_id,farmer_id,name,address);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	public void addFarm(String famid, String famnm, String famad, String longi, String latit) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("famid", famid); // id
		values.put("famnm", famnm); // name
		values.put("famad", famad); // address
		values.put("longi", longi); // longi
		values.put("latit", latit); // latit

		Cursor check = null;
		String sql ="SELECT famid FROM farm WHERE famid='"+famid+"'";
		check= db.rawQuery(sql,null);
		long id;
		if(check.getCount()>0){
			//PID Found
		}else{
			// Inserting Row
			id = db.insert("farm", null, values);
//			Log.d(TAG, "New farm inserted into sqlite: " + id);
		}

		db.close(); // Closing database connection

	}
	public void addGmap(String mapcd, String longi, String latit) {
		SQLiteDatabase db = this.getWritableDatabase();
//		String CREATE_GMAP_TABLE = "CREATE TABLE IF NOT EXISTS gmap("
//				+ "" + " TEXT," + "zdati" + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
//				+ "zuser" + " TEXT," + "longi" + " TEXT," + "latit" + " TEXT, PRIMARY KEY (mapcd, zdati)" +")";
		String CREATE_GMAP_TABLE = "CREATE TABLE IF NOT EXISTS gmap(mapcd TEXT, zuser TEXT, longi TEXT," +
				" latit TEXT, primary key (mapcd))";
//		db.execSQL("DROP TABLE IF EXISTS " + "gmap");
		db.execSQL(CREATE_GMAP_TABLE);
		//db.delete("farm", null, null);

		ContentValues values = new ContentValues();
		values.put("mapcd", mapcd);
		values.put("longi", longi);
		values.put("latit", latit);

		Cursor check = null;
		String sql ="SELECT mapcd FROM gmap WHERE mapcd='"+mapcd+"'";
		check= db.rawQuery(sql,null);
		long id;
		if(check.getCount()>0){
			//PID Found
		}else{
			// Inserting Row
//			id = db.insert("gmap", null, values);
//			Log.d(TAG, "New gmap inserted into sqlite: " + id);
		}
		id = db.insert("gmap", null, values);
		Log.d(TAG, "New gmap inserted into sqlite: " + id);

		db.close(); // Closing database connection

	}
	public void DropGmap(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor1 = db.rawQuery("SELECT * FROM sqlite_master WHERE name ='gmap' and type='table'", null);
		if(cursor1!=null) {
			db.execSQL("DELETE FROM gmap");
		}
		else {

		}
	}
	public void DropLhkh(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor2 = db.rawQuery("SELECT * FROM sqlite_master WHERE name ='lhkh' and type='table'", null);
		if(cursor2!=null) {
			db.execSQL("DELETE FROM lhkh");
		}
		else{

		}
	}
	public void DropTsci(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM tsci");
	}
	public void addLhk(String ztsid, String zfmid, String recdt, String morta, String donum, String avgbd, String feedc, String feedi, String longi, String latit){
		SQLiteDatabase db = this.getWritableDatabase();
		String quant="";
		String lhkcd="";
		if(!morta.equals("")){
			quant=morta;
			lhkcd="MORTA";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!avgbd.equals("")){
			quant=avgbd;
			lhkcd="AVGBW";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!donum.equals("")){
			quant=donum;
			lhkcd="DONUM";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!feedc.equals("")&&!feedc.equals("[]")){
			quant=feedc;
			lhkcd="FEEDC";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!feedi.equals("")&&!feedi.equals("[]")){
			quant=feedi;
			lhkcd="FEEDI";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		Log.d(TAG, "New report inserted into sqlite");
		db.close(); // Closing database connection

	}
	public void add_doc(String ztsid, String zfmid, String recdt, String docqt, String doctr, String longi, String latit){
		SQLiteDatabase db = this.getWritableDatabase();
		String quant="";
		String lhkcd="";
		if(!docqt.equals("")){
			quant=docqt;
			lhkcd="DOCIN";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!doctr.equals("")){
			quant=doctr;
			lhkcd="DOCTR";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
	}
	public void add_final(String ztsid, String zfmid, String recdt, String fnlqt, String fnlbw, String isFInal, String longi, String latit){
		SQLiteDatabase db = this.getWritableDatabase();
		String quant="";
		String lhkcd="";
		if(!fnlqt.equals("")){
			quant=fnlqt;
			lhkcd="FNLQT";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!fnlbw.equals("")){
			quant=fnlbw;
			lhkcd="FNLBW";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
		if(!isFInal.equals("")){
			quant=isFInal;
			lhkcd="ISFNL";
			ContentValues values = new ContentValues();
			values.put("ztsid", ztsid);
			values.put("zfmid", zfmid);
			values.put("lhkcd", lhkcd);
			values.put("recdt", recdt);
			values.put("quant", quant);
			values.put("longi", longi);
			values.put("latit", latit);
			db.insert("lhkh", null, values);
		}
	}

	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
			user.put("pass", cursor.getString(5));
			user.put("ztsid", cursor.getString(6));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}
	public String getToken() {
		String token="";
		String selectQuery = "SELECT pass FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			token = cursor.getString(cursor.getColumnIndex("pass"));

		}
		cursor.close();
		db.close();

		return token;
	}
	public String getEmail() {
		String email="";
		String selectQuery = "SELECT email FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			email = cursor.getString(cursor.getColumnIndex("email"));

		}
		cursor.close();
		db.close();

		return email;
	}
	public ArrayList<String> getFarmDetails() {
//		HashMap<String, String> user = new HashMap<String, String>();
		ArrayList<String> ListFarm = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM farm";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		if  (cursor.moveToFirst()) {
			do {
				//Get version from Cursor
				String famnm = cursor.getString(1);
				//Add the version to Arraylist 'results'
				ListFarm.add(famnm);
			}while (cursor.moveToNext()); //Move to next row
		}
		cursor.close();
		db.close();
		// return user
		for(String d:ListFarm) {
			System.out.println(d);
			Log.d(TAG, d);
			// prints [Tommy, tiger]
		}
		Log.d(TAG, "Fetching farm from Sqlite: " + ListFarm.toString());

		return ListFarm;
	}
	public List<Farmer> getAllFarmer() {
		List<Farmer> FarmerList = new ArrayList<Farmer>();
		// Select All Query
		String selectQuery = "SELECT  * FROM farm ORDER BY famid ";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Farmer Farmer = new Farmer();
				Farmer.setFarmCode(cursor.getString(cursor.getColumnIndex("famid")));
				Farmer.setFarmName(cursor.getString(cursor.getColumnIndex("famnm")));
				Farmer.setFarmAddress(cursor.getString(cursor.getColumnIndex("famad")));
				Farmer.set_longi(cursor.getString(cursor.getColumnIndex("longi")));
				Farmer.set_latit(cursor.getString(cursor.getColumnIndex("latit")));
				// Adding contact to list
				FarmerList.add(Farmer);
			} while (cursor.moveToNext());
		}

		// return contact list
		return FarmerList;
	}
	public JSONArray getAllTs(){
		String selectQuery = "SELECT  * FROM MsTs ";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		JSONArray result = new JSONArray();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("ts_id",cursor.getString(cursor.getColumnIndex("ts_id")));
					jobj.put("area_id",cursor.getString(cursor.getColumnIndex("area_id")));
					jobj.put("branch_id",cursor.getString(cursor.getColumnIndex("branch_id")));
					jobj.put("ts",cursor.getString(cursor.getColumnIndex("ts")));
					result.put(jobj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());
		}
		return  result;
	}
	public JSONArray getFarmByTs(String TsId){
		String area_id = TsId.substring(0,2);
		String branch_id = TsId.substring(2,5);
		String ts_id = TsId.substring(5,8);
		String selectQuery = "SELECT  * FROM MsFarm WHERE area_id='"+area_id+"' AND branch_id='"+branch_id+"' AND ts_id='"+ts_id+"'  ";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		JSONArray result = new JSONArray();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("farmer_id",cursor.getString(cursor.getColumnIndex("farmer_id")));
					jobj.put("ts_id",cursor.getString(cursor.getColumnIndex("ts_id")));
					jobj.put("area_id",cursor.getString(cursor.getColumnIndex("area_id")));
					jobj.put("branch_id",cursor.getString(cursor.getColumnIndex("branch_id")));
					jobj.put("name",cursor.getString(cursor.getColumnIndex("name")));
					result.put(jobj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());
		}
		return  result;
	}
	public JSONArray getAllTsci(){
		String selectQuery = "SELECT  * FROM tsci ";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		JSONArray result = new JSONArray();
		if (cursor.moveToFirst()) {
			do {
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("ztsid",cursor.getString(cursor.getColumnIndex("ztsid")));
					jobj.put("zfmid",cursor.getString(cursor.getColumnIndex("zfmid")));
					jobj.put("descr",cursor.getString(cursor.getColumnIndex("descr")));
					jobj.put("pic",cursor.getString(cursor.getColumnIndex("pic")));
					jobj.put("longi",cursor.getString(cursor.getColumnIndex("longi")));
					jobj.put("latit",cursor.getString(cursor.getColumnIndex("latit")));
					result.put(jobj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			while (cursor.moveToNext());
		}
		return  result;

	}
	public List<gmap> getAllGmap() {
		List<gmap> GmapList = new ArrayList<gmap>();
		// Select All Query
		String selectQuery = "SELECT  * FROM gmap";
		String CREATE_GMAP_TABLE = "CREATE TABLE IF NOT EXISTS gmap(mapcd TEXT, zuser TEXT, longi TEXT, latit TEXT, primary key (mapcd))";

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(CREATE_GMAP_TABLE);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				gmap gmap = new gmap();
				gmap.setMapCode(cursor.getString(cursor.getColumnIndex("mapcd")));
				gmap.setLatit(cursor.getString(cursor.getColumnIndex("latit")));
				gmap.setLongi(cursor.getString(cursor.getColumnIndex("longi")));
				// Adding contact to list
				GmapList.add(gmap);
			} while (cursor.moveToNext());
		}

		// return contact list
		return GmapList;
	}
	public JSONArray getAllLhk(){
		JSONArray data = new JSONArray();
		SQLiteDatabase db = this.getWritableDatabase();
		String select_lhk="SELECT * FROM lhkh";
		Cursor cursor = db.rawQuery(select_lhk, null);
		if (cursor.moveToFirst()) {
			do {
//				lhkh lhkh = new lhkh();
				JSONObject jlhk = new JSONObject();
				try {
					jlhk.put("latit",cursor.getString(cursor.getColumnIndex("latit")));
					jlhk.put("lhkcd",cursor.getString(cursor.getColumnIndex("lhkcd")));
					jlhk.put("longi",cursor.getString(cursor.getColumnIndex("longi")));
					jlhk.put("recdt",cursor.getString(cursor.getColumnIndex("recdt")));
					jlhk.put("zfmid",cursor.getString(cursor.getColumnIndex("zfmid")));
					jlhk.put("ztsid",cursor.getString(cursor.getColumnIndex("ztsid")));
					if(cursor.getString(cursor.getColumnIndex("lhkcd")).equals("FEEDC")||cursor.getString(cursor.getColumnIndex("lhkcd")).equals("FEEDI")){//jika ada data feed
//						String fedsr = cursor.getString(cursor.getColumnIndex("quant"));
						JSONArray fedar = new JSONArray(cursor.getString(cursor.getColumnIndex("quant")));
//						JSONObject fedar = new JSONObject("{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
//						Log.e("isi_fedar", fedar.toString());
						jlhk.put("quant",fedar);
					}
					else {
//						jlhk.put("quant","non feed");
						jlhk.put("quant", cursor.getString(cursor.getColumnIndex("quant")));
					}
				} catch (JSONException e) {
					Log.e("error_json",e.toString());
					e.printStackTrace();
				}
//				lhkh.setLatit(cursor.getString(cursor.getColumnIndex("latit")));
//				lhkh.setLhkcd(cursor.getString(cursor.getColumnIndex("lhkcd")));
//				lhkh.setLongi(cursor.getString(cursor.getColumnIndex("longi")));
//				lhkh.setQuant(cursor.getString(cursor.getColumnIndex("quant")));
//				lhkh.setRecdt(cursor.getString(cursor.getColumnIndex("recdt")));
//				lhkh.setZfmid(cursor.getString(cursor.getColumnIndex("zfmid")));
//				lhkh.setZtsid(cursor.getString(cursor.getColumnIndex("ztsid")));
				data.put(jlhk);
			} while (cursor.moveToNext());
		}
		return data;
	}
	public void deleteTabel() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.delete("farm", null, null);
		db.delete("MsFarm", null, null);
		db.delete("MsArea", null, null);
		db.delete("MsBranch", null, null);
		db.delete("MsTs", null, null);
		db.close();
		Log.d(TAG, "Deleted all table from sqlite");
	}
	public void collTabel() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete("farm", null, null);
		db.delete("MsFarm", null, null);
		db.delete("MsArea", null, null);
		db.delete("MsBranch", null, null);
		db.delete("MsTs", null, null);
		db.close();
		Log.d(TAG, "Deleted all table except user table from sqlite");
	}

}

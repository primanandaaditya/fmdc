package id.co.cp.mdc.app;

import org.json.JSONArray;

import id.co.cp.mdc.helper.Notif;

public class AppConfig {
	// Server user login url
	public static String mapty= "FM";
	public static String clien = "88";
//	public static String clien= "28";
//	public static String ip = "http://192.168.1.109/eis";
//	public static String ip = "http://10.1.3.106/eis";
	public static String ip = "https://cpis.cp.co.id";
	public static String url_api_fmdc = ip + "/fmds/fmdc_android_api.php";
	public static String urlSpinner = ip+"/fmds/fmds_get_list_spinner_ajax.php";
	public static String URL_LOGIN = ip+"/ajax/login_ajax.php";
	public static String urlJsonArry = ip+"/ajax/zget_servercode_ajax.php?mobile=android";
	public static String urlTrxFeed = ip+"/fmds/fmds_get_list_feed_ajax.php";
	public static String urlTrxFeedDetail = ip+"/fmds/fmds_get_detail_feed_ajax.php";
	public static String urlPimsFarm = ip+"/fmds/fmds_change_data_ajax.php?mobile=android";
	public static String urlLhkFarm = ip+"/fmds/fmds_change_lhk_ajax.php";
	public static String urlSync = ip+"/fmds/fmds_sync_farm.php";
	public static String urlDvcId = ip+"/fmds/fmds_set_device_id.php";
	public static String urlJson = ip+"/fmds/fmds_load_data_ajax.php";
	public static String urlMyTag =   ip+"/fmds/fmds_get_mytag_ajax.php";
	public static String urlMyImage =  ip+"/fmds/fmds_upload_pict_ajax.php";
	public static String urlMyTagStatus = ip+"/fmds/fmds_get_geotag_status_ajax.php";
	public static String urlMyInfo = ip+"/fmds/fmds_get_summary_stock.php";
	public static String urlGetData = ip+"/lcfm/lcfm_load_view_android_ajax.php";
	public static String urlMonitoring1 =  ip+"/fmds/fmds_get_list_pict_ajax.php";
	public static String urlTrfFeed = ip+"/fmds/fmds_change_feed_ajax.php";
	public static String urlMonitoring2 = ip+"/fmds/fmds_get_list_farm_ajax.php";
	public static String urlMonitoring3 = ip+"/fmds/fmds_get_list_activity_ajax.php";
	public static String urlMassInput = ip+"/fmds/fmds_load_data_ajax.php";
	public static String urlMassInput2 = ip+"/fmds/fmds_send_obj_data_ajax.php";
	public static String server_ip =ip;
	public static Notif SelectedNotif = new Notif();
	public static String SelectedTS = "";
	public static String Ztsnm = "";
	public static String Zfmid = "";

	public static String Zfmnm = "";
	public static String Trxcd = "";
	public static int interval_time = 180;//n hari paling depan
	public static int timeout = 100000; //Milliseconds

	//keperluan mass input
	public static String partialjum ="";
	public static String fulljum="";
	public static String blockedjum="";

	//spinner value
	public static Integer FmSpinNum=0;

	// Server user register url
	public static String URL_REGISTER = "http://192.168.0.102/android_login_api/register.php";

	// Request Farmer
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_FARMER = "name";
	public static final String TAG_FARMER_ID = "farmer_id";
	public static final String TAG_AREA_ID = "area_id";
	public static final String TAG_BRANCH_ID = "branch_id";
	public static final String TAG_CODE = "code";
	public static final String TAG_NAME = "name";

	public static JSONArray tesArray;

}

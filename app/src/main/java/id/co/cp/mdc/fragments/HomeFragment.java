package id.co.cp.mdc.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.cp.mdc.LoginActivity;
import id.co.cp.mdc.R;
import id.co.cp.mdc.app.AppConfig;
import id.co.cp.mdc.controller.ChartTagController;
import id.co.cp.mdc.helper.ChartHelper;
import id.co.cp.mdc.helper.SQLiteHandler;
import id.co.cp.mdc.interfaces.ChartTagInterface;
import id.co.cp.mdc.model.ChartModel;
import id.co.cp.mdc.other.BaseFragment;
import id.co.cp.mdc.other.CircleTransform;
import id.co.cp.mdc.model.ChartModel.Datum;
import id.co.cp.mdc.model.ChartModel.Chart;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements ChartTagInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_OK = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Camera";

    private ProgressDialog pDialog;
    private Uri fileUri; // file url to store image/video
    private GraphView graph;
    private PieChart pieChart;
    private List<PieEntry> entries = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String image;
    private String token;
    private String name;
    private String email;
    private String ztsid;
    private String mParam1;
    private String mParam2;
    private SQLiteHandler db;
    private TextView ztxtName;
    private TextView ztxtEmail;
    private TextView ztxtTsId;
    private TextView ztxtAreaId;
    private TextView ztxtBrcId;
    private TextView zVrs;
    private TextView docsppastok;
    private TextView s00;
    private TextView s10;
    private TextView s11;
    private TextView s12;
    private TextView total;
    private TextView tekscs;
    private RelativeLayout ll1;
    private RelativeLayout ll2;
    private RelativeLayout ll3;
    private RelativeLayout ll4;
    private RelativeLayout ll5;
    private String TAG = "Home Fragment";
    ChartTagController chartTagController;
//    DonutProgress dp;

    //    private static final String urlNavHeaderBg = AppConfig.server_ip+"/images/background-slider.jpg";
//    private String urlProfileImg = AppConfig.server_ip+"/images/no_photo.jpg";
    private ImageView imgNavHeaderBg, imgProfile;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
//        dp=(DonutProgress) rootView.findViewById(R.id.dp);
        imgProfile = (ImageView) rootView.findViewById(R.id.img_profile);

        ll1 = (RelativeLayout) rootView.findViewById(R.id.ll1);
        ll2 = (RelativeLayout) rootView.findViewById(R.id.ll2);
        ll3 = (RelativeLayout) rootView.findViewById(R.id.ll3);
        ll4 = (RelativeLayout) rootView.findViewById(R.id.ll4);
        ll5 = (RelativeLayout) rootView.findViewById(R.id.ll5);
        tekscs = (TextView) rootView.findViewById(R.id.tekscs);

        db = new SQLiteHandler(getActivity().getApplicationContext());
        ztxtEmail = (TextView) rootView.findViewById(R.id.ztxtEmail);
        ztxtName = (TextView) rootView.findViewById(R.id.ztxtName);
        ztxtTsId = (TextView) rootView.findViewById(R.id.zTxtTsId);
        ztxtAreaId = (TextView) rootView.findViewById(R.id.zTxtAreaId);
        ztxtBrcId = (TextView) rootView.findViewById(R.id.zTxtBrcId);
        // graph = (GraphView) rootView.findViewById(R.id.graph);
        pieChart = (PieChart) rootView.findViewById(R.id.chart);
        zVrs = (TextView) rootView.findViewById(R.id.zVrs);

        docsppastok = (TextView) rootView.findViewById(R.id.docsppastok);
        s00 = (TextView) rootView.findViewById(R.id.s00);
        s10 = (TextView) rootView.findViewById(R.id.s10);
        s11 = (TextView) rootView.findViewById(R.id.s11);
        s12 = (TextView) rootView.findViewById(R.id.s12);

        Context context = getContext();
        String version = "";

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            version = info.versionName;
            zVrs.setText("Version : " + version);
        } catch (Exception e) {
            Log.e("YourActivity", "Error getting version");
        }

        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");
        ztsid = user.get("ztsid");
        token = user.get("pass");

        ztxtName.setText(name);
        ztxtEmail.setText(email);
        ztxtTsId.setText(ztsid.substring(5, 8));
        ztxtAreaId.setText(ztsid.substring(0, 2));
        ztxtBrcId.setText(ztsid.substring(2, 5));
        draw_chart();
        getImageProfile();

        //handle profile pict click
        imgProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                select_image_from();

            }
        });

        myinfo();
        this.setTitle("Home");
        return rootView;
    }


    public void getImageProfile() {
//        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlMyImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("isi_gambar",AppConfig.server_ip+response);

                        loadImg(AppConfig.server_ip + response);
                        //   Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                        hidepDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity().getApplicationContext(), "Gagal mengambil data profil", Toast.LENGTH_LONG).show();
//                        hidepDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("clien", AppConfig.clien);
                params.put("zuser", email);
                params.put("ztype", "download");
                params.put("token", token);


                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void select_image_from() {
        final CharSequence[] items = {"Choose from Library", "Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Change Profile");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    getCapturesProfilePicFromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    getProfilePicFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void getCapturesProfilePicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void getProfilePicFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onChartError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChartSuccess(ChartModel.Chart chart) {
        if (chart.getData()==null){

        }else{
            if (chart.getData().size()==0){

            }else{
                Datum datum = chart.getData().get(0);
                Double myTag = Double.parseDouble(datum.getMytag());
                Double total = Double.parseDouble(datum.getMytag()) + Double.parseDouble(datum.getRemain().toString());
                Double persen = myTag/total * 100;
                Integer hasil = (int)Math.floor(persen);

//                dp.setDonut_progress(String.valueOf(hasil));

                final HashMap<Integer, String> map = new HashMap<>();
                map.put(1,"My Tag");
                map.put(2,"Remain");

                List<Float> floats = new ArrayList<Float>();
                floats.add((float)Integer.parseInt(datum.getMytag()));
                floats.add((float)datum.getRemain());

                ChartHelper.PieChartFormat(pieChart, map,floats,"Keterangan");
                pieChart.setCenterText(String.valueOf(hasil) + "% tagged");

            }
        }


        //BaseFunction.LineChartFormat(grafikb1,map,floats,"Bulan");
        //Toast.makeText(getActivity(), chart.getData().get(0).getMytag(), Toast.LENGTH_LONG).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void loadImg(String filePath) {

        // loading header background image
        // Glide.with(this).load(urlNavHeaderBg)
        //       .crossFade()
        //    .diskCacheStrategy(DiskCacheStrategy.ALL)
        //    .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(filePath)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getActivity()))
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)

                .into(imgProfile);

        // showing dot next to notifications label
//        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
    }

    private void send_to_server() {
        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.urlMyImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        hidepDialog();
                        startActivity(getActivity().getIntent());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "tidak terhubung dengan server...", Toast.LENGTH_LONG).show();
                        hidepDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("clien", AppConfig.clien);
                params.put("zuser", email);
                params.put("ztype", "upload");
                params.put("image", image);
                params.put("token", token);
                // ztxtAreaId.setText(params.toString());

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage(fileUri.getPath());
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();


                previewCapturedImage(picturePath);

            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "failed to load image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private void previewCapturedImage(String picturePath) {
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 10;

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
            ExifInterface ei = new ExifInterface(picturePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
            // imgProfile.setImageBitmap(bitmap);
            image = getStringImage(bitmap);
            loadImg(picturePath);
            send_to_server();
//            Matrix matrix = new Matrix();
//            imgPreview.setScaleType(ImageView.ScaleType.MATRIX);   //required
//            //matrix.postRotate(90f, (imgPreview.getDrawable().getBounds().width()/2)*0.88f, (imgPreview.getDrawable().getBounds().height()/2)*1.9f);
//            imgPreview.setImageMatrix(matrix);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public void draw_chart() {

        chartTagController=new ChartTagController(getActivity(),this);
        chartTagController.getChart();

//        JSONObject JList = new JSONObject();
//        try {
//            JList.put("zuser", email);
//            JList.put("clien", AppConfig.clien);
//            JList.put("token", token);
//            JList.put("mapty", AppConfig.mapty);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //showpDialog();
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.POST, AppConfig.urlMyTagStatus, JList, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        JSONArray j;
//
//                        try {
//                            if (response.getString("status").equals("0")) {
//                                j = response.getJSONArray("data");
//                                // for (int i=0;i< j.length();i++){
//                                JSONObject data;
//                                data = j.getJSONObject(0);
//                                float remain = Float.parseFloat(data.getString("remain"));
//                                float mytag = Float.parseFloat(data.getString("mytag"));
//                                float percent = mytag * 100 / (remain + mytag);
//
//
//                                entries.add(new PieEntry(mytag, "Geo Tag : " + String.format("%.0f", mytag)));
//                                entries.add(new PieEntry(remain, "Remaining : " + String.format("%.0f", remain)));
//
//                                PieDataSet dataset = new PieDataSet(entries, "");
//                                PieData Piedata = new PieData(dataset);
//                                dataset.setColors(ColorTemplate.JOYFUL_COLORS);
//
//
//                                pieChart.setCenterText(String.format("%.1f", percent) + "% Tagged");
//                                pieChart.setDrawEntryLabels(false);
//                                Description desc;
//                                //   desc.setText("A");
//                                //   pieChart.setDescription(desc);
//                                pieChart.setData(Piedata);
//                                pieChart.invalidate();
////                            }
//                            } else {//kalau session time out
//                                Toast.makeText(getActivity(), "Session time out. Harap Login kembali", Toast.LENGTH_LONG).show();
//                                db.deleteTabel();
//                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
//                                startActivity(intent);
//                                getActivity().finish();
//                            }
//                        } catch (JSONException e) {
//                            Log.e("error_geotag", e.toString());
//                            Toast.makeText(getActivity(), "Gagal memperoleh data geo tag", Toast.LENGTH_LONG).show();
//
//                        }
//                        //              hidepDialog();
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        hidepDialog();
//                        // TODO Auto-generated method stub
//                        Log.e("error tag status", error.toString());
//                        Toast.makeText(getActivity().getApplicationContext(), "Tidak terhubung dengan server. Harap mencari signal 0.1", Toast.LENGTH_LONG).show();
//                        //  Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                500000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(jsObjRequest);
    }

    public void myinfo() {
        final JSONObject JList = new JSONObject();
        try {
            JList.put("zuser", email);
            JList.put("clien", AppConfig.clien);
            JList.put("token", token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //showpDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, AppConfig.urlMyInfo, JList, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String formatdocin;
                        String formatmorta;
                        String formatstock;
                        String formats00s;
                        String formats00p;
                        String formats10s;
                        String formats10p;
                        String formats11s;
                        String formats11p;
                        String formats12s;
                        String formats12p;

                        Log.e("output",response.toString());
                        try {
                            JSONObject jobj = response.getJSONObject("data");
                            String docin = jobj.getString("docin");
                            String morta = jobj.getString("morta");
                            String stock = jobj.getString("stock");
                            String s00s = jobj.getString("s00s");
                            String s00p = jobj.getString("s00p");
                            String s10s = jobj.getString("s10s");
                            String s10p = jobj.getString("s10p");
                            String s11s = jobj.getString("s11s");
                            String s11p = jobj.getString("s11p");
                            String s12s = jobj.getString("s12s");
                            String s12p = jobj.getString("s12p");

                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            if(docin.equals("null")){
                                formatdocin="0";
                            }else{
                                formatdocin = formatter.format(Double.parseDouble(docin));
                            }

                            if(morta.equals("null")){
                                formatmorta="0";
                            }else{
                                formatmorta = formatter.format(Double.parseDouble(morta));
                            }

                            if(stock.equals("null")){
                                formatstock="0";
                            }else{
                                formatstock = formatter.format(Double.parseDouble(stock));
                            }

                            if(s00s.equals("null")){
                                formats00s="0";
                            }else{
                                formats00s = formatter.format(Double.parseDouble(s00s));
                            }

                            if(s00p.equals("null")){
                                formats00p="0";
                            }else{
                                formats00p = formatter.format(Double.parseDouble(s00p));
                            }

                            if(s10s.equals("null")){
                                formats10s="0";
                            }else{
                                formats10s = formatter.format(Double.parseDouble(s10s));
                            }

                            if(s10p.equals("null")){
                                formats10p="0";
                            }else{
                                formats10p = formatter.format(Double.parseDouble(s10p));
                            }

                            if(s11s.equals("null")){
                                formats11s="0";
                            }else{
                                formats11s = formatter.format(Double.parseDouble(s11s));
                            }

                            if(s11p.equals("null")){
                                formats11p="0";
                            }else{
                                formats11p = formatter.format(Double.parseDouble(s11p));
                            }

                            if(s12s.equals("null")){
                                formats12s="0";
                            }else{
                                formats12s = formatter.format(Double.parseDouble(s12s));
                            }

                            if(s12p.equals("null")){
                                formats12p="0";
                            }else{
                                formats12p = formatter.format(Double.parseDouble(s12p));
                            }

                            docsppastok.setText(formatdocin+"/"+formatmorta+"/"+formatstock);
                            s00.setText(formats00s+"/"+formats00p);
                            s10.setText(formats10s+"/"+formats10p);
                            s11.setText(formats11s+"/"+formats11p);
                            s12.setText(formats12s+"/"+formats12p);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(docsppastok.getText().toString().equals("")){
                            ll1.setVisibility(View.INVISIBLE);
                            ll2.setVisibility(View.INVISIBLE);
                            ll3.setVisibility(View.INVISIBLE);
                            ll4.setVisibility(View.INVISIBLE);
                            ll5.setVisibility(View.INVISIBLE);
                            tekscs.setVisibility(View.VISIBLE);
                        }else{
                            ll1.setVisibility(View.VISIBLE);
                            ll2.setVisibility(View.VISIBLE);
                            ll3.setVisibility(View.VISIBLE);
                            ll4.setVisibility(View.VISIBLE);
                            ll5.setVisibility(View.VISIBLE);
                            tekscs.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        // TODO Auto-generated method stub
                        Log.e("error tag status", error.toString());
                        Toast.makeText(getActivity().getApplicationContext(), "Tidak terhubung dengan server. Harap mencari signal 0.1", Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsObjRequest);
    }

    public void draw_graph() {

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }
}

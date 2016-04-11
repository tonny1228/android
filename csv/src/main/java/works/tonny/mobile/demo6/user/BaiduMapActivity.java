package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import works.tonny.mobile.demo6.R;
import works.tonny.mobile.utils.Log;

public class BaiduMapActivity extends Activity {
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static List<Activity> activityList = new LinkedList<Activity>();
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String mSDCardPath = null;
    private boolean clicked = false;
    private ProgressDialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); SDKInitializer.initialize(getApplicationContext());
        activityList.add(this);
        setContentView(R.layout.user_baidu_map); mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap(); String latitude = getIntent().getStringExtra("latitude");
        final String longitude = getIntent().getStringExtra("longitude");
        final String l = getIntent().getStringExtra("latitude");
        final String clatitude = getIntent().getStringExtra("cur_latitude");
        final String clongitude = getIntent().getStringExtra("cur_longitude");

        Log.info(latitude); Log.info(longitude);
        LatLng point = new LatLng(Float.parseFloat(l), Float.parseFloat(longitude));
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions().position(point).title("nav").icon(bitmap).zIndex(10);
        mBaiduMap.addOverlay(option); MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(13).build();


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        if (initDirs()) {
            initNavi();
        }

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    if (clicked || !BaiduNaviManager.isNaviInited()) {
                        return false;
                    }
                    myDialog = ProgressDialog.show(BaiduMapActivity.this, "", "载入中", true);
                    BNRoutePlanNode sNode = null; BNRoutePlanNode eNode = null;
                    sNode = new BNRoutePlanNode(Float.parseFloat(clongitude), Float.parseFloat(clatitude), "start", null, BNRoutePlanNode.CoordinateType.WGS84);
                    eNode = new BNRoutePlanNode(Float.parseFloat(longitude), Float.parseFloat(l), "end", null, BNRoutePlanNode.CoordinateType.WGS84);
                    List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>(); list.add(sNode); list.add(eNode);
                    BaiduNaviManager.getInstance().launchNavigator(BaiduMapActivity.this, list, 1, true, new DemoRoutePlanListener(sNode));
                    clicked = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(BaiduMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } return true;
            }
        });


    }


    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        } return null;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir(); if (mSDCardPath == null) {
            return false;
        } File f = new File(mSDCardPath, APP_FOLDER_NAME); if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace(); return false;
            }
        } return true;
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(BaiduMapActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("routePlanNode", (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Log.info("8888888888888888888888888888888888888888888888888888888888888888888888888888");
        }
    }

    private void initNavi() {
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            public String authinfo;

            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "success!";
                } else {
                    authinfo = "faild, " + msg;
                } BaiduMapActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //Toast.makeText(BaiduMapActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                //Toast.makeText(BaiduMapActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            public void initStart() {
                // Toast.makeText(BaiduMapActivity.this, "start", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(BaiduMapActivity.this, "faild!!", Toast.LENGTH_SHORT).show();
            }
        }, null /*mTTSCallback*/);
    }

    @Override
    protected void onResume() {
        super.onResume(); mMapView.onResume();
        clicked = false;
    }

    @Override
    protected void onPause() {
        super.onPause(); mMapView.onPause();
        if (myDialog != null) {
            myDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy(); mMapView.onDestroy();
    }
}

package works.tonny.mobile;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Date;

import works.tonny.mobile.utils.DateUtils;
import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2015/9/20.
 */
public class LocationService {
    private final Options options;
    private LocationClient mLocationClient = null;

    public LocationService(Options options) {
        this.options = options;
        init(options);
    }

    public static LocationService locate(Options options) {
        return new LocationService(options);
    }

    private void init(Options options) {
        BDLocationListener myListener = new MyLocationListener(options);
        mLocationClient = new LocationClient(options.applicationContext);
        mLocationClient.registerLocationListener(myListener);
        initLocation(mLocationClient, options);
        mLocationClient.start();
    }

    public void stop() {
        mLocationClient.stop();
    }

    private static void initLocation(LocationClient mLocationClient, Options options) {
        LocationClientOption option = new LocationClientOption();
        if (options.mode == 1) {
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setOpenGps(true);
        } else {
            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
            option.setOpenGps(false);
        }
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        if (!options.autoStop) {
            option.setScanSpan(options.refreshIntervalTime);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        }
        //option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        //option.setOpenGps(true);//可选，默认false,设置是否使用gps
        //option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        //option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        //option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        //option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {
        Options options;

        public MyLocationListener(Options options) {
            this.options = options;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (options.autoStop)
                mLocationClient.stop();
            Log.info(location.getTime());
            options.onLocationChanged.onChanged(location.getLatitude(), location.getLongitude(), DateUtils.toDate(location.getTime()));
            //Receive Location

            if (location.getLocType() == BDLocation.TypeServerError) {
                options.onLocationChanged.onError(0);
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                options.onLocationChanged.onError(1);
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                options.onLocationChanged.onError(2);
            }
        }
    }

    public static class Options {
        /**
         * network0,gps1
         */
        int mode = 0;

        int refreshIntervalTime = 0;

        boolean autoStop = true;

        private Context applicationContext;

        private OnLocationChanged onLocationChanged;

        Options(Context applicationContext, OnLocationChanged onLocationChanged) {
            this.applicationContext = applicationContext;
            this.onLocationChanged = onLocationChanged;
        }

        public static Options buildOptions(Context applicationContext, OnLocationChanged onLocationChanged) {
            return new Options(applicationContext, onLocationChanged);
        }

        public Options networkMode() {
            this.mode = 0;
            return this;
        }

        public Options gpsMode() {
            this.mode = 1;
            return this;
        }

        public Options refreshIntervalTime(int time) {
            this.refreshIntervalTime = time;
            return this;
        }

        public Options notStop() {
            autoStop = false;
            return this;
        }
    }

    public static interface OnLocationChanged {
        void onChanged(double latitude, double longitude, Date time);

        void onError(int code);
    }
}

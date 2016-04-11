package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.LocationService;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.widget.LoadingDialog;

/**
 * Created by tonny on 2016/1/29.
 */
public class Nearby extends Activity {

    private ActivityHelper instance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_nearby);
        instance = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).setTitle("附近").enableBack();

        instance.setOnClickListener(R.id.fj, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog loadingDialog = LoadingDialog.newInstance(Nearby.this);
                loadingDialog.show();
                LocationService.locate(LocationService.Options.buildOptions(Nearby.this, new LocationService.OnLocationChanged() {
                    @Override
                    public void onChanged(double latitude, double longitude, Date time) {
                        loadingDialog.dismiss();
                        Intent intent = IntentUtils.newInstance(Nearby.this, NearbyDogActivity.class, "latitude", String.valueOf(latitude), "longitude", String.valueOf(longitude));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int code) {
                        Toast.makeText(Nearby.this, "定位失败", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                }).networkMode());
            }
        });
        instance.setOnClickListener(R.id.fjjlb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog loadingDialog = LoadingDialog.newInstance(Nearby.this);
                loadingDialog.show();
                LocationService.locate(LocationService.Options.buildOptions(Nearby.this, new LocationService.OnLocationChanged() {
                    @Override
                    public void onChanged(double latitude, double longitude, Date time) {
                        loadingDialog.dismiss();
                        Intent intent = IntentUtils.newInstance(Nearby.this, NearbyClubActivity.class, "latitude", String.valueOf(latitude), "longitude", String.valueOf(longitude));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int code) {
                        Toast.makeText(Nearby.this, "定位失败", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                }).networkMode());
            }
        });
        instance.setOnClickListener(R.id.fjqs, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog loadingDialog = LoadingDialog.newInstance(Nearby.this);
                loadingDialog.show();
                LocationService.locate(LocationService.Options.buildOptions(Nearby.this, new LocationService.OnLocationChanged() {
                    @Override
                    public void onChanged(double latitude, double longitude, Date time) {
                        loadingDialog.dismiss();
                        Intent intent = IntentUtils.newInstance(Nearby.this, NearbyQuanSheActivity.class, "latitude", String.valueOf(latitude), "longitude", String.valueOf(longitude));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int code) {
                        Toast.makeText(Nearby.this, "定位失败", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                }).networkMode());
            }
        });

    }

}

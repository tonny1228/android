package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;

import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.LocationService;
import works.tonny.mobile.widget.LoadingDialog;

public class LocationActivity extends Activity {
    private LoadingDialog loadingDialog;

    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog = LoadingDialog.newInstance(this);
        loadingDialog.show();
        LocationService.locate(LocationService.Options.buildOptions(getApplicationContext(), new LocationService.OnLocationChanged() {
            @Override
            public void onChanged(double latitude, double longitude, Date time) {
//                LocationActivity.super.onResume();
                loadingDialog.dismiss();
                Intent intent = IntentUtils.newInstance(getBaseContext(), (Class) getIntent().getSerializableExtra("redirect"), "latitude", String.valueOf(latitude), "longitude", String.valueOf(longitude));
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int code) {
                Toast.makeText(LocationActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }).networkMode());
    }

}

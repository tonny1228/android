package works.tonny.mobile.autobackup;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import works.tonny.mobile.Launcher;
import works.tonny.mobile.widget.AbstractWelcomeActivity;


public class WelcomeActivity extends AbstractWelcomeActivity {


    @Override
    protected void init() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected Class getMainClass() {
        return ServiceListActivity.class;
    }

}

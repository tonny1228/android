package works.tonny.mobile.demo6.test;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.ZoomImagePaperFragment;

public class MainActivity extends Activity {

    private ZoomImageView zoomImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        final ArrayList<ZoomImagePaperFragment.Entity> list = new ArrayList<ZoomImagePaperFragment.Entity>();
        try {
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/1604931666457971354720.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048159551478716787627.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16041282167746262208624.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046795489277744177940.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048464729518265326140.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046629941891897969332.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048767766490740973434.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16047136720272844984748.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042323858041574093930.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/1604751518598448079924.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042068614627394362225.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045773315182348499787.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049054230099081796463.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048893382731837661563.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048662635370385015835.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046488977109231572503.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045435015249543887187.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042043069506351823045.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16043413343938936007521.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16041570038468416132579.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046580412583121227732.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049175123784829996269.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042020197567608685180.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045580155074172868094.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049047354124754622911.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16044095599362121124683.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/1604931666457971354720.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048159551478716787627.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16041282167746262208624.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046795489277744177940.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048464729518265326140.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046629941891897969332.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048767766490740973434.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16047136720272844984748.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042323858041574093930.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/1604751518598448079924.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042068614627394362225.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045773315182348499787.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049054230099081796463.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048893382731837661563.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048662635370385015835.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046488977109231572503.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045435015249543887187.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042043069506351823045.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16043413343938936007521.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16041570038468416132579.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046580412583121227732.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049175123784829996269.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042020197567608685180.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045580155074172868094.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049047354124754622911.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16044095599362121124683.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/1604931666457971354720.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048159551478716787627.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16041282167746262208624.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046795489277744177940.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048464729518265326140.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046629941891897969332.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048767766490740973434.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16047136720272844984748.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042323858041574093930.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/1604751518598448079924.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042068614627394362225.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045773315182348499787.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049054230099081796463.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048893382731837661563.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16048662635370385015835.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046488977109231572503.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045435015249543887187.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042043069506351823045.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16043413343938936007521.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16041570038468416132579.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16046580412583121227732.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049175123784829996269.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16042020197567608685180.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16045580155074172868094.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16049047354124754622911.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
            list.add(new ZoomImagePaperFragment.Entity("http://www.csvclub.org:80/res/csvclub/u/1604/16044095599362121124683.gif", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));
//            list.add(new ZoomImagePaperFragment.Entity("http://image5.tuku.cn/wallpaper/Landscape%20Wallpapers/2490_1680x1050.jpg", "", null, works.tonny.mobile.demo6.WebActivity.VIEW, new HashMap<String, String>()));


        } catch (Exception e) {
            Log.error(e);
//            list.add(new ZoomImagePaperFragment.Entity(R.drawable.header, "加入我们", null, null, null));
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final ZoomImagePaperFragment fragment = ZoomImagePaperFragment.newInstance(list);
        fragmentTransaction.replace(R.id.list, fragment);
        fragmentTransaction.commit();


    }
}
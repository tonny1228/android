package works.tonny.mobile.demo6;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.breed.BuzfyzhshActivity;
import works.tonny.mobile.demo6.breed.BuzxtActivity;
import works.tonny.mobile.demo6.breed.DNAActivity;
import works.tonny.mobile.demo6.breed.PeiQuanxxActivity;
import works.tonny.mobile.demo6.breed.PeiqzhmActivity;
import works.tonny.mobile.demo6.breed.QuanzbgActivity;
import works.tonny.mobile.demo6.breed.XinshqActivity;


/**

 */
public class BreedFragment extends Fragment {

    private View view;

    public BreedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_fragment_breed, container, false);

        ActivityHelper activityHelper = ActivityHelper.getInstance((ViewGroup) view);
        TitleHelper titleHelper = TitleHelper.getInstance((ViewGroup) view).setTitle("繁殖");
        activityHelper.setOnClickListener(R.id.fz_pqzm, PeiqzhmActivity.class);
        activityHelper.setOnClickListener(R.id.fz_dna, DNAActivity.class);
        activityHelper.setOnClickListener(R.id.fz_pqxx, PeiQuanxxActivity.class);
        activityHelper.setOnClickListener(R.id.fz_xsq, XinshqActivity.class);
        activityHelper.setOnClickListener(R.id.fz_qzbg, QuanzbgActivity.class);
        activityHelper.setOnClickListener(R.id.fz_bzxtzs, BuzxtActivity.class);
        activityHelper.setOnClickListener(R.id.fz_bzfyzs, BuzfyzhshActivity.class);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Application.getUser() == null && !MainActivity.switchToHome) {
            LoginActivity.startLoginActivity(getActivity(), null);

            return;
        }
    }
}

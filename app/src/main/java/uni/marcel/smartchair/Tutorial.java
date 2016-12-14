package uni.marcel.smartchair;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class Tutorial extends FragmentActivity {

    private static final int PAGES = 4;
    private ViewPager pager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        try {
            pager = (ViewPager) findViewById(R.id.pager);
            adapter = new TutorialPageAdapter(getSupportFragmentManager());
            pager.setAdapter(adapter);
        }
        catch (Exception ex) {
            Log.e("tutorial", "oncreate " + ex.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private class TutorialPageAdapter extends FragmentStatePagerAdapter {

        public TutorialPageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new TutorialFragmentHello();
                case 1: return new TutorialFragment1();
                case 2: return new TutorialFragment2();
                case 3: return new TutorialFragment3();
                default: return new TutorialFragmentHello();
            }
        }

        @Override
        public int getCount() {
            return PAGES;
        }
    }

}

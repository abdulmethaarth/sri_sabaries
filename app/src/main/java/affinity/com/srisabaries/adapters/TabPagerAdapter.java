package affinity.com.srisabaries.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import affinity.com.srisabaries.ui.fragments.HomeTabTrendingFragment;

/**
 * Created by akash on 15/6/16.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    final int PAGE_COUNT = 1;
    //    private String tabTitles[] = new String[]{"RECOMMENDED", "COUPONS", "TRENDING"};
    //private String tabTitles[] = new String[]{"RECOMMENDED", "TRENDING"};
    private String tabTitles[] = new String[]{"TRENDING"};

    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                HomeTabRecommendedFragment homeTabRecommendedFragment = new HomeTabRecommendedFragment();
//                return homeTabRecommendedFragment;
//            case 1:
//                HomeTabCouponsFragment homeTabCouponsFragment = new HomeTabCouponsFragment();
//                return homeTabCouponsFragment;
//            case 1:
//                HomeTabTrendingFragment homeTabTrendingFragment = new HomeTabTrendingFragment();
//                return homeTabTrendingFragment;
            case 0:
                HomeTabTrendingFragment homeTabTrendingFragment = new HomeTabTrendingFragment();
                return homeTabTrendingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

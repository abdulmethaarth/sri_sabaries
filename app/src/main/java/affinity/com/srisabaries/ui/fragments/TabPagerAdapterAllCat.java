package affinity.com.srisabaries.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapterAllCat extends FragmentStatePagerAdapter {
    public TabPagerAdapterAllCat(FragmentManager fm) {
        super(fm);
    }

    final int PAGE_COUNT = 1;
    //    private String tabTitles[] = new String[]{"RECOMMENDED", "COUPONS", "TRENDING"};
    //private String tabTitles[] = new String[]{"RECOMMENDED", "TRENDING"};
    private String tabTitles[] = new String[]{"CATEGORIES"};

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
                HomeAllCatFragment homeAllCatFragment = new HomeAllCatFragment();
                return homeAllCatFragment;
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

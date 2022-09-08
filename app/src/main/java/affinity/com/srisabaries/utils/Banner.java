package affinity.com.srisabaries.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import affinity.com.srisabaries.network.BaseResponse;

public class Banner extends BaseResponse {
    @SerializedName("banners")
    @Expose
    private ArrayList<BannerList> bannerLists;

    public ArrayList<BannerList> getBannerLists() {
        return bannerLists;
    }

    public void setBannerLists(ArrayList<BannerList> bannerLists) {
        this.bannerLists = bannerLists;
    }
}

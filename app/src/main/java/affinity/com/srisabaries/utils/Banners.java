package affinity.com.srisabaries.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import affinity.com.srisabaries.network.BaseResponse;

public class Banners extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<Banner> banners;

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }
}
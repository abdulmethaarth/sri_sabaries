package affinity.com.srisabaries.ui.fragments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import affinity.com.srisabaries.models.response.ResBase;

public class ResGetAllProductsDatas extends ResBase {
    @SerializedName("data")
    @Expose
    private ArrayList<ResAllProductsData> allProductData = new ArrayList<ResAllProductsData>();


    public ArrayList<ResAllProductsData> getAllProductData() {
        return allProductData;
    }

    public void setAllProductData(ArrayList<ResAllProductsData> allProductData) {
        this.allProductData = allProductData;
    }

}

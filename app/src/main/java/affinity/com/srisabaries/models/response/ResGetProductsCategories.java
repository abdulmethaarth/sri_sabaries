package affinity.com.srisabaries.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akash on 14/7/16.
 */
public class ResGetProductsCategories extends ResBase {
    @SerializedName("data")
    @Expose
    private ArrayList<ResProductsCategoryData> productCatData = new ArrayList<ResProductsCategoryData>();

    /**
     * @return The productCatData
     */
    public ArrayList<ResProductsCategoryData> getProductCatData() {
        return productCatData;
    }

    /**
     * @param productCatData The productCatData
     */
    public void setProductCatData(ArrayList<ResProductsCategoryData> productCatData) {
        this.productCatData = productCatData;
    }

}

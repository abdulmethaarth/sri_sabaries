package affinity.com.srisabaries.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Products extends BaseResponse{

    @SerializedName("productCatData")
    @Expose
    private ArrayList<ProductDetails> details = null;

    public ArrayList<ProductDetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ProductDetails> details) {
        this.details = details;
    }
}

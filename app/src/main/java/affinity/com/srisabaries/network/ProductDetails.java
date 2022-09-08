package affinity.com.srisabaries.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetails {
    @SerializedName("proCatID")
    @Expose
    private String proCatID;
    @SerializedName("proCatName")
    @Expose
    private String proCatName;
    @SerializedName("proCatImage")
    @Expose
    private String proCatImage;

    public String getProCatID() {
        return proCatID;
    }

    public void setProCatID(String proCatID) {
        this.proCatID = proCatID;
    }

    public String getProCatName() {
        return proCatName;
    }

    public void setProCatName(String proCatName) {
        this.proCatName = proCatName;
    }

    public String getProCatImage() {
        return proCatImage;
    }

    public void setProCatImage(String proCatImage) {
        this.proCatImage = proCatImage;
    }
}

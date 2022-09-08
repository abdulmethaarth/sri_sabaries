package affinity.com.srisabaries.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akash on 18/7/16.
 */
public class ReqProductDetail extends ReqBase {

    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("productID")
    @Expose
    private String productID;

    /**
     * @return The userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID The userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return The productID
     */
    public String getProductID() {
        return productID;
    }

    /**
     * @param productID The productID
     */
    public void setProductID(String productID) {
        this.productID = productID;
    }

}

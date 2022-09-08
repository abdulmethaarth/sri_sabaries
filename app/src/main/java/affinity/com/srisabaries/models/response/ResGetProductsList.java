package affinity.com.srisabaries.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash on 14/7/16.
 */
public class ResGetProductsList extends ResBase {
    @SerializedName("data")
    @Expose
    private List<ResProductsListData> catProductData = new ArrayList<>();
   /* @SerializedName("favCount")
    @Expose
    private String favCount;
*/

    public List<ResProductsListData> getCatProductData() {
        return catProductData;
    }

    public void setCatProductData(List<ResProductsListData> catProductData) {
        this.catProductData = catProductData;
    }
}

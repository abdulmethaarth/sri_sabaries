package affinity.com.srisabaries.ui.fragments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResAllProductsData {
    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("name")
    @Expose
    private String name;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

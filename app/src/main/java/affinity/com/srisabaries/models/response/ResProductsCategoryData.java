package affinity.com.srisabaries.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akash on 14/7/16.
 */
public class ResProductsCategoryData {
    @SerializedName("category_id")
    @Expose
    private String category_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("original_image")
    @Expose
    private String original_image;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_image() {
        return original_image;
    }

    public void setOriginal_image(String original_image) {
        this.original_image = original_image;
    }
}

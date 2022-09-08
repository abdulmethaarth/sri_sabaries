package affinity.com.srisabaries.network;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("errstr")
    public String errstr;

   @SerializedName("errorCode")
    public String errorCode;

    @SerializedName("success")
    public String success;

    public String getErrstr() {
        return errstr;
    }

    public void setErrstr(String errstr) {
        this.errstr = errstr;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}

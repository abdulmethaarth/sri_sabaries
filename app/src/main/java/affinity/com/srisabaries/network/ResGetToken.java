package affinity.com.srisabaries.network;

import com.google.gson.annotations.SerializedName;

public class ResGetToken extends BaseResponse {

    @SerializedName("data")
    private LoginTokenDetails loginTokenDetails ;

    public LoginTokenDetails getLoginTokenDetails() {
        return loginTokenDetails;
    }

    public void setLoginTokenDetails(LoginTokenDetails loginTokenDetails) {
        this.loginTokenDetails = loginTokenDetails;
    }

    public class LoginTokenDetails{

        @SerializedName("access_token")
        private String access_token;

         @SerializedName("token_type")
        private String token_type;

         @SerializedName("expires_in")
        private String expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }
    }
}

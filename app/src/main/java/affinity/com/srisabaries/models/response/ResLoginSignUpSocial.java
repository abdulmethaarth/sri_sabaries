package affinity.com.srisabaries.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 6/14/16.
 */
public class ResLoginSignUpSocial extends ResBase {
	@SerializedName("data")
	@Expose
	private ResUserData userData;

	/**
	 * @return The userData
	 */
	public ResUserData getUserData() {
		return userData;
	}

	/**
	 * @param userData The userData
	 */
	public void setUserData(ResUserData userData) {
		this.userData = userData;
	}

	public class ResUserData {
		@SerializedName("customer_id")
		@Expose
		private String customer_id;
		@SerializedName("firstname")
		@Expose
		private String firstname;
		@SerializedName("lastname")
		@Expose
		private String lastname;
		@SerializedName("customer_group_id")
		@Expose
		private String customer_group_id;
		@SerializedName("email")
		@Expose
		private String email;
		@SerializedName("telephone")
		@Expose
		private String telephone;
		/*@SerializedName("image")
		@Expose
		private String image;
		@SerializedName("dob")
		@Expose
		private String dob;
		@SerializedName("gender")
		@Expose
		private String gender;
		@SerializedName("registrationType")
		@Expose
		private String registrationType;
		@SerializedName("isEmailVerified")
		@Expose
		private String isEmailVerified;
		@SerializedName("service_key")
		@Expose
		private String serviceKey;
		@SerializedName("homeScreen")
		@Expose
		private String homeScreen;
		@SerializedName("notification")
		@Expose
		private String notification;
		@SerializedName("unit")
		@Expose
		private String unit;
		@SerializedName("totalPoints")
		@Expose
		private String totalPoints;
		@SerializedName("height")
		@Expose
		private String height;
		@SerializedName("weight")
		@Expose
		private String weight;
		@SerializedName("loginTime")
		@Expose
		private String loginTime;
		@SerializedName("distance")
		@Expose
		private String distance;
		@SerializedName("calories")
		@Expose
		private String calories;
		@SerializedName("isRunkeeperConnect")
		@Expose
		private String isRunkeeperConnect;
		@SerializedName("isFitbitConnect")
		@Expose
		private String isFitbitConnect;
		@SerializedName("shareCode")
		@Expose
		private String shareCode;
		@SerializedName("shareCodeUser")
		@Expose
		private String shareCodeUser;*/

		public String getCustomer_id() {
			return customer_id;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getCustomer_group_id() {
			return customer_group_id;
		}

		public void setCustomer_group_id(String customer_group_id) {
			this.customer_group_id = customer_group_id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
	}
}

package affinity.com.srisabaries.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.interfaces.IOnItemClickListener;
import affinity.com.srisabaries.models.response.ResFavouriteProductsData;
import affinity.com.srisabaries.ui.fragments.FavouriteFragment;

/**
 * Created by akash on 11/7/16.
 */
public class FavouriteProductsRecyclerAdapter extends RecyclerView.Adapter<FavouriteProductsRecyclerAdapter.ViewHolder> implements View.OnClickListener, Filterable {
	private static IOnItemClickListener onItemClickListener;
	public List<ResFavouriteProductsData> favouriteProductsData = new ArrayList<>();
	private List<ResFavouriteProductsData> completeFavouriteProductsData = new ArrayList<>();
	private Context mContext;
	private FavouriteFragment fragment;
	private Filter filter;

	public FavouriteProductsRecyclerAdapter(Context context, List<ResFavouriteProductsData> favouriteProductsData, Fragment fragment) {
		this.favouriteProductsData = favouriteProductsData;
		completeFavouriteProductsData = favouriteProductsData;
		this.mContext = context;
		this.fragment = (FavouriteFragment) fragment;
	}

	public static void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
		FavouriteProductsRecyclerAdapter.onItemClickListener = onItemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favorites, null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {

		ResFavouriteProductsData product = favouriteProductsData.get(position);

		holder.tvProductName.setText(product.getName());
		holder.tvProductPrice.setText("Rs. " + product.getPrice());
		holder.tvProductPrice.setPaintFlags(holder.tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		//holder.ivSetFavourite.setVisibility(View.VISIBLE);
		holder.ivShareProduct.setVisibility(View.VISIBLE);

		float discountedPrice = calculateDiscountPrice(Float.parseFloat(product.getPrice()), Float.parseFloat(product.getDiscount()));

		holder.tvDiscountedPrice.setText("Rs. " + String.valueOf(discountedPrice));

		if (!(product.getDiscount().equals("0") | product.getDiscount().equals("0"))) {
			holder.tvProductDiscount.setText(product.getDiscount() + "% off");
			holder.tvProductDiscount.setVisibility(View.VISIBLE);
			holder.tvProductPrice.setVisibility(View.VISIBLE);
		} else {
			holder.tvProductDiscount.setVisibility(View.GONE);
			holder.tvProductPrice.setVisibility(View.GONE);
		}
		//holder.tvProductDesc.setText(favouriteProductsData.get(position).getDescription());
	//	holder.ivCancelFavouriteProduct.setVisibility(View.VISIBLE);
		Picasso.with(mContext)
				.load(favouriteProductsData.get(position).getImage())
				.placeholder(R.drawable.no_image_icon)
				.into(holder.ivProductImage);
//        Glide.with(mContext)
//                .load(favouriteProductsData.get(position).getImage())
//                .placeholder(R.drawable.no_image_icon)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        holder.ivProductImage.setImageResource(R.drawable.no_image_available);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        return false;
//                    }
//                })
//                .into(holder.ivProductImage);
		//holder.ivCancelFavouriteProduct.setTag(R.id.tag_position, position);
		//holder.ivCancelFavouriteProduct.setOnClickListener(this);
		holder.itemView.setTag(favouriteProductsData.get(position));
	}

	@Override
	public int getItemCount() {
		return favouriteProductsData.size();
	}

	@Override
	public void onClick(View v) {
		final int position = (int) v.getTag(R.id.tag_position);
		ResFavouriteProductsData favouriteProduct = favouriteProductsData.get(position);
		switch (v.getId()) {
			/*case R.id.iv_cancel_favourite_product:
				fragment.showProgressDialog();
				IApiClient client = ApiClient.getApiClient();
				ReqUnFavouriteProduct reqUnFavouriteProduct = new ReqUnFavouriteProduct();
				reqUnFavouriteProduct.setProductID(favouriteProduct.getProductID());
				reqUnFavouriteProduct.setMethod(MethodFactory.UNFAVOURITE_PRODUCT.getMethod());
				reqUnFavouriteProduct.setUserID(AppSharedPreference.getInstance(mContext).getString(PreferenceKeys.KEY_USER_ID, ""));
				reqUnFavouriteProduct.setServiceKey(AppSharedPreference.getInstance(mContext).getString(PreferenceKeys.KEY_SERVICE_KEY, ""));
				Call<ResBase> resBaseCall = client.unFavouriteProduct(reqUnFavouriteProduct);
				resBaseCall.enqueue(new Callback<ResBase>() {
					@Override
					public void onResponse(Call<ResBase> call, Response<ResBase> response) {
						fragment.dismissProgressDialog();
						ResBase resBase = response.body();
						if (resBase != null) {
							if (resBase.getSuccess() == ServiceConstants.SUCCESS) {
								fragment.dismissProgressDialog();
								ToastUtils.showShortToast(mContext, mContext.getString(R.string.txt_success_unfavourite_product));
								favouriteProductsData.remove(position);
								fragment.notifyData();

							} else {
								ToastUtils.showShortToast(mContext, resBase.getErrstr());
							}
						} else {
							ToastUtils.showShortToast(mContext, R.string.err_network_connection);
						}
					}

					@Override
					public void onFailure(Call<ResBase> call, Throwable t) {
						ToastUtils.showShortToast(mContext, R.string.err_network_connection);
						fragment.dismissProgressDialog();
					}
				});
				break;*/
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new Filter() {
				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					favouriteProductsData = (ArrayList<ResFavouriteProductsData>) results.values;
					notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					List<ResFavouriteProductsData> nItems = new ArrayList<ResFavouriteProductsData>();
					if (constraint == null || constraint.length() == 0) {
						nItems = completeFavouriteProductsData;
					} else {
						for (ResFavouriteProductsData product : completeFavouriteProductsData) {
							if (product.getName().toUpperCase().contains(constraint.toString().toUpperCase())) {
								nItems.add(product);
							}
						}
					}
					results.values = nItems;
					results.count = nItems.size();
					return results;
				}
			};
		}
		return filter;
	}

	public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView ivProductImage;
		public TextView tvProductName;
		public TextView tvProductDesc;
	//	public ImageView ivCancelFavouriteProduct;
		public TextView tvProductPrice;
		public ImageView ivSetFavourite;
		public ImageView ivShareProduct;
		public TextView tvDiscountedPrice;
		public TextView tvProductDiscount;

		public ViewHolder(View itemView) {
			super(itemView);
			ivProductImage = (ImageView) itemView.findViewById(R.id.iv_favorite_product);
			tvProductDesc = (TextView) itemView.findViewById(R.id.tv_product_desc);
			tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
			tvProductPrice = (TextView) itemView.findViewById(R.id.tv_product_actual_price);
		//	ivCancelFavouriteProduct = (ImageView) itemView.findViewById(R.id.iv_cancel_favourite_product);
			ivSetFavourite = (ImageView) itemView.findViewById(R.id.iv_set_favourite_product);
			ivShareProduct = (ImageView) itemView.findViewById(R.id.iv_share_product);
			tvDiscountedPrice = (TextView) itemView.findViewById(R.id.tv_product_price);
			tvProductDiscount = (TextView) itemView.findViewById(R.id.tv_discounted_price);
			ivProductImage.setDrawingCacheEnabled(true);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			fragment.onItemClick(view, getAdapterPosition(), 0);
		}
	}

	public float calculateDiscountPrice(float actualPrice, float discount) {
		float discountValue = (actualPrice * discount) / 100;
		return actualPrice - discountValue;
	}
}

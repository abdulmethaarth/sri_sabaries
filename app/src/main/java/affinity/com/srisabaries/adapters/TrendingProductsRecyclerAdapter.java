package affinity.com.srisabaries.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
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
import affinity.com.srisabaries.models.response.ResTrendingProductsData;
import affinity.com.srisabaries.network.MethodFactory;
import affinity.com.srisabaries.network.ServiceConstants;
import affinity.com.srisabaries.ui.activities.TrendingActivity;

/**
 * Created by akash on 11/7/16.
 */
public class TrendingProductsRecyclerAdapter extends RecyclerView.Adapter<TrendingProductsRecyclerAdapter.ViewHolder> implements View.OnClickListener, Filterable {
	private static IOnItemClickListener onItemClickListener;
	private List<ResTrendingProductsData> resTrendingProductsData = new ArrayList<>();
	private List<ResTrendingProductsData> completeResTrendingProductsData = new ArrayList<>();
	private TrendingActivity mContext;
	private Filter mFilter;

	public TrendingProductsRecyclerAdapter(List<ResTrendingProductsData> resTrendingProductsData) {
		this.resTrendingProductsData = resTrendingProductsData;
		completeResTrendingProductsData = resTrendingProductsData;
		this.mContext = (TrendingActivity) mContext;
	}

	public static void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
		TrendingProductsRecyclerAdapter.onItemClickListener = onItemClickListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favorites, null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		ResTrendingProductsData product = resTrendingProductsData.get(position);

		holder.tvProductPrice.setText("Rs. " + resTrendingProductsData.get(position).getPrice());
	//	holder.tvProductPrice.setPaintFlags(holder.tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvProductName.setText(resTrendingProductsData.get(position).getName());
		holder.ivShareProduct.setVisibility(View.VISIBLE);
		holder.ivSetFavourite.setVisibility(View.VISIBLE);

		/*float discountedPrice = calculateDiscountPrice(Float.parseFloat(product.getPrice()), Float.parseFloat(product.getDiscount()));

		holder.tvDiscountedPrice.setText("Rs. " + String.valueOf(discountedPrice));

		if (!(product.getDiscount().equals("0") | product.getDiscount().equals("0"))) {
			holder.tvProductDiscount.setText(product.getDiscount() + "% off");
			holder.tvProductDiscount.setVisibility(View.VISIBLE);
			holder.tvProductPrice.setVisibility(View.VISIBLE);
		} else {
			holder.tvProductDiscount.setVisibility(View.GONE);
			holder.tvProductPrice.setVisibility(View.GONE);
		}*/
//        Glide.with(mContext)
//                .load(resTrendingProductsData.get(position).getImage())
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
		Picasso.with(mContext)
				.load(resTrendingProductsData.get(position).getThumb())
				.placeholder(R.drawable.no_image_icon)
				.into(holder.ivProductImage);
		holder.ivCancelFavouriteProduct.setOnClickListener(holder);
		/*if (resTrendingProductsData.get(position).getFavourite() == ServiceConstants.FAVOURITE) {
			holder.ivSetFavourite.setImageResource(R.drawable.heart);
		} else {
			holder.ivSetFavourite.setImageResource(R.drawable.empty_heart);
		}*/
		holder.ivSetFavourite.setTag(R.id.tag_position, position);
		holder.ivSetFavourite.setOnClickListener(this);
		holder.ivShareProduct.setTag(holder);
		holder.ivShareProduct.setOnClickListener(this);
	}

	@Override
	public int getItemCount() {
		return resTrendingProductsData.size();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_set_favourite_product:
				int position = (int) v.getTag(R.id.tag_position);
				ResTrendingProductsData product = resTrendingProductsData.get(position);
			/*	if (product.getFavourite() == ServiceConstants.FAVOURITE)
					mContext.setFavouriteUnFavourite(position, MethodFactory.UNFAVOURITE_PRODUCT.getMethod());
				else
					mContext.setFavouriteUnFavourite(position, MethodFactory.FAVOURITE_PRODUCT.getMethod());*/
				break;
			case R.id.iv_share_product:
				ViewHolder holder = (ViewHolder) v.getTag();
				mContext.shareProduct(holder.ivProductImage.getDrawingCache(), holder.tvProductName.getText().toString());
				break;

		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new Filter() {
				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					resTrendingProductsData = (ArrayList<ResTrendingProductsData>) results.values;
					notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					List<ResTrendingProductsData> nItems = new ArrayList<ResTrendingProductsData>();
					if (constraint == null || constraint.length() == 0) {
						nItems = completeResTrendingProductsData;
					} else {
						for (ResTrendingProductsData product : completeResTrendingProductsData) {
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
		return mFilter;
	}

	public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView ivProductImage;
		public TextView tvProductName;
		public TextView tvProductDesc;
		public ImageView ivCancelFavouriteProduct;
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
			ivCancelFavouriteProduct = (ImageView) itemView.findViewById(R.id.iv_cancel_favourite_product);
			ivSetFavourite = (ImageView) itemView.findViewById(R.id.iv_set_favourite_product);
			ivShareProduct = (ImageView) itemView.findViewById(R.id.iv_share_product);
			tvDiscountedPrice = (TextView) itemView.findViewById(R.id.tv_product_price);
			tvProductDiscount = (TextView) itemView.findViewById(R.id.tv_discounted_price);
			ivProductImage.setDrawingCacheEnabled(true);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			onItemClickListener.onItemClick(v, getAdapterPosition(), 0);
		}
	}

	public float calculateDiscountPrice(float actualPrice, float discount) {
		float discountValue = (actualPrice * discount) / 100;
		return actualPrice - discountValue;
	}
}

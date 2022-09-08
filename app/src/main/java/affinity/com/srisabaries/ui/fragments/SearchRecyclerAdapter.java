package affinity.com.srisabaries.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.ui.activities.FsStoreProductDetail;
import affinity.com.srisabaries.utils.Logger;

class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> implements Filterable {
    List<ResAllProductsData> productsCategoryData;
    List<ResAllProductsData> completeProductsCategoryData;
    private Context context;
    private Filter filter;

    public SearchRecyclerAdapter(Context context, List<ResAllProductsData> resProductsCategoryData) {
        this.context = context;
        this.productsCategoryData = resProductsCategoryData;
        completeProductsCategoryData = resProductsCategoryData;
    }

    @Override
    public SearchRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_recycler_view_items, null);
        return new SearchRecyclerAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(SearchRecyclerAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(productsCategoryData.get(position).getImage())
                .placeholder(R.drawable.no_image_available)
                .into(holder.ivRecommended);
        if (productsCategoryData.get(position).getImage().equals("")) {
            holder.overLayView.setVisibility(View.VISIBLE);
        } else {
            holder.overLayView.setVisibility(View.GONE);
        }
        holder.rlRecommended.setOnClickListener(holder);
        holder.tvRecommended.setText(productsCategoryData.get(position).getName().toString());

    }

    @Override
    public int getItemCount() {
        return productsCategoryData.size();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    productsCategoryData = (ArrayList<ResAllProductsData>) results.values;
                    Logger.e("LIST SIZE -----> " + productsCategoryData.size());
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<ResAllProductsData> nItems = new ArrayList<ResAllProductsData>();
                    if (constraint == null || constraint.length() == 0) {
                        nItems = completeProductsCategoryData;
                    } else {
                        for (ResAllProductsData productCategory : completeProductsCategoryData) {
                            if (productCategory.getName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                                nItems.add(productCategory);
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
        public TextView tvRecommended;
        public ImageView ivRecommended;
        public RelativeLayout rlRecommended;
        public View overLayView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRecommended = (TextView) itemView.findViewById(R.id.tv_recommended);
            ivRecommended = (ImageView) itemView.findViewById(R.id.iv_recommended);
            rlRecommended = (RelativeLayout) itemView.findViewById(R.id.rl_recommended);
            overLayView = itemView.findViewById(R.id.iv_recommended_overlay);
        }

        @Override
        public void onClick(View v) {
            String catId = productsCategoryData.get(this.getLayoutPosition()).getProduct_id();
            String catName = productsCategoryData.get(this.getLayoutPosition()).getName();
            Intent intent = new Intent(context, FsStoreProductDetail.class);
            intent.putExtra("productId", catId);
            intent.putExtra("productName", catName);
            context.startActivity(intent);
        }
/*
        Intent intent = new Intent(this, FsStoreProductDetail.class);
        intent.putExtra("productId", mProductsListData.get(position).getProductID());
        intent.putExtra("productName", mProductsListData.get(position).getName());
        intent.putExtra("productPosition", position);
        startActivityForResult(intent, AppConstants.RC_PRODUCTS_DETAIL);*/
    }
}

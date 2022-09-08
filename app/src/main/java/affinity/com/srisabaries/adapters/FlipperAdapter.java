package affinity.com.srisabaries.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.utils.Banner;
import affinity.com.srisabaries.utils.BannerList;

public class FlipperAdapter extends BaseAdapter {

    private Context mCtx;
    private ArrayList<BannerList> banners;


    public FlipperAdapter(Context mCtx, ArrayList<BannerList> banners){
        this.mCtx = mCtx;
        this.banners = banners;
    }
    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BannerList banner = banners.get(position);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.flipper_items, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(mCtx).load(banner.getImage()).into(imageView);
        return view;
    }
}

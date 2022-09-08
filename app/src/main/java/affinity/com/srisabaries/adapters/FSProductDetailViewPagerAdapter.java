package affinity.com.srisabaries.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.interfaces.IOnItemClickListener;
import affinity.com.srisabaries.ui.activities.ImageZoomActivity;

/**
 * Created by akash on 2/9/16.
 */
public class FSProductDetailViewPagerAdapter extends PagerAdapter implements View.OnClickListener {
	ArrayList<String> arrayList;
	Context context;
	private static IOnItemClickListener onItemClickListener;
	ImageView mImageView;

	public static void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
		FSProductDetailViewPagerAdapter.onItemClickListener = onItemClickListener;
	}

	public FSProductDetailViewPagerAdapter(Context context, ArrayList<String> imageList) {
		this.arrayList = imageList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return (arrayList.size() > 0 ? arrayList.size() : 0);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}


	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.fs_store_view_pager_fragment_layout, container, false);
		mImageView = (ImageView) view.findViewById(R.id.iv_view_pager_products_image);

		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ImageZoomActivity.class);
				intent.putExtra("image", arrayList.get(position));
				context.startActivity(intent);
			}
		});

		Picasso.with(context)
				.load(arrayList.get(position))
				.placeholder(R.drawable.no_image_available)
				.into(mImageView);
		container.addView(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                DialogFragment dialogFragment = new DialogFragment();
////                FragmentTransaction ft =((FsStoreProductDetail) context).getSupportFragmentManager().beginTransaction();
////                Fragment prev = ((FsStoreProductDetail) context).getSupportFragmentManager().findFragmentByTag("dialog");
////                if (prev != null) {
////                    ft.remove(prev);
////                }
////                ft.addToBackStack(null);
////
////                // Create and show the dialog.
////                DialogFragment newFragment = ViewPagerDialog.newInstance();
////                newFragment.show(ft, "dialog");
//
//                ViewPagerDialog newFragment = new ViewPagerDialog();
//                FragmentTransaction ft =((FsStoreProductDetail) context).getSupportFragmentManager().beginTransaction();
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.add(android.R.id.content, newFragment)
//                        .addToBackStack(null).commit();
//
//
//
//
//            }
//        });
		view.setOnClickListener(this);
		view.setTag(position);
		return view;
	}


	@Override
	public void onClick(View v) {
		int position = (int) v.getTag();
		onItemClickListener.onItemClick(v, position, 0);

	}
}
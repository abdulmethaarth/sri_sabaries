package affinity.com.srisabaries.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.interfaces.AdapterCallback;
import affinity.com.srisabaries.preference.AppSharedPreference;
import affinity.com.srisabaries.preference.PreferenceKeys;

/**
 * Created by root on 6/16/16.
 */
public class CartSizeQuantityAdapter extends ArrayAdapter<String> {
    //list of client data
    private List<String> mDistanceUnitList;
    private LayoutInflater mInflater;
    private int mCheckedPos;
    private AdapterCallback callback;
    private AppSharedPreference mAppSharedPreference;

    public CartSizeQuantityAdapter(Activity activity, int textViewResourceId, List<String> distanceUnitList,AdapterCallback callback) {
        super(activity, textViewResourceId, distanceUnitList);
        this.callback = callback;
        mDistanceUnitList = distanceUnitList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAppSharedPreference = AppSharedPreference.getInstance(activity);
    }

    public int getCheckedPos() {
        return mCheckedPos;
    }

    public void setCheckedPos(int mCheckedPos) {
        this.mCheckedPos = mCheckedPos;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    public View getCustomView(final int position, View convertView, final ViewGroup parent, boolean isShowRadioBtn) {
        View v = convertView;
        Holder holder = null;
        if (v == null) {
            v = mInflater.inflate(R.layout.row_spinner_distance, null);
            holder = new Holder(v);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        holder.tvDistanceUnit.setText(mDistanceUnitList.get(position));
        if (isShowRadioBtn) {
            if (mCheckedPos == position) {
                holder.rbDistanceUnit.setChecked(true);
            } else {
                holder.rbDistanceUnit.setChecked(false);
            }
            holder.rbDistanceUnit.setVisibility(View.VISIBLE);
        } else {
            holder.rbDistanceUnit.setVisibility(View.GONE);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(position);
                onItemClick(position);
            }
        });
        return v;
    }

    private void onItemClick(int position) {
        if (position != mCheckedPos) {
            mCheckedPos = position;
            mAppSharedPreference.setString(PreferenceKeys.KEY_DISTANCE_UNIT, mDistanceUnitList.get(position));
            mAppSharedPreference.setInt(PreferenceKeys.KEY_DISTANCE_UNIT_INDEX, position);
            Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    }

    private static class Holder {
        private RadioButton rbDistanceUnit;
        private TextView tvDistanceUnit;

        public Holder(View view) {
            tvDistanceUnit = (TextView) view.findViewById(R.id.tv_distance_unit);
            rbDistanceUnit = (RadioButton) view.findViewById(R.id.rb_distance);
        }
    }
}
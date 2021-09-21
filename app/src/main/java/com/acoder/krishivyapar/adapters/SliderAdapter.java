package com.acoder.krishivyapar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.acoder.krishivyapar.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> list;
    private boolean zoomable, openActivity;

    public SliderAdapter(Activity context, ArrayList<String> list, boolean zoomable, boolean openActivity) {
        this.activity = context;
        this.list = list;
        this.zoomable = zoomable;
        this.openActivity = openActivity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        Log.d("the11amshopping", "Notify Data change ");
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.product_slide_full_layout, null);
        if (openActivity) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(activity, FullImageActivity.class);
//                    intent.putExtra("images", arrayToString(list));
//                    intent.putExtra("pos", position);
//                    activity.startActivity(intent);
                }
            });
        }
        ImageView image = view.findViewById(R.id.thumb);
//        image.setZoomable(zoomable);
        Glide.with(activity).load(list.get(position))
//                .placeholder(R.drawable.loading)
                .error(R.color.colorOutline)
                .into(image);


        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        container.addView(view);
        getItemPosition(view);
        return view;
    }

    private String[] arrayToString(ArrayList<String> arrayList) {
        String[] strArr = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            String string = arrayList.get(i);
            strArr[i] = string;
        }
        return strArr;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }
}

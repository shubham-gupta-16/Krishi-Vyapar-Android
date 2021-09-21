package com.acoder.krishivyapar.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.acoder.krishivyapar.R;
import com.shubhamgupta16.materialkit.UtilsKit;

public class IndicatorS extends RadioGroup {

    private Context context;
    private ViewPager pager;
    private final int autoScrollDuration = 4000;
    boolean autoScroll;
    private Handler handler = new Handler();
    private int counter = 0, size = 0;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (autoScroll) {
                counter++;
                if (counter == size) {
                    counter = 0;
                }
                changeIndicator(counter);
                pager.setCurrentItem(counter, true);
                handler.postDelayed(runnable, autoScrollDuration);
            }
        }

    };

    public IndicatorS(Context context) {
        super(context);
        preSetup(context);
    }

    public IndicatorS(Context context, AttributeSet attrs) {
        super(context, attrs);
        preSetup(context);
    }

    private void preSetup(Context context){
        this.context = context;
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
    }

    public void applyIndicator(ViewPager viewPager, final PagerAdapter adapter, boolean autoScroll){
        this.autoScroll = autoScroll;
        pager = viewPager;
        size = adapter.getCount();
        initializeIndicator(size, (int) UtilsKit.dpToPx(18f, context), 0, R.drawable.indicator_drawable);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                size = adapter.getCount();
                update(size);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                counter = position;
                changeIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void update(int size) {
        removeAllViews();
        initializeIndicator(size,  (int) UtilsKit.dpToPx(18f, context), 0, R.drawable.indicator_drawable);
        if (size != 0) {
            changeIndicator(0);
        }

    }

    private void initializeIndicator(int size, int indicatorSize, int indicatorDistance, int indicatorDrawable) {
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    RadioButton indicatorButton = new RadioButton(context);
                    indicatorButton.setClickable(true);
                    LayoutParams params = new LayoutParams(indicatorSize, indicatorSize);
                    params.setMargins(indicatorDistance, 0, indicatorDistance, 0);
                    indicatorButton.setLayoutParams(params);
                    indicatorButton.setBackgroundResource(indicatorDrawable);
                    indicatorButton.setButtonDrawable(null);
                    if (size == 1){
                        indicatorButton.setAlpha(0f);
                    }
                    addView(indicatorButton);
                }

                changeIndicator(0);
            }
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                int position = radioGroup.indexOfChild(radioButton);
                pager.setCurrentItem(position, true);
            }
        });
    }

    private void changeIndicator(int i) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, autoScrollDuration);
        ((RadioButton) getChildAt(i)).setChecked(true);

    }

    public void autoSliderResume() {
        if (autoScroll) {
            handler.postDelayed(runnable, autoScrollDuration);
        }
    }

    public void autoSliderPause() {
        if (autoScroll) {
            handler.removeCallbacks(runnable);
        }
    }


}

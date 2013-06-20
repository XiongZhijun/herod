/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.worker.phone.view;

import org.herod.framework.widget.TabPageIndicator;
import org.herod.worker.phone.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class OrderTabPageIndicator extends TabPageIndicator {

	public OrderTabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OrderTabPageIndicator(Context context) {
		super(context);
	}

	protected View createTabView(int index, CharSequence text, int iconResId) {
		final View tabView = new TabView(getContext());
		tabView.setFocusable(true);
		((TextView) tabView.findViewById(R.id.text)).setText(text);

		return tabView;
	}

	protected class TabView extends RelativeLayout implements Indexable {
		protected int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
			inflate(context, R.layout.activity_order_list_tab_item, this);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}

		@Override
		public void setIndex(int index) {
			this.mIndex = index;
		}
	}
}

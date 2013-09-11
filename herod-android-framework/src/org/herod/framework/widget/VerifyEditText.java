/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.widget;

import org.herod.framework.R;
import org.herod.framework.utils.StringUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class VerifyEditText extends EditText {
	private String emptyErrorMessage;
	private boolean canEmpty = true;

	public VerifyEditText(Context context) {
		super(context);
		init(context, null, 0);
	}

	public VerifyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public VerifyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		initAttributes(context, attrs, defStyle);
		addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtils.isNotBlank(s.toString())) {
					setError(null);
				}
			}
		});
	}

	private void initAttributes(Context context, AttributeSet attrs,
			int defStyle) {
		if (attrs == null) {
			return;
		}
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.VerifyEditText, defStyle, 0);
		this.emptyErrorMessage = a
				.getString(R.styleable.VerifyEditText_emptyErrorMessage);
		this.canEmpty = a.getBoolean(R.styleable.VerifyEditText_canEmpty, true);
		a.recycle();
	}

	public void setEmptyErrorMessage(String emptyErrorMessage) {
		this.emptyErrorMessage = emptyErrorMessage;
	}

	public void setCanEmpty(boolean canEmpty) {
		this.canEmpty = canEmpty;
	}

	public boolean isInvalid() {
		return !isValid();
	}

	public boolean isValid() {
		if (canEmpty) {
			return true;
		}
		String text = getText().toString();
		if (StringUtils.isBlank(text)) {
			setError(emptyErrorMessage);
			return false;
		}
		return true;
	}

}

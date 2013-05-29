/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package com.nostra13.universalimageloader.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public abstract class ImageLoaderUtils {

	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions defaultOptions;
	private static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).discCacheSize(10 * 1000 * 1000).build();
		initImageLoader(context, configuration);
	}

	public static void initImageLoader(Context context,
			ImageLoaderConfiguration configuration) {
		imageLoader.init(configuration);
		defaultOptions = buildDefaultDisplayOptions();
	}

	public static DisplayImageOptions buildDefaultDisplayOptions() {
		return new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.build();
	}

	public static void loadImage(ImageView imageView, String url) {
		loadImage(imageView, url, defaultOptions);
	}

	public static void loadImage(ImageView imageView, String url,
			DisplayImageOptions options) {
		imageLoader.displayImage(url, imageView, options, animateFirstListener);
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}

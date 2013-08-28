/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone.event;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class MediaPlayerService extends Service implements
		OnCompletionListener, OnPreparedListener {

	public static final String MEDIA_PATH = "mediaPath";
	public static final String MEDIA_TYPE = "mediaType";
	public static final String DEFAULT_TYPE = "local";
	private static final String TAG = MediaPlayerService.class.getSimpleName();
	public MediaPlayer mMediaPlayer = new MediaPlayer();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return 0;
		}
		String path = intent.getStringExtra(MEDIA_PATH);
		String type = intent == null ? "" : intent.getStringExtra(MEDIA_TYPE);
		try {
			initMediaPlayer(path, type);
		} catch (IOException e) {
			Log.w(TAG,
					"MediaPlayer set dataSource path failed, path : " + path, e);
		}
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.prepareAsync();
		return super.onStartCommand(intent, flags, startId);
	}

	private void initMediaPlayer(String path, String type) throws IOException {
		if (DEFAULT_TYPE.equals(type)) {
			AssetManager am = getAssets();
			AssetFileDescriptor afd = am.openFd(path);
			FileDescriptor fd = afd.getFileDescriptor();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setDataSource(fd, afd.getStartOffset(),
					afd.getLength());
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mp != null)
			mp.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mp != null)
			mp.release();
	}

	public static void playMedia(Context context, String mediaPath) {
		playMedia(context, mediaPath, DEFAULT_TYPE);
	}

	public static void playMedia(Context context, String mediaPath,
			String mediaType) {
		Intent intent = new Intent(context, MediaPlayerService.class);
		intent.putExtra(MEDIA_PATH, mediaPath);
		intent.putExtra(MEDIA_TYPE, mediaType);
		context.startService(intent);
	}

}

package com.arandroid.risultatilive;

import com.ads.core.BannerManager;

import android.app.Activity;
import android.widget.LinearLayout;

public abstract class BannerActivity extends Activity{
	protected LinearLayout containerLayout;
	protected BannerManager manager;
	
	public void setupBanners() {
		manager.setContainerLayout(containerLayout);
		manager.setContext(this);
		manager.load();
	}

}

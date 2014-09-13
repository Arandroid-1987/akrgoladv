package com.arandroid.risultatilive.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.ads.core.BannerView;
import com.arandroid.risultatilive.BannerActivity;
import com.arandroid.risultatilive.R;
import com.arandroid.risultatilive.core.GlobalState;

public class BannerReader extends AsyncTask<Void, Void, List<BannerView>> {
	private BannerActivity context;
	private GlobalState gs;

	public BannerReader(BannerActivity context) {
		this.context = context;
		gs = (GlobalState) context.getApplication();
	}

	private List<BannerView> read(Context context) {
		List<BannerView> ris = new LinkedList<BannerView>();
		String feed = "http://www.arandroid.altervista.org/bannerGOL.txt";
		try {
			URL url = new URL(feed);
			InputStream in = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				String imgSrc = st.nextToken();
				Bitmap b = null;
				while (b == null) {
					InputStream is = new URL(imgSrc).openStream();
					b = BitmapFactory.decodeStream(is);
				}
				ImageView view = new ImageView(context);
				view.setImageBitmap(b);
				BannerView bv;
				if (st.countTokens() == 0) {
					bv = new BannerView(view, null);
				} else {
					String link = st.nextToken();
					bv = new BannerView(view, link);
				}
				ris.add(bv);
				line = br.readLine();
			}
			if(ris.isEmpty()){
				addDefaultBanner(ris);
			}
			gs.setBannerDownloaded(true);
		} catch (Exception e) {
			e.printStackTrace();
			ris.clear();
			addDefaultBanner(ris);
			return ris;
		}
		return ris;
	}
	
	private void addDefaultBanner(List<BannerView> ris){
		ImageView view = new ImageView(context);
		Bitmap b = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bannerstatico);
		view.setImageBitmap(b);
		BannerView bv = new BannerView(view, "http://www.arandroid.altervista.org");
		ris.add(bv);
		gs.setBannerDownloaded(false);
	}

	@Override
	protected List<BannerView> doInBackground(Void... params) {
		return read(context);
	}

	@Override
	protected void onPostExecute(List<BannerView> result) {
		gs.storeBanners(result);
		context.setupBanners();
	}

}

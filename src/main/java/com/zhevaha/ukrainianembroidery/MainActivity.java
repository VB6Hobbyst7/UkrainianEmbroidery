package com.zhevaha.ukrainianembroidery;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends Activity implements OnClickListener {

	public static Activity activity;
	private Gallery gallery;
	public static Library library;
	ImageFromWeb imageFromWeb;

	public static int width, height, setPosition;
	public static String cacheFolder, rootFolder, imagesFolder;

	public static String[] imagesInWeb;
	public static File[] listFile;

	public static LinearLayout layout;
	public static Button btnSet;
	
	public static boolean btnSetEnable = false;
	public static boolean itemClick = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		universalImageLoaderSetup();
		activity = this;  
		getScreenSize();

		library = new Library();
		imagesInWeb = getResources().getStringArray(R.array.imagesInWeb);
		Log.d(library.LOG, imagesInWeb[0]);
		rootFolder = Environment.getExternalStorageDirectory()
				+ library.separator + getString(R.string.ukrainian_embroidery);
		getDirectory(rootFolder);
		cacheFolder = rootFolder + library.separator
				+ getString(R.string.cache);

		imagesFolder = rootFolder + library.separator
				+ getString(R.string.images);
		layout = (LinearLayout) findViewById(R.id.layout);

		btnSet = (Button) findViewById(R.id.button1);
		btnSet.setOnClickListener(this);

		gallery = (Gallery) findViewById(R.id.gallery1);
		gallery.setSpacing(10);
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				new Thread(new Runnable() {

					@Override
					public void run() {
//						Log.d(library.LOG, "run Gallery");
						getDirectory(cacheFolder);
						gallery.setAdapter(new ImageAdapter(
								this));
						
					}
				}).start();
			}
		});
		
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				itemClick = true;
				if(!btnSetEnable){
					setPosition = position;
					getDirectory(imagesFolder);
				
				}
			}
			
			
		});
	}
	public Bitmap setTheImageInTheCenter(Bitmap bitmapLL, Bitmap bitOk) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		if (width > height) {
			bitmapLL = Bitmap.createBitmap(bitOk, 0, (width - height) / 2, width,
					height);
		}
		if (width < height) {
			bitmapLL = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmapLL);
			Rect rectSrc = new Rect((bitOk.getWidth() - width) / 2, 0,
					bitOk.getWidth() - (bitOk.getWidth() - width) / 2,
					height);
			Rect rectDst = new Rect(0, 0, width, height);
			canvas.drawBitmap(bitOk, rectSrc, rectDst, paint);
		}
		return bitmapLL;
	}
	
	private Bitmap bitmapFromWeb(int numPosition) {
//		Log.d(library.LOG, "\nnumPosition =  "+numPosition);
		String url = imagesInWeb[numPosition].toString();
//		Log.d(library.LOG, "\nurl =  "+url);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				.build();
		File file = imageLoader.getDiskCache().get(url);
//		Log.d(library.LOG, "\nboot from web ");
//		 Log.d(library.LOG, "\nfile = "+file.toString());
		Bitmap bitOk = BitmapFactory.decodeFile(file.getName());
		
		return bitOk;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		btnSetEnable = true;
	}

	protected void getScreenSize() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	}

	static void getDirectory(String string) {
		File isDirExist = new File(string);
		if (!isDirExist.exists()) {

			isDirExist.mkdir();
		}
	}
	
	private void universalImageLoaderSetup() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(defaultOptions).memoryCache(new WeakMemoryCache()).build();

		ImageLoader.getInstance().init(config);
	}

}

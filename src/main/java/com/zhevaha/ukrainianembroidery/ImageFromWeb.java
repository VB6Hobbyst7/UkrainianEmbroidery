package com.zhevaha.ukrainianembroidery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageFromWeb extends Activity {
	
	Context context;
	MainActivity activity;
	Picasso mPicasso;
	Library library;
	File downLoadFile;
	ImageView imageFrom;

	String folder;
	String imageName;
	Bitmap bitmap;
	
	
	public ImageFromWeb(MainActivity activity, String folder) {
		this.context = activity;
		this.folder = folder;
		imageFrom = new ImageView(context);
		library = new Library();
		mPicasso = Picasso.with(context);
		bitmap = null;
		
		for (int i = 0; i < MainActivity.imagesInWeb.length; i++) {
			imageName = MainActivity.imagesInWeb[i].substring(library.spotter);
			downLoadFile = new File(folder + library.separator + imageName);
			if(folder.equals(MainActivity.cacheFolder)){
				if (!downLoadFile.exists()){
					mPicasso.load(imageName)
					.resize(MainActivity.width / library.sizeRatio,
							MainActivity.height / library.sizeRatio).centerInside()
					.into(imageFrom);
				}
			}else{
				
				if(MainActivity.btnSetEnable){
					if(!downLoadFile.exists()){
						mPicasso.load(imageName)
						.resize(MainActivity.width,
								MainActivity.height).centerInside()
						.into(target);
					}						
					setWallpaper(downLoadFile);
				}else{
					if(!downLoadFile.exists()){
						mPicasso.load(imageName)
						.resize(MainActivity.width,
								MainActivity.height).centerInside()
						.into(target);
					}
					bitmap = BitmapFactory
							.decodeFile(downLoadFile.toString());
					Drawable d = new BitmapDrawable(getResources(), bitmap);
					MainActivity.layout.setBackgroundDrawable(d);
					
				}
				
			}
			
		}

	}
	
	public void setWallpaper(File downLoadFile) {
		// TODO Auto-generated method stub
		Bitmap bg = null;
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Bitmap bitmap = BitmapFactory.decodeFile(downLoadFile.toString());
		
		Log.d(library.LOG, "\nbitmap size = "+bitmap.getWidth() +" x "+bitmap.getHeight());
		if (MainActivity.width > MainActivity.height) {
			bg = Bitmap.createBitmap(MainActivity.width,MainActivity.width, Bitmap.Config.RGB_565);
		} else {
			bg = Bitmap.createBitmap(MainActivity.height, MainActivity.height, Bitmap.Config.RGB_565);
		}

		Log.d(library.LOG, "\nbg size = "+bg.getWidth() +" x "+bg.getHeight());
		
		Canvas canvas = new Canvas(bg);
		canvas.drawBitmap(bitmap, 0, 0, paint);

		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vib.vibrate(new long[] { 0, 300, 100, 300 }, -9);

		WallpaperManager myWallpaperManager = WallpaperManager
				.getInstance(getApplicationContext());

		try {
			myWallpaperManager.setBitmap(bitmap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), "Installed new wallpaper",
				Toast.LENGTH_LONG).show();

		MainActivity.btnSetEnable = false;
		this.finish();
	}

	Target target = new Target() {		
		 @Override
		 public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
		 new Thread(new Runnable() {
		 @Override
		 public void run() {

		File file = new File(folder + library.separator + imageName.substring(library.spotter));
		 try 
		 {
		 file.createNewFile();
		 FileOutputStream ostream = new FileOutputStream(file);
		 bitmap.compress(CompressFormat.PNG, 75, ostream);
		 ostream.close();
		 } 
		 catch (Exception e) 
		 {
		 e.printStackTrace();
		 }

		 }
		 }).start();
		 }
		 @Override
		 public void onBitmapFailed(Drawable errorDrawable) {
		 }
		 
		 @Override
		 public void onPrepareLoad(Drawable placeHolderDrawable) {
		 if (placeHolderDrawable != null) {
		 }
		 }
		 };

		 
}

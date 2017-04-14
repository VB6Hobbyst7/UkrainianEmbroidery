package com.zhevaha.ukrainianembroidery;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageAdapter extends BaseAdapter {
	
	 Context context;
	 Picasso mPicasso;
	Library library;
	public static ImageView image;
	ImageFromWeb  imageFromWeb;
	public static int position;
	
	String folder;
	MainActivity activity;
	String imageName;

	public ImageAdapter(Runnable runnable) {
		library = new Library();
		mPicasso = Picasso.with(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.imagesInWeb.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MainActivity.imagesInWeb[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int index, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		image = new ImageView(context);
		imageFromWeb = new ImageFromWeb(activity, MainActivity.cacheFolder);
		imageName = MainActivity.imagesInWeb[index];
		position = index;
//		Log.d(library.LOG, "\nposition = "+position);
		
		File file = new File(MainActivity.cacheFolder+library.separator+MainActivity.imagesInWeb[index].substring(library.spotter));
		if(file.exists()){
			
			mPicasso.load( file)
			.resize(MainActivity.width/library.sizeRatio, MainActivity.height/library.sizeRatio).centerInside()
			.into(image);
//			Log.d(library.LOG, "\nstart from Sd ");
		}else{
			Log.d(library.LOG, "\nstart from web ");
			mPicasso.load(imageName)
			.resize(MainActivity.width/library.sizeRatio, MainActivity.height/library.sizeRatio).centerInside()
			.into(image);
	}
		
		return imageFromWeb.imageFrom;
	}
	
	Target target = new Target() {		
		 @Override
		 public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
		 new Thread(new Runnable() {
		 @Override
		 public void run() {           
		File file = new File(MainActivity.cacheFolder + library.separator +  MainActivity.imagesInWeb[position].substring(library.spotter));
		 try 
		 { 
		 file.createNewFile();
		 FileOutputStream ostream = new FileOutputStream(file);
		 bitmap.compress(CompressFormat.PNG, 75, ostream);
//		 Log.d(library.LOG, "\nbitmap Height = "+bitmap.getHeight());
		 
		 
		  	if (ostream != null)
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

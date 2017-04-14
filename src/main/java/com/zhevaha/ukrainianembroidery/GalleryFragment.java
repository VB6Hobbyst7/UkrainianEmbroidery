package com.zhevaha.ukrainianembroidery;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GalleryFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootLayout = inflater
				.inflate(R.layout.fragment_gallery, container);
		return rootLayout;
	}
}

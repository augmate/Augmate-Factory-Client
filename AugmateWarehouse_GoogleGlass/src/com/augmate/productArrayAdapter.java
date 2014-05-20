package com.augmate;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class productArrayAdapter extends ArrayAdapter<orderProducts> {
	
	private final Context context;
	private final ArrayList<orderProducts> products;

	public productArrayAdapter(Context context,ArrayList<orderProducts> objects) {
		super(context, R.layout.order_list_row, objects);		
		this.context= context;
		this.products= objects;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.order_list_row, parent, false);
		
		TextView textOrderNum = (TextView) rowView.findViewById(R.id.textOrderNum);
		TextView textState = (TextView) rowView.findViewById(R.id.textState);
		
		textOrderNum.setText(products.get(position).toString());
		String state="Not picked";
		if(products.get(position).Picked == 1)
			state="Picked";
		textState.setText(state);		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageStatus);
		
		// Change the icon for Windows and iPhone
		if (products.get(position).Picked == 1) {
			imageView.setImageResource(R.drawable.progress);
		} else {
			imageView.setImageResource(R.drawable.wait);
		}

		return rowView;
	}
}

package com.augmate;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class orderArrayAdapter extends ArrayAdapter<Order> {
	
	private final Context context;
	private final ArrayList<Order> orders;

	public orderArrayAdapter(Context context,ArrayList<Order> objects) {
		super(context, R.layout.order_list_row, objects);
		// TODO Auto-generated constructor stub
		this.context= context;
		this.orders= objects;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.order_list_row, parent, false);
		
		TextView textOrderNum = (TextView) rowView.findViewById(R.id.textOrderNum);
		TextView textState = (TextView) rowView.findViewById(R.id.textState);
		
		textOrderNum.setText(orders.get(position).toString());
		String state="No started";
		if(orders.get(position).status == 1)
			state="In progress";
		textState.setText(state);
		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageStatus);
		// Change the icon for Windows and iPhone
		if (orders.get(position).status==1) {
			imageView.setImageResource(R.drawable.progress);
		} else {
			imageView.setImageResource(R.drawable.wait);
		}

		return rowView;
	}
}

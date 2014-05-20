package com.augmate;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertMessageDialogFragment extends DialogFragment
{
    private CharSequence mMessageText;
    private int mIconId;
    private int mTextColor;
    
    public static final AlertMessageDialogFragment newInstance(String messageText, int itemIconId, int textColor)
    {
    	AlertMessageDialogFragment alertDialog = new AlertMessageDialogFragment();
        Bundle bundle = new Bundle(3);
        bundle.putString("messageText", messageText);
        bundle.putInt("itemIconId", itemIconId);
        bundle.putInt("textColor", textColor);
        alertDialog.setArguments(bundle);
        return alertDialog;
    }	
    
    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        Bundle args = getArguments();
        if (args != null) 
        {
        	mMessageText = args.getCharSequence("messageText", mMessageText);
        	mIconId = args.getInt("itemIconId");
        	mTextColor = args.getInt("textColor");
        }
        else
        {
        	mMessageText = "";
        	mIconId = R.drawable.complete;
        	mTextColor = Color.RED;
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu_item_layout, container, false); 
        TextView textView = (TextView) view.findViewById(R.id.menu_item_name);
        SpannableString text = new SpannableString(mMessageText);
        text.setSpan(new ForegroundColorSpan(mTextColor), 0, mMessageText.length(), 0);
        textView.setText(text);
        
        ImageView iconView = (ImageView) view.findViewById(R.id.menu_item_imageview);
        iconView.setImageResource(mIconId);
        
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        
        view.getBackground().setAlpha(200);
        Drawable d = new ColorDrawable(Color.TRANSPARENT);
        getDialog().getWindow().setBackgroundDrawable(d);
        
        
        return view;
    }
}

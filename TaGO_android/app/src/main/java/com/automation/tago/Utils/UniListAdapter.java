package com.automation.tago.Utils;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransitionImpl;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.automation.tago.Main.ShopFragment;
import com.automation.tago.Models.Uni;
import com.automation.tago.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class UniListAdapter extends ArrayAdapter<Uni> {

    private static final String TAG = "UniListAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private List<Uni> unis;
    private FragmentManager fragmentManager;

    public UniListAdapter(Context context, int layoutResource, List<Uni> unis, FragmentManager fragmentManager) {
        super(context, layoutResource, unis);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        this.unis = unis;
        this.fragmentManager = fragmentManager;
    }

    private static class ViewHolder{
        SquareImageView image;
        TextView university_name;
        Uni uni;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.gridImageView);
            holder.university_name = convertView.findViewById(R.id.university_name);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.uni = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(holder.uni.getUniversity_picture(),holder.image);

        holder.university_name.setText(holder.uni.getUniversity_name());

        startingSetup(holder, parent);

        return convertView;
    }

    private void startingSetup(final ViewHolder holder, final ViewGroup parent){
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopFragment fragment = new ShopFragment();
                Bundle args = new Bundle();
                args.putString("uni",holder.uni.getUniversity_name());
                fragment.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout,fragment);
                transaction.addToBackStack("ShopFragment");
                transaction.commit();
            }
        });
    }
}


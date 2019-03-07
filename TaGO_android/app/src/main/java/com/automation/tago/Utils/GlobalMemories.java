package com.automation.tago.Utils;

import android.app.Application;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.automation.tago.Models.CartItem;
import com.automation.tago.Models.TicketItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GlobalMemories extends Application {

    private static final String TAG = "GlobalMemories";

    private static GlobalMemories mInstance = null;

    // for Main Activity
    public ViewPager viewPager;

    public int main_counter;

    public int authorization;

    public int item_cap;

    public int ticket_cap;

    // for Food Fragment
    public String food_name;

    public TextView quantity;

    // for Cart Fragment
    public List<CartItem> list_cart_items;

    public CartListAdapter adapterGlobal;

    public TextView cart_item_number;

    public String current_date;

    public int current_number_of_ticket;

    public int isListening;

    // for Ticket Fragment
    public List<TicketItem> list_ticket_items;

    public int claiming;

    // Profile Activity

    public int profile_counter;

    protected GlobalMemories(){};

    public static synchronized GlobalMemories getmInstance(){
        if(mInstance == null){
            mInstance = new GlobalMemories();
            mInstance.list_cart_items = new ArrayList<>();
            mInstance.list_ticket_items = new ArrayList<>();
            mInstance.main_counter = 0;
            mInstance.claiming = 0;
            mInstance.authorization = 0;
            mInstance.item_cap = 0;
            mInstance.ticket_cap = 0;
            mInstance.current_number_of_ticket = -1;
            mInstance.isListening = 0;
        }
        return mInstance;
    }

    public static synchronized void adjustingDatabase(final String ticket_number){
        FirebaseDatabase.getInstance().getReference()
                .child("Cart_list")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(ticket_number)
                .removeValue();
    }

    public static synchronized void changingClaim(final String ticket_number){
        Log.d(TAG, "changingClaim: claim changed");
        FirebaseDatabase.getInstance().getReference()
                .child("Cart_list")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(ticket_number)
                .child("claim")
                .setValue(1);
    }

    public static synchronized void settingUpCartItemNumber(TextView cart_item_number){
        Log.d(TAG, "settingUpCartItemNumber: adding to k");
        int k = 0;
        List<CartItem> list_cart_items = GlobalMemories.mInstance.list_cart_items;
        for(int i = 0; i < list_cart_items.size(); i++){
            k = list_cart_items.get(i).getFood_quantity() + k;
        }
        Log.d(TAG, "settingUpCartItemNumber: adding to k and update textview");
        cart_item_number.setText(String.valueOf(k));
    }
}

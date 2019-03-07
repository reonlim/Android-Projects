package com.automation.tago.Main;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.tago.Models.CartItem;
import com.automation.tago.Models.TicketItem;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.microedition.khronos.opengles.GL;

public class CartFragment extends Fragment {

    //preliminaries in this fragment
    private static final String TAG = "CartFragment";
    private Context mContext;

    //not view object initiation
    private int check_counter;
    private int last_counter;

    //view objects initiation
    private ListView listView;
    private TextView total_price;
    private Button check_out_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        check_counter = 0;
        last_counter = -1;
        listView = view.findViewById(R.id.listView);
        total_price = view.findViewById(R.id.total_price);
        check_out_button = view.findViewById(R.id.check_out_button);
        settingUpTotalPrice();

        check_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartItem> list_cart_items = GlobalMemories.getmInstance().list_cart_items;
                int current_number_of_ticket = GlobalMemories.getmInstance().current_number_of_ticket;
                int ticket_cap = GlobalMemories.getmInstance().ticket_cap;
                int isListening = GlobalMemories.getmInstance().isListening;
                if(current_number_of_ticket < ticket_cap && isListening != 1) {
                    if (check_counter != last_counter) {
                        if (list_cart_items.size() != 0) {
                            ArrayList<TicketItem> list_ticket_items = new ArrayList<>();
                            for (int i = 0; i < list_cart_items.size(); i++) {
                                TicketItem ticketItem = new TicketItem(list_cart_items.get(i).getFood_name(),
                                        list_cart_items.get(i).getFood_quantity());
                                list_ticket_items.add(ticketItem);
                            }
                            last_counter = check_counter;
                            updateCartAfterPayment(list_ticket_items);
                        } else {
                            Toast.makeText(mContext, "Currently you have no item in your cart", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please wait", Toast.LENGTH_SHORT).show();
                    }
                }else if(current_number_of_ticket == ticket_cap){
                    Toast.makeText(mContext, "The daily ticket limit is " + ticket_cap, Toast.LENGTH_SHORT).show();
                }else if(isListening == 1){
                    Toast.makeText(mContext, "Please wait for the ticket system to load.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        settingListView();
    }

    private void updateCartAfterPayment(final ArrayList<TicketItem> list_ticket_items){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String referenceNumber = reference.push().getKey();
        String ticketNumber = "";
        if(referenceNumber != null) {
            ticketNumber = referenceNumber.substring(3, 7);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            GlobalMemories.getmInstance().current_number_of_ticket =
                    GlobalMemories.getmInstance().current_number_of_ticket + 1;
            final String user_id = user.getUid();
            reference.child("Cart_list")
                    .child(user_id)
                    .child(ticketNumber)
                    .setValue(list_ticket_items);
            reference.child("Cart_list")
                    .child(user_id)
                    .child(ticketNumber)
                    .child("date_time")
                    .setValue(sdf.format(new Date()));
            reference.child("Cart_list")
                    .child(user_id)
                    .child(ticketNumber)
                    .child("claim")
                    .setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    GlobalMemories.getmInstance().list_cart_items.clear();
                    GlobalMemories.getmInstance().adapterGlobal.notifyDataSetChanged();
                    GlobalMemories.getmInstance().quantity.setText(String.valueOf(0));
                    GlobalMemories.settingUpCartItemNumber(GlobalMemories.getmInstance().cart_item_number);
                    check_counter = check_counter + 1;
                    reference.child("users")
                            .child(user_id)
                            .child("ticket")
                            .child("date")
                            .setValue(date.format(new Date()));
                    reference.child("users")
                            .child(user_id)
                            .child("ticket")
                            .child("ticket_quantity")
                            .setValue(GlobalMemories.getmInstance().current_number_of_ticket);
                }
            });
        }
    }

    private void settingListView(){
        listView.setAdapter(GlobalMemories.getmInstance().adapterGlobal);
        GlobalMemories.getmInstance().adapterGlobal.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                settingUpTotalPrice();
            }
        });
    }

    private void settingUpTotalPrice(){
        double k = 0;
        List<CartItem> list_cart_items = GlobalMemories.getmInstance().list_cart_items;
        for(int i = 0; i < list_cart_items.size(); i++){
            k = list_cart_items.get(i).getFood_price()*list_cart_items.get(i).getFood_quantity() + k;
        }
        total_price.setText(String.format(Locale.ENGLISH,"%.2f", k));
    }
}

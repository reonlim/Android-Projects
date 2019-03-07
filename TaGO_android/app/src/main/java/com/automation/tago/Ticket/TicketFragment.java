package com.automation.tago.Ticket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.automation.tago.Models.TicketItem;
import com.automation.tago.R;
import com.automation.tago.Utils.GlobalMemories;
import com.automation.tago.Utils.TicketListAdapter;

import java.util.ArrayList;

public class TicketFragment extends Fragment {

    public interface OnConfirmDeleteTicket{
        void onConfirmDeleteTicket(String ticket_number);
    }
    OnConfirmDeleteTicket mOnConfirmDeleteTicket;

    //preliminaries in this fragment
    private static final String TAG ="TicketFragment";
    private Context mContext;

    //not view object initiation
    private String ticket_number;
    private ArrayList<TicketItem> list_ticket_items;
    private int claim_;
    private TicketListAdapter adapter;
    private final int interval = 1000 * 30;
    private CountDownTimer timer_;

    //view objects initiation
    private ListView listView;
    private TextView close;
    private TextView timer;
    private Button claim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_ticket, container, false);
        Log.d(TAG, "onCreateView: setting up ticket layout");
        mContext = getActivity();

        startingSetup(view);

        return view;
    }

    private void startingSetup(View view){
        listView = view.findViewById(R.id.listView);
        close = view.findViewById(R.id.close);
        timer = view.findViewById(R.id.timer);
        claim = view.findViewById(R.id.claim);
        list_ticket_items = new ArrayList<>();
        timer_ = new CountDownTimer(interval, 1000) {

            public void onTick(long millisUntilFinished) {
                String timerText = "Expired in: " + millisUntilFinished / 1000 + "s";
                timer.setText(timerText);
            }

            public void onFinish() {
                GlobalMemories.adjustingDatabase(ticket_number);
                GlobalMemories.getmInstance().claiming = 0;
                mOnConfirmDeleteTicket.onConfirmDeleteTicket(ticket_number);
            }
        };
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_.cancel();
                GlobalMemories.adjustingDatabase(ticket_number);
                GlobalMemories.getmInstance().claiming = 0;
                mOnConfirmDeleteTicket.onConfirmDeleteTicket(ticket_number);

            }
        });
        claim.setSelected(true);
        claim.setText("Claimed");
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable() && GlobalMemories.getmInstance().claiming == 0) {
                    claim_ = 1;
                    claim.setSelected(true);
                    claim.setText("Claimed");
                    timer_.start();
                    close.setVisibility(View.VISIBLE);
                    GlobalMemories.getmInstance().claiming = 1;
                    GlobalMemories.changingClaim(ticket_number);
                }else if(!isNetworkAvailable() && GlobalMemories.getmInstance().claiming == 0){
                    Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }else if(isNetworkAvailable() && GlobalMemories.getmInstance().claiming == 1){
                    Toast.makeText(mContext, "Currently you are claiming one ticket", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        insertingTicketDetails();
    }

    private void insertingTicketDetails(){
        Bundle bundle = getArguments();
        if(bundle != null) {
            ticket_number = bundle.getString("ticket_number");
            list_ticket_items = bundle.getParcelableArrayList("testing 123");
            if(claim_ != 1) {
                claim_ = bundle.getInt("claim_");
                Log.d(TAG, "insertingTicketDetails: claim_:" + claim_);
            }

            adapter = new TicketListAdapter(mContext, R.layout.layout_listview_orderitem, list_ticket_items);
            listView.setAdapter(adapter);

            if(claim_ == 0) {
                claim.setSelected(false);
                claim.setText("Claim Now");
                close.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnConfirmDeleteTicket = (OnConfirmDeleteTicket) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

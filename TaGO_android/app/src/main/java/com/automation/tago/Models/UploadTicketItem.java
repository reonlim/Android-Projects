package com.automation.tago.Models;

import java.text.SimpleDateFormat;
import java.util.List;

public class UploadTicketItem {

    private List<TicketItem> list_ticket_item;
    private SimpleDateFormat date_time;
    private int claim;

    public UploadTicketItem(){}

    public UploadTicketItem(List<TicketItem> list_ticket_item, SimpleDateFormat date_time, int claim) {
        this.list_ticket_item = list_ticket_item;
        this.date_time = date_time;
        this.claim = claim;
    }

    public List<TicketItem> getList_ticket_item() {
        return list_ticket_item;
    }

    public void setList_ticket_item(List<TicketItem> list_ticket_item) {
        this.list_ticket_item = list_ticket_item;
    }

    public SimpleDateFormat getDate_time() {
        return date_time;
    }

    public void setDate_time(SimpleDateFormat date_time) {
        this.date_time = date_time;
    }

    public int getClaim() {
        return claim;
    }

    public void setClaim(int claim) {
        this.claim = claim;
    }

    @Override
    public String toString() {
        return "UploadTicketItem{" +
                "list_ticket_item=" + list_ticket_item +
                ", date_time=" + date_time +
                ", claim=" + claim +
                '}';
    }
}

package com.kshitijharsh.dairymanagement.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.model.CattleFeed;

import java.util.List;

public class CattleAdapter extends RecyclerView.Adapter<CattleAdapter.ViewHolder> {

    private List<CattleFeed> cattleList;
    private Context context;
    private ItemClickListener clickListener;

    public CattleAdapter(List<CattleFeed> cattleList, Context context, ItemClickListener listener) {
        this.cattleList = cattleList;
        this.context = context;
        this.clickListener = listener;
    }

    @Override
    public CattleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cattle, parent, false);

        return new CattleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CattleAdapter.ViewHolder holder, final int position) {
        final CattleFeed cattleFeed = cattleList.get(position);
        final Bundle bundle = new Bundle();
        final String id, name, date, item, rate, qty, amt, part;
        id = cattleFeed.getMemId();
        name = cattleFeed.getName();
        date = cattleFeed.getDate();
        item = cattleFeed.getItemName();
        rate = cattleFeed.getRate();
        qty = cattleFeed.getQty();
        amt = cattleFeed.getAmt();
        part = cattleFeed.getParticulars();

        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("date", date);
        bundle.putString("itemName", item);
        bundle.putString("rate", rate);
        bundle.putString("qty", qty);
        bundle.putString("amt", amt);
        bundle.putString("part", part);

        holder.memberId.setText(cattleFeed.getMemId());
        holder.memberName.setText(cattleFeed.getName());
        holder.date.setText(cattleFeed.getDate());
        holder.itemName.setText(cattleFeed.getItemName());
        holder.rate.setText(cattleFeed.getRate());
        holder.qty.setText(cattleFeed.getQty());
        holder.amt.setText(cattleFeed.getAmt());
        holder.part.setText(cattleFeed.getParticulars());


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO delete code
//                Toast.makeText(context, "Delete: " + id, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO edit code
//                clickListener.onClick(bundle);
//                Toast.makeText(context, "Edit: " + id, Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return cattleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberId, memberName, date, itemName, rate, qty, amt, part;
        ImageView edit;
        ImageView delete;
        View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            memberId = view.findViewById(R.id.mem_id);
            memberName = view.findViewById(R.id.mem_name);
            date = view.findViewById(R.id.date);
            itemName = view.findViewById(R.id.item_name);
            rate = view.findViewById(R.id.rate);
            qty = view.findViewById(R.id.qty);
            amt = view.findViewById(R.id.amt);
            part = view.findViewById(R.id.part);

//            edit = view.findViewById(R.id.edit);
//            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteMatch(String id, final int position) {

    }
}
package com.kshitijharsh.dairymanagement.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.activities.CattleFeedActivity;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
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
        final String id, name, date, item, rate, qty, amt, part, _id;
        _id = cattleFeed.getId();
        id = cattleFeed.getMemId();
        name = cattleFeed.getName();
        date = cattleFeed.getDate();
        item = cattleFeed.getItemName();
        rate = cattleFeed.getRate();
        qty = cattleFeed.getQty();
        amt = cattleFeed.getAmt();
        part = cattleFeed.getParticulars();

        bundle.putString("id", _id);
        bundle.putString("memId", id);
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


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteEntry(v.getContext(), _id, position);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
                ab.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDetails(bundle);
            }
        });
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

            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteEntry(Context context, String id, final int position) {
        DatabaseClass databaseClass = new DatabaseClass(context);
        Cursor c = databaseClass.deleteCattleItem(id);
        if (c != null) {
            cattleList.remove(position);
            notifyDataSetChanged();
            c.close();
            Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Couldn't delete, try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void editDetails(Bundle bundle) {
        Intent intent = new Intent(context, CattleFeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
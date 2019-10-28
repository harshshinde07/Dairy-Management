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
import com.kshitijharsh.dairymanagement.activities.CollectionActivity;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Collection;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> collectionList;
    private Context context;
    private ItemClickListener clickListener;

    public CollectionAdapter(List<Collection> collectionList, Context context, ItemClickListener listener) {
        this.collectionList = collectionList;
        this.context = context;
        this.clickListener = listener;
    }

    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);

        return new CollectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionAdapter.ViewHolder holder, final int position) {
        final Collection collection = collectionList.get(position);
        final Bundle bundle = new Bundle();
        final String id, name, date, milkType, morEve, rate, qty, amt, snf, fat, memType, _id;
        _id = collection.getId();
        id = collection.getMemId();
        name = collection.getMemName();
        date = collection.getDate();
        milkType = collection.getMilktype();
        morEve = collection.getMorEve();
        memType = collection.getMemType();
        rate = collection.getRate();
        qty = collection.getQty();
        amt = collection.getAmt();
//        snf = collection.getDegree();
        fat = collection.getFat();

        bundle.putString("id", _id);
        bundle.putString("memId", id);
        bundle.putString("name", name);
        bundle.putString("morEve", morEve);
        bundle.putString("milkType", milkType);
        bundle.putString("memType", memType);
        bundle.putString("date", date);
        bundle.putString("rate", rate);
        bundle.putString("qty", qty);
        bundle.putString("amt", amt);
//        bundle.putString("snf", snf);
        bundle.putString("fat", fat);

        holder.memberId.setText(collection.getMemId());
        holder.memberName.setText(collection.getMemName());
        holder.morEve.setText(collection.getMorEve());
        holder.milkType.setText(collection.getMilktype());
        holder.memberType.setText(collection.getMemType());
        holder.date.setText(collection.getDate());
        holder.rate.setText(collection.getRate());
        holder.qty.setText(collection.getQty());
        holder.amt.setText(collection.getAmt());
//        holder.snf.setText(collection.getSnf());
        holder.fat.setText(collection.getFat());


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
        return collectionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberId, memberName, milkType, memberType, date, rate, qty, amt, snf, fat, morEve;
        ImageView edit;
        ImageView delete;
        View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            memberId = view.findViewById(R.id.mem_id);
            memberName = view.findViewById(R.id.mem_name);
            milkType = view.findViewById(R.id.milk_type);
            memberType = view.findViewById(R.id.member_type);
            morEve = view.findViewById(R.id.mor_eve);
            date = view.findViewById(R.id.date);
            fat = view.findViewById(R.id.fat);
            rate = view.findViewById(R.id.rate);
            qty = view.findViewById(R.id.qty);
            amt = view.findViewById(R.id.amt);
//            snf = view.findViewById(R.id.snf);


            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteEntry(Context context, String id, final int position) {
        DatabaseClass databaseClass = new DatabaseClass(context);
        Cursor c = databaseClass.deleteCollectionItem(id);
        if (c != null) {
            collectionList.remove(position);
            notifyDataSetChanged();
            c.close();
            Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Couldn't delete, try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void editDetails(Bundle bundle) {
        Intent intent = new Intent(context, CollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
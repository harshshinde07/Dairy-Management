package com.kshitijharsh.dairymanagement.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.model.Collection;
import com.kshitijharsh.dairymanagement.model.Member;

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
        final String id, name, date, milkType, morEve, rate, qty, amt, snf, fat, memType;
        id = collection.getMemId();
        name = collection.getMemName();
        date = collection.getDate();
        milkType = collection.getMilktype();
        morEve = collection.getMorEve();
        memType = collection.getMemType();
        rate = collection.getRate();
        qty = collection.getQty();
        amt = collection.getAmt();
        snf = collection.getSnf();
        fat = collection.getFat();

        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("morEve", morEve);
        bundle.putString("milkType", milkType);
        bundle.putString("memType", memType);
        bundle.putString("date", date);
        bundle.putString("rate", rate);
        bundle.putString("qty", qty);
        bundle.putString("amt", amt);
        bundle.putString("snf", snf);
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
        return collectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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


//            edit = view.findViewById(R.id.edit);
//            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteMatch(String id, final int position) {

    }
}
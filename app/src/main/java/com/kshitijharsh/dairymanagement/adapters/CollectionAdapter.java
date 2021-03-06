package com.kshitijharsh.dairymanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.activities.CollectionActivity;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> implements Filterable {

    private List<Collection> collectionList;
    private Context context;
    private List<Collection> collectionListFiltered;
    private CollectionAdapterListener listener;

    public CollectionAdapter(List<Collection> collectionList, Context context, CollectionAdapterListener listener) {
        this.collectionList = collectionList;
        this.context = context;
        this.listener = listener;
        this.collectionListFiltered = collectionList;
    }

    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection, parent, false);

        return new CollectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Collection collection = collectionListFiltered.get(position);
        final Bundle bundle = new Bundle();
        final String id, name, date, milkType, morEve, rate, qty, amt, snf, fat, memType, _id, degree;
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


        String val;
        degree = collection.getMorEve();
        snf = collection.getSnf();

        Log.e("CollectionAdapter: ", " Milk type: " + milkType + " MorEve: " + morEve + " memType: " + memType + " rate: " + rate + " qty: " + qty + " amt: " + amt + "fat: " + fat + " snf: " + snf + "degree: " + collection.getDegree());


        if (snf.equals("0") && degree.equals("0"))
            val = "0";
        else if (!snf.equals("0") && degree.equals("0"))
            val = snf;
        else
            val = degree;

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
        bundle.putString("snf", val);
        bundle.putString("fat", fat);

        holder.memberId.setText(collection.getMemId());
        holder.memberName.setText(collection.getMemName());
        holder.morEve.setText(val);
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
        return collectionListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    collectionListFiltered = collectionList;
                } else {
                    List<Collection> filteredList = new ArrayList<>();
                    for (Collection row : collectionList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getMemName().toLowerCase().contains(charString.toLowerCase()) || row.getMemId().contains(charSequence) || row.getDate().contains(charSequence) || row.getMemType().toLowerCase().contains(charString.toLowerCase()) || row.getMilktype().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    collectionListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = collectionListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                collectionListFiltered = (ArrayList<Collection>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onCollectionSelected(collectionListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    private void deleteEntry(Context context, String id, final int position) {
        DatabaseClass databaseClass = new DatabaseClass(context);
        Cursor c = databaseClass.deleteCollectionItem(id);
        if (c != null) {
            collectionListFiltered.remove(position);
            notifyDataSetChanged();
            c.close();
            Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Couldn't delete, try again!", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CollectionAdapterListener {
        void onCollectionSelected(Collection collection);
    }

    private void editDetails(Bundle bundle) {
        Intent intent = new Intent(context, CollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
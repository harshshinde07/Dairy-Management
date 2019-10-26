package com.kshitijharsh.dairymanagement.adapters;

import android.content.Context;
import android.content.DialogInterface;
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
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Member;
import com.kshitijharsh.dairymanagement.model.Sale;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {

    private List<Sale> saleList;
    private Context context;
    private ItemClickListener clickListener;

    public SaleAdapter(List<Sale> saleList, Context context, ItemClickListener listener) {
        this.saleList = saleList;
        this.context = context;
        this.clickListener = listener;
    }

    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);

        return new SaleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SaleAdapter.ViewHolder holder, final int position) {
        final Sale sale = saleList.get(position);
        final Bundle bundle = new Bundle();
        final String id, name, date, milkType, morEve, rate, qty, amt, fat, _id;
        _id = sale.getId();
        id = sale.getMemId();
        name = sale.getMemName();
        date = sale.getDate();
        milkType = sale.getMilkType();
        morEve = sale.getMorEve();
        rate = sale.getRate();
        qty = sale.getQty();
        amt = sale.getAmount();
        fat = sale.getFat();

        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("morEve", morEve);
        bundle.putString("milkType", milkType);
        bundle.putString("date", date);
        bundle.putString("rate", rate);
        bundle.putString("qty", qty);
        bundle.putString("amt", amt);
        bundle.putString("fat", fat);

        holder.memberId.setText(sale.getMemId());
        holder.memberName.setText(sale.getMemName());
        holder.morEve.setText(sale.getMorEve());
        holder.milkType.setText(sale.getMilkType());
        holder.date.setText(sale.getDate());
        holder.rate.setText(sale.getRate());
        holder.qty.setText(sale.getQty());
        holder.amt.setText(sale.getAmount());
        holder.fat.setText(sale.getFat());

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
        return saleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberId, memberName, milkType, morEve, date, rate, qty, amt, fat;
        ImageView edit;
        ImageView delete;
        View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            memberId = view.findViewById(R.id.mem_id);
            memberName = view.findViewById(R.id.mem_name);
            milkType = view.findViewById(R.id.milk_type);
            morEve = view.findViewById(R.id.mor_eve);
            date = view.findViewById(R.id.date);
            rate = view.findViewById(R.id.rate);
            qty = view.findViewById(R.id.qty);
            amt = view.findViewById(R.id.amt);
            fat = view.findViewById(R.id.fat);

//            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteEntry(Context context, String id, final int position) {
        DatabaseClass databaseClass = new DatabaseClass(context);
        Cursor c = databaseClass.deleteSaleItem(id);
        if (c != null) {
            saleList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Couldn't delete, try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
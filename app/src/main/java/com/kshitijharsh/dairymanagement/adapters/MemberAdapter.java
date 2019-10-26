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
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private List<Member> memberList;
    private Context context;
    private ItemClickListener clickListener;

    public MemberAdapter(List<Member> memberList, Context context, ItemClickListener listener) {
        this.memberList = memberList;
        this.context = context;
        this.clickListener = listener;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);

        return new MemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, final int position) {
        final Member member = memberList.get(position);
        final Bundle bundle = new Bundle();
        final String id = member.getCode();
        String name = member.getName();
        String memType = member.getMembType();
        String milkType = member.getCowbfType();
        String rateName = member.getRateGrpNo();

        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("memType", memType);
        bundle.putString("milkType", milkType);
        bundle.putString("rateName", rateName);

        holder.memberId.setText(member.getCode());
        holder.memberName.setText(member.getName());
        holder.memberType.setText(member.getMembType());
        holder.milkType.setText(member.getCowbfType());
        holder.rateGrpName.setText(member.getRateGrpNo());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteEntry(v.getContext(), id, position);
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
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberId, memberName, milkType, memberType, rateGrpName;
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
            rateGrpName = view.findViewById(R.id.rate_grp_name);

//            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteEntry(Context context, String id, final int position) {
        DBQuery dbQuery = new DBQuery(context);
        Cursor c = dbQuery.deleteMember(id);
        if (c != null) {
            memberList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Couldn't delete, try again!", Toast.LENGTH_SHORT).show();
        }
    }
}

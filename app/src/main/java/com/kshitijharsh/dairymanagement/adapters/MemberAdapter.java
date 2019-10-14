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
//            delete = view.findViewById(R.id.delete);
        }
    }

    private void deleteMatch(String id, final int position) {

    }
}

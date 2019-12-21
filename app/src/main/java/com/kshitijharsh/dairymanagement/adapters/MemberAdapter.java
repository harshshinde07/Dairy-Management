package com.kshitijharsh.dairymanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.activities.MemberActivity;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements Filterable {
    private List<Member> memberList;
    private Context context;
    private List<Member> memberListFiltered;
    private MemberAdapterListener listener;

    public MemberAdapter(List<Member> memberList, Context context, MemberAdapterListener listener) {
        this.memberList = memberList;
        this.context = context;
        this.listener = listener;
        this.memberListFiltered = memberList;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);

        return new MemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, final int position) {
        final Member member = memberListFiltered.get(position);
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
//            public void onClick(final View v) {
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                deleteEntry(v.getContext(), id, position);
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                dialog.dismiss();
//                                break;
//                        }
//                    }
//                };
//                AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
//                ab.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialogClickListener)
//                        .setNegativeButton("No", dialogClickListener).show();
//
//            }
//        });
//
//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editDetails(bundle);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return memberListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    memberListFiltered = memberList;
                } else {
                    List<Member> filteredList = new ArrayList<>();
                    for (Member row : memberList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCode().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    memberListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = memberListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                memberListFiltered = (ArrayList<Member>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onMemberSelected(memberListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    private void deleteEntry(Context context, String id, final int position) {
        DBQuery dbQuery = new DBQuery(context);
        Cursor c = dbQuery.deleteMember(id);
        if (c != null) {
            memberListFiltered.remove(position);
            notifyDataSetChanged();
            c.close();
            Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Couldn't delete, try again!", Toast.LENGTH_SHORT).show();
        }
    }

    public interface MemberAdapterListener {
        void onMemberSelected(Member member);
    }

    private void editDetails(Bundle bundle) {
        Intent intent = new Intent(context, MemberActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}

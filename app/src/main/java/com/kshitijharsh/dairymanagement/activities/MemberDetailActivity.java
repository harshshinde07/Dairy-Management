package com.kshitijharsh.dairymanagement.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.adapters.MemberAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberDetailActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private MemberAdapter mAdapter;
    DBQuery dbQuery;

    List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        getSupportActionBar().setTitle("Member Details");

        dbQuery = new DBQuery(this);
        dbQuery.open();

        recyclerView = findViewById(R.id.member_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        memberList = new ArrayList<>();

        getMemDetails();

    }

    public void getMemDetails() {
        Cursor cursor = dbQuery.getAllMembers();
        String memType = "", milkType = "", rateGrpName = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(2).equals("1"))
                milkType = "Cow";
            if (cursor.getString(2).equals("2"))
                milkType = "Buffalo";
            if (cursor.getString(2).equals("3"))
                milkType = "Both";
            if (cursor.getString(3).equals("1"))
                memType = "Member";
            if (cursor.getString(3).equals("2"))
                memType = "Contractor";
            if (cursor.getString(3).toString().equals("3"))
                memType = "Labour Contractor";
            rateGrpName = getRateGrpNoFromNo(cursor.getString(4));
            Member mem = new Member(cursor.getString(0),
                    cursor.getString(1),
                    milkType,
                    memType,
                    rateGrpName);
            memberList.add(mem);
            cursor.moveToNext();
        }
        mAdapter = new MemberAdapter(memberList, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(Bundle bundle) {

    }

    public String getRateGrpNoFromNo(String no) {
        Cursor c = dbQuery.getRateGrpName(no);
        String name = "";
        c.moveToFirst();
        if (c.getCount() > 0 && c != null) {
            name = c.getString(c.getColumnIndex("RateGrname"));
            return name;
        } else {
            return name;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

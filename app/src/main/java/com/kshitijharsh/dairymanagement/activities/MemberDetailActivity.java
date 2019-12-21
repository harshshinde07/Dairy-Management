package com.kshitijharsh.dairymanagement.activities;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.adapters.MemberAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberDetailActivity extends AppCompatActivity implements MemberAdapter.MemberAdapterListener {

    private RecyclerView recyclerView;
    DBQuery dbQuery;
    List<Member> memberList;
    private SearchView searchView;
    MemberAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Member Details");

        dbQuery = new DBQuery(this);
        dbQuery.open();

        recyclerView = findViewById(R.id.member_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        memberList = new ArrayList<>();

        getMemDetails();

    }

    public void getMemDetails() {
        Cursor cursor = dbQuery.getAllMembers();
        String memType = "", milkType = "", rateGrpName;
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
            if (cursor.getString(3).equals("3"))
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
        cursor.close();
        mAdapter = new MemberAdapter(memberList, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    public String getRateGrpNoFromNo(String no) {
        Cursor c = dbQuery.getRateGrpName(no);
        String name = "Not available";
        c.moveToFirst();
        if (c.getCount() > 0) {
            name = c.getString(c.getColumnIndex("RateGrname"));
            c.close();
            return name;
        } else {
            c.close();
            return name;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
//                Toast.makeText(MemberDetailActivity.this, "Filtered", Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
//                Toast.makeText(MemberDetailActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            return true;
        }
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMemberSelected(Member member) {
    }
}

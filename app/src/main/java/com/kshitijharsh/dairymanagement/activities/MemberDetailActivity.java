package com.kshitijharsh.dairymanagement.activities;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.adapters.MemberAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.Member;
import com.kshitijharsh.dairymanagement.utils.FilterDialogFragment;
import com.kshitijharsh.dairymanagement.utils.Filters;
import com.kshitijharsh.dairymanagement.viewmodel.MemberDetailActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberDetailActivity extends AppCompatActivity implements MemberAdapter.MemberAdapterListener, FilterDialogFragment.FilterListener {

    private RecyclerView recyclerView;
    DBQuery dbQuery;
    List<Member> memberList;
    private SearchView searchView;
    MemberAdapter mAdapter;

    private FilterDialogFragment mFilterDialog;
    private MemberDetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Member Details");

        mViewModel = ViewModelProviders.of(this).get(MemberDetailActivityViewModel.class);
        mFilterDialog = new FilterDialogFragment();

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

//        getMemDetails();

    }

    public void getMemDetails(String filter) {
//        Log.e("TAAAAAAAAAAAAAAG", filter);
        Cursor cursor = dbQuery.getAllMembers(filter);
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
        String name = "";
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
    public void onStart() {
        super.onStart();

        // Apply filters
        onFilter(mViewModel.getFilters());
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
        if (item.getItemId() == R.id.menu_filter) {// Show the dialog containing filter options
            mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
            return true;
        }
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO
    @Override
    public void onFilter(Filters filters) {
        // Construct query basic query
//        Query query = mFirestore.collection("restaurants");
//
        // Category (equality filter)
//        if (filters.hasType()) {
//            getMemDetails(filters.getType());
//        }

        // City (equality filter)
//        if (filters.hasTime()) {
//            getMemDetails(filters.getTime());
//        }

        // Sort by (orderBy with direction)
//        if (filters.hasSortBy()) {
        getMemDetails(filters.getSortBy());
//        }
//
//        // Limit items
//        query = query.limit(LIMIT);
//
//        // Update the query
//        mQuery = query;
//        mAdapter.setQuery(query);
//
//        // Set header
//        mCurrentSearchView.setText(Html.fromHtml(filters.getSearchDescription(this)));
//        mCurrentSortByView.setText(filters.getOrderDescription(this));
//
        // Save filters
        mViewModel.setFilters(filters);
    }

    @Override
    public void onMemberSelected(Member member) {
        Toast.makeText(getApplicationContext(), "Selected: " + member.getName(), Toast.LENGTH_LONG).show();
    }
}

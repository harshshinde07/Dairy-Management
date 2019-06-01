package com.kshitijharsh.dairymanagement.activities;

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
import com.kshitijharsh.dairymanagement.adapters.CattleAdapter;
import com.kshitijharsh.dairymanagement.adapters.MemberAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.CattleFeed;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.List;

public class CattleDetailActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private CattleAdapter mAdapter;
    DBQuery dbQuery;
    List<CattleFeed> cattleFeedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_detail);

        getSupportActionBar().setTitle("Cattle Feed Details");

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
        cattleFeedList = new ArrayList<>();

        getCattleDetails();
    }

    public void getCattleDetails() {
        //TODO
        mAdapter = new CattleAdapter(cattleFeedList, this, this);
        recyclerView.setAdapter(mAdapter);
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

    @Override
    public void onClick(Bundle bundle) {

    }
}

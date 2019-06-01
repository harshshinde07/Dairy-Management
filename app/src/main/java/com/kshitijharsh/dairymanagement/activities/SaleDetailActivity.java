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
import com.kshitijharsh.dairymanagement.adapters.CollectionAdapter;
import com.kshitijharsh.dairymanagement.adapters.MemberAdapter;
import com.kshitijharsh.dairymanagement.adapters.SaleAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.model.Member;
import com.kshitijharsh.dairymanagement.model.Sale;

import java.util.ArrayList;
import java.util.List;

public class SaleDetailActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private SaleAdapter mAdapter;
    DBQuery dbQuery;
    List<Sale> saleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_detail);

        getSupportActionBar().setTitle("Sale Details");

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
        saleList = new ArrayList<>();

        getSaleDetails();
    }

    public void getSaleDetails() {
        //TODO
        mAdapter = new SaleAdapter(saleList, this, this);
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

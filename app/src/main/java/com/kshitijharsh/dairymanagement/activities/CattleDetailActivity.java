package com.kshitijharsh.dairymanagement.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.adapters.CattleAdapter;
import com.kshitijharsh.dairymanagement.adapters.MemberAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.CattleFeed;
import com.kshitijharsh.dairymanagement.model.Member;
import com.kshitijharsh.dairymanagement.model.Sale;

import java.util.ArrayList;
import java.util.List;

public class CattleDetailActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    private CattleAdapter mAdapter;
    DBQuery dbQuery;
    List<CattleFeed> cattleFeedList;
    DatabaseClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_detail);

        getSupportActionBar().setTitle("Cattle Feed Details");

        dbQuery = new DBQuery(this);
        dbQuery.open();

        db = new DatabaseClass(this);
        db.getReadableDatabase();

        recyclerView = findViewById(R.id.cattle_list);
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
//        Cursor cursor = db.getAllCattle();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Log.e("ERRRRRRRRRR: ",cursor.getString(cursor.getColumnIndex("itemName")));
////            Toast.makeText(this, "INSIDE", Toast.LENGTH_SHORT).show();
//            CattleFeed cattleFeed = new CattleFeed();
//            cattleFeed.setAmt(cursor.getString(cursor.getColumnIndex("amount")));
//            cattleFeed.setDate(cursor.getString(cursor.getColumnIndex("trnDate")));
//            cattleFeed.setMemId(cursor.getString(cursor.getColumnIndex("memId")));
//            cattleFeed.setItemName(cursor.getString(cursor.getColumnIndex("itemName")));
//            cattleFeed.setRate(cursor.getString(cursor.getColumnIndex("rate")));
//            cattleFeed.setQty(cursor.getString(cursor.getColumnIndex("quantity")));
//            cattleFeed.setParticulars(cursor.getString(cursor.getColumnIndex("particulars")));
////            Toast.makeText(this, "Item: "+cursor.getString(cursor.getColumnIndex("itemName")), Toast.LENGTH_SHORT).show();
//            cattleFeedList.add(cattleFeed);
//            cursor.moveToNext();
//        }
        Cursor cursor = db.getAllCattle();
        int count = cursor.getCount();
        Toast.makeText(this, "Count: " + count, Toast.LENGTH_SHORT).show();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CattleFeed cattleFeed = new CattleFeed(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            cattleFeedList.add(cattleFeed);
            cursor.moveToNext();
        }
        mAdapter = new CattleAdapter(cattleFeedList, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    //TODO next version
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_filter, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

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
        //TODO
    }
}

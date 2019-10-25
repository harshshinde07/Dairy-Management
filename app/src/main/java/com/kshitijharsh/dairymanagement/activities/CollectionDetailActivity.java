package com.kshitijharsh.dairymanagement.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.ItemClickListener;
import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.adapters.CollectionAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionDetailActivity extends AppCompatActivity implements ItemClickListener {

    private RecyclerView recyclerView;
    DBQuery dbQuery;
    List<Collection> collectionList;
    DatabaseClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Collection Details");

        dbQuery = new DBQuery(this);
        dbQuery.open();

        db = new DatabaseClass(this);
        db.getReadableDatabase();

        recyclerView = findViewById(R.id.collection_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        collectionList = new ArrayList<>();

        getCollDetails();
    }

    public void getCollDetails() {
        Cursor cursor = db.getAllCollection();
        int count = cursor.getCount();
        if (count == 0)
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Collection collection = new Collection(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
            collectionList.add(collection);
            cursor.moveToNext();
        }
        CollectionAdapter mAdapter = new CollectionAdapter(collectionList, this, this);
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
        if (item.getItemId() == R.id.menu_filter) {//TODO
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Bundle bundle) {
        //TODO
    }
}

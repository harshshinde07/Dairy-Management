package com.kshitijharsh.dairymanagement.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.kshitijharsh.dairymanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialog";

    public interface FilterListener {

        void onFilter(Filters filters);

    }

    private View mRootView;

    @BindView(R.id.spinner_category)
    Spinner mCategorySpinner;

    @BindView(R.id.spinner_sort)
    Spinner mSortSpinner;

    @BindView(R.id.spinner_price)
    Spinner mPriceSpinner;

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.button_search)
    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }

        dismiss();
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        dismiss();
    }

    @Nullable
    private String getSelectedType() {
        String selected = (String) mCategorySpinner.getSelectedItem();
        if (getString(R.string.value_any_type).equals(selected)) {
            return null;
        } else {
            return selected;
        }
    }

    @Nullable
    private String getSelectedTime() {
        String selected = (String) mPriceSpinner.getSelectedItem();
        if (getString(R.string.value_any_time).equals(selected)) {
            return null;
        } else {
            return selected;
        }
    }

    @Nullable
    private String getSelectedSortBy() {
        String selected = (String) mSortSpinner.getSelectedItem();
        if (getString(R.string.sort_by_name).equals(selected)) {
            return Constants.CONST.FIELD_NAME;
        }
        if (getString(R.string.sort_by_code).equals(selected)) {
            return Constants.CONST.FIELD_CODE;
        }

        return null;
    }

//    @Nullable
//    private Query.Direction getSortDirection() {
//        String selected = (String) mSortSpinner.getSelectedItem();
//        if (getString(R.string.sort_by_rating).equals(selected)) {
//            return Query.Direction.DESCENDING;
//        } if (getString(R.string.sort_by_price).equals(selected)) {
//            return Query.Direction.ASCENDING;
//        } if (getString(R.string.sort_by_popularity).equals(selected)) {
//            return Query.Direction.DESCENDING;
//        }
//
//        return null;
//    }

    public void resetFilters() {
        if (mRootView != null) {
            mCategorySpinner.setSelection(0);
            mPriceSpinner.setSelection(0);
            mSortSpinner.setSelection(0);
        }
    }

    public Filters getFilters() {
        Filters filters = new Filters();

        if (mRootView != null) {
            filters.setType(getSelectedType());
            filters.setTime(getSelectedTime());
            filters.setSortBy(getSelectedSortBy());
//            filters.setSortDirection(getSortDirection());
        }

        return filters;
    }
}

package com.kshitijharsh.dairymanagement.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.kshitijharsh.dairymanagement.utils.Filters;

public class MemberDetailActivityViewModel extends ViewModel {

    private Filters mFilters;

    public MemberDetailActivityViewModel() {
        mFilters = Filters.getDefault();
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
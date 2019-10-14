package com.kshitijharsh.dairymanagement.utils;

import android.content.Context;
import android.text.TextUtils;

import com.kshitijharsh.dairymanagement.R;

/**
 * Sort and Filter
 * member code, cow / buffalo, morning / evening, litres, amount,
 * TODO sort by date and Sort Direction
 */
public class Filters {

    private String type = null;
    private String time = null;
    private String sortBy = null;
//    private Query.Direction sortDirection = null;

    public Filters() {
    }

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(Constants.CONST.FIELD_NAME);
//        filters.setSortDirection(Query.Direction.DESCENDING);

        return filters;
    }

    public boolean hasType() {
        return !(TextUtils.isEmpty(type));
    }

    public boolean hasTime() {
        return !(TextUtils.isEmpty(time));
    }

    public boolean hasSortBy() {
        return !(TextUtils.isEmpty(sortBy));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

//    public Query.Direction getSortDirection() {
//        return sortDirection;
//    }
//
//    public void setSortDirection(Query.Direction sortDirection) {
//        this.sortDirection = sortDirection;
//    }

//    public String getSearchDescription(Context context) {
//        StringBuilder desc = new StringBuilder();
//
//        if (type == null && time == null) {
//            desc.append("<b>");
//            desc.append(context.getString(R.string.all_records));
//            desc.append("</b>");
//        }
//
//        if (type != null) {
//            desc.append("<b>");
//            desc.append(type);
//            desc.append("</b>");
//        }
//
//        if (type != null && time != null) {
//            desc.append(" in ");
//        }
//
//        if (time != null) {
//            desc.append("<b>");
//            desc.append(time);
//            desc.append("</b>");
//        }
//
//        return desc.toString();
//    }
//
//    public String getOrderDescription(Context context) {
//        if (Restaurant.FIELD_PRICE.equals(sortBy)) {
//            return context.getString(R.string.sorted_by_price);
//        } else if (Restaurant.FIELD_POPULARITY.equals(sortBy)) {
//            return context.getString(R.string.sorted_by_popularity);
//        } else {
//            return context.getString(R.string.sorted_by_rating);
//        }
//    }
}

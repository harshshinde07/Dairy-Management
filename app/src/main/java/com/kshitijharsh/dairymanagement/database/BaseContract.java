package com.kshitijharsh.dairymanagement.database;

public class BaseContract {
    public static final class BaseEntry {
        //TABLES FROM DB FILE
        public static final String TABLE_MEMBER = "member";
        public static final String TABLE_ITEM = "item";
        public static final String TABLE_RATEMASTER = "ratemst";
        public static final String TABLE_RATEGRPMASTER = "Rt_grmst";
        public static final String TABLE_CUSTOMER = "customer";

        //COLUMN NAMES
        public static final String COLUMN_MEMB_CODE = "memb_code";
        public static final String COLUMN_MEMB_NAME = "memb_name";
        public static final String COLUMN_COWBF_TYPE = "Cobf_type";
        public static final String COLUMN_MEMB_TYPE = "memb_type";
        public static final String COLUMN_RATEGRP_NO = "rategrno";

        //Rate Group table columns
        public static final String COLUMN_RATEGRNO = "RateGrno";
        public static final String COLUMN_RATEGRNAME = "RateGrname";
        public static final String COLUMN_RATE_TYPE = "RateTyp";
        public static final String COLUMN_COW_RATE = "CowRate";
        public static final String COLUMN_BUFFALO_RATE = "BufRate";
    }
}

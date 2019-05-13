package com.example.fatcalapplication;

import android.provider.BaseColumns;

public class DBStructure {
    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "steps";
        public static final String COLUMN_USER_ID = "userid";
        public static final String COLUMN_STEPS_TAKEN = "steps";
        public static final String COLUMN_DATE = "stepsdate";
        public static final String COLUMN_TIME = "stepstime";
    }

}

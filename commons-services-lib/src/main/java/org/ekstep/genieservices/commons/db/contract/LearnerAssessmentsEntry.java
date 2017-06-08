package org.ekstep.genieservices.commons.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

/**
 * Created on 18/03/16.
 *
 * @author anil
 */
public abstract class LearnerAssessmentsEntry implements BaseColumns {

    public static final String TABLE_NAME = "learner_assessments";
    public static final String COLUMN_NAME_UID = "uid";
    public static final String COLUMN_NAME_CONTENT_ID = "content_id";
    public static final String COLUMN_NAME_QID = "qid";
    public static final String COLUMN_NAME_Q_INDEX = "q_index";
    public static final String COLUMN_NAME_CORRECT = "correct";
    public static final String COLUMN_NAME_SCORE = "score";
    public static final String COLUMN_NAME_MAX_SCORE = "max_score";
    public static final String COLUMN_NAME_TIME_SPENT = "time_spent";
    public static final String COLUMN_NAME_RES = "res";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_Q_DESC = "q_desc";
    public static final String COLUMN_NAME_Q_TITLE = "q_title";
    public static final String COLUMN_NAME_HIERARCHY_DATA = "h_data";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_CONTENT_ID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_QID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_Q_INDEX + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_CORRECT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_SCORE + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_MAX_SCORE + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_TIME_SPENT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_RES + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_TIMESTAMP + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_Q_DESC + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_Q_TITLE + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_HIERARCHY_DATA + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                " UNIQUE (" + COLUMN_NAME_UID + DbConstants.COMMA_SEP + COLUMN_NAME_CONTENT_ID + DbConstants.COMMA_SEP + COLUMN_NAME_QID + COLUMN_NAME_HIERARCHY_DATA + ") ON CONFLICT REPLACE" +
                " )";
    }

    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}

package org.ekstep.genieservices.summarizer.db.contract;

import org.ekstep.genieservices.commons.db.BaseColumns;
import org.ekstep.genieservices.commons.db.DbConstants;

import java.util.List;

import static java.util.Arrays.asList;

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
    public static final String COLUMN_NAME_QINDEX = "qindex";
    public static final String COLUMN_NAME_CORRECT = "correct";
    public static final String COLUMN_NAME_SCORE = "score";
    public static final String COLUMN_NAME_TIME_SPENT = "time_spent";
    public static final String COLUMN_NAME_RES = "res";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_QDESC = "qdesc";
    public static final String COLUMN_NAME_QTITLE = "qtitle";

    public static final String getCreateEntry() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UID + DbConstants.TEXT_TYPE + " NOT NULL" + DbConstants.COMMA_SEP +
                COLUMN_NAME_CONTENT_ID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_QID + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_QINDEX + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_CORRECT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_SCORE + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_TIME_SPENT + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_RES + DbConstants.TEXT_TYPE + DbConstants.COMMA_SEP +
                COLUMN_NAME_TIMESTAMP + DbConstants.INT_TYPE + DbConstants.COMMA_SEP +
                " UNIQUE (" + COLUMN_NAME_UID + DbConstants.COMMA_SEP + COLUMN_NAME_CONTENT_ID + DbConstants.COMMA_SEP + COLUMN_NAME_QID + ") ON CONFLICT REPLACE" +
                " )";
    }


    public static final String getDeleteEntry() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static List<String> getAlterEntryForQDescAndTitle() {
        return asList("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_QDESC + " TEXT NULL;"
                , "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_QTITLE + " TEXT NULL;");
    }
}

package org.ekstep.genieservices.migration.beforeMigration;

import org.ekstep.genieservices.summarizer.db.contract.LearnerAssessmentsEntry;
import org.ekstep.genieservices.summarizer.db.contract.LearnerContentSummaryEntry;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 18/03/16.
 *
 * @author anil
 */
public final class SummarizerDBContract {

    public SummarizerDBContract() {
    }

    public static List<String> getTableCreateEntries() {
        return Arrays.asList(
                LearnerAssessmentsEntry.getCreateEntry(),
                LearnerContentSummaryEntry.getCreateEntry()
        );
    }

    public static List<String> getTableDeleteEntries() {
        return Arrays.asList(
                LearnerAssessmentsEntry.getDeleteEntry(),
                LearnerContentSummaryEntry.getDeleteEntry()
        );
    }

}
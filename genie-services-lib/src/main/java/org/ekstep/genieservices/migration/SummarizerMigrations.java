package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummarizerMigrations {

    public static List<Migration> getSummarizerMigrations() {
        List<Migration> migrations = new ArrayList<>();
        //add migrations here
        migrations.add(new _01_CreateQDescAndTitleForAssessmentMigration());

        Collections.sort(migrations);
        return migrations;
    }

}

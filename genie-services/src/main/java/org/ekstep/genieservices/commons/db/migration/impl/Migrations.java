package org.ekstep.genieservices.commons.db.migration.impl;

import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author anil
 */
public abstract class Migrations {

    public static List<Migration> getGeServiceMigrations() {
        List<Migration> migrations = new ArrayList<>();

        migrations.add(new _01_CreatePageMigration());
        migrations.add(new _02_CreatedAtForProfileMigration());
        migrations.add(new _03_CreateResourceBundleMigration());
        migrations.add(new _04_CreateOrdinalsMigration());
        migrations.add(new _05_CreateRefCountContentMigration());
        migrations.add(new _06_CreateContentFeedbackMigration());
        migrations.add(new _07_UIBrandingMigration());
        migrations.add(new _08_PartnerFilterMigration());
        migrations.add(new _09_SdkMigration());
        migrations.add(new _10_StorageManagementMigration());
        migrations.add(new _11_PragmanProfileMigration());
        migrations.add(new _12_ProfileSyllabusMigration());
        migrations.add(new _13_GroupAndProfileMigration());
        migrations.add(new _14_MillisecondsToSecondsMigration());
        migrations.add(new _15_ContentMarkerMigration());

        Collections.sort(migrations);

        return migrations;
    }

}

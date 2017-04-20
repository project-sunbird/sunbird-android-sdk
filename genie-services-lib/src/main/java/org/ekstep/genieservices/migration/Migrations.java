package org.ekstep.genieservices.migration;

import org.ekstep.genieservices.commons.db.migration.Migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Migrations {
    public static List<Migration> getGeServiceMigrations() {
        List<Migration> migrations = new ArrayList<>();

        migrations.add(new _01_CreatePageMigration());
        migrations.add(new _02_CreatedAtForProfileMigration());
        migrations.add(new _03_CreateResourceBundleMigration());
        migrations.add(new _04_CreateOrdinalsMigration());
        migrations.add(new _05_CreateRefCountContentMigration());
        migrations.add(new _06_CreateContentFeedbackMigration());
        migrations.add(new _07_UIBrandingMigration());
        migrations.add(new _08_DeleteGameAndLanguageEntryMigration());

        Collections.sort(migrations);

        return migrations;
    }
}

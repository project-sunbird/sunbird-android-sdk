package org.ekstep.genieservices.commons.db.migration;

/**
 * Created on 4/19/2017.
 *
 * @author anil
 */
public abstract class Migration implements IMigrate, Comparable<Migration> {

    private int migrationNumber;
    private int targetDbVersion;

    public Migration(int migrationNumber, int targetDbVersion) {
        this.migrationNumber = migrationNumber;
        this.targetDbVersion = targetDbVersion;
    }

    @Override
    public void revert() {
    }

    @Override
    public boolean shouldBeApplied(int oldVersion, int newVersion) {
        return oldVersion < targetDbVersion && targetDbVersion <= newVersion;
    }

    @Override
    public int compareTo(Migration another) {
        return (migrationNumber < another.migrationNumber) ? -1 : (migrationNumber > another.migrationNumber ? 1 : 0);
    }

}

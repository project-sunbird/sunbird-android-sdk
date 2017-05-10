package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.commons.db.contract.TelemetryTagEntry;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryTagsModel implements IReadable {

    private List<TelemetryTagModel> telemetryTags;
    private IDBSession mDBSession;

    public TelemetryTagsModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
        this.telemetryTags = new ArrayList<>();
    }

    public static TelemetryTagsModel find(IDBSession dbSession) {
        TelemetryTagsModel telemetryTags = new TelemetryTagsModel(dbSession);
        dbSession.read(telemetryTags);
        return telemetryTags;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                telemetryTags.add(TelemetryTagModel.build(mDBSession,
                        resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_NAME)),
                        resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_HASH)),
                        resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_DESCRIPTION)),
                        resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_START_DATE)),
                        resultSet.getString(resultSet.getColumnIndex(TelemetryTagEntry.COLUMN_NAME_END_DATE))
                ));
            } while (resultSet.moveToNext());
        return this;
    }

    @Override
    public String getTableName() {
        return TelemetryTagEntry.TABLE_NAME;
    }

    @Override
    public String orderBy() {
        return TelemetryTagEntry.COLUMN_NAME_NAME;
    }

    @Override
    public String filterForRead() {
        return "";
    }

    @Override
    public String[] selectionArgsForFilter() {
        return null;
    }

    @Override
    public String limitBy() {
        return "";
    }

    public List<TelemetryTagModel> tags() {
        return telemetryTags;
    }

    public Set<String> activeTagHashes() {
        Set<String> tags = new HashSet<>();
        for (TelemetryTagModel tag : telemetryTags) {
            LocalDate today = DateTime.now().toLocalDate();
            LocalDate startDate = StringUtil.isNullOrEmpty(tag.startDate()) ? today : LocalDate.parse(tag.startDate());
            LocalDate endDate = StringUtil.isNullOrEmpty(tag.endDate()) ? today : LocalDate.parse(tag.endDate());
            if (!startDate.isAfter(today) && !endDate.isBefore(today)) {
                tags.add(tag.tagHash());
            }
        }
        return tags;
    }
}

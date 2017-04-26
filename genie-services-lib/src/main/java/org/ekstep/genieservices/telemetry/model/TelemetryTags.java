package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.telemetry.db.contract.TelemetryTagEntry;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TelemetryTags implements IReadable {

    private List<TelemetryTag> telemetryTags;
    private AppContext mAppContext;

    public TelemetryTags(AppContext appContext) {
        this.mAppContext = appContext;
        this.telemetryTags = new ArrayList<>();
    }

    public static TelemetryTags find(AppContext appContext) {
        TelemetryTags telemetryTags = new TelemetryTags(appContext);
        appContext.getDBSession().read(telemetryTags);
        return telemetryTags;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                telemetryTags.add(TelemetryTag.build(mAppContext,
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
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

    public List<TelemetryTag> tags() {
        return telemetryTags;
    }

    public Set<String> activeTagHashes() {
        Set<String> tags = new HashSet<>();
        for (TelemetryTag tag : telemetryTags) {
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

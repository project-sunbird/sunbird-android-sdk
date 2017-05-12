package org.ekstep.genieservices.telemetry.model;

import org.ekstep.genieservices.commons.db.contract.TelemetryTagEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

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
        TelemetryTagsModel telemetryTagsModel = new TelemetryTagsModel(dbSession);
        dbSession.read(telemetryTagsModel);
        return telemetryTagsModel;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                TelemetryTagModel telemetryTagModel=TelemetryTagModel.build(mDBSession);
                telemetryTagModel.readWithoutMoving(resultSet);
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

    public List<TelemetryTagModel> getTags() {
        return telemetryTags;
    }

}

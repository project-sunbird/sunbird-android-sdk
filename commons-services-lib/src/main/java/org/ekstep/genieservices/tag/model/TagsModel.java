package org.ekstep.genieservices.tag.model;

import org.ekstep.genieservices.commons.db.contract.TelemetryTagEntry;
import org.ekstep.genieservices.commons.db.core.IReadable;
import org.ekstep.genieservices.commons.db.core.IResultSet;
import org.ekstep.genieservices.commons.db.operations.IDBSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swayangjit on 26/4/17.
 */

public class TagsModel implements IReadable {

    private List<TagModel> telemetryTags;
    private IDBSession mDBSession;

    public TagsModel(IDBSession dbSession) {
        this.mDBSession = dbSession;
        this.telemetryTags = new ArrayList<>();
    }

    public static TagsModel find(IDBSession dbSession) {
        TagsModel telemetryTagsModel = new TagsModel(dbSession);
        dbSession.read(telemetryTagsModel);
        return telemetryTagsModel;
    }

    @Override
    public IReadable read(IResultSet resultSet) {
        if (resultSet != null && resultSet.moveToFirst())
            do {
                TagModel telemetryTagModel= TagModel.build(mDBSession);
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

    public List<TagModel> getTags() {
        return telemetryTags;
    }

}

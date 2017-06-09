package org.ekstep.genieservices.profile.chained;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.profile.db.model.ImportedMetadataModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/8/2017.
 *
 * @author anil
 */
public class UpdateImportedMetadata implements IChainable {

    private static final String TAG = UpdateImportedMetadata.class.getSimpleName();
    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        String importId = (String) importContext.getMetadata().get(ServiceConstants.EXPORT_ID);
        String did = (String) importContext.getMetadata().get(ServiceConstants.DID);
        int count = getCount(importContext.getMetadata());

        ImportedMetadataModel importedMetadataModel = ImportedMetadataModel.build(appContext.getDBSession(), importId, did, count);
        if (ImportedMetadataModel.find(appContext.getDBSession(), importId, did) == null) {
            importedMetadataModel.save();
        } else {
            importedMetadataModel.update();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import profile failed.", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }

    private int getCount(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty() || !metadata.containsKey(ServiceConstants.EXPORT_TYPES)) {
            return 0;
        }
        String countKey = getTypes(metadata).contains(ServiceConstants.EXPORT_TYPE_TELEMETRY) ? ServiceConstants.EVENTS_COUNT : ServiceConstants.PROFILES_COUNT;
        try {
            return Integer.parseInt(String.valueOf(metadata.get(countKey)));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private List<String> getTypes(Map<String, Object> metadata) {
        return Arrays.asList(GsonUtil.fromJson((String) metadata.get(ServiceConstants.EXPORT_TYPES), String[].class));
    }
}

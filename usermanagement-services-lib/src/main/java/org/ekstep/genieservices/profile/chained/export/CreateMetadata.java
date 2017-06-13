package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CreateMetadata implements IChainable {

    private static final String TAG = CreateMetadata.class.getSimpleName();
    private IChainable nextLink;

    private List<String> userIds;
    private String destinationDBFilePath;

    public CreateMetadata(String destinationDBFilePath, List<String> userIds) {
        this.destinationDBFilePath = destinationDBFilePath;
        this.userIds = userIds;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        importContext.getDBSession().execute(MetaEntry.getCreateEntry());

        importContext.getMetadata().put(ServiceConstants.EXPORT_ID, UUID.randomUUID().toString());
        importContext.getMetadata().put(GETransferEventKnowStructure.FILE_SIZE, new File(destinationDBFilePath).length());
        importContext.getMetadata().put(ServiceConstants.PROFILES_COUNT, String.valueOf(userIds.size()));

        for (String key : importContext.getMetadata().keySet()) {
            MetadataModel metadataModel = MetadataModel.build(importContext.getDBSession(), key, String.valueOf(importContext.getMetadata().get(key)));
            metadataModel.save();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export profile failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}

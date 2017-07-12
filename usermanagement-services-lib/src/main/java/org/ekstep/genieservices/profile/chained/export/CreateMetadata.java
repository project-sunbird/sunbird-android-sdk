package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ProfileExportResponse;
import org.ekstep.genieservices.commons.bean.telemetry.GETransferEventKnowStructure;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.db.contract.MetaEntry;
import org.ekstep.genieservices.importexport.db.model.MetadataModel;
import org.ekstep.genieservices.profile.bean.ExportProfileContext;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class CreateMetadata implements IChainable<ProfileExportResponse, ExportProfileContext> {

    private static final String TAG = CreateMetadata.class.getSimpleName();
    private IChainable<ProfileExportResponse, ExportProfileContext> nextLink;

    private List<String> userIds;
    private String destinationDBFilePath;

    public CreateMetadata(String destinationDBFilePath, List<String> userIds) {
        this.destinationDBFilePath = destinationDBFilePath;
        this.userIds = userIds;
    }

    @Override
    public GenieResponse<ProfileExportResponse> execute(AppContext appContext, ExportProfileContext exportContext) {

        exportContext.getDBSession().execute(MetaEntry.getCreateEntry());

        exportContext.getMetadata().put(ServiceConstants.EXPORT_ID, UUID.randomUUID().toString());
        exportContext.getMetadata().put(GETransferEventKnowStructure.FILE_SIZE, new File(destinationDBFilePath).length());
        exportContext.getMetadata().put(ServiceConstants.PROFILES_COUNT, String.valueOf(userIds.size()));

        for (String key : exportContext.getMetadata().keySet()) {
            MetadataModel metadataModel = MetadataModel.build(exportContext.getDBSession(), key, String.valueOf(exportContext.getMetadata().get(key)));
            metadataModel.save();
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export profile failed", TAG);
        }
    }

    @Override
    public IChainable<ProfileExportResponse, ExportProfileContext> then(IChainable<ProfileExportResponse, ExportProfileContext> link) {
        nextLink = link;
        return link;
    }
}

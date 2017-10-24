package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ImportContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.util.List;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CreateContentImportManifest implements IChainable<List<ContentImportResponse>, ImportContentContext> {

    private static final String TAG = CreateContentImportManifest.class.getSimpleName();

    private IChainable<List<ContentImportResponse>, ImportContentContext> nextLink;

    @Override
    public GenieResponse<List<ContentImportResponse>> execute(AppContext appContext, ImportContentContext importContentContext) {
        List<ContentModel> contentModelList = ContentHandler.findAllContentsWithIdentifiers(appContext.getDBSession(), importContentContext.getIdentifiers());

        if (!CollectionUtil.isNullOrEmpty(contentModelList)) {
            ContentHandler.createAndWriteManifest(appContext, contentModelList);
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContentContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Import content failed", TAG);
        }
    }

    @Override
    public IChainable<List<ContentImportResponse>, ImportContentContext> then(IChainable<List<ContentImportResponse>, ImportContentContext> link) {
        nextLink = link;
        return link;
    }
}

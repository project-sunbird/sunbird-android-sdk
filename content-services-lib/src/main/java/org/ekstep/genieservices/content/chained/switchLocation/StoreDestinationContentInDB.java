package org.ekstep.genieservices.content.chained.switchLocation;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SwitchContentResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.SwitchContentContext;

import java.util.List;

public class StoreDestinationContentInDB implements IChainable<List<SwitchContentResponse>, SwitchContentContext> {

    private static final String TAG = StoreDestinationContentInDB.class.getSimpleName();

    @Override
    public GenieResponse<List<SwitchContentResponse>> execute(AppContext appContext, SwitchContentContext moveContentContext) {
        List<String> validContentIdsInDestn = moveContentContext.getValidContentIdsInDestination();

        if (!CollectionUtil.isNullOrEmpty(validContentIdsInDestn)) {
            // Read content in destination folder.
            for (String file : validContentIdsInDestn) {
                ContentHandler.addContentToDb(appContext, file, moveContentContext.getContentRootFolder(), false);
            }
        }

        return GenieResponseBuilder.getSuccessResponse(ServiceConstants.SUCCESS_RESPONSE);
    }

    @Override
    public IChainable<List<SwitchContentResponse>, SwitchContentContext> then(IChainable<List<SwitchContentResponse>, SwitchContentContext> link) {
        return link;
    }
}

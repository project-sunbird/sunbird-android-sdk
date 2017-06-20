package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ContentImportStep {

    private static final String TAG = ContentImportStep.class.getSimpleName();

    public static IChainable initImportContent() {
        return new IChainable() {
            private IChainable nextLink;

            @Override
            public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
                if (nextLink != null) {
                    return nextLink.execute(appContext, importContext);
                } else {
                    return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed", TAG);
                }
            }

            @Override
            public IChainable then(IChainable link) {
                nextLink = link;
                return link;
            }
        };
    }
}

package org.ekstep.genieservices.profile.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;

import java.io.File;

/**
 * Created on 6/10/2017.
 *
 * @author anil
 */
public class RemoveExportFile implements IChainable {

    private static final String TAG = RemoveExportFile.class.getSimpleName();
    private IChainable nextLink;

    private String destinationDBFilePath;

    public RemoveExportFile(String destinationDBFilePath) {
        this.destinationDBFilePath = destinationDBFilePath;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {

        File file = new File(destinationDBFilePath);
        file.delete();

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

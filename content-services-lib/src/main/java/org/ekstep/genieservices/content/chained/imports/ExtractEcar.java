package org.ekstep.genieservices.content.chained.imports;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentImportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Decompress;
import org.ekstep.genieservices.content.bean.ImportContentContext;

import java.io.File;
import java.util.List;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class ExtractEcar implements IChainable<List<ContentImportResponse>, ImportContentContext> {

    private static final String TAG = ExtractEcar.class.getSimpleName();
    private final File tmpLocation;

    private IChainable<List<ContentImportResponse>, ImportContentContext> nextLink;

    public ExtractEcar(File tmpLocation) {
        this.tmpLocation = tmpLocation;
    }

    @Override
    public GenieResponse<List<ContentImportResponse>> execute(AppContext appContext, ImportContentContext importContext) {
        tmpLocation.mkdirs();
        File ecarFile = new File(importContext.getEcarFilePath());

        if (Decompress.unzip(ecarFile, tmpLocation) && nextLink != null) {
            importContext.getMetadata().put(ServiceConstants.FILE_SIZE, ecarFile.length());
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.IMPORT_FAILED, "Import content failed while extracting ecar.", TAG);
        }
    }

    @Override
    public IChainable<List<ContentImportResponse>, ImportContentContext> then(IChainable<List<ContentImportResponse>, ImportContentContext> link) {
        nextLink = link;
        return link;
    }
}

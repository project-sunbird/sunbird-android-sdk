package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Compress;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CompressContent implements IChainable {

    private static final String TAG = CompressContent.class.getSimpleName();

    private IChainable nextLink;
    private List<ContentModel> contentModelsToExport;

    public CompressContent(List<ContentModel> contentModelsToExport) {
        this.contentModelsToExport = contentModelsToExport;
    }

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        int progressPercent = 0;

        List<Map<String, Object>> items = importContext.getItems();
        File path;
        Compress compressWorker;
        File payload;
        String artifactUrl;

        int jump;
        if (items.size() > 0) {
            jump = 90 / items.size();
        } else {
            jump = 90;
        }

        int iteration = 0;

        for (ContentModel contentModel : contentModelsToExport) {
            if (!ContentHandler.isAvailableLocally(contentModel.getContentState())) {
                continue;
            }

            artifactUrl = ContentHandler.readArtifactUrl(GsonUtil.fromJson(contentModel.getLocalData(), Map.class));

            if (!StringUtil.isNullOrEmpty(artifactUrl)) {
                payload = new File(importContext.getTmpLocation(), artifactUrl);
                path = new File(contentModel.getPath());

                if (artifactUrl.contains("." + ServiceConstants.FileExtension.APK)) {
                    compressWorker = new Compress(path, importContext.getTmpLocation(), artifactUrl);
                    try {
                        compressWorker.zipAPK();
                    } catch (IOException e) {
                        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
                    }
                } else {
                    compressWorker = new Compress(path, payload);
                    try {
                        compressWorker.zip();
                    } catch (IOException e) {
                        return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
                    }
                }

                iteration++;
                progressPercent = iteration * jump;
            }
        }

        progressPercent = 90;

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}

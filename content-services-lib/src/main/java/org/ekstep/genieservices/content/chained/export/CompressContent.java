package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.Compress;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ExportContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CompressContent implements IChainable<ContentExportResponse, ExportContentContext> {

    private static final String TAG = CompressContent.class.getSimpleName();

    private IChainable<ContentExportResponse, ExportContentContext> nextLink;

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ExportContentContext exportContext) {
        File path;
        File payload;
        String artifactUrl;

        int progressPercent = 0;
        int iteration = 0;
        int jump;
        if (exportContext.getItems().size() > 0) {
            jump = 90 / exportContext.getItems().size();
        } else {
            jump = 90;
        }

        for (ContentModel contentModel : exportContext.getContentModelsToExport()) {
            Map contentData = GsonUtil.fromJson(contentModel.getLocalData(), Map.class);

            if (!ContentHandler.isAvailableLocally(contentModel.getContentState())
                    || ContentHandler.isOnlineContent(contentData)
                    || ContentHandler.isInlineIdentity(ContentHandler.readContentDisposition(contentData), ContentHandler.readContentEncoding(contentData))) {
                continue;
            }

            artifactUrl = ContentHandler.readArtifactUrl(contentData);

            if (!StringUtil.isNullOrEmpty(artifactUrl)) {
                payload = new File(exportContext.getTmpLocation(), artifactUrl);
                path = new File(contentModel.getPath());

                try {
                    if (artifactUrl.contains("." + ServiceConstants.FileExtension.APK)) {
                        Compress.zipAPK(path, exportContext.getTmpLocation(), artifactUrl);
                    } else {
                        List<String> skippDirectoriesName = new ArrayList<>();
                        skippDirectoriesName.add(contentModel.getIdentifier());

                        List<String> skippFilesName = new ArrayList<>();
                        String sFileName = contentModel.getIdentifier() + "/" + FileUtil.MANIFEST_FILE_NAME;
                        skippFilesName.add(sFileName);

                        Compress.zip(path, payload, skippDirectoriesName, skippFilesName);
                    }
                } catch (IOException e) {
                    return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
                }

                iteration++;
                progressPercent = iteration * jump;
            }
        }
        progressPercent = 90;

        if (nextLink != null) {
            return nextLink.execute(appContext, exportContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable<ContentExportResponse, ExportContentContext> then(IChainable<ContentExportResponse, ExportContentContext> link) {
        nextLink = link;
        return link;
    }
}

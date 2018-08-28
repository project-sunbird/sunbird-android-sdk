package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.ExportContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created on 6/19/2017.
 *
 * @author anil
 */
public class CopyAsset implements IChainable<ContentExportResponse, ExportContentContext> {

    private static final String TAG = CopyAsset.class.getSimpleName();

    private IChainable<ContentExportResponse, ExportContentContext> nextLink;

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ExportContentContext exportContext) {
        try {
            int i = 0;
            for (ContentModel contentModel : exportContext.getContentModelsToExport()) {
                Map contentData = exportContext.getItems().get(i);

                String appIcon = ContentHandler.readAppIcon(contentData);
                if (!StringUtil.isNullOrEmpty(appIcon)) {
                    copyAsset(contentModel.getPath(), exportContext.getTmpLocation(), appIcon);
                }

                String posterImage = ContentHandler.readPosterImage(contentData);
                if (!StringUtil.isNullOrEmpty(posterImage)) {
                    copyAsset(contentModel.getPath(), exportContext.getTmpLocation(), posterImage);
                }

                String grayScaleAppIcon = ContentHandler.readGrayScaleAppIcon(contentData);
                if (!StringUtil.isNullOrEmpty(grayScaleAppIcon)) {
                    copyAsset(contentModel.getPath(), exportContext.getTmpLocation(), grayScaleAppIcon);
                }

                if (ContentHandler.isInlineIdentity(ContentHandler.readContentDisposition(contentData), ContentHandler.readContentEncoding(contentData))) {
                    String artifactUrl = ContentHandler.readArtifactUrl(contentData);
                    if (!StringUtil.isNullOrEmpty(artifactUrl)) {
                        copyAsset(contentModel.getPath(), exportContext.getTmpLocation(), artifactUrl);
                    }
                }

                i++;
            }
        } catch (IOException e) {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
        }

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

    private void copyAsset(String sourcePath, File destinationPath, String appIcon) throws IOException {
        File source = new File(sourcePath, appIcon);

        if (source.exists()) {
            File dest = new File(destinationPath, appIcon);
            dest.getParentFile().mkdirs();

            FileUtil.cp(source, dest);
        }
    }
}

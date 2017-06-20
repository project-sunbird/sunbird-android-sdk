package org.ekstep.genieservices.content.chained.export;

import org.ekstep.genieservices.ServiceConstants;
import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.GenieResponseBuilder;
import org.ekstep.genieservices.commons.bean.ContentExportResponse;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.ImportContext;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;
import org.ekstep.genieservices.content.ContentHandler;
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
public class CopyAsset implements IChainable<ContentExportResponse> {

    private static final String TAG = CopyAsset.class.getSimpleName();

    private IChainable<ContentExportResponse> nextLink;
    private List<ContentModel> contentModelsToExport;

    public CopyAsset(List<ContentModel> contentModelsToExport) {
        this.contentModelsToExport = contentModelsToExport;
    }

    @Override
    public GenieResponse<ContentExportResponse> execute(AppContext appContext, ImportContext importContext) {

        List<String> assets;
        int i = 0;

        for (ContentModel contentModel : contentModelsToExport) {
            assets = new ArrayList<>();
            Map item = importContext.getItems().get(i);

            String appIcon = ContentHandler.readAppIcon(item);
            if (!StringUtil.isNullOrEmpty(appIcon)) {
                assets.add(appIcon);
            }

            String posterImage = ContentHandler.readPosterImage(item);
            if (!StringUtil.isNullOrEmpty(posterImage)) {
                assets.add(posterImage);
            }

            String grayScaleAppIcon = ContentHandler.readGrayScaleAppIcon(item);
            if (!StringUtil.isNullOrEmpty(grayScaleAppIcon)) {
                assets.add(grayScaleAppIcon);
            }

            for (String asset : assets) {
                if (!StringUtil.isNullOrEmpty(asset)) {
                    File source = new File(contentModel.getPath(), asset);

                    if (source.exists()) {
                        File dest = new File(importContext.getTmpLocation(), asset);
                        dest.getParentFile().mkdirs();

                        try {
                            FileUtil.cp(source, dest);
                        } catch (IOException e) {
                            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, e.getMessage(), TAG);
                        }
                    }
                }
            }

            i++;
        }

        if (nextLink != null) {
            return nextLink.execute(appContext, importContext);
        } else {
            return GenieResponseBuilder.getErrorResponse(ServiceConstants.ErrorCode.EXPORT_FAILED, "Export content failed", TAG);
        }
    }

    @Override
    public IChainable<ContentExportResponse> then(IChainable<ContentExportResponse> link) {
        nextLink = link;
        return link;
    }
}

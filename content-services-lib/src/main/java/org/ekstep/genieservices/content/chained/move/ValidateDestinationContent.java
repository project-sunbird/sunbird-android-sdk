package org.ekstep.genieservices.content.chained.move;

import com.google.gson.internal.LinkedTreeMap;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.commons.utils.FileUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.MoveContentContext;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/26/2017.
 *
 * @author anil
 */
public class ValidateDestinationContent implements IChainable<Void, MoveContentContext> {

    private static final String TAG = ValidateDestinationContent.class.getSimpleName();

    private IChainable<Void, MoveContentContext> nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, MoveContentContext moveContentContext) {
        // Read content in destination folder.
        String files[] = moveContentContext.getDestinationFolder().list();
        if (files != null) {
            for (String file : files) {
                File destFile = new File(moveContentContext.getDestinationFolder(), file);
                if (destFile.isDirectory()) {
                    String manifestJson = FileUtil.readManifest(destFile);
                    if (manifestJson == null) {
                        continue;
                    }

                    LinkedTreeMap map = GsonUtil.fromJson(manifestJson, LinkedTreeMap.class);

                    String manifestVersion = (String) map.get("ver");
                    if (manifestVersion.equals("1.0")) {
                        continue;
                    }

                    LinkedTreeMap archive = (LinkedTreeMap) map.get("archive");
                    List<Map<String, Object>> items = null;
                    if (archive.containsKey("items")) {
                        items = (List<Map<String, Object>>) archive.get("items");
                    }

                    if (items == null || items.isEmpty()) {
                        continue;
                    }

                    Logger.d(TAG, items.toString());

                    for (Map<String, Object> item : items) {
                        String visibility = ContentHandler.readVisibility(item);

                        // If compatibility level is not in range then do not copy artifact
                        if (ContentConstants.Visibility.PARENT.equals(visibility)
                                || !ContentHandler.isCompatible(appContext, ContentHandler.readCompatibilityLevel(item))) {
                            continue;
                        }

                        boolean isDraftContent = ContentHandler.isDraftContent(ContentHandler.readStatus(item));
                        //Draft content expiry .To prevent import of draft content if the expires date is lesser than from the current date.
                        if (isDraftContent && ContentHandler.isExpired(ContentHandler.readExpiryDate(item))) {
                            continue;
                        }

                        moveContentContext.getValidContentIdsInDestination().add(file);
                    }
                }
            }
        }

        return nextLink.execute(appContext, moveContentContext);
    }

    @Override
    public IChainable<Void, MoveContentContext> then(IChainable<Void, MoveContentContext> link) {
        nextLink = link;
        return link;
    }
}

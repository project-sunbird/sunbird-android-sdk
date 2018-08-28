package org.ekstep.genieservices.content.chained.switchLocation;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.SwitchContentResponse;
import org.ekstep.genieservices.commons.chained.IChainable;
import org.ekstep.genieservices.content.ContentConstants;
import org.ekstep.genieservices.content.ContentHandler;
import org.ekstep.genieservices.content.bean.SwitchContentContext;
import org.ekstep.genieservices.content.db.model.ContentModel;

import java.util.List;

/**
 * Created on 9/25/2017.
 *
 * @author anil
 */
public class DeleteSourceFolder implements IChainable<List<SwitchContentResponse>, SwitchContentContext> {

    private IChainable<List<SwitchContentResponse>, SwitchContentContext> nextLink;

    @Override
    public GenieResponse<List<SwitchContentResponse>> execute(AppContext appContext, SwitchContentContext switchContentContext) {

        List<ContentModel> contentsInSource = ContentHandler.findAllContent(appContext.getDBSession());
        if (contentsInSource != null && contentsInSource.size() > 0) {
            switchContentContext.getContentsInSource().addAll(contentsInSource);

            for (ContentModel contentModel : switchContentContext.getContentsInSource()) {
                contentModel.addOrUpdateContentState(ContentConstants.State.ONLY_SPINE);
                contentModel.addOrUpdateRefCount(0);
                contentModel.setVisibility(ContentConstants.Visibility.PARENT);
                contentModel.update();
            }
        }

        return nextLink.execute(appContext, switchContentContext);
    }

    @Override
    public IChainable<List<SwitchContentResponse>, SwitchContentContext> then(IChainable<List<SwitchContentResponse>, SwitchContentContext> link) {
        nextLink = link;
        return link;
    }
}

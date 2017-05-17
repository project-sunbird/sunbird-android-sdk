package org.ekstep.genieservices.content.chained;

import org.ekstep.genieservices.commons.AppContext;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.content.bean.ImportContext;

/**
 * Created on 5/16/2017.
 *
 * @author anil
 */
public class EcarCleanUp implements IChainable {

    private static final String TAG = EcarCleanUp.class.getSimpleName();

    private IChainable nextLink;

    @Override
    public GenieResponse<Void> execute(AppContext appContext, ImportContext importContext) {
        return null;
    }

    @Override
    public Void postExecute() {
        return null;
    }

    @Override
    public GenieResponse<Void> breakChain() {
        return null;
    }

    @Override
    public IChainable then(IChainable link) {
        nextLink = link;
        return link;
    }
}

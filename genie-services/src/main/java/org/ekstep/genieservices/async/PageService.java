package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.IPageService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PageAssemble;
import org.ekstep.genieservices.commons.bean.PageAssembleCriteria;

/**
 * Created by souvikmondal on 21/3/18.
 */

public class PageService {

    private IPageService pageService;

    public PageService(GenieService genieService) {
        this.pageService = genieService.getPageService();
    }

    public void getPageAssemble(final PageAssembleCriteria pageAssembleCriteria,
                                IResponseHandler<PageAssemble> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable() {
            @Override
            public GenieResponse perform() {
                return pageService.getPageAssemble(pageAssembleCriteria);
            }
        }, responseHandler);
    }

}

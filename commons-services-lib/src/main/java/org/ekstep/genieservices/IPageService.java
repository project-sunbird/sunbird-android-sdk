package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PageAssemble;
import org.ekstep.genieservices.commons.bean.PageAssembleCriteria;

/**
 * Created by souvikmondal on 21/3/18.
 */

public interface IPageService {

    GenieResponse<PageAssemble> getPageAssemble(PageAssembleCriteria pageAssembleCriteria);

}

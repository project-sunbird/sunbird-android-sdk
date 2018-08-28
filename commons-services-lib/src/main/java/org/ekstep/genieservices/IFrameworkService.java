package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.Channel;
import org.ekstep.genieservices.commons.bean.ChannelDetailsRequest;
import org.ekstep.genieservices.commons.bean.Framework;
import org.ekstep.genieservices.commons.bean.FrameworkDetailsRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * This is the interface with all the required APIs to get framework specific data.
 */
public interface IFrameworkService {

    GenieResponse<Channel> getChannelDetails(ChannelDetailsRequest channelDetailsRequest);

    GenieResponse<Framework> getFrameworkDetails(FrameworkDetailsRequest frameworkDetailsRequest);

    GenieResponse<Void> persistFrameworkDetails(String responseBody);
}

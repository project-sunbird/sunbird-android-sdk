package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;

import java.util.List;

/**
 * Created by swayangjit on 12/5/17.
 */

public interface ITagService {

    GenieResponse<Void> setTag(Tag tag);

    GenieResponse<List<Tag>> getTags();

    GenieResponse<Void> deleteTag(String name);

    GenieResponse<Void> updateTag(Tag tag);
}

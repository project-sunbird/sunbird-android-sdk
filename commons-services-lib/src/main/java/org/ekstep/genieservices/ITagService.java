package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;

import java.util.List;

/**
 * This is the interface with all the required APIs to perform tagging related
 * operations.
 */

public interface ITagService {

    /**
     * This api is used to set the tag.
     * <p><p>
     * On successful setting the tag, the response will have status set to be TRUE.
     * <p><p>
     * On failing to set to tag, the response will have status set to be FALSE, with the following error
     * <p>VALIDATION_ERROR
     *
     * @param tag - {@link Tag} should be passes as parameter
     * @return
     */
    GenieResponse<Void> setTag(Tag tag);

    /**
     * This api gets all the tags that are present.
     * <p><p>
     * Response will always have the status set to TRUE and the {@link List<Tag>} will be set in result.
     *
     * @return
     */
    GenieResponse<List<Tag>> getTags();

    /**
     * This api is used to delete the tag.
     * <p><p>
     * On successful deleting the tag, the response will have status set to be TRUE.
     * <p><p>
     * On failing to delete the tag, the response will have status set to be FALSE, with the following error
     * <p>VALIDATION_ERROR
     *
     * @param name
     * @return
     */
    GenieResponse<Void> deleteTag(String name);

    /**
     * This api is used to update the tag.
     * <p><p>
     * On successful updating the tag, the response will have the status set to be TRUE.
     * <p><p>
     * On failing to update the tag, the response will have status set to be FALSE, with the following error
     * <p>VALIDATION_ERROR
     *
     * @param tag
     * @return
     */
    GenieResponse<Void> updateTag(Tag tag);
}

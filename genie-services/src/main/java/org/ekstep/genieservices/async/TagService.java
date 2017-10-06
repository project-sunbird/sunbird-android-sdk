package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ITagService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;

import java.util.List;

/**
 * This class provides all the required APIs to perform tagging related
 * operations on a separate thread.
 */
public class TagService {
    private ITagService tagService;

    public TagService(GenieService genieService) {
        this.tagService = genieService.getTagService();
    }

    /**
     * This api is used to set the tag.
     * <p><p>
     * On successful setting the tag, the response will have status set to be TRUE.
     * <p><p>
     * On failing to set to tag, the response will have status set to be FALSE, with the following error
     * <p>VALIDATION_ERROR
     *
     * @param tag - {@link Tag}
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void setTag(final Tag tag, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return tagService.setTag(tag);
            }
        }, responseHandler);
    }

    /**
     * This api gets all the tags that are present.
     * <p><p>
     * Response will always have the status set to TRUE and the {@link List<Tag>} will be set in result.
     *
     * @param responseHandler - {@link IResponseHandler<List<Tag>>}
     */
    public void getTags(IResponseHandler<List<Tag>> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<List<Tag>>() {
            @Override
            public GenieResponse<List<Tag>> perform() {
                return tagService.getTags();
            }
        }, responseHandler);
    }

    /**
     * This api is used to delete the tag.
     * <p><p>
     * On successful deleting the tag, the response will have status set to be TRUE.
     * <p><p>
     * On failing to delete the tag, the response will have status set to be FALSE, with the following error
     * <p>VALIDATION_ERROR
     *
     * @param name
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void deleteTag(final String name, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return tagService.deleteTag(name);
            }
        }, responseHandler);
    }

    /**
     * This api is used to update the tag.
     * <p><p>
     * On successful updating the tag, the response will have the status set to be TRUE.
     * <p><p>
     * On failing to update the tag, the response will have status set to be FALSE, with the following error
     * <p>VALIDATION_ERROR
     *
     * @param tag
     * @param responseHandler - {@link IResponseHandler<Void>}
     */
    public void updateTag(final Tag tag, IResponseHandler<Void> responseHandler) {
        ThreadPool.getInstance().execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return tagService.updateTag(tag);
            }
        }, responseHandler);
    }

}

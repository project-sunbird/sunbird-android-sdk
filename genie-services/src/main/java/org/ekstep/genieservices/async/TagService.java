package org.ekstep.genieservices.async;

import org.ekstep.genieservices.GenieService;
import org.ekstep.genieservices.ITagService;
import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.Tag;

import java.util.List;

public class TagService {
    private ITagService tagService;

    public TagService(GenieService genieService) {
        //this.tagService = genieService.getTagService();
    }

    public void setTag(final Tag tag, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return tagService.setTag(tag);
            }
        });
    }

    public void getTags(IResponseHandler<List<Tag>> responseHandler) {
        new AsyncHandler<List<Tag>>(responseHandler).execute(new IPerformable<List<Tag>>() {
            @Override
            public GenieResponse<List<Tag>> perform() {
                return tagService.getTags();
            }
        });
    }

    public void deleteTag(final String name, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return tagService.deleteTag(name);
            }
        });
    }

    public void updateTag(final Tag tag, IResponseHandler<Void> responseHandler) {
        new AsyncHandler<Void>(responseHandler).execute(new IPerformable<Void>() {
            @Override
            public GenieResponse<Void> perform() {
                return tagService.updateTag(tag);
            }
        });
    }

}

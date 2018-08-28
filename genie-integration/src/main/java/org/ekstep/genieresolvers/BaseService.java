package org.ekstep.genieresolvers;

import org.ekstep.genieservices.commons.IResponseHandler;

/**
 * Created on 26/5/17.
 * shriharsh
 */
public class BaseService {

    protected void createAndExecuteTask(IResponseHandler responseHandler, BaseTask task) {
        new TaskHandler(responseHandler).execute(task);
    }
}

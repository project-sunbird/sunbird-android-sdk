package org.ekstep.genieresolvers;

import android.os.AsyncTask;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 22/5/17.
 */
public class TaskHandler extends AsyncTask<BaseTask, Void, GenieResponse> {

    private IResponseHandler handler;

    public TaskHandler(IResponseHandler handler) {
        this.handler = handler;
    }

    @Override
    protected GenieResponse doInBackground(BaseTask... params) {
        GenieResponse response = params[0].perform();
        return response;
    }

    @Override
    protected void onPostExecute(GenieResponse genieResponse) {
        if (genieResponse.getStatus()) {
            handler.onSuccess(genieResponse);
        } else {
            handler.onError(genieResponse);
        }
    }
}

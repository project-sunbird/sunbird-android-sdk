package org.ekstep.genieservices.async;

import android.os.AsyncTask;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.Logger;

/**
 * Created on 22/5/17.
 */

public class AsyncHandler<T> extends AsyncTask<IPerformable<T>, Void, GenieResponse<T>> {

    private IResponseHandler<T> handler;

    public AsyncHandler(IResponseHandler<T> handler) {
        this.handler = handler;
    }

    @Override
    protected GenieResponse<T> doInBackground(IPerformable<T>... params) {
        GenieResponse<T> response = params[0].perform();
        if (Logger.isDebugEnabled()) Logger.d("GenieAsyncResponse", GsonUtil.toJson(response));
        return response;
    }

    @Override
    protected void onPostExecute(GenieResponse<T> genieResponse) {
        if (genieResponse.getStatus()) {
            handler.onSuccess(genieResponse);
        } else {
            handler.onError(genieResponse);
        }
    }
}

package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.utils.GsonUtil;

/**
 * Created by swayangjit on 27/4/17.
 */

public abstract class BaseTelemetry extends TelemetryEvent {

    public GameData getGameData(){
        return new GameData("genieservice.android","");
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    protected abstract String getEID();
}

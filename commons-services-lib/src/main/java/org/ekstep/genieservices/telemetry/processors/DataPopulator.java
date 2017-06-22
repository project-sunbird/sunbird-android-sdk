package org.ekstep.genieservices.telemetry.processors;

import org.ekstep.genieservices.commons.IDeviceInfo;
import org.ekstep.genieservices.commons.db.operations.IDBSession;
import org.ekstep.genieservices.commons.utils.DateUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.telemetry.model.EventsModel;
import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by swayangjit on 26/4/17.
 */

public class DataPopulator implements IProcessEvent {
    private EventsModel events;
    private IDeviceInfo deviceInfo;
    private IDBSession mDbSession;

    public DataPopulator(IDBSession dbSession,EventsModel events, IDeviceInfo deviceInfo) {
        this.mDbSession=dbSession;
        this.events = events;
        this.deviceInfo = deviceInfo;
    }

    @Override
    public ProcessedEventModel process(ProcessedEventModel processedEvent) {
        String msgId = UUID.randomUUID().toString();
        HashMap<String, Object> processedEventMap = new HashMap<>();
        processedEventMap.put("id", "ekstep.telemetry");
        processedEventMap.put("ver", "1.0");
        processedEventMap.put("ts", DateUtil.getCurrentTimestamp());
        processedEventMap.put("params", getParams(msgId));
        processedEventMap.put("events", events.getEventsMap());
        byte[] data = GsonUtil.toJson(processedEventMap).getBytes();
        return ProcessedEventModel.build(mDbSession,msgId, data, events.size(), events.getPriority());
    }

    private HashMap<String, String> getParams(String msgId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("did", deviceInfo.getDeviceID());
        params.put("msgid", msgId);
        params.put("key", "");
        params.put("requesterId", "");
        return params;
    }
}

//package org.ekstep.genieservices.telemetry.processors;
//
//import com.google.gson.Gson;
//
//import org.ekstep.genieservices.commons.utils.DateUtil;
//import org.ekstep.genieservices.telemetry.model.Events;
//import org.ekstep.genieservices.telemetry.model.ProcessedEvent;
//
//import java.util.HashMap;
//import java.util.UUID;
//
///**
// * Created by swayangjit on 26/4/17.
// */
//
//public class DataPopulator implements IProcessEvent {
//    private Events events;
//    private DeviceInfo deviceInfo;
//
//    public DataPopulator(Events events, DeviceInfo deviceInfo) {
//        this.events = events;
//        this.deviceInfo = deviceInfo;
//    }
//
//    @Override
//    public ProcessedEvent process(ProcessedEvent processedEvent) {
//        String msgId = UUID.randomUUID().toString();
//        HashMap<String, Object> processedEventMap = new HashMap<>();
//        processedEventMap.put("id", "ekstep.telemetry");
//        processedEventMap.put("ver", "1.0");
//        processedEventMap.put("ts", DateUtil.getCurrentTimestamp());
//        processedEventMap.put("params", getParams(msgId));
//        processedEventMap.put("events", events.getEventsMap());
//        byte[] data = new Gson().toJson(processedEventMap).getBytes();
//        return new ProcessedEvent(msgId, data, events.size(), events.getPriority());
//    }
//
//    private HashMap<String, String> getParams(String msgId) {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("did", deviceInfo.getDeviceID());
//        params.put("msgid", msgId);
//        params.put("key", "");
//        params.put("requesterId", "");
//        return params;
//    }
//}

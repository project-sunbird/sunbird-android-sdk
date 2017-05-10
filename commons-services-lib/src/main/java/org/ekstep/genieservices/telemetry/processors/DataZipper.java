package org.ekstep.genieservices.telemetry.processors;

import org.ekstep.genieservices.telemetry.model.ProcessedEventModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by swayangjit on 26/4/17.
 */

public class DataZipper implements IProcessEvent {
    @Override
    public ProcessedEventModel process(ProcessedEventModel processedEvent) {

        String data = new String(processedEvent.getData());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length());
        try {
            GZIPOutputStream zipOutputStream = new GZIPOutputStream(outputStream);
            zipOutputStream.write(data.getBytes());
            zipOutputStream.close();
            byte[] compressedStream = outputStream.toByteArray();
            outputStream.close();
            processedEvent.setData(compressedStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processedEvent;
    }
}

package org.ekstep.genieservices.commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by shriharsh on 20/4/17.
 */

public class FileUtil {

    public static String readFileFromClasspath(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(FileUtil.class.getResourceAsStream("/" + filename)));;
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDownloadedFileLocation(FileInputStream fileInputStream) {
        File targetFile = null;
        try {
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            targetFile = FileHandler.getTempLocation(new File(System.currentTimeMillis() + ".ecar"));
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return targetFile.toString();
    }

}

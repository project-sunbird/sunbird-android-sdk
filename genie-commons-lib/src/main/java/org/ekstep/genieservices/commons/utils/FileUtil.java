package org.ekstep.genieservices.commons.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

}

package org.ekstep.genieservices.commons;

import java.util.List;
import java.util.Map;

/**
 * Created by anil on 6/23/2017.
 */
public interface IAPKInstaller {
    void showInstallAPKPrompt(String destPath, String downloadUrl, List<Map<String, Object>> preRequisites);
}

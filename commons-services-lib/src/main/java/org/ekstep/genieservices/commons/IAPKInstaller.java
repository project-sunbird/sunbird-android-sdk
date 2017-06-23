package org.ekstep.genieservices.commons;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anil on 6/23/2017.
 */
public interface IAPKInstaller {
    void showInstallAPKPrompt(String destPath, String downloadUrl, List<HashMap<String, Object>> preRequisites);
}

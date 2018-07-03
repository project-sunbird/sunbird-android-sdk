package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.FormRequest;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Created by swayangjit on 29/5/18.
 */
public interface IFormService {

    GenieResponse<Map<String, Object>> getForm(FormRequest formRequest);
}

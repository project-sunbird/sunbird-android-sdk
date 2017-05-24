package org.ekstep.genieservices;

import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created on 5/23/2017.
 *
 * @author anil
 */
public interface ILanguageService {

    GenieResponse<String> getLanguageTraversalRule(String languageId);

    GenieResponse<String> getLanguageSearch(String requestData);
}

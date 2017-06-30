package org.ekstep.genieservices.commons;

import java.util.List;

/**
 * Created on 24/4/17.
 *
 * @author shriharsh
 */
public interface IValidate {

    boolean isValid();

    List<String> getErrors();
}

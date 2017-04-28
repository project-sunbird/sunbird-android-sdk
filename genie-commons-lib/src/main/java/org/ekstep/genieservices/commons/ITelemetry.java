package org.ekstep.genieservices.commons;

import java.util.List;

/**
 * Created by swayangjit on 27/4/17.
 */

public interface ITelemetry {

    String getEID();

    boolean isValid();

    List<String> getErrors();}

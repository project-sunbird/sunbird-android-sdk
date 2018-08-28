package org.ekstep.genieservices.commons;

import android.content.Context;
import android.content.Intent;

import org.ekstep.genieservices.commons.bean.Content;

/**
 * Created on 7/17/2017.
 *
 * @author anil
 */
public interface IPlayerConfig {
    Intent getPlayerIntent(Context context, Content content);
}

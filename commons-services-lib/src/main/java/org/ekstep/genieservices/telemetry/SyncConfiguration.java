package org.ekstep.genieservices.telemetry;

import org.ekstep.genieservices.commons.network.IConnectionInfo;

/**
 * Created by swayangjit on 8/5/17.
 */

public enum SyncConfiguration {

    MANUAL {
        @Override
        public boolean canSync(IConnectionInfo connectionInfo) {
            return false;
        }
    },
    OVER_WIFI_ONLY {
        @Override
        public boolean canSync(IConnectionInfo connectionInfo) {
            return connectionInfo.isConnectedOverWifi();
        }
    },
    NO_CONFIG {
        @Override
        public boolean canSync(IConnectionInfo connectionInfo) {
            return true;
        }
    },
    OVER_ANY_MODE {
        @Override
        public boolean canSync(IConnectionInfo connectionInfo) {
            return connectionInfo.isConnected();
        }
    };


    @Override
    public String toString() {
        return name();
    }

    public abstract boolean canSync(IConnectionInfo connectionInfo);
}

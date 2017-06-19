package org.ekstep.genieservices.commons.bean;

import org.ekstep.genieservices.commons.bean.enums.NotificationStatus;

/**
 * Created by Indraja Machani on 6/19/2017.
 */

public class NotificationFilterCriteria {
    private NotificationStatus notificationStatus;

    private NotificationFilterCriteria(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public static class Builder {
        private NotificationStatus notificationStatus;

        public Builder notificationStatus(NotificationStatus notificationStatus) {
            this.notificationStatus = notificationStatus;
            return this;
        }

        public NotificationFilterCriteria build() {
            return new NotificationFilterCriteria(notificationStatus);
        }

    }

}

package org.ekstep.genieservices.commons.bean;

import java.util.List;

/**
 * @author indraja on 02/04/18.
 */
public class UserInboxAnnouncements {
    private int count;
    private List<Announcement> announcements;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }
}

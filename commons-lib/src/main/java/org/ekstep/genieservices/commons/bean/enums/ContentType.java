package org.ekstep.genieservices.commons.bean.enums;

import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 5/11/2017.
 *
 * @author anil
 */
public enum ContentType {

    GAME("Game"), STORY("Story"), WORKSHEET("Worksheet"),
    COLLECTION("Collection"), TEXTBOOK("Textbook"), TEXTBOOK_UNIT("TextbookUnit"),
    COURSE("Course"), LESSON_PLAN("LessonPlan");

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public static String getCommaSeparatedContentTypes(ContentType[] contentTypes) {
        if (contentTypes == null) {
            contentTypes = ContentType.values();
        }

        List<String> contentTypeList = new ArrayList<>();
        for (ContentType contentType : contentTypes) {
            contentTypeList.add(contentType.getValue().toLowerCase());
        }

        return StringUtil.join("','", contentTypeList);
    }

    public String getValue() {
        return value;
    }

}

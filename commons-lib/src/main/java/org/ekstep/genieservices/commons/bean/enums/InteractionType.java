package org.ekstep.genieservices.commons.bean.enums;

/**
 * Created by swayangjit on 2/5/17.
 * <p/>
 * type of interaction TOUCH,DRAG,DROP,PINCH,ZOOM,SHAKE,ROTATE,SPEAK,LISTEN,WRITE,DRAW,START,END,CHOOSE,ACTIVATE,SHOW,HIDE,OTHER
 */

public enum InteractionType {

    TOUCH("TOUCH"), DRAG("DRAG"), DROP("DROP"),
    PINCH("PINCH"), ZOOM("ZOOM"),
    SHAKE("SHAKE"), ROTATE("ROTATE"),
    SPEAK("SPEAK"), LISTEN("LISTEN"), WRITE("WRITE"), DRAW("DRAW"),
    START("START"), END("END"),
    CHOOSE("CHOOSE"), ACTIVATE("ACTIVATE"),
    SHOW("SHOW"), HIDE("HIDE"),
    OTHER("OTHER");

    private String value;

    InteractionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

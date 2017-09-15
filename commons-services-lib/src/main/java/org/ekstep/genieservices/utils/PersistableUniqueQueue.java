package org.ekstep.genieservices.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ekstep.genieservices.commons.db.cache.IKeyValueStore;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PersistableUniqueQueue<Element> {

    private static final int INITIAL_SIZE = 50;
    private static final float LOAD_FACTOR = 0.75f;

    private Set<Element> elementSet;
    private IKeyValueStore keyValueStore;
    private String tag;

    public PersistableUniqueQueue(IKeyValueStore keyValueStore, String tag) {
        this(keyValueStore, tag, INITIAL_SIZE);
    }

    public PersistableUniqueQueue(IKeyValueStore keyValueStore, String tag, String persistedData) {
        this(keyValueStore, tag, INITIAL_SIZE);
        read(persistedData);
    }

    public PersistableUniqueQueue(IKeyValueStore keyValueStore, String tag, int initialSize) {
        this.elementSet = new LinkedHashSet<>(initialSize, LOAD_FACTOR);
        this.keyValueStore = keyValueStore;
        this.tag = tag;
    }

    private void read(String persistedData) {
        Gson gson = new GsonBuilder().create();
        elementSet.addAll(gson.fromJson(persistedData, List.class));
    }

    public int size() {
        return elementSet.size();
    }

    public void push(Element element) {
        Gson gson = new GsonBuilder().create();
        elementSet.add(element);
        keyValueStore.putString(tag, gson.toJson(element));
    }

    public Element peek() {
        if (elementSet.size() == 0)
            return null;
        return elementSet.iterator().next();
    }

    public Element pop() {
        if (elementSet.size() == 0)
            return null;
        Iterator<Element> iterator = elementSet.iterator();
        Element element = iterator.next();
        iterator.remove();
        if (elementSet.size() == 0) {
            keyValueStore.remove(tag);
        } else {
            Gson gson = new GsonBuilder().create();
            keyValueStore.putString(tag, gson.toJson(elementSet));
        }
        return element;
    }


}

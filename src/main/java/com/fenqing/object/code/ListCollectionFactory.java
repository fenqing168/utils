package com.fenqing.object.code;

import com.fenqing.object.code.CollectionFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * List
 * @author fenqing
 */
public class ListCollectionFactory implements CollectionFactory {

    private List<Object> list = new LinkedList<>();

    @Override
    public void add(Object t) {
        list.add(t);
    }

    @Override
    public List<?> getCollection() {
        return list;
    }
}
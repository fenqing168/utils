package com.fenqing.object.code;

import com.fenqing.object.code.CollectionFactory;

import java.util.HashSet;
import java.util.Set;


/**
 * set工厂
 * @author fenqing
 */
public class SetCollectionFactory implements CollectionFactory {

    private Set<Object> set = new HashSet<>();

    @Override
    public void add(Object t) {
        set.add(t);
    }

    @Override
    public Set<?> getCollection() {
        return set;
    }
}
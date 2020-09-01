package com.fenqing.object.code;

/**
 * 数组
 * @author fenqing
 */
public class ArrayCollectionFactory implements CollectionFactory {

    private ListCollectionFactory tListCollectionFactory = new ListCollectionFactory();

    @Override
    public void add(Object t) {
        tListCollectionFactory.add(t);
    }

    @Override
    public Object getCollection() {
        return tListCollectionFactory.getCollection().toArray();
    }
}
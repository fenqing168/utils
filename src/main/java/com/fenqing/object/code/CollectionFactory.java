package com.fenqing.object.code;

import java.util.List;
import java.util.Set;

/**
 * @author fenqing
 */
public interface CollectionFactory{
    /**
     * 添加元素
     * @param t
     */
    void add(Object t);

    /**
     * 获取集合
     * @return
     */
    Object getCollection();

    static CollectionFactory getInstance(Class<?> tClass){
        if(tClass.isArray()){
            return new ArrayCollectionFactory();
        }else if(List.class.isAssignableFrom(tClass)){
            return new ListCollectionFactory();
        }else if(Set.class.isAssignableFrom(tClass)){
            return new SetCollectionFactory();
        }
        return null;
    }
}




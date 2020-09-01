package com.fenqing.object.bean;

import lombok.Data;

/**
 * @author fenqing
 */
@Data
public class Kv<K, V>{
    private K k;
    private V v;
}
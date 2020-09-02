package com.fenqing.object.bean;

import lombok.*;

/**
 * @author fenqing
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kv<K, V>{
    private K k;
    private V v;
}
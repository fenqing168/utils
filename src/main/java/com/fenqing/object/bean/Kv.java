package com.fenqing.object.bean;

import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Kv<?, ?> kv = (Kv<?, ?>) o;
        return Objects.equals(k, kv.k) &&
                Objects.equals(v, kv.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, v);
    }
}
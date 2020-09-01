package com.fenqing.object.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fenqing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareResults<T> {
    private List<T> as;
    private List<T> disagree;
    private List<T> news;
}

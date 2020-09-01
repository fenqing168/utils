package com.fenqing.object.bean;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * @author fenqing
 */
@Data
@Builder
public class MatchMapper {
    private Collection<?> src;
    private String targetMapField;
    private String srcMapField;
    private String targetValueField;
    private String srcValueField;
}
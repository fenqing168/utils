package com.fenqing.excel.bean;

import java.util.ArrayList;

/**
 * @author fenqing
 */
public class ExcelPageList extends ArrayList<Object> {

    private int pageSize;

    private int[] repetitionItemIndexs;

    private String direction;

    public ExcelPageList(int pageSize, String direction) {
        this.pageSize = pageSize;
        this.direction = direction;
    }

    public ExcelPageList(int pageSize, int[] repetitionItemIndexs, String direction) {
        this.pageSize = pageSize;
        this.repetitionItemIndexs = repetitionItemIndexs;
        this.direction = direction;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRepetitionItemKeys(int[] repetitionItemIndexs) {
        this.repetitionItemIndexs = repetitionItemIndexs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int[] getRepetitionItemIndexs() {
        return repetitionItemIndexs;
    }

    public void setRepetitionItemIndexs(int[] repetitionItemIndexs) {
        this.repetitionItemIndexs = repetitionItemIndexs;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

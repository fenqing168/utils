package com.fenqing.excel.function;

/**
 * 上传文件回调函数
 * @author fenqing
 */
@FunctionalInterface
public interface UploadFile<T>{

    /**
     * 上传方法
     * @param bytes
     * @param fileType
     * @return
     */
    T upload(byte[] bytes, String fileType);
}
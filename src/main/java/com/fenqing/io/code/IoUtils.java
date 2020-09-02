package com.fenqing.io.code;

import com.fenqing.object.code.ArrayUtils;
import com.fenqing.object.code.DataUtils;
import com.fenqing.object.code.ListUtils;

import java.io.*;
import java.util.List;

/**
 * @author fenqing
 */
public class IoUtils {

    /**
     * 获取字节
     * @param is
     * @return
     */
    public static byte[] getBytes(InputStream is){
        List<Byte> bytes = ListUtils.newArrayList();
        byte[] bytesArray = new byte[1024];
        int len;
        try(
            InputStream isTemp = is;
        ){
            while((len = isTemp.read(bytesArray)) != -1){
                for (int i = 0; i < len; i++) {
                    bytes.add(bytesArray[i]);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Byte[] bs = new Byte[bytes.size()];
        Byte[] bytes1 = bytes.toArray(bs);
        return ArrayUtils.openBox(bytes1);
    }

    /**
     * 获取字节
     * @param path
     * @return
     */
    public static byte[] getBytes(String path){
        FileInputStream fis;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return getBytes(fis);
    }

    /**
     * 输出
     * @param path
     * @param bytes
     * @return
     */
    public static boolean output(String path, byte[] bytes){
        try(
            OutputStream os = new FileOutputStream(path);
        ){
            os.write(bytes);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return true;
    }

}

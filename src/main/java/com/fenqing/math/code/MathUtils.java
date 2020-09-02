package com.fenqing.math.code;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author fenqing
 */
public class MathUtils {

    public static int min(int ...nums){
        if(nums == null || nums.length == 0){
            throw new RuntimeException("比较内容不能为null或空");
        }
        int min = nums[0];
        int len = nums.length;
        for (int i = 1; i < len; i++) {
            min = Math.min(min, nums[i]);
        }
        return min;
    }

    public static int min(Collection<Integer> nums){
        if(nums == null || nums.size() == 0){
            throw new RuntimeException("比较内容不能为null或空");
        }
        Iterator<Integer> iterator = nums.iterator();
        int min = 0;
        if(iterator.hasNext()){
            min = iterator.next();
        }
        while (iterator.hasNext()){
            min = Math.min(min, iterator.next());
        }
        return min;
    }

    public static int max(Collection<Integer> nums){
        if(nums == null || nums.size() == 0){
            throw new RuntimeException("比较内容不能为null或空");
        }
        Iterator<Integer> iterator = nums.iterator();
        int min = 0;
        if(iterator.hasNext()){
            min = iterator.next();
        }
        while (iterator.hasNext()){
            min = Math.max(min, iterator.next());
        }
        return min;
    }

}

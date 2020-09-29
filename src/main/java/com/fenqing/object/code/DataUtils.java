package com.fenqing.object.code;

import com.fenqing.object.bean.CompareResults;
import com.fenqing.object.bean.Kv;
import com.fenqing.object.bean.MatchMapper;
import com.fenqing.object.constant.AbstractConst;
import com.fenqing.common.function.RunTime;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author fenqing
 */
public class DataUtils {

    private final static String THIS = "this";

    /**
     * 获取对象里属性值
     * @param o     对象
     * @param field 字段名
     * @return      属性值
     */
    public static Object getValue(Object o, String field){
        if(o == null){
            return null;
        }
        Class<?> clazz = o.getClass();
        Matcher matcher = AbstractConst.PATTERN.matcher(field);
        if(matcher.find()){
            String group = matcher.group(0);
            int index = Integer.parseInt(group);
            if(o instanceof List){
                return ((List<?>) o).get(index);
            }else{
                return Array.get(o, index);
            }
        }else{
            if(o instanceof Map){
                Method get = AbstractConst.getMapGetMethod();
                try {
                    return get.invoke(o, field);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    Field classField = clazz.getDeclaredField(field);
                    classField.setAccessible(true);
                    return classField.get(o);
                } catch (Exception e) {
                    try {
                        Method getMethod = getGetMethod(clazz, field);
                        return getMethod.invoke(o);
                    } catch (Exception ex) {
                        return null;
                    }
                }
            }
        }
    }

    /**
     * 获取对象里属性值
     * @param o     对象
     * @param field 字段名
     * @return      属性值
     */
    @SuppressWarnings("unchecked")
    public static boolean setValue(Object o, String field, Object value){
        if(o == null){
            return false;
        }
        Class<?> clazz = o.getClass();
        Matcher matcher = AbstractConst.PATTERN.matcher(field);
        if(matcher.find()){
            String group = matcher.group(0);
            int index = Integer.parseInt(group);
            if(o instanceof List){
                ((List<Object>) o).set(index, value);
                return true;
            }else{
                Array.set(o, index, value);
                return true;
            }
        }else{
            try {
                Field classField = clazz.getDeclaredField(field);
                classField.setAccessible(true);
                classField.set(o, value);
                return true;
            } catch (Exception e) {
                Method getMethod = getSetMethod(clazz, field, value.getClass());
                try {
                    getMethod.invoke(o, value);
                    return true;
                } catch (Exception ex) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 获取对象里属性值
     * @param o          对象
     * @param field      属性
     * @param returnType 返回类型
     * @param <T>        返回类型
     * @return           属性值
     */
    public static <T> T getValue(Object o, String field, Class<T> returnType){
        return (T) getValue(o, field);
    }

    /**
     * 获取对象里属性值
     * @param o     对象
     * @param field 字段名,支持级联
     * @return      属性值
     */
    public static Object getValueDeep(Object o, String field){
        String[] split = field.split("\\.");
        return getValueDeep(o, split, 0);
    }

    /**
     * 获取对象里属性值
     * @param o     对象
     * @param field 字段名,支持级联
     * @return      属性值
     */
    public static <T> T getValueDeep(Object o, String field, Class<T> returnType){
        return (T) getValueDeep(o, field);
    }

    /**
     * 获取对象里属性
     * @param collection
     * @param fields
     * @return
     */
    public static Map<String, List> getValues2List(Collection<?> collection, String... fields){
        return getValues2Collection(collection, ArrayList::new, fields);
    }

    /**
     * 匹配字段
     * @param collection
     * @param fields
     * @return
     */
    public static Map<String, Set> getValues2Set(Collection<?> collection, String... fields){
        return getValues2Collection(collection, HashSet::new, fields);
    }

    /**
     * 匹配字段
     * @param target
     * @param src
     * @param targetMapField
     * @param srcMapField
     * @param targetValueField
     */
    public static void match(Collection<?> target, Collection<?> src, String targetMapField, String srcMapField, String targetValueField){
        Map<Object, ?> map = src.stream().collect(Collectors.toMap(item -> getValue(item, srcMapField), e -> e));
        target.forEach(item -> setValue(item, targetValueField, map.get(getValue(item, targetMapField))));
    }

    /**
     * 匹配字段
     * @param target              目标集合
     * @param src                 数据来源
     * @param targetMapField      目标匹配字段
     * @param srcMapField         来源匹配字段
     * @param targetValueField    目标值字段
     * @param srcValueField       来源值字段
     */
    public static void match(Collection<?> target, Collection<?> src, String targetMapField, String srcMapField, String targetValueField, String srcValueField){
        Map<Object, ?> map = src.stream().collect(Collectors.toMap(item -> getValue(item, srcMapField), e -> e));
        target.forEach(item -> {
            Object o = map.get(getValue(item, targetMapField));
            if(THIS.equals(srcValueField)){
                setValue(item, targetValueField, o);
            }else{
                setValue(item, targetValueField, Optional.ofNullable(o).map(e -> getValue(e, srcValueField)).orElse(null));
            }
        });
    }

    /**
     * 匹配字段
     * @param target              目标集合
     * @param src                 数据来源
     * @param targetMapField      目标匹配字段
     * @param srcMapField         来源匹配字段
     * @param targetValueField    目标值字段
     * @param srcValueField       来源值字段
     */
    public static void match(Collection<?> target, Collection<?> src, String targetMapField, String srcMapField, String targetValueField, String srcValueField, Object defaultValue){
        Map<Object, ?> map = src.stream().collect(Collectors.toMap(item -> getValue(item, srcMapField), e -> e));
        target.forEach(item -> {
            Object o = map.get(getValue(item, targetMapField));
            if(THIS.equals(srcValueField)){
                setValue(item, targetValueField, Optional.ofNullable(o).orElse(defaultValue));
            }else{
                setValue(item, targetValueField, Optional.ofNullable(o).map(e -> getValue(e, srcValueField)).orElse(defaultValue));
            }
        });
    }

    public static void match(Collection<?> target, Collection<?> src, String targetMapField, String srcMapField, Map<String, String> valueMapper){
        Map<Object, ?> map = src.stream().collect(Collectors.toMap(item -> getValue(item, srcMapField), e -> e));
        target.forEach(item -> {
            valueMapper.forEach((targetValue, srcValue) -> {
                Object srcValueObject = map.get(getValue(item, targetMapField));
                if(THIS.equals(srcValue)){
                    setValue(item, targetValue, srcValueObject);
                }else{
                    setValue(item, targetValue, Optional.ofNullable(srcValueObject).map(e -> getValue(e, srcValue)).orElse(null));
                }
            });
        });
    }

    public static void match2Many(Collection<?> target, Collection<?> src, String targetField, String targetMapperField, String srcMapperField){
        match2Many(target, src, targetField, "this", targetMapperField, srcMapperField);
    }

    private static Class<?> getFieldType(Class<?> clazz, String field){
        try {
            Field declaredField = clazz.getDeclaredField(field);
            return declaredField.getType();
        }catch (Exception e){
            return getGetMethod(clazz, field).getReturnType();
        }
    }

    public static void match2Many(Collection<?> target, Collection<?> src, String targetField, String srcField, String targetMapperField, String srcMapperField){
        if(src.isEmpty()){
            return;
        }
        Class<?> clazz = target.iterator().next().getClass();
        Class<?> fieldType = getFieldType(clazz, targetField);
        Map<Object, CollectionFactory> objectCollectionFactoryMap = new HashMap<>(8);
        src.forEach(item -> {
            Object value = getValue(item, srcMapperField);
            CollectionFactory collectionFactory = objectCollectionFactoryMap.get(value);
            if(collectionFactory == null){
                collectionFactory = CollectionFactory.getInstance(fieldType);
            }

            Object setItem;
            if(THIS.equals(srcField)){
                setItem = item;
            }else{
                setItem = getValue(item, srcField);
            }

            collectionFactory.add(setItem);
            objectCollectionFactoryMap.put(value, collectionFactory);
        });
        target.forEach(item -> {
            Object value = getValue(item, targetMapperField);
            CollectionFactory collectionFactory = objectCollectionFactoryMap.get(value);
            if (collectionFactory == null) {
                collectionFactory = CollectionFactory.getInstance(fieldType);
            }
            Object collection = collectionFactory.getCollection();
            setValue(item, targetField, collection);
        });

    }

    public static void match(Collection<?> target, Supplier<List<MatchMapper>> supplier){
        supplier.get().forEach(matchMapper -> match(target, matchMapper.getSrc(), matchMapper.getTargetMapField(), matchMapper.getSrcMapField(), matchMapper.getTargetValueField(), matchMapper.getSrcValueField()));
    }

    public static <T> List<T> merge(Collection<T>... colls){
        List<T> result = new ArrayList<>();
        for (Collection<T> coll : colls) {
            result.addAll(coll);
        }
        return result;
    }

    public static <T> Set<T> mergeBatDistinct(Collection<T>... colls){
        Set<T> result = new HashSet<>();
        for (Collection<T> coll : colls) {
            result.addAll(coll);
        }
        return result;
    }

    /**
     * 方法运行时间
     * @param supplier 回调函数
     * @return         方法运行时间
     */
    public static long runtime(RunTime supplier){
        long time = System.currentTimeMillis();
        supplier.run();
        long result = System.currentTimeMillis() - time;
        return result;
    }

    /**
     * 获取values
     * @param collection
     * @param supplier
     * @param fields
     * @param <T>
     * @return
     */
    private static <T extends Collection> Map<String, T> getValues2Collection(Collection<?> collection, Supplier<T> supplier, String... fields){
        Map<String, T> result = new HashMap<>(8);
        for(String fieid : fields){
            result.put(fieid, supplier.get());
        }
        if(collection.isEmpty()){
            return result;
        }
        collection.forEach(item -> {
            for(String fieid : fields){
                result.get(fieid).add(getValue(item, fieid));
            }
        });
        return result;
    }

    /**
     * 获取对象里属性值
     * @param o      对象
     * @param fields 字段名,支持级联
     * @return       属性值
     */
    private static Object getValueDeep(Object o, String[] fields, int index){
        if(o == null || index == fields.length){
            return o;
        }
        String field = fields[index];
        Matcher matcher = AbstractConst.PATTERN.matcher(field);
        int indexStart = field.indexOf("[");
        if(matcher.find() && indexStart != 0){
            String fidld1 = field.substring(0, indexStart);
            String indexField = field.substring(indexStart);
            Object value1 = getValue(o, fidld1);
            Object value2 = getValue(value1, indexField);
            return getValueDeep(value2, fields, index + 1);
        }else{
            Object value = getValue(o, field);
            return getValueDeep(value, fields, index + 1);
        }
    }

    /**
     * 获取get方法
     * @param clazz 类
     * @param field 属性名
     * @return      方法对象
     */
    private static Method getGetMethod(Class<?> clazz, String field){
        try {
            return clazz.getMethod("get" + field.toUpperCase().substring(0, 1) + field.substring(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取get方法
     * @param clazz 类
     * @param field 属性名
     * @return      方法对象
     */
    private static Method getSetMethod(Class<?> clazz, String field, Class<?> fieldClass){
        try {
            return clazz.getMethod("set" + field.toUpperCase().substring(0, 1) + field.substring(1), fieldClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <I> List<I> combination(List<I> list, String childrenFieldName, String key, String parentKey, Object topParentKeyValue){
        Map<Object, List<I>> maps = new HashMap<>(8);
        for (I item : list) {
            Object parentKeyValue = getValue(item, parentKey);
            List<I> is = maps.get(parentKeyValue);
            if(is == null){
                is = new ArrayList<>();
            }
            is.add(item);
            maps.put(parentKeyValue, is);
        }

        Deque<Kv<Object, Integer>> stack = new LinkedList<>();
        Kv<Object, Integer> kv = new Kv<>();
        kv.setK(topParentKeyValue);
        kv.setV(0);
        stack.push(kv);
        while (!stack.isEmpty()){
            Kv<Object, Integer> peek = stack.peek();
            List<I> is = maps.get(peek.getK());
            if(is == null || is.isEmpty() || is.size() <= peek.getV()){
                stack.pop();
                continue;
            }
            I i = is.get(peek.getV());
            Object value = getValue(i, key);
            List<I> is1 = maps.get(value);
            if(is1 == null){
                is1 = new ArrayList<>();
            }
            setValue(i, childrenFieldName, is1);
            Kv<Object, Integer> temp = new Kv<>();
            temp.setK(value);
            temp.setV(0);
            stack.push(temp);
            peek.setV(peek.getV() + 1);
        }
        return Optional.ofNullable(maps.get(topParentKeyValue)).orElse(new ArrayList<>());
    }

    /**
     * 比较
     * @param old 原来数据
     * @param now 现在数据
     * @param <T> 数据里类型
     * @return    比较结果
     */
    public static <T> CompareResults<T> compare(List<T> old, List<T> now, BiPredicate<T, T> biPredicate){
        List<T> as = new ArrayList<>(),
                disagree = new ArrayList<>(),
                news = new ArrayList<>();
        old.forEach(item -> {
            now.forEach(nItem -> {
                boolean test = biPredicate.test(item, nItem);
                if(test){
                    as.add(item);
                }
            });
        });
        return new CompareResults<>(as, disagree, news);
    }


    public static boolean isNotEmpty(Object obj) {
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        boolean flag = false;
        int fieLen = fields.length;
        for(int i = 0; i < fieLen; ++i) {
            Field field = fields[i];
            if (flag) {
                break;
            }

            try {
                field.setAccessible(true);
                Object val = field.get(obj);
                if (val != null) {
                    Class<?> fieldType = field.getType();
                    Integer len;
                    if (fieldType.isArray()) {
                        Field length = fieldType.getField("length");
                        len = (Integer)length.get(val);
                        flag = len > 0;
                    } else if (!Map.class.isAssignableFrom(fieldType) && !Collection.class.isAssignableFrom(fieldType)) {
                        if (String.class.equals(fieldType)) {
                            flag = !"".equals(val);
                        } else if (!fieldType.isPrimitive() && !isWrapClass(fieldType)) {
                            flag = isEmpty(val);
                        }
                    } else {
                        Method size = fieldType.getMethod("size");
                        len = (Integer)size.invoke(val);
                        flag = len > 0;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return flag;
    }

    public static boolean isEmpty(Object obj) {
        return !isNotEmpty(obj);
    }

    /**
     * 是否包装类型
     * @param clz
     * @return
     */
    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class)clz.getField("TYPE").get((Object)null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否为结构语法
     */
    public static boolean isArray(String text, Map<String, Object> data){
        Object o = data.get(text);
        if(o != null){
            return o.getClass().isArray() || o instanceof Iterable;
        }
        return false;
    }


}


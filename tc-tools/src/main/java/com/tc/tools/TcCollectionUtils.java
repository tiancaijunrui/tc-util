package com.tc.tools;

import com.tc.TcErrorCode;
import com.tc.TcException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * 集合操作工具类
 */
public class TcCollectionUtils {

    /**
     * 针对集合进行分批操作
     *
     * @param source    集合
     * @param pieceSize 需要分页尺寸
     * @param <T> 泛型
     * @return 分割后的集合
     */
    @NotNull
    public static <T> List<List<T>> separateIntoPieces(List<T> source, int pieceSize) {
        if (CollectionUtils.isEmpty(source)) return new ArrayList<>(0);
        List<List<T>> result = new ArrayList<>();
        int sourceSize = source.size();
        if (sourceSize <= pieceSize) {
            result.add(source);
        } else {
            int piece = sourceSize / pieceSize;
            int fromIndex = 0;
            for (int i = 0; i < piece; i++) {
                result.add(source.subList(fromIndex, fromIndex + pieceSize));
                fromIndex += pieceSize;
            }
            result.add(source.subList(fromIndex, sourceSize));
        }
        return result;
    }

    /**
     * 将字符串类型的List转化为以separator分割的字符串。
     *
     * @param list      字符串类型的List，若为空，返回null
     * @param separator 分割符 若为空，则抛出异常{@link TcErrorCode#PARAMETER_NOT_BE_BLANK}
     * @return 集合转字符串
     */
    @Nullable
    public static String listToString(List<String> list, String separator) {
        if (StringUtils.isBlank(separator)) {
            throw new TcException(TcErrorCode.PARAMETER_NOT_BE_BLANK, "separator");
        }

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        list.forEach(str -> {
            builder.append(str).append(separator);
        });
        return builder.toString().substring(0, builder.toString().length() - 1);
    }

    /**
     * 将字符串以分割符转化为集合每个元素左右做去空操作
     *
     * @param str       若为空则抛出异常{@link TcErrorCode#PARAMETER_NOT_BE_BLANK}
     * @param separator 分割符{@link TcErrorCode#PARAMETER_NOT_BE_BLANK}
     * @return 集合
     * @throws TcException 异常
     */
    @NotNull
    public static List<String> stringToList(String str, String separator) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(separator)) {
            throw new TcException(TcErrorCode.PARAMETER_NOT_BE_BLANK, "str,separator");
        }
        String[] array = str.split(separator);
        List<String> list = new ArrayList<>(array.length);
        for (String s : array) {
            list.add(s.trim());
        }
        return list;
    }

    /**
     * 集合 差集
     *
     * @param source 集合一号
     * @param remove 集合二号
     * @param <E>  泛型
     * @return 集合
     */
    @NotNull
    public static <E> List<E> difference(Collection<E> source, Collection<E> remove) {
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>(0);
        }

        if (CollectionUtils.isEmpty(remove)) {
            return new ArrayList<>(source);
        }

        Map<E, Boolean> elementMap = new HashMap<>(remove.size());
        for (E element : remove) {
            elementMap.put(element, true);
        }

        List<E> result = new ArrayList<>(source);
        result.removeIf(next -> BooleanUtils.isTrue(elementMap.get(next)));
        return result;
    }

    /**
     * 集合交集
     *
     * @param remove 需要移除的集合
     * @param source 被移除的集合
     * @param <E> 泛型
     * @return 结果
     */
    @NotNull
    public static <E> List<E> intersection(Collection<E> source, Collection<E> remove) {
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>(0);
        }

        if (CollectionUtils.isEmpty(remove)) {
            return new ArrayList<>(0);
        }

        Map<E, Integer> elementMap = new HashMap<>(remove.size());
        for (E element : remove) {
            Integer cnt = elementMap.get(element);
            if (cnt == null) {
                cnt = 0;
            }

            elementMap.put(element, ++cnt);
        }

        List<E> result = new ArrayList<>(source);
        Iterator<E> iterator = result.iterator();
        while (iterator.hasNext()) {
            E next = iterator.next();
            Integer cnt = elementMap.get(next);
            if (cnt != null && cnt != 0) {
                elementMap.put(next, --cnt);
            } else {
                iterator.remove();
            }
        }

        return result;
    }

    /**
     * 将给定的对象数组转化为list
     * @param array 数组
     * @param <T> 泛型
     * @return 集合
     */
    @NotNull
    @Contract("null -> new")
    public static <T> List<T> transToList(T... array) {
        if (array == null || array.length == 0) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(array);
    }

    /**
     * 集合合并
     * @param sourceOne 集合一号
     * @param sourceTwo 集合二号
     * @param <E> 泛型
     * @return 合并的集合
     */
    @NotNull
    @Contract("_, _ -> new")
    public static <E> ArrayList union(Collection sourceOne, Collection<E> sourceTwo) {
        if (CollectionUtils.isEmpty(sourceOne) && CollectionUtils.isEmpty(sourceTwo)) {
            return new ArrayList<>(0);
        }
        if (CollectionUtils.isEmpty(sourceOne)) {
            return new ArrayList<>(sourceTwo);
        }
        if (CollectionUtils.isEmpty(sourceTwo)) {
            return new ArrayList<>(sourceOne);
        }
        return new ArrayList<>(CollectionUtils.union(sourceOne, sourceTwo));
    }

}



package com.jmsoftware.maf.common.enumeration;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.val;

/**
 * <h1>ValueDescriptionBaseEnum</h1>
 * <p>
 * Change description here.
 *
 * @param <V> the type of value
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/27/22 11:32 AM
 */
@SuppressWarnings("unused")
public interface ValueDescriptionBaseEnum<V> {
    /**
     * Gets value, which represents the value stored in database
     *
     * @return the value
     */
    V getValue();

    /**
     * Gets description, which indicates what the value represents for.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Check if the value exists in the enums.
     *
     * @param <T>   the type parameter
     * @param enums the enums
     * @param value the value
     * @return the boolean
     */
    static <T> boolean exists(@NonNull ValueDescriptionBaseEnum<T>[] enums, T value) {
        if (!EnumUtil.isEnum(enums[0])) {
            throw new IllegalArgumentException("Not an enum type");
        }
        if (value == null) {
            return false;
        }
        if (ArrayUtil.isEmpty(enums)) {
            return false;
        }
        for (ValueDescriptionBaseEnum<T> e : enums) {
            if (value.equals(e.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exists boolean.
     *
     * @param <E>       the type of enum
     * @param <V>       the type of value
     * @param enumClass the enum class
     * @param value     the value
     * @return the boolean
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<? extends ValueDescriptionBaseEnum<V>>, V> boolean exists(@NonNull Class<E> enumClass, V value) {
        for (Enum<? extends ValueDescriptionBaseEnum<V>> e : enumClass.getEnumConstants()) {
            if (((ValueDescriptionBaseEnum<V>) e).getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets description by value.
     *
     * @param <E>       the type parameter
     * @param <V>       the type parameter
     * @param enumClass the enum class
     * @param value     the value
     * @return the description by value
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<? extends ValueDescriptionBaseEnum<V>>, V> String getDescriptionByValue(@NonNull Class<E> enumClass, V value) {
        if (value == null) {
            return null;
        }
        for (Enum<? extends ValueDescriptionBaseEnum<V>> e : enumClass.getEnumConstants()) {
            if (((ValueDescriptionBaseEnum<V>) e).getValue().equals(value)) {
                return ((ValueDescriptionBaseEnum<?>) e).getDescription();
            }
        }
        return null;
    }

    /**
     * Gets by enum name.
     *
     * @param <E>       the type parameter
     * @param <V>       the type parameter
     * @param enumClass the enum class
     * @param enumName  the enum name
     * @return the by enum name
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<? extends ValueDescriptionBaseEnum<V>>, V> E getByEnumName(
            @NonNull Class<E> enumClass,
            String enumName
    ) {
        if (StrUtil.isEmpty(enumName)) {
            return null;
        }
        for (val e : enumClass.getEnumConstants()) {
            if (StrUtil.equalsIgnoreCase(e.name(), enumName)) {
                return e;
            }
        }
        return null;
    }


    /**
     * Gets value by enum name.
     *
     * @param <E>       the type parameter
     * @param <V>       the type parameter
     * @param enumClass the enum class
     * @param enumName  the enum name
     * @return the value by enum name
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<? extends ValueDescriptionBaseEnum<V>>, V> V getValueByEnumName(
            @NonNull Class<E> enumClass,
            String enumName
    ) {
        if (StrUtil.isEmpty(enumName)) {
            return null;
        }
        val e = ((ValueDescriptionBaseEnum<V>) getByEnumName(enumClass, enumName));
        if (e != null) {
            return e.getValue();
        }
        return null;
    }

    /**
     * Gets by value.
     *
     * @param <E>       the type parameter
     * @param <V>       the type parameter
     * @param enumClass the enum class
     * @param value     the value
     * @return the by value
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<? extends ValueDescriptionBaseEnum<V>>, V> E getByValue(
            @NonNull Class<E> enumClass,
            V value
    ) {
        if (ObjectUtil.isNull(value)) {
            return null;
        }
        for (val e : enumClass.getEnumConstants()) {
            if (((ValueDescriptionBaseEnum<V>) e).getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}

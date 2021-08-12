package com.jmsoftware.maf.springcloudstarter.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.jmsoftware.maf.springcloudstarter.annotation.TreeElement;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Description: TreeUtil
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 5/28/2021 4:27 PM
 */
@SuppressWarnings("unused")
public class TreeUtil {
    private TreeUtil() {
    }

    /**
     * Convert List to Tree
     *
     * @param <T>      the type parameter
     * @param list     t
     * @param rootNode the root node
     * @return result list
     * @throws IllegalAccessException    result
     * @throws IntrospectionException    the introspection exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static <T> List<T> listToTree(List<T> list, String rootNode) throws IllegalAccessException,
            IntrospectionException, InvocationTargetException {
        // Root node collection
        List<T> rootList = new ArrayList<>();
        HashMap<Object, List<T>> parentIdToTree = Maps.newHashMap();
        for (final T t : list) {
            Object value = getValue(t, TreeElementType.PARENT_ID);
            if (ObjectUtil.isNotNull(value)) {
                if (ObjectUtil.isNotNull(parentIdToTree.get(value))) {
                    parentIdToTree.get(value).add(t);
                } else {
                    parentIdToTree.put(value, CollUtil.newArrayList(t));
                }
                if (rootNode.equals(value)) {
                    rootList.add(t);
                }
            }
        }
        buildChildTree(rootList, parentIdToTree);
        return rootList;
    }

    /**
     * Gets value.
     *
     * @param <T>             the type parameter
     * @param t               the t
     * @param treeElementType the tree element type
     * @return the value
     * @throws IllegalAccessException    the illegal access exception
     * @throws IntrospectionException    the introspection exception
     * @throws InvocationTargetException the invocation target exception
     */
    private static <T> Object getValue(T t, TreeElementType treeElementType) throws IllegalAccessException,
            IntrospectionException,
            InvocationTargetException {
        final BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            final Field treeElementField = getTreeElementField(t, propertyName, treeElementType);
            if (treeElementField != null) {
                Method readMethod = propertyDescriptor.getReadMethod();
                return readMethod.invoke(t);
            }
        }
        return null;
    }


    /**
     * Gets tree element field.
     *
     * @param <T>             the type parameter
     * @param t               the t
     * @param fieldName       the field name
     * @param treeElementType the tree element type
     * @return the tree element field
     */
    private static <T> Field getTreeElementField(T t, String fieldName, TreeElementType treeElementType) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            TreeElement treeElement = field.getAnnotation(TreeElement.class);
            if (ObjectUtil.isNull(treeElement)) {
                continue;
            }
            if (fieldName.equals(field.getName()) && treeElementType.equals(treeElement.type())) {
                return field;
            }
        }
        return null;
    }


    /**
     * Build child tree.
     *
     * @param <T>            the type parameter
     * @param currentTree    the current tree
     * @param parentIdToTree the parent id to tree
     * @throws IllegalAccessException    the illegal access exception
     * @throws IntrospectionException    the introspection exception
     * @throws InvocationTargetException the invocation target exception
     */
    private static <T> void buildChildTree(List<T> currentTree, HashMap<Object, List<T>> parentIdToTree) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        for (T t : currentTree) {
            Object currentId = getValue(t, TreeElementType.ID);
            // Data exists with current ID as PARENT_ID
            if (parentIdToTree.get(currentId) != null) {
                @SuppressWarnings("unchecked") List<T> list = (List<T>) getValue(t, TreeElementType.CHILDREN_LIST);
                Objects.requireNonNull(list).addAll(parentIdToTree.get(currentId));
                buildChildTree(parentIdToTree.get(currentId), parentIdToTree);
            }
        }
    }
}

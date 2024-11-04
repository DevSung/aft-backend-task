package com.example.domain.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class EntityUpdateUtil {

    /**
     * 주어진 source 객체의 null이 아닌 속성 값을 target 객체에 복사합니다.
     *
     * @param source null이 아닌 속성 값을 가져올 객체
     * @param target 값이 복사될 객체
     */
    public static <T> void updateNonNull(T source, T target) {
        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        BeanWrapper trgWrapper = new BeanWrapperImpl(target);

        // source 객체의 null이 아닌 속성 이름을 가져온다.
        for (var propertyName : getNonNullProps(source)) {
            var newValue = srcWrapper.getPropertyValue(propertyName);
            trgWrapper.setPropertyValue(propertyName, newValue);
        }
    }

    /**
     * 주어진 객체에서 null이 아닌 속성 이름을 반환합니다.
     *
     * @param source null이 아닌 속성 이름을 가져올 객체
     */
    private static <T> Set<String> getNonNullProps(T source) {
        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        Set<String> nonNullProperties = new HashSet<>();

        for (var propertyDescriptor : srcWrapper.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            // 'class' 프로퍼티는 제외
            if ("class".equals(propertyName)) {
                continue;
            }

            Object propertyValue = srcWrapper.getPropertyValue(propertyName);
            if (propertyValue != null) {
                nonNullProperties.add(propertyName);
            }
        }
        return nonNullProperties;
    }
}

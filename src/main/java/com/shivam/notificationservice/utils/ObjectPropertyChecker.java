package com.shivam.notificationservice.utils;

import com.shivam.notificationservice.responseBody.ResponseError;
import com.shivam.notificationservice.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class ObjectPropertyChecker {

    public static boolean passNullOrEmptyCheck(Object obj) throws BadRequestException {
        if (obj == null) {
            return true;
        }
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                if (value == null || (value instanceof String && StringUtils.isEmpty((String) value))) {
                    return true;
                }
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (list.isEmpty()) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("some fields are empty or null");
                throw new BadRequestException(new ResponseError("Empty/Undeclared Fields","all fields are required according to api documentation"));
            }
        }

        return false;
    }
}


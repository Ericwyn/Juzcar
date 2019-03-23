package com.ericwyn.juzcar.scan.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 默认的测试值
 *
 * Created by Ericwyn on 19-3-23.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JuzcarDefaultValue {
    String value() default "";
}

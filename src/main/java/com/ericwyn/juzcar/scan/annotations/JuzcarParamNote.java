package com.ericwyn.juzcar.scan.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用来备注 Method
 * Created by Ericwyn on 19-3-3.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JuzcarParamNote {
    String value() default "";
}

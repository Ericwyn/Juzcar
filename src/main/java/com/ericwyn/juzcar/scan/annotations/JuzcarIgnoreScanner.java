package com.ericwyn.juzcar.scan.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 禁止扫描的注解，Juzcar 将会忽略被该注解注释的 Controller 或者 Controller
 *
 * Created by Ericwyn on 18-11-26.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JuzcarIgnoreScanner {

}

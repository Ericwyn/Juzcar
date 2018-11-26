package com.ericwyn.juzcar.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来手动规定一些需要被扫描的方法、类的
 * 但是如果该注解使用在 Method 上面，但是 Method 所在的 Class 被 IgnoreScanner 注解的话，那么该注解将不会起作用
 *
 * Created by Ericwyn on 18-11-26.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JuzcarScanner {

}

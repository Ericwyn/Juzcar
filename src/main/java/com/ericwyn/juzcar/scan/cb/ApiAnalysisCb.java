package com.ericwyn.juzcar.scan.cb;

import com.ericwyn.juzcar.scan.obj.JuzcarApi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * API 分析回调函数
 *
 * Created by Ericwyn on 18-11-26.
 */
public interface ApiAnalysisCb {
    // 原本的 Method
    public JuzcarApi analysis(Method method, Annotation annotation);
}

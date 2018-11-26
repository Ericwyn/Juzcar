package com.ericwyn.juzcar.utils.cb;

import com.ericwyn.juzcar.utils.obj.JuzcarApi;

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

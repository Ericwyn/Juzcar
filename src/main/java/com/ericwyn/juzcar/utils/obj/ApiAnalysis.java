package com.ericwyn.juzcar.utils.obj;

import java.lang.annotation.Annotation;

/**
 * API 分析回调函数
 *
 * Created by Ericwyn on 18-11-26.
 */
public interface ApiAnalysis {
    public JuzcarApi analysis(Annotation annotation);
}

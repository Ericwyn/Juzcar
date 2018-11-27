package com.ericwyn.juzcar.scan.cb;

import com.ericwyn.juzcar.scan.obj.JuzcarParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Created by Ericwyn on 18-11-26.
 */
public interface ParamAnalysisCb {
    public JuzcarParam analysis(Parameter parameter, Annotation annotation);
    public List<JuzcarParam> analysisBody(Parameter parameter, Annotation annotation);
}

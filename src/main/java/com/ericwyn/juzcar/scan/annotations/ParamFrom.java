package com.ericwyn.juzcar.scan.annotations;

import com.ericwyn.juzcar.scan.obj.ApiType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 标记该参数从其他哪个接口而来，实现测试链条的功能
 *
 * 该注解通过标明参数来源接口的
 *      url
 *      返回格式
 *      对返回数据的提取
 *
 * 来获得所需的参数，例如 接口 B ( /api/v1/api-b )  依赖于 接口 A ( /api/v1/api-a )，
 * 然后接口 A 的返回格式如下
 *
 *      {
 *          code:1000
 *          data:{
 *              token:user-token-xxxxx
 *          }
 *      }
 *
 * 然后 B 的话可以在 Controller 的方法当中，使用
 *      `@ParamFrom(url = "/api/v1/api-a", type = ApiType.JSON, regex = "data.token")`
 * 来标记方法的参数，其中
 *      url 代表的是接口 a 的请求地址
 *      type 代表的是接口 a 返回数据的格式
 *      regex 代表的是对返回数据的提取规则，如果返回数据不是 JSON 的话，可以使用正则表达式来提取
 *
 * Juzcar 在处理和测试的时候，就会先测试接口 B 并从当中获取
 *
 * Created by Ericwyn on 18-11-26.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamFrom {
    // 来源的接口 url
    String path();
    // 来源接口的数据返回格式
    ApiType type();
    // 提取格式
    String regex();
}

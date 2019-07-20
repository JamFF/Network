package com.ff.network.library.annotation;

import com.ff.network.library.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 作用、目标在方法之上
@Retention(RetentionPolicy.RUNTIME) // jvm在运行时，通过反射获取注解的值
public @interface Network {

    NetType netType() default NetType.AUTO;
}

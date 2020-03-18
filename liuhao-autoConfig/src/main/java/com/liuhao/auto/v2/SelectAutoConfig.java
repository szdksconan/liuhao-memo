package com.liuhao.auto.v2;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ImportSecetorTest.class)
public @interface SelectAutoConfig {
}

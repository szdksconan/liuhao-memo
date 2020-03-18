package com.liuhao.auto.demo1;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan("com.liuhao.auto.demo1")
@Import(TestC.class)
@Configuration
public class AnnotationBeanFactoryConfig {





}

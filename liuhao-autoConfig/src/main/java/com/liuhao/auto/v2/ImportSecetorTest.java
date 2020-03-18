package com.liuhao.auto.v2;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

public class ImportSecetorTest implements ImportSelector {
    /**
     * 批量导入组件
     * @param importingClassMetadata
     * @return
     */
   /* @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String[] s = {"com.liuhao.auto.v2.TestA"};
        return s;
    }*/


    /**
     * 通过配置导入
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> list = getCandidateConfigurations(importingClassMetadata,null);
        return StringUtils.toStringArray(list);
    }

    /**
     * 装载器class
     * @return
     */
    private Class<?> getUsFactoriesLoaderFactoryClass(){
        return SelectAutoConfig.class;
    }

    /**
     * AutoConfigurationImportSelector 源码获取配置 META-INF/spring.factories 信息
     * @param metadata
     * @param attributes
     * @return
     */
    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        //根据导入器的全路径获取 到 对应的spring.factries 里面的值 这里我们配置的TestA的全路径
        //这个方法里面有个loadSpringFactories() 方法 它会加载所有这种资源路径下的spring.factories META-INF/spring.factories
        //这个方法 2个参数一个是 properties 的key  一个是 classloader 来定位资源路径
        List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getUsFactoriesLoaderFactoryClass(),
                ImportSecetorTest.class.getClassLoader());
        Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
                + "are using a custom packaging, make sure that file is correct.");
        return configurations;
    }

}

package com.liuhao.auto.demo1;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryPostProcessorTest  implements BeanFactoryPostProcessor{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //获取TestA beanDefinition 对象 修改其指向的CLASS 再进行测试 通过testA beanName获取的实例就是testC的
      /*  GenericBeanDefinition beanDefinition = (GenericBeanDefinition) configurableListableBeanFactory.getBeanDefinition("testA");
        beanDefinition.setBeanClass(TestC.class);
*/

        //获取TestA beanDefinition bean描述对象 修改其参数注入model 默认为0 通过@Autowired 设置为1 即通过 getset注入
    /* GenericBeanDefinition beanDefinition = (GenericBeanDefinition) configurableListableBeanFactory.getBeanDefinition("testA");
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_NAME);
*/

        //获取TestA beanDefinition bean描述对象 修改其调用构造函数  为 第一个为String的构造函数
        /*GenericBeanDefinition beanDefinition = (GenericBeanDefinition) configurableListableBeanFactory.getBeanDefinition("testD");
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0,"1");
        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);*/


    }
}

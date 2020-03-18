1.spring xml 和 @bean spring会转化生成一个bean描述对象 AbstractBeanDefinition  存储在beanDefinitionMap<name,definitionBean>，==这里说一下spring3开始使用注解 spring提供了AnnotationConfigApplicationContext来加载注解配置，这个就和以前applicationcontext引入的是xml一个道理，@Configuration等同于xml @bean就等于<bean></bean> @scan也就等同于扫描包路径配置 等等==。
```java
    
    // 对象的class信息
    @Nullable
    private volatile Object beanClass;
    //scope 定义是否是单例 或者 原型
    @Nullable
    private String scope;
    //是否抽象
    private boolean abstractFlag;
    //懒加载
    private boolean lazyInit;
    //注入模型 0为默认值 注解注入 1为getsetname注入
    private int autowireMode;
    private int dependencyCheck;
    @Nullable
    private String[] dependsOn;
    private boolean autowireCandidate;
    private boolean primary;
    private final Map<String, AutowireCandidateQualifier> qualifiers;
    @Nullable
    private Supplier<?> instanceSupplier;
    private boolean nonPublicAccessAllowed;
    private boolean lenientConstructorResolution;
    // bean name
    @Nullable
    private String factoryBeanName;
    @Nullable
    private String factoryMethodName;
    @Nullable
    private ConstructorArgumentValues constructorArgumentValues;
    @Nullable
    private MutablePropertyValues propertyValues;
    private MethodOverrides methodOverrides;
    @Nullable
    private String initMethodName;
    @Nullable
    private String destroyMethodName;
    private boolean enforceInitMethod;
    private boolean enforceDestroyMethod;
    private boolean synthetic;
    private int role;
    @Nullable
    private String description;
    @Nullable
    private Resource resource;

```


2.beanFactory从beanDefinitionMap bean定义map中获取beanDefinition 进行实例化，在这过程中 spring提供了一种机制，可以在实例化的路上修改beanDefinition属性，通过beanFactoryPostProcessor 接口来操作

```java

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


 public class ApplicationRunTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotationBeanFactoryConfig.class);
        TestA testA = (TestA) ctx.getBean("testA");

    }
}


@ComponentScan("com.liuhao.auto.demo1")
@Configuration
public class AnnotationBeanFactoryConfig {





}


```


3.通过@import方式导入beandefinition bean描述,这个注解有三种用法
    ==第三种实现importSelecor接口批量导入组件很重要，是实现自动装配的基石==

```java 

//1.指定导入一个普通组件

public class TestC {

    public TestC(){
        System.out.println("初始化TestC");
    }
}


@Import(TestC.class)
@Configuration
public class AnnotationBeanFactoryConfig {




}


//2.注册一个组件





//3.实现importSelector接口 批量导入组件 这个很重要！

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ImportSecetorTest.class)
public @interface SelectAutoConfig {
}



public class ImportSecetorTest implements ImportSelector {
    /**
     * 批量导入组件
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String[] s = {"com.liuhao.auto.v2.TestA"};
        return s;
    }

}

@SelectAutoConfig
public class TestConfig {
}


public class ApplicationRunTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfig.class);
        ctx.getBean("com.liuhao.auto.v2.TestA");
    }
}
    



```
4. 根据spring源码实现一个简单的自动装配
```java

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
     * 模仿springBoot通过配置导入
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




```






5. ==源码解析 附调试信息==

```java

private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
		MultiValueMap<String, String> result = cache.get(classLoader);
		if (result != null) {
			return result;
		}

		try {
			Enumeration<URL> urls = (classLoader != null ?
					classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
					ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
			result = new LinkedMultiValueMap<>();
			while (urls.hasMoreElements()) {
			    //这里就会根据classloader 加载到对应工程的resource下面的META-INF 下面的 spirng.factories的 properties文件 这个路径是springboot约定好 写死的
				URL url = urls.nextElement();//file:/Users/liuhao/Documents/coding/liuhao-memo/liuhao-autoConfig/target/classes/META-INF/spring.factories
				UrlResource resource = new UrlResource(url);
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);//"com.liuhao.auto.v2.SelectAutoConfig" -> "com.liuhao.auto.v2.TestA"
				//下面就会把值放入MultiValueMap这种K,values 结构里面 对应涵义就是 <装载器全路径，装载器要去装载的类的全路径>
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					String factoryClassName = ((String) entry.getKey()).trim();
					for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
						result.add(factoryClassName, factoryName.trim());
					}
				}
			}
			//放入缓存 以免重复读取 这个就是读取到
			/**
			*0 = {LinkedHashMap$Entry@1792} "       com.liuhao.auto.v2.SelectAutoConfig" -> " size = 1"
                1 = {LinkedHashMap$Entry@1793} "org.springframework.boot.diagnostics.FailureAnalyzer" -> " size = 17"
                2 = {LinkedHashMap$Entry@1794} "org.springframework.boot.env.EnvironmentPostProcessor" -> " size = 3"
                3 = {LinkedHashMap$Entry@1795} "org.springframework.boot.SpringApplicationRunListener" -> " size = 1"
                4 = {LinkedHashMap$Entry@1796} "org.springframework.context.ApplicationContextInitializer" -> " size = 6"
                5 = {LinkedHashMap$Entry@1797} "org.springframework.boot.env.PropertySourceLoader" -> " size = 2"
                6 = {LinkedHashMap$Entry@1798} "org.springframework.context.ApplicationListener" -> " size = 11"
                7 = {LinkedHashMap$Entry@1799} "org.springframework.boot.diagnostics.FailureAnalysisReporter" -> " size = 1"
                8 = {LinkedHashMap$Entry@1800} "org.springframework.boot.SpringBootExceptionReporter" -> " size = 1"
                9 = {LinkedHashMap$Entry@1801} "org.springframework.boot.autoconfigure.AutoConfigurationImportFilter" -> " size = 3"
                10 = {LinkedHashMap$Entry@1802} "org.springframework.boot.autoconfigure.AutoConfigurationImportListener" -> " size = 1"
                11 = {LinkedHashMap$Entry@1803} "org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider" -> " size = 5"
                12 = {LinkedHashMap$Entry@1804} "org.springframework.boot.autoconfigure.EnableAutoConfiguration" -> " size = 117"
                13 = {LinkedHashMap$Entry@1805} "org.springframework.beans.BeanInfoFactory" -> " size = 1"
			*/
			cache.put(classLoader, result);
			return result;
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" +
					FACTORIES_RESOURCE_LOCATION + "]", ex);
		}
	}





```

6. ==看一下官方自带常用的组件的自动装配==

我们在官方的autoConfig工程下面找到了spring.factries 然后找redis的配置
org.springframework.boot.autoconfigure.EnableAutoConfiguration = org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\

EnableAutoConfiguration这个就是@SpringBootApplication 注解里面的@EnableAutoConfiguration ，这个就是系统经常用到的装载器 和我们之前实现的一样 下面我们看下这个注解，找到AutoConfigurationImportSelector，通过AutoConfigurationImportSelector就回给spring返回RedisAutoConfiguration的全路径使其进入beanDefinitionMap spring的bean描述文件集合


==EnableAutoConfigurations实现==

```java

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)//通过这里把配置文件的类扩展进来
public @interface EnableAutoConfiguration {

	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

	/**
	 * Exclude specific auto-configuration classes such that they will never be applied.
	 * @return the classes to exclude
	 */
	Class<?>[] exclude() default {};

	/**
	 * Exclude specific auto-configuration class names such that they will never be
	 * applied.
	 * @return the class names to exclude
	 * @since 1.3.0
	 */
	String[] excludeName() default {};

}

```

==有兴趣可以去看看LettuceConnectionConfiguration，下面import都是继承RedisConnectionConfiguration实现的，只是LettuceConnectionConfiguration设置成了默认资源==

```
    
    @Configuration
    @ConditionalOnClass(RedisOperations.class)//判断是否有redis的包
    @EnableConfigurationProperties(RedisProperties.class)//注入redis相关配置
    @Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
    public class RedisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
			throws UnknownHostException {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}






@Configuration
@ConditionalOnClass(RedisClient.class)
class LettuceConnectionConfiguration extends RedisConnectionConfiguration {

	private final RedisProperties properties;

	private final ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers;

	LettuceConnectionConfiguration(RedisProperties properties,
			ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
			ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
			ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers) {
		super(properties, sentinelConfigurationProvider, clusterConfigurationProvider);
		this.properties = properties;
		this.builderCustomizers = builderCustomizers;
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean(ClientResources.class)
	public DefaultClientResources lettuceClientResources() {
		return DefaultClientResources.create();
	}
    
    
    //这个类也看看吧，里面设置很多加载条件 比如	@ConditionalOnMissingBean(RedisConnectionFactory.class) 认为容器已经存在RedisConnectionFactory描述就不加载
    @@ConditionalOnClass(RedisClient.class) 容器已经存在redis的客户端就不加载
    
    
	@Bean
	@ConditionalOnMissingBean(RedisConnectionFactory.class)
	public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources)
			throws UnknownHostException {
		LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(clientResources,
				this.properties.getLettuce().getPool());
		return createLettuceConnectionFactory(clientConfig);
	}

	private LettuceConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration) {
		if (getSentinelConfig() != null) {
			return new LettuceConnectionFactory(getSentinelConfig(), clientConfiguration);
		}
		if (getClusterConfiguration() != null) {
			return new LettuceConnectionFactory(getClusterConfiguration(), clientConfiguration);
		}
		return new LettuceConnectionFactory(getStandaloneConfig(), clientConfiguration);
	}

	private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources, Pool pool) {
		LettuceClientConfigurationBuilder builder = createBuilder(pool);
		applyProperties(builder);
		if (StringUtils.hasText(this.properties.getUrl())) {
			customizeConfigurationFromUrl(builder);
		}
		builder.clientResources(clientResources);
		customize(builder);
		return builder.build();
	}

	private LettuceClientConfigurationBuilder createBuilder(Pool pool) {
		if (pool == null) {
			return LettuceClientConfiguration.builder();
		}
		return new PoolBuilderFactory().createBuilder(pool);
	}

	private LettuceClientConfigurationBuilder applyProperties(
			LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
		if (this.properties.isSsl()) {
			builder.useSsl();
		}
		if (this.properties.getTimeout() != null) {
			builder.commandTimeout(this.properties.getTimeout());
		}
		if (this.properties.getLettuce() != null) {
			RedisProperties.Lettuce lettuce = this.properties.getLettuce();
			if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
				builder.shutdownTimeout(this.properties.getLettuce().getShutdownTimeout());
			}
		}
		return builder;
	}

	private void customizeConfigurationFromUrl(LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
		ConnectionInfo connectionInfo = parseUrl(this.properties.getUrl());
		if (connectionInfo.isUseSsl()) {
			builder.useSsl();
		}
	}

	private void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
		this.builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
	}

	/**
	 * Inner class to allow optional commons-pool2 dependency.
	 */
	private static class PoolBuilderFactory {

		public LettuceClientConfigurationBuilder createBuilder(Pool properties) {
			return LettucePoolingClientConfiguration.builder().poolConfig(getPoolConfig(properties));
		}

		private GenericObjectPoolConfig<?> getPoolConfig(Pool properties) {
			GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
			config.setMaxTotal(properties.getMaxActive());
			config.setMaxIdle(properties.getMaxIdle());
			config.setMinIdle(properties.getMinIdle());
			if (properties.getTimeBetweenEvictionRuns() != null) {
				config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
			}
			if (properties.getMaxWait() != null) {
				config.setMaxWaitMillis(properties.getMaxWait().toMillis());
			}
			return config;
		}

	}

}


```
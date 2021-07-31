# Consul KV Bootstrapper

This application is a utility application to bootstrap application configurations held 
in Consul KV upon client application initialization. Upon initialization of a Spring Bean (`@Bean, @Component`, etc.) use `@BootstrapperMethod` to return properties declared in a `consul-config.yml` file. 

```java
@Configuration
public static class TestConfig {

    @Autowired
    static Properties properties;

    @ConsulKVBootstrapper(configFilePath = "src/test/resources/consul-config.yml")
    @Bean
    Properties properties( Properties properties )
    {
        this.properties = properties;
        return properties;
    }

    public static Properties getProperties() {
        return properties;
    }
}
```

If it is required to set up the `consul-config.yml` file in JVM environment variables, make sure you indicate the `configFilePath` parameter as follows:

```java
@ConsulKVBootstrapper(configFilePath = "{FILE_PATH}")
```

`FILE_PATH` you need to add to the JVM environments.

Configuration file:

```yaml
consulHost: localhost
consulPort: 8500
serviceId: 1
serviceName: example
configurations:
  - TEST_KEY_1
  - TEST_KEY_2

```

**Note**: Please add a `@ComponentScan` as `@ComponentScan(basePackages = {"io.realitix.*", "<your base packages>.*"})` in your main `SpringBootApplication` so that the dependency components will get scanned. 
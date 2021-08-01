# Consul KV Bootstrapper

Applications use different variables (e.g. connector hosts, ports, application-specific environment values)
for the tasks they accomplish. Most applications require these parameters at startup, where the application is
initialized with the configurations as global constants which are used at runtime. Consul KV is such a Key-Value store
for storing said configurations.

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

## What does the annotation `ConsulKVBootstrapper` do?

The annotation sends Consul agent a request and fetches key-values according to the 
criteria that the user provides in the configuration file. Afterwards, it serializes those
configurations into a `Properties` objects and injects into the method which is annotated with 
`@ConsulKVBootstrapper`. **Note that the method annotated must take in `Properties` object as an argument**.


## Configuration file

```yaml
consulHost: localhost
consulPort: 8500
serviceId: 1
serviceName: example
configurations:
  - TEST_KEY_1
  - TEST_KEY_2

```

If it is required to fetch all configurations, remove `configurations` node from the `yml`.

**Note**: Please add a `@ComponentScan` as `@ComponentScan(basePackages = {"io.realitix.*", "<your base packages>.*"})` in your main `SpringBootApplication` so that the dependency components will get scanned. 
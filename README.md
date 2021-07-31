# Consul KV Bootstrapper

This application is a utility application to bootstrap application configurations held 
in Consul KV upon client application initialization.

```java
ConsulClient client = ConsulConnectorFactory.getConsulClient("localhost", 8500, "1", "example");

// More examples can be found in Tests.
```
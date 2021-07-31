package io.realitix.consulkvbootstrapper.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.realitix.consulkvbootstrapper.ConsulConnectorFactory;
import io.realitix.consulkvbootstrapper.config.BootstrapperConfig;
import io.realitix.consulkvbootstrapper.connector.ConsulClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

@Aspect
@Component
public class AnnotationProcessor {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = Logger.getAnonymousLogger();
    private ConsulClient consulClient;

    @Pointcut("execution(@io.realitix.consulkvbootstrapper.annotation.BootstrapperMethod * *(..))")
    public void checkBootstrapperMethod() {}

    @Around("checkBootstrapperMethod()")
    public Object beforeBootstrapperMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Initializing Consul Bootstrapper utility..");
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BootstrapperMethod annotation = method.getAnnotation(BootstrapperMethod.class);
        String configFilePath = annotation.configFilePath();
        BootstrapperConfig bootstrapperConfig = mapper
                .readValue(new FileReader(configFilePath), BootstrapperConfig.class);
        Properties properties = setUpAsEnvironmentVariables(bootstrapperConfig);
        return joinPoint.proceed(new Object[] {properties});
    }

    private Properties setUpAsEnvironmentVariables( BootstrapperConfig config ) {
        Properties properties = new Properties();
        consulClient = ConsulConnectorFactory.getConsulClient(
                config.getConsulHost(),
                config.getConsulPort(),
                config.getServiceId(),
                config.getServiceName()
        );
        consulClient
                .getValues(config.getConfigurations())
                .forEach(properties::put);
        return properties;
    }

}

package io.realitix.consulkvbootstrapper.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.realitix.consulkvbootstrapper.connector.ConsulConnectorFactory;
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

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private final Logger logger = Logger.getAnonymousLogger();
    private ConsulClient consulClient;

    @Pointcut("@annotation(BootstrapperMethod)")
    public void pointcut()
    {

    }


    @Around("pointcut()")
    public Object beforeBootstrapperMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Hello");
        logger.info("Initializing Consul Bootstrapper utility..");
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BootstrapperMethod annotation = method.getAnnotation(BootstrapperMethod.class);
        String configFilePath = annotation.configFilePath();
        BootstrapperConfig bootstrapperConfig = mapper
                .readValue(new FileReader(configFilePath), BootstrapperConfig.class);
        Properties properties = setUpAsEnvironmentVariables(bootstrapperConfig);
        joinPoint.proceed(new Object[]{properties});
        return properties;
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

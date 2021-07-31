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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class BootstrapperAnnotationProcessor {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private ConsulClient consulClient;

    @Pointcut("@annotation(io.realitix.consulkvbootstrapper.annotation.ConsulKVBootstrapper)")
    public void pointcut()
    {

    }


    @Around("pointcut()")
    public void beforeBootstrapperMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ConsulKVBootstrapper annotation = method.getAnnotation(ConsulKVBootstrapper.class);
        String configFilePath = annotation.configFilePath();
        String processFilePath = processFilePath(configFilePath);
        BootstrapperConfig bootstrapperConfig = mapper
                .readValue(new FileReader(processFilePath), BootstrapperConfig.class);
        Properties properties = setUpAsEnvironmentVariables(bootstrapperConfig);
        joinPoint.proceed(new Object[]{properties});
    }

    private String processFilePath( String filePath ) {
        Pattern compile = Pattern.compile("\\{(.*?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher( filePath );
        if (matcher.find()) {
            return System.getenv(filePath.replace("{", "").replace("}", ""));
        } else {
            return filePath;
        }
    }

    private Properties setUpAsEnvironmentVariables( BootstrapperConfig config ) {
        Properties properties = new Properties();
        consulClient = ConsulConnectorFactory.getConsulClient(
                config.getConsulHost(),
                config.getConsulPort(),
                config.getServiceId(),
                config.getServiceName()
        );
        if (config.getConfigurations() == null) {
            consulClient.getValues().forEach(properties::put);
        } else {
            consulClient
                    .getValues(config.getConfigurations())
                    .forEach(properties::put);
        }
        return properties;
    }

}

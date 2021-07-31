package io.realitix.consulkvbootstrapper.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BootstrapperMethod {

    String configFilePath() default "consul-config.yml";

}

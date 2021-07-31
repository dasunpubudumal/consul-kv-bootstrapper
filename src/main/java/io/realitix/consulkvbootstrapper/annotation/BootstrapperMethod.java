package io.realitix.consulkvbootstrapper.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BootstrapperMethod {

    String configFilePath() default "consul-config.yml";

}

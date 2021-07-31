package io.realitix.consulkvbootstrapper;

import io.realitix.consulkvbootstrapper.annotation.BootstrapperMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Properties;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestPropertyAssigner {

    @Configuration
    public static class TestConfig {

        @Autowired
        static Properties properties;

        @BootstrapperMethod(configFilePath = "src/test/resources/consul-config.yml")
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

    @Test
    public void testProperties()
    {
        Assertions.assertNotEquals(0, TestConfig.getProperties().size());
    }

}

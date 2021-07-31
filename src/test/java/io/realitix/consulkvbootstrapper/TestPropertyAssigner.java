package io.realitix.consulkvbootstrapper;

import io.realitix.consulkvbootstrapper.annotation.ConsulKVBootstrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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

    static {
        System.setProperty("FILE_PATH", "src/test/resources/consul-config.yml");
    }

    @Configuration
    public static class TestConfig {

        @Autowired
        static Properties properties;

        @ConsulKVBootstrapper(configFilePath = "{FILE_PATH}")
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

    @Disabled
    @Test
    public void testProperties()
    {
        Assertions.assertNotEquals(0, TestConfig.getProperties().size());
    }

}

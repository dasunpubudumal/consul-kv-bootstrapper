package io.realitix.consulkvbootstrapper;

import io.realitix.consulkvbootstrapper.connector.ConsulClient;
import io.realitix.consulkvbootstrapper.connector.ConsulConnectorFactory;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Disabled
class ConsulConnectorFactoryTest {

    static ConsulClient client;

    @BeforeAll
    static void setUp() {
        client = ConsulConnectorFactory
                .getConsulClient("localhost", 8500, "1", "example");
    }

    @Test
    @Order(1)
    public void testGetConsulClient()
    {
        Assertions.assertNotNull(
                client
        );
    }

    @Test
    @Order(2)
    public void testKvAdditions() {
        HashMap<String, String> testKeyValues = new HashMap<>();
        testKeyValues.put("TEST_KEY_1", "VALUE_1");
        testKeyValues.put("TEST_KEY_2", "VALUE_2");
        testKeyValues.put("TEST_KEY_3", "VALUE_3");
        testKeyValues.put("TEST_KEY_4", "VALUE_4");
        testKeyValues.put("TEST_KEY_5", "VALUE_5");
        Assertions.assertDoesNotThrow(() -> client.addKeyValues(testKeyValues));
    }

    @Test
    @Order(3)
    public void testKvGet()
    {
        Assertions.assertDoesNotThrow(() -> client.getValues(List.of(
                "TEST_KEY_1", "TEST_KEY_2", "TEST_KEY_3", "TEST_KEY_4", "TEST_KEY_5"
        )));

        Map<String, String> values = client.getValues(List.of(
                "TEST_KEY_1", "TEST_KEY_2", "TEST_KEY_3", "TEST_KEY_4", "TEST_KEY_5"
        ));

        Assertions.assertEquals(List.of(
                "VALUE_1", "VALUE_2", "VALUE_3", "VALUE_4", "VALUE_5"
        ), values.values().stream().sorted().collect(Collectors.toList()));
    }

}
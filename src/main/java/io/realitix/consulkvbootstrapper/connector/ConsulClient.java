package io.realitix.consulkvbootstrapper.connector;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import io.realitix.consulkvbootstrapper.exception.ConsulClientException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConsulClient {

    private final Consul client;
    private final AgentClient agentClient;
    private final KeyValueClient keyValueClient;
    private final Logger logger;

    public ConsulClient(Consul client, String serviceId, String serviceName) {
        this.client = client;
        this.agentClient = this.client.agentClient();
        this.keyValueClient = this.client.keyValueClient();
        this.logger = Logger.getAnonymousLogger();;
        try {
            initialize(
                    serviceId,
                    serviceName
            );
        } catch (NotRegisteredException e) {
            logger.severe(e.getMessage());
        }

    }

    private void initialize(String serviceId, String serviceName) throws NotRegisteredException {
        Registration service = ImmutableRegistration
                .builder()
                .id(serviceId)
                .name(serviceName)
                .meta(Collections.singletonMap("version", "1.0"))
                .build();
        agentClient.register(service);
        agentClient.pass(serviceId);
    }

    public void addKeyValues(Map<String, String> keyValues)
    {
        keyValues.forEach((key, value) -> {
            boolean success = keyValueClient.putValue(key, value);
            if (!success) throw new ConsulClientException("Key Value Persistence Failed.");
        });
    }

    public Map<String, String> getValues()
    {
        Map<String, String> output = new HashMap<>();
        keyValueClient.getValues("").forEach(value -> {
            value.getValueAsString().ifPresent(v -> output.put(v, value.getKey()));
        });
        return output;
    }

    public String getValue(String key)
    {
        return keyValueClient.getValue(key)
                .orElseThrow(() -> new ConsulClientException("No value for the key."))
                .getValue()
                .orElseThrow(() -> new ConsulClientException("No value for the key."));

    }

    public Map<String, String> getValues(List<String> keys) {
        Map<String, String> output = new HashMap<>();
        keys.forEach(key -> keyValueClient.getValueAsString(key).ifPresent(value -> output.put(key, value)));
        return output;
    }

    private Consul getClient()
    {
        return client;
    }

}

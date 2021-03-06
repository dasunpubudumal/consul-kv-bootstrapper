package io.realitix.consulkvbootstrapper.connector;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;

public class ConsulConnectorFactory {

    private static Consul client;

    public static ConsulClient getConsulClient(String host, int port, String serviceId, String serviceName)
    {
        client = Consul.builder()
                .withHostAndPort(HostAndPort.fromString(host.concat(":").concat(String.valueOf(port))))
                .build();
        return new ConsulClient(client, serviceId, serviceName);
    }

    private static Consul getClient() {return client;}


}

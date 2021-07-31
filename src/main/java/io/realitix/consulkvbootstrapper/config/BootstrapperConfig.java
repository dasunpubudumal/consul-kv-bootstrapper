package io.realitix.consulkvbootstrapper.config;

import java.util.List;

public class BootstrapperConfig {

    private String consulHost;
    private String serviceName;
    private String serviceId;
    private int consulPort;
    private List<String> configurations;

    public List<String> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<String> configurations) {
        this.configurations = configurations;
    }

    public String getConsulHost() {
        return consulHost;
    }

    public void setConsulHost(String consulHost) {
        this.consulHost = consulHost;
    }

    public int getConsulPort() {
        return consulPort;
    }

    public void setConsulPort(int consulPort) {
        this.consulPort = consulPort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}

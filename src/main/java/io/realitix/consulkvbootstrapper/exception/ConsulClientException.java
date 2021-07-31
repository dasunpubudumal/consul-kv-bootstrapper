package io.realitix.consulkvbootstrapper.exception;

public class ConsulClientException extends RuntimeException{

    public ConsulClientException(Throwable throwable)
    {
        super("Consul Client Exception: ".concat(throwable.getMessage()));
    }

    public ConsulClientException(String message)
    {
        super("Consul Client Exception: ".concat(message));
    }

}

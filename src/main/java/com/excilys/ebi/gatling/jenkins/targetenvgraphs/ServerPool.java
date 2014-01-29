package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

public enum ServerPool {
    APISERVER_POOL("apiserver"),
    APPSERVER_POOL("appserver"),
    GIMSERVER_POOL("gimserver"),
    GRFSERVER_POOL("grfserver"),
    IMSERVER_POOL("imserver"),
    UPLOADSERVER_POOL("upload"),
    WSSERVER_POOL("wsserver");

    public final String name;

    private ServerPool(String name) {
        this.name = name;
    }
}

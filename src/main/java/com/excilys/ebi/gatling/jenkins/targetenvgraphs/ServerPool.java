package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

import java.util.logging.Level;
import java.util.logging.Logger;

public enum ServerPool {
    APISERVER("apiserver", "api"),
    APPSERVER("appserver", "app"),
    GIMSERVER("gimserver", "gim"),
    GRFSERVER("grfserver", "grf"),
    IMSERVER("imserver", "im"),
    UPLOADSERVER("upload", "up"),
    WSSERVER("wsserver", "ws");

    public final String longName;
    public final String shortName;

    private static final Logger logger = Logger.getLogger(ServerPool.class.getName());

    private ServerPool(String longNname, String shortName) {
        this.longName = longNname;
        this.shortName = shortName;
    }

    public static ServerPool getEnumForPoolName(String poolName) {
        if(null != poolName && poolName.trim().length() > 0) {
            for(ServerPool serverPool: ServerPool.values()) {
                if(poolName.trim().equalsIgnoreCase(serverPool.longName)) {
                    return serverPool;
                }
            }
        }
        return null;
    }
}

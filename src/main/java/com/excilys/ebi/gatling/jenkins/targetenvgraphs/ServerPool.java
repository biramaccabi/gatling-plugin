/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.jenkins.targetenvgraphs;

public enum ServerPool {
    APISERVER("apiserver", "api"),
    APPSERVER("appserver", "app"),
    GIMSERVER("gimserver", "gim"),
    GRFSERVER("grfserver", "grf"),
    IMAGESERVER("imageserver", "im"),
    IMSAGE("image", "im"),
    MEDIA("mediaserver", "media"),
    OMS("oms", "oms"),
    SHARE("shareserver", "share"),
    UPLOADSERVER("upload", "up"),
    WSSERVER("wsserver", "ws");

    public final String longName;
    public final String shortName;

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

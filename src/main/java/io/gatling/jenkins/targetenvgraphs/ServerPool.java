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
package io.gatling.jenkins.targetenvgraphs;

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
    WSSERVER("wsserver", "ws"),
    ADMINSERVER("admin", "admin"),
    CCSERVER("cc","cc"),
    CMSERVER("cm","cm"),
    CSSERVER("cs","cs"),
    DOWNLOADSERVER("download","download"),
    EPINDEXSERVER("epindex","epindex"),
    EPSEARCHSERVER("epsearch","epsearch"),
    ESSEARCHSERVER("essearch","essearch"),
    FIREHOSESERVER("firehose","firehose"),
    MONGODBSERVER("mongodb","mongodb"),
    ORACLESERVER("oracle","oracle"),
    ORDERSERVER("order","order"),
    ORDERAPISERVER("orderapi","orderapi"),
    ORDERCONSSERVER("ordercons","ordercons"),
    POSTALSERVER("postal","postal"),
    SSAPISERVER("sharedservices","ssapi"),
    SSPROMOSERVER("sspromo","sspromo"),
    UNIFIEDUPLOADSERVER("uniup","uniup"),
    WEBSERVER("web","web"),
    WSLABSERVER("wslab","ws-lab");


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

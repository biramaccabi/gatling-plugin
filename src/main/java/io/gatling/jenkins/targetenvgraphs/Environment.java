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

public enum Environment {
    KAPPA ("kappa", Brand.SHUTTERFLY),
    STAGE("stage", Brand.SHUTTERFLY),
    PROD("prod", Brand.SHUTTERFLY),
    BETA("beta", Brand.SHUTTERFLY),
    TPLNP("lnp", Brand.TINYPRINTS);

    public final String name;
    public final Brand brand;

    private Environment(String name, Brand brand) {
        this.name = name;
        this.brand = brand;
    }
}

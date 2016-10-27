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
package io.gatling.jenkins;

public interface PluginConstants {
	String ICON_URL = "/plugin/gatling/img/logo.png";
    //String DISPLAY_NAME = "Gatling";
    String TARGET_ENV_GRAPHS_ICON = "/plugin/gatling/img/graph_icon.png";

    String DISPLAY_NAME_REPORTS = "Gatling - Reports";

    String URL_NAME = "gatling";

	String DISPLAY_NAME_SOURCE = "Gatling - Test Source";
    String TARGET_ENV_GRAPHS_DISPLAY_STRING = "Graphs - System Information";

	int MAX_BUILDS_TO_DISPLAY = 30;
	int MAX_BUILDS_TO_DISPLAY_DASHBOARD = 15;
}

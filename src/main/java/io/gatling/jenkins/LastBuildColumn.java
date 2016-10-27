/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.jenkins;

import hudson.Extension;
import hudson.XmlFile;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.views.ListViewColumn;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import hudson.views.ListViewColumnDescriptor;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class LastBuildColumn extends ListViewColumn {

	private static final Logger logger = Logger.getLogger(LastBuildColumn.class.getName());
	private static final String GatlingPluginConfigTest = "io.gatling.jenkins.GatlingPublisher";

	public String getLastBuildDescription(String description,Result result) {
		StringBuilder stringBuilder = new StringBuilder();
		if (result != null){
			if (result.equals(Result.UNSTABLE)){
				stringBuilder.append(description);
			}else if (result.equals(Result.FAILURE)){
				stringBuilder.append("FAILURE");
			}else if (result.equals(Result.SUCCESS)){
				// Do nothing for successful case
			}else if (result.equals(Result.ABORTED)){
				stringBuilder.append("ABORTED");
			}else{
				stringBuilder.append("UNKNOWN");
			}
		}else{
			logger.fine("No Result");
		}
		return stringBuilder.toString();
	}

	private boolean isGatlingJob(XmlFile configFile) throws IOException{
		boolean result = false;
		BufferedReader br = new BufferedReader(configFile.readRaw());
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			if (currentLine.contains(GatlingPluginConfigTest)){
				result = true;
				break;
			}
		}
		return result;
	}

	public String getShortName(Job job){
		StringBuilder stringBuilder = new StringBuilder();
		try {
			Run lastBuild = job.getLastCompletedBuild();
			XmlFile configFile = job.getConfigFile();
			if (isGatlingJob(configFile)){
				if (lastBuild != null) {
					String tempdescription = lastBuild.getDescription();
					Result result = lastBuild.getResult();
					String description = getLastBuildDescription(tempdescription,result);
					stringBuilder.append(description);
				} else {
					stringBuilder.append("N/A");
				}
			}else{
				logger.fine("Not Gatling Project");
			}
		}catch (Exception e){
			logger.log(Level.WARNING,"Exception - " + job.getDisplayName(), e);
		}finally {
			logger.fine(job.getDisplayName() + " - " + stringBuilder.toString());
		}
		return stringBuilder.toString();
	}

	@Extension
	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();

	public Descriptor<ListViewColumn> getDescriptor() {
		return DESCRIPTOR;
	}

	private static class DescriptorImpl extends ListViewColumnDescriptor {
		@Override
		public ListViewColumn newInstance(StaplerRequest req,
										  JSONObject formData) throws FormException {
			return new LastBuildColumn();
		}

		@Override
		public boolean shownByDefault() {
			return false;
		}

		@Override
		public String getDisplayName() {
			return "Gatling Result Summary";
		}


	}
}
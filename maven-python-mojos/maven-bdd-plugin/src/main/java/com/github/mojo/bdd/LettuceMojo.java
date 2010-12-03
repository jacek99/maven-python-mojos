package com.github.mojo.bdd;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Goat which runs Python Lettuce feature story tests from src/test/python/features
 * 
 * @goal lettuce
 * @phase integration-test
 */
public class LettuceMojo extends AbstractBddMojo {

	public static final String FEATURE = "feature";
	
	public LettuceMojo() {
		super("Lettuce","lettuce.txt","src/test/python","src/test/python/features","lettuce","-v 2");
	}
	
	@Override
	protected void preExecute() throws MojoExecutionException, MojoFailureException {
		super.preExecute();
		
		//see if a particular feature was specified to run
		if (System.getProperty(FEATURE) != null) {
			String featureName = System.getProperty(FEATURE);
			//we need to find the first file that corresponds to that feature name
			File testDir = new File(getTestDirectory());
			Collection<File> features = FileUtils.listFiles(testDir, new String[]{"feature"}, true);
			
			boolean found = false;
			for(File feature : features) {
				if (feature.getName().equals(featureName + ".feature")) {
					found = true;
					getRequestOptions().add(feature.getAbsolutePath());
					break;
				}
			}
			
			if (!found) {
				throw new MojoFailureException("Unable to find " + featureName + ".feature under " + getTestDirectory());
			}
		}
		
	}

}

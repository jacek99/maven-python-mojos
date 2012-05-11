package com.github.mojo.bdd;

import java.io.File;
import java.io.IOException;

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
 * Goal which runs regular Python Nose tests from src/test/python
 * 
 * @goal nose
 * @phase integration-test
 */
public class NoseMojo extends AbstractBddMojo {

	public static final String TAGS = "tags";
	public static final String PATH = "path";
	
	public NoseMojo() {
		super("Nose","nose.txt","nosetests","--with-freshen","-v","-s","--failure-detail");
	}

	/* (non-Javadoc)
	 * @see com.github.mojo.bdd.AbstractBddMojo#preExecute()
	 */
	@Override
	protected void preExecute() throws MojoExecutionException, MojoFailureException {
		super.preExecute();
		try {
			FileUtils.touch(new File("target/bdd-reports/nosetests.xml"));
		} catch (IOException e) {
			getLog().error("Failed to create touch target/bdd-reports/nosetests.xml", e);
			throw new MojoExecutionException("Failed to create touch target/bdd-reports/nosetests.xml");
		}
		
		//add support for explicitly specifying a path for BDD tests
		if (System.getProperty(PATH) != null) {
			String path = System.getProperty(PATH);
			getRequestOptions().add(path);
		}
		
		//add support for running with tags
		if (System.getProperty(TAGS) != null) {
			String tags = System.getProperty(TAGS);
			getRequestOptions().add("--tags=" + tags);
		}
		
		//add support for re-running last failed tesrs only
		if ("true".equals(System.getProperty(BddConstants.FAILED_ONLY))) {
			getRequestOptions().add("--failed");
		}
	}
	

	/* (non-Javadoc)
	 * @see com.github.mojo.bdd.AbstractBddMojo#postExecute(java.lang.StringBuilder)
	 */
	@Override
	protected void postExecute(StringBuilder output) throws MojoExecutionException, MojoFailureException {
		//workaround for a Nose/Freshen shortcoming where UNDEFINED steps do not fail the build
		if (output.indexOf("UNDEFINED") > 0) {
			getLog().error("UNDEFINED steps found! Failing build.");
			throw new MojoExecutionException("UNDEFINED steps found! Failing build.");
		}
	}
}

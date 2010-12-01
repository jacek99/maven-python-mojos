package com.github.mojos.distribute;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Installs a Python module using distribute
 * 
 * @goal install
 * @phase install
 */
public class InstallMojo extends AbstractMojo {

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
	
		File setup = new File("src/main/python/setup.py");
		
		try {
			//execute setup script
			ProcessBuilder t = new ProcessBuilder("python","setup.py","install");
			t.directory(new File("src/test/python"));
			t.redirectErrorStream(true);

			Process pr = t.start();
			int exitCode = pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				getLog().info(line);
			}
			
			if (exitCode != 0) {
				throw new MojoExecutionException("'python setup.py install' returned error code " + exitCode);
			}
			
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException("Unable to find " + setup.getPath(),e);
		} catch (IOException e) {
			throw new MojoExecutionException("Unable to read " + setup.getPath(),e);		
		} catch (InterruptedException e) {
			throw new MojoExecutionException("Unable to execute python " + setup.getPath(),e);
		}
	}
}

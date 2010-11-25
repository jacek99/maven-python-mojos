package com.github.mojo.bdd.test;

import static org.junit.Assert.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import com.github.mojo.bdd.CucumberMojo;
import com.github.mojo.bdd.LettuceMojo;
import com.github.mojo.bdd.NoseMojo;

/**
 * Tests for the Lettuce Mojo
 * @author jacekf
 *
 */
public class MojosTest {

	@Test
	public void testLettuce() throws MojoExecutionException, MojoFailureException {
		LettuceMojo lettuce = new LettuceMojo();
		
		assertEquals("src/test/python",lettuce.getWorkingDirectory());
		assertEquals("src/test/python/features",lettuce.getTestDirectory());
		
		//just for the test, point to a different folder to avoid conflicting with the Nose tests
		lettuce.setWorkingDirectory("src/test/lettuce");
		lettuce.setTestDirectory("src/test/lettuce/features");
		
		lettuce.execute();
	}
	
	@Test
	public void testNose() throws MojoExecutionException, MojoFailureException {
		NoseMojo mojo = new NoseMojo();
		mojo.execute();
	}
	
	@Test
	public void testCucumber() throws MojoExecutionException, MojoFailureException {
		CucumberMojo mojo = new CucumberMojo();
		mojo.execute();
	}
	
}

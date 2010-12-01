package com.github.mojo.bdd.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.mojo.bdd.BddConstants;
import com.github.mojo.bdd.LettuceMojo;
import com.github.mojo.bdd.NoseMojo;

/**
 * Tests for the Lettuce Mojo
 * @author jacekf
 *
 */
public class MojosTest {

	@Before
	public void setUp() {
		File dir = new File("target/bdd-reports");
		if (dir.exists()) {
			for(File file : dir.listFiles()) {
				file.delete();
			}
		}
	}
	
	@Test
	public void testLettuce() throws MojoExecutionException, MojoFailureException {
		LettuceMojo lettuce = new LettuceMojo();
		
		assertEquals("src/test/python",lettuce.getWorkingDirectory());
		assertEquals("src/test/python/features",lettuce.getTestDirectory());
		
		//just for the test, point to a different folder to avoid conflicting with the Nose tests
		lettuce.setWorkingDirectory("src/test/lettuce");
		lettuce.setTestDirectory("src/test/lettuce/features");
		
		lettuce.execute();
		
		assertFile("lettuce.txt");
	}
	
	@Test 
	public void testNose() throws MojoExecutionException, MojoFailureException {
		NoseMojo mojo = new NoseMojo();
		mojo.execute();
		
		assertFile("nose.txt");
		assertFile("nosetests.xml");
	}
	
	@Test 
	public void testNoseFailed() throws MojoExecutionException, MojoFailureException {
		System.setProperty(BddConstants.FAILED_ONLY, "true");
		NoseMojo mojo = new NoseMojo();
		
		//assert that the "--failed" option has been added
		boolean found = false;
		for(String command : mojo.getTestCommands()) {
			if (command.equals("--failed")) {
				found = true;
				break;
			}
		}
		assertTrue("Did not find --failed option for Nose",found);
		
		mojo.execute();
		
		assertFile("nose.txt");
		assertFile("nosetests.xml");
	}
	
	@Test(expected=MojoExecutionException.class)
	public void testNoseUndefinedFails() throws MojoExecutionException, MojoFailureException {
		NoseMojo mojo = new NoseMojo();
		
		mojo.setWorkingDirectory("src/test/nose-undefined");
		mojo.setTestDirectory("src/test/nose-undefined/features");
		
		mojo.execute();
	}
	
	private void assertFile(String fileName) {
		File file = new File("target/bdd-reports/" + fileName);
		assertTrue(fileName + " did not get created", file.exists());
	}
	
}

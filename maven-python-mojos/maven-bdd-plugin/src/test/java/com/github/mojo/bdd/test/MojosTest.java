package com.github.mojo.bdd.test;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Test;

import com.github.mojo.bdd.AbstractBddMojo;
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
	
	//mocks the Plexus container dependency injection
	private void mockSetUp(AbstractBddMojo mojo) {
		mojo.setOutputDirectory(new File("target"));
	}
	
	@Test
	public void testLettuce() throws MojoExecutionException, MojoFailureException {
		LettuceMojo mojo = new LettuceMojo();
		mockSetUp(mojo);
		
		assertEquals("src/test/python",mojo.getWorkingDirectory());
		assertEquals("src/test/python/features",mojo.getTestDirectory());
		
		//just for the test, point to a different folder to avoid conflicting with the Nose tests
		mojo.setWorkingDirectory("src/test/lettuce");
		mojo.setTestDirectory("src/test/lettuce/features");
		
		mojo.execute();
		
		assertFile("lettuce.txt");
	}
	
	@Test 
	public void testNose() throws MojoExecutionException, MojoFailureException {
		NoseMojo mojo = new NoseMojo();
		mockSetUp(mojo);
		
		mojo.execute();
		
		assertFile("nose.txt");
		assertFile("nosetests.xml");
	}
	
	@Test 
	public void testNoseFailed() throws MojoExecutionException, MojoFailureException {
		System.setProperty(BddConstants.FAILED_ONLY, "true");
		
		NoseMojo mojo = new NoseMojo();
		mockSetUp(mojo);
		
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
		mockSetUp(mojo);
		
		mojo.setWorkingDirectory("src/test/nose-undefined");
		mojo.setTestDirectory("src/test/nose-undefined/features");
		
		mojo.execute();
	}
	
	private void assertFile(String fileName) {
		File file = new File("target/bdd-reports/" + fileName);
		assertTrue(fileName + " did not get created", file.exists());
	}
	
}

package com.github.mojo.bdd.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
		
		//clean up any used properties
		System.getProperties().remove(LettuceMojo.FEATURE);
		System.getProperties().remove(NoseMojo.TAGS);
		System.getProperties().remove(BddConstants.FAILED_ONLY);
	}
	
	//mocks the Plexus container dependency injection
	private void mockSetUp(AbstractBddMojo mojo) {
		mojo.setPython("python");
		mojo.setProjectDirectory(new File("."));
		mojo.setBuildDirectory(new File("target"));
	}
	
	@Test
	public void testLettuce() throws MojoExecutionException, MojoFailureException, IOException {
		LettuceMojo mojo = new LettuceMojo();
		mockSetUp(mojo);
		
		assertEquals("src/test/python",mojo.getWorkingDirectory());
		assertEquals("src/test/python/features",mojo.getTestDirectory());
		
		//just for the test, point to a different folder to avoid conflicting with the Nose tests
		mojo.setWorkingDirectory("src/test/lettuce");
		mojo.setTestDirectory("src/test/lettuce/features");
		
		mojo.execute();
		
		assertFile("lettuce.txt","Factorial","18 steps (18 passed)");
	}
	
	@Test
	public void testLettuceWithFeature() throws MojoExecutionException, MojoFailureException, IOException {
		
		System.getProperties().put(LettuceMojo.FEATURE, "zero"); //zero.feature
		
		LettuceMojo mojo = new LettuceMojo();
		mockSetUp(mojo);
		
		//just for the test, point to a different folder to avoid conflicting with the Nose tests
		mojo.setWorkingDirectory("src/test/lettuce");
		mojo.setTestDirectory("src/test/lettuce/features");
		
		mojo.execute();
		
		//verify feature option was added
		for(String opt : mojo.getRequestOptions()) {
			assertTrue("zero.feature not found",opt.endsWith("zero.feature"));
		}
		
		assertFile("lettuce.txt","Factorial","9 steps (9 passed)");
	}
	
	@Test(expected=MojoFailureException.class)
	public void testLettuceWithWrongFeature() throws MojoExecutionException, MojoFailureException {
		
		System.getProperties().put(LettuceMojo.FEATURE, "wrong_feature"); //zero.feature
		
		LettuceMojo mojo = new LettuceMojo();
		mockSetUp(mojo);
		
		//just for the test, point to a different folder to avoid conflicting with the Nose tests
		mojo.setWorkingDirectory("src/test/lettuce");
		mojo.setTestDirectory("src/test/lettuce/features");
		
		mojo.execute();
	}

	
	@Test 
	public void testNose() throws MojoExecutionException, MojoFailureException, IOException {
		NoseMojo mojo = new NoseMojo();
		mockSetUp(mojo);
		
		mojo.execute();
		
		assertFile("nose.txt","OK","Addition:","Division:","Bill students");
		assertFile("nosetests.xml");
	}
	
	@Test 
	public void testNoseFailed() throws MojoExecutionException, MojoFailureException, IOException {
		System.setProperty(BddConstants.FAILED_ONLY, "true");
		
		NoseMojo mojo = new NoseMojo();
		mockSetUp(mojo);
		
		mojo.execute();
		
		assertTrue("Did not find --failed option for Nose",mojo.getRequestOptions().contains("--failed"));
		
		assertFile("nose.txt","OK");
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

	@Test
	public void testNoseTags() throws MojoExecutionException, MojoFailureException, IOException {
		
		System.getProperties().put("tags", "addition");
		
		NoseMojo mojo = new NoseMojo();
		mockSetUp(mojo);
		
		mojo.execute();
		
		for(String opt : mojo.getRequestOptions()) {
			assertEquals("--tags=addition",opt);
		}
		
		assertFile("nose.txt","Addition:","OK");
	}

	
	private void assertFile(String fileName,String... contains) throws IOException {
		File file = new File("target/bdd-reports/" + fileName);
		assertTrue(fileName + " did not get created", file.exists());
		
		String content = FileUtils.readFileToString(file);
		for(String contain : contains) {
			assertTrue(fileName + " does not contain '" + contain + "'",content.contains(contain));
		}
	}
	
}

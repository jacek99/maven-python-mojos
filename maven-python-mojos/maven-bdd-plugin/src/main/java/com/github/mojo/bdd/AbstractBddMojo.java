package com.github.mojo.bdd;

import static com.google.common.collect.Sets.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.google.common.collect.Sets;

/**
 * Common class for BDD test runs
 * 
 * @author Jacek Furmankiewicz
 */
@EqualsAndHashCode(of = "toolName", callSuper = false)
public abstract class AbstractBddMojo extends AbstractMojo {

	/**
	 * @parameter default-value="${project.reporting.outputDirectory}"
	 */
	@Getter
	@Setter
	private File outputDirectory;

	@Getter
	@Setter
	private String toolName;
	@Getter
	@Setter
	private String testReportName;
	@Getter
	@Setter
	private String workingDirectory;
	@Getter
	@Setter
	private String testDirectory;
	@Getter
	@Setter
	private String[] testCommands;

	/**
	 * Extra command options appended to default test command
	 */
	@Getter
	@Setter
	private Set<String> requestOptions = newLinkedHashSet();

	protected AbstractBddMojo(String toolName, String testReportName, String workingDirectory, String testDirectory,
			String... testCommands) {
		this.toolName = toolName;
		this.testReportName = testReportName;
		this.workingDirectory = workingDirectory;
		this.testDirectory = testDirectory;
		this.testCommands = testCommands;
	}

	/**
	 * Can be overriden in descendants to do Mojo-specific stuff
	 */
	protected void preExecute() throws MojoExecutionException, MojoFailureException {
	}

	/**
	 * Can be overriden in descendants for any post-processing Only called upon
	 * success
	 * 
	 * @param output
	 */
	protected void postExecute(StringBuilder output) throws MojoExecutionException, MojoFailureException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		BddUtils.createReportsFolder(outputDirectory);

		try {

			preExecute();

			if (new File(testDirectory).exists()) {

				getLog().info("");
				getLog().info("Running " + toolName + " from " + workingDirectory);
				getLog().info("");

				StringBuilder bld = new StringBuilder();

				// merge the default and request-specific commands
				Set<String> commands = Sets.union(Sets.newLinkedHashSet(Arrays.asList(testCommands)), requestOptions);
				
				ProcessBuilder t = new ProcessBuilder(commands.toArray(new String[commands.size()]));
				t.directory(new File(workingDirectory));
				t.redirectErrorStream(true);

				Process pr = t.start();
				int exitCode = pr.waitFor();
				BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line = "";
				while ((line = buf.readLine()) != null) {
					bld.append(line).append("\n");
					getLog().info(line);
				}

				BddUtils.writeReport(outputDirectory, testReportName, bld.toString(), getLog());

				if (bld.length() == 0) {
					getLog().warn(toolName + " did not return any output. No unit test(s) found?");
					throw new MojoFailureException(toolName + " did not return any output. No unit test(s) found?");
				}

				if (exitCode != 0) {
					throw new MojoFailureException(toolName + " unit test(s) failed");
				}

				postExecute(bld);

			} else {

				getLog().warn("No " + toolName + " unit test(s) found. Please create some in " + testDirectory);
				throw new MojoFailureException("No " + toolName + " unit test(s) found. Please create some in " + testDirectory);

			}
		} catch (MojoFailureException ex) {
			throw ex;
		} catch (MojoExecutionException ex) {
			throw ex;
		} catch (Exception ex) {
			getLog().error(ex);
			throw new MojoExecutionException("Failed to run " + toolName + " unit test(s)", ex);
		} finally {
			// TODO: cleanup
		}

	}

}

package com.github.mojo.bdd;

import static com.google.common.collect.Sets.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import com.google.common.collect.Sets;

/**
 * Common class for BDD test runs
 * 
 * @author Jacek Furmankiewicz
 */
@EqualsAndHashCode(of = "toolName", callSuper = false)
public abstract class AbstractBddMojo extends AbstractMojo {

	public static final String REPORTS_FOLDER = "bdd-reports";

	/**
	 * @parameter default-value="${project.basedir}"
	 */
	@Getter
	@Setter
	private File projectDirectory;
	/**
	 * @parameter default-value="${project.build.directory}"
	 */
	@Getter
	@Setter
	private File buildDirectory;

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
		requestOptions.clear();
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

		createReportsFolder();

		try {
			preExecute();

			File testFolder = new File(projectDirectory,testDirectory);
			
			if (testFolder.exists()) {

				getLog().info("");
				getLog().info("Running " + toolName + " from " + workingDirectory);
				getLog().info("");

				StringBuilder bld = new StringBuilder();

				// merge the default and request-specific commands
				Set<String> commands = Sets.union(Sets.newLinkedHashSet(Arrays.asList(testCommands)), requestOptions);

				ProcessBuilder t = new ProcessBuilder(commands.toArray(new String[commands.size()]));

				File directory = new File(projectDirectory + File.separator + this.workingDirectory);

				t.directory(directory);
				t.redirectErrorStream(true);

				Process pr = t.start();
				int exitCode = pr.waitFor();
				BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line = "";
				while ((line = buf.readLine()) != null) {
					bld.append(line).append("\n");
					getLog().info(line);
				}

				writeReport(testReportName, bld.toString(), getLog());

				if (bld.length() == 0) {
					getLog().warn(toolName + " did not return any output. No unit test(s) found?");
					throw new MojoFailureException(toolName + " did not return any output. No unit test(s) found?");
				}

				if (exitCode != 0) {
					throw new MojoFailureException(toolName + " unit test(s) failed");
				}

				postExecute(bld);

			} else {

				getLog().warn("No " + toolName + " unit test(s) found. Please create some in " + testFolder.getPath());
				throw new MojoFailureException("No " + toolName + " unit test(s) found. Please create some in " + testFolder.getPath());

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

	private void createReportsFolder() throws MojoExecutionException {

		File reports = new File(buildDirectory, REPORTS_FOLDER);
		if (reports.exists()) {

			if (!reports.isDirectory()) {
				throw new MojoExecutionException("Unable to create reports folder in " + reports.getAbsolutePath()
						+ ", a file already exists with that name");
			}

		} else {
			if (!reports.mkdirs()) {
				throw new MojoExecutionException("Unable to create reports folder in " + reports.getAbsolutePath());
			}
		}
	}

	private void writeReport(String reportName, String body, Log log) throws MojoExecutionException {
		File reportsFolder = new File(buildDirectory, REPORTS_FOLDER);
		File report = new File(reportsFolder, reportName);
		if (report.exists()) {
			if (!report.delete()) {
				throw new MojoExecutionException("Failed to delete " + report.getAbsolutePath());
			}
		} else {
			try {
				log.info("");
				log.info("Writing report to " + report.getAbsolutePath());
				IOUtils.write(body, new FileOutputStream(report));
			} catch (Exception e) {
				throw new MojoExecutionException("Failed to write report " + report.getAbsolutePath(), e);
			}
		}
	}

}

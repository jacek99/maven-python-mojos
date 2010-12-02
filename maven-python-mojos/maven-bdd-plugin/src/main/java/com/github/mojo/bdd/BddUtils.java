package com.github.mojo.bdd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * Common utils
 * @author Jacek Furmankiewicz
 *
 */
public class BddUtils {

	public static final String REPORTS_FOLDER = "target/bdd-reports";
	
	public static void createReportsFolder() throws MojoExecutionException {
		
		File reports = new File(REPORTS_FOLDER);
		if (reports.exists()) {
			
			if (!reports.isDirectory()) {
				throw new MojoExecutionException("Unable to create reports folder in " + REPORTS_FOLDER + ", a file already exists with that name");
			}
			
		} else {
			if (!reports.mkdirs()) {
				throw new MojoExecutionException("Unable to create reports folder in " + REPORTS_FOLDER);
			}
		}
	}
	
	/**
	 * Writes a BDD report to the target folder
	 * @param reportName
	 * @param body
	 * @throws MojoExecutionException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void writeReport(String reportName, String body, Log log) throws MojoExecutionException {
		File report = new File(REPORTS_FOLDER + File.separator + reportName);
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
				throw new MojoExecutionException("Failed to write report " + report.getAbsolutePath(),e);
			} 
		}
	}
	
}

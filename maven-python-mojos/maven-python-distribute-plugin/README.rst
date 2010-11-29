Maven Python Distribute Plugin
==============================

This plugin integrates the Python **distribute** module into the Maven build:

http://packages.python.org/distribute/

This allows you to build and package Python code together with your Java code,
which is useful for IT shops that develop in both of these languages.
  
Functionality
-------------

* keeps the *setup.py* version in sync with the Maven project version
* packages the Python module during the Maven **package* phase
* allows specifying which format should the Python module be distributed as: source, RPM, egg, tar, zip, etc.


Configuration
-------------

Add the following to your


Maven Repository
----------------

Add the following plugin repository to your *pom.xml* in order to use this plugin:

::

	<pluginRepositories>
		<pluginRepository>
			<id>javabuilders</id>
			<url>http://javabuilders.googlecode.com/svn/repo</url>
		</pluginRepository>
	</pluginRepositories>






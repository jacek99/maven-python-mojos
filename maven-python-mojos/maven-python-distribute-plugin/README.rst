Maven Python Distribute Plugin
==============================

**version**: 0.1.1

This plugin integrates the Python **distribute** module into the Maven build:

http://packages.python.org/distribute/

This allows you to build and package Python code together with your Java code,
which is useful for IT shops that develop in both of these languages.
  
Functionality
-------------

* keeps the *setup.py* version in sync with the Maven project version
* packages the Python module during the Maven **package** phase
* allows specifying which format should the Python module be distributed as: source, RPM, egg, tar, zip, etc.


Configuration
-------------

Add the following to your *pom.xml* build section:
::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-python-distribute-plugin</artifactId>
		<version>..</version>
		<executions>
			<execution>
				<goals>
					<goal>package</goal>
				</goals>
			</execution>
		</executions>
	</plugin>

setup.py
--------

Set the *version* field in your *setup.py* to a hardcoded constant of **${VERSION}**, e.g.
::
	from setuptools import setup, find_packages
	
	setup(
	      install_requires=['distribute'],
	      name = 'my-library',
	      version = '${VERSION}',
	      packages = find_packages('.')
	)


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






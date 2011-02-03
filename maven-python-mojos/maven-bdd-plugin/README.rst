Maven BDD Plugin 
================

**version**: 1.0.0

The goal of this plugin is to allow using BDD tools from the Python world
for *integration testing* of Java web applications.
   
Basically, the idea is that you execute
   
  **mvn integration-test**

and it would deploy your Java app to your container and then run integration tests against
it that are written in BDD tools, such as Freshen or Lettuce.
   
How is this different than regular JUnit tests?
-----------------------------------------------

JUnit tests are great, but they require you to write them in Java (which is verbose) and the test cases you
test against are embedded directly in your code. They are impossible to extract by external teams,
such as QA.
   
The idea behind BDD is that you will write the test cases/requirements in a plain text file
using basic English language, e.g.
  
::   
   
	Feature: User REST service
		
	  In order to maintain Users
	  As an Admin/Operations user
	  I want to be able to add, delete and update the list of users in the DB
		
	  Background:
	    Given I am the user Admin with the ADMIN role
	    And there is an existing user called demo
		
	  Scenario: Adding a new user
	    When I add a new user called john_doe
	    Then I should have users Admin, demo and john_doe in the database
		
	  Scenario: Removing a user
	    When I delete the demo user
	    Then I should have users Admin and john_doe in the database
		   

BDD tools allow you map each of these steps into code during integration test runs.

Writing them in Pythony should allow you to be more productive than in regular Java 
and take advantage of the vast Python library ecosystem.
   
Goals
-----

Although the recommended solution is hooking up to the **mvn integration-test** phase, you can also run
the BDD goals separately:

* **bdd:nose** - runs all the BDD tests using Nose with Freshen
* **bdd:lettuce** - runs all the BDD tests using Lettuce   
   
Supported tools
---------------

The following BDD tools are supported:

Nose / Freshen 
^^^^^^^^^^^^^^

https://github.com/rlisagor/freshen

http://somethingaboutorange.com/mrl/projects/nose/

Nose is an extensive unit testing framework for Python. Freshen is a BDD plugin written for that framework.
Hence we support running unit tests written in *Nose* in general.

Installation (on Ubuntu):
::
	sudo apt-get install python-setuptools
	sudo easy_install freshen 

Place all your stories and Python test code in

	src/test/python/features

*pom.xml* integration

::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>..</version>
		<executions>
			<execution>
				<goals>
					<goal>nose</goal>
				</goals>
			</execution>
		</executions>
	</plugin>


**Re-running failed tests only**

You can pass the *failed=true* command line option, e.g.

  **mvn integration-test -Dfailed=true**
  
That will append the Nose "--failed" option which will tell it to only re-run the tests that failed during
the last test run.

	*Note*: there seem to be some issues with Freshen supporting the "--failed" option currently. An issue has been logged with them.

**Re-running tagged tests only**

In Freshen, you can annotate scenarios with tags, e.g. 
::
	@my_special_test
	Scenario: Do something special

You can then pass the *tags=<comma separated list of tags>* command line option, e.g.

  **mvn integration-test -Dtags=my_special_test,some_other_test**
  
That will append the Freshen "--tags" option which will tell it to only re-run the tagged tests

Lettuce
^^^^^^^

http://lettuce.it/

Lettuce is a stand-alone unit BDD framework for Python.

Installation (on Ubuntu):
::
	sudo apt-get install python-setuptools
	sudo easy_install lettuce 

Place all your stories and Python test code in

	src/test/python/features

*pom.xml* integration

::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>..</version>
		<executions>
			<execution>
				<goals>
					<goal>lettuce</goal>
				</goals>
			</execution>
		</executions>
	</plugin>


**Testing a specific feature only**

To run just a specific feature, add the *feature=<feature name>* command line option, e.g.

  **mvn integration-test -Dfeature=my_feature**
  
This will search underneath the test folder for the first file called *my_feature.feature* and tell
Lettuce to test it.

Reports
-------

All the BDD reports are created in the
 
	**target/bdd-reports**
 
folder

Configuration
-------------

Changing the default Python interpreter
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

On some Linux flavors (e.g. CentOS) the default Python interpreter is ancient (2.4). In order to gain access to newer Python version
an alternate install must be made with a separate executable (e.g. "python2.6","python2.7", etc.).

The specific python interpreter name can be specified in the configuration by adding a *configuration* section for the *python* parameter, e.g.:
::
	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>..</version>
		<executions>
			<execution>
				<goals>
					<goal>nose</goal>
				</goals>
			</execution>
		</executions>
		<configuration>
			<python>python2.7</python>
		</configuration>
	</plugin>
				
Changing the default working directory
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

By default the tests are launched from the *src/test/python* folder in your current projects. You can override this
by setting the *workingDirectory* parameter, e.g.:
::
	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>..</version>
		<executions>
			<execution>
				<goals>
					<goal>nose</goal>
				</goals>
			</execution>
		</executions>
		<configuration>
			<workingDirectory>../../bdd/myapp</python>
		</configuration>
	</plugin>

This is useful if you keep your Java and Python code in totally separate projects, but want to run the Python BDD tests
as part of your Java server integration tests.								

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




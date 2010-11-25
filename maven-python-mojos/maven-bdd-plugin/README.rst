Maven BDD Plugin
================

The goal of this plugin is to allow using BDD tools from the Python and Ruby world
for *integration testing* of Java web applications.
   
Basically, the idea is that you execute
   
  **mvn integration-test**

and it would deploy your Java app to your container and then run integration tests against
it that are written in BDD tools, such as Cucumber, Freshen or Lettuce
   
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

Writing them in Python or Ruby should allow you to be more productive than in regular Java 
and take advantage of the vast Python/Ruby library ecosystem.
   
Supported tools
---------------

The following BDD tools are supported

Nose / Freshen 
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

https://github.com/rlisagor/freshen

Nose is an extensive unit testing framework for Python. Freshen is a BDD plugin written for that framework.
Hence we support running unit tests writtein in *nose* in general.

Installation (on Ubuntu):
::
	sudo apt-get install python-setuptools
	sudo easy_install freshen 

Place all your stories and Python test code in

	src/test/python/features

or:

	src/test/python/<module name>/features

This is our **recommended** tool, due to its maturity and Python background.

*pom.xml* integration

::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>0.1</version>
		<executions>
			<execution>
				<phase>integration-test</phase>
				<goals>
					<goal>nose</goal>
				</goals>
			</execution>
		</executions>
	</plugin>


Lettuce
^^^^^^^

http://lettuce.it/

Lettuce is a stand-alone uni testing framework for Python.nose Not as extensive as Nose/Freshen at this point.

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
		<version>0.1</version>
		<executions>
			<execution>
				<phase>integration-test</phase>
				<goals>
					<goal>lettuce</goal>
				</goals>
			</execution>
		</executions>
	</plugin>

Cucumber
^^^^^^^^

http://cukes.info/

Cucumber is the granddaddy of BDD tools.

Place all your stories and Ruby test code in

	src/test/ruby/features

*pom.xml* integration:

::

	<plugin>
		<groupId>maven-python-mojos</groupId>
		<artifactId>maven-bdd-plugin</artifactId>
		<version>0.1</version>
		<executions>
			<execution>
				<phase>integration-test</phase>
				<goals>
					<goal>cucumber</goal>
				</goals>
			</execution>
		</executions>
	</plugin>

Reports
-------

All the BDD reports are created in the
 
	**target/bdd-reports**
 
folder

Maven Repository
----------------

Add the following plugin repository to your *pom.xml* in order to use this plugin:

:: xml

	<pluginRepositories>
		<pluginRepository>
			<id>javabuilders</id>
			<url>http://javabuilders.googlecode.com/svn/repo</url>
		</pluginRepository>
	</pluginRepositories>




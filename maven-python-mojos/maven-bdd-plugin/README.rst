Maven BDD Plugin
================

   The goal of this plugin is to allow using BDD tools from the Python and Ruby world
   for *integration testing* of Java web applications.
   
   Basically, the idea is that you execute
   
      _mvn integration-test_

   and it would deploy your Java app to your container and then run integration tests against
   it that are written in BDD tools, such as Cucumber, Node/Freshen or Lettuce
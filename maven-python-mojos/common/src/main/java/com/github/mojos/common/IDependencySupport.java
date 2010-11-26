package com.github.mojos.common;

/**
 * Defines interface for module that support installing dependencies in scripting languages
 * @author Jacek Furmankiewicz
 *
 */
public interface IDependencySupport {

	/**
	 * @return e.g. "sudo easy_install lettuce django nose" or "sudo gem install rake cucumber rails"
	 */
	public String getDependencyCommand();
	
}

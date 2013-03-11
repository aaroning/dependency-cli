package com.aaron.cli;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A class representing a system component
 * 
 * @author aaron ingber
 *
 */
public class Component {
	/**
	 * A set of dependencies for this component
	 */
	private Set<Component> dependencies = new HashSet<Component>();
	
	/**
	 * A set of components that depend on this component 
	 */
	private Set<Component> parents = new HashSet<Component>();
	
	/**
	 * The name of the component
	 */
	private final String name;
	
	/**
	 * True if this component is installed
	 */
	private boolean installed;
	
	/**
	 * Construct a component object
	 * 
	 * @param name
	 */
	public Component(String name) {
		this.name = name;
	}

	/**
	 * Add a dependency for this component
	 * 
	 * @param dependency
	 */
	public void addDependency(Component dependency) {
		dependencies.add(dependency);
	}
	
	/**
	 * Add a component that depends on this component
	 * 
	 * @param parent
	 */
	public void addParent(Component parent) {
		parents.add(parent);
	}

	/**
	 * Get the component name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get all of the components dependencies
	 * 
	 * @return
	 */
	public Set<Component> getDependencies() {
		return dependencies;
	}

	/**
	 * Get all of the components parents
	 * 
	 * @return
	 */
	public Set<Component> getParents() {
		return parents;
	}

	/**
	 * Returns true if this component is installed
	 * @return
	 */
	public boolean isInstalled() {
		return installed;
	}

	/**
	 * Sets whether this component is installed
	 * 
	 * @param installed
	 */
	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

}

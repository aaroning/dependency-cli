package com.aaron.cli;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A command line interface for managing dependencies
 * 
 * @author aaron ingber
 *
 */
public class ManageDependencies {
	
	private static final String DEPEND = "DEPEND";
	private static final String INSTALL = "INSTALL";
	private static final String REMOVE = "REMOVE";
	private static final String LIST = "LIST";
	private static final String INDENT = "   "; 
	
	/**
	 * A map of all the known components. Uses a hashmap because it provides constant time lookup (assuming a well implemented hashcode),
	 * and there should be no duplicates.
	 */
	private Map<String, Component> allComponents = new HashMap<String, Component>();
	
	
	/**
	 * Create a dependency list for a give component
	 * 
	 * @param component
	 * @param dependencies
	 */
	public void createDependency(String componentName, String[] dependencies) {
		// add the installed component to the map of known components
		Component component = addComponent(componentName);
		for (String dependencyName: dependencies) {
			// add the dependency to the map of know components
			Component dependency = addComponent(dependencyName);
			// create a pointer from both parent to child relationship and vice versa. this will create a graph object
			component.addDependency(dependency);
			dependency.addParent(component);
		}
	}
	
	/**
	 * Install a component and all of its dependencies
	 * 
	 * @param component
	 */
	public void install(String componentName, boolean alertInstalled) {
		Component component = addComponent(componentName);
		if (!component.isInstalled())
		{
			for (Component dependency: component.getDependencies())
			{
				install(dependency.getName(), false);
			}
			System.out.println(INDENT + "Installing " + component.getName());
			component.setInstalled(true);
		}
		else if (alertInstalled) {
			System.out.println(INDENT + component.getName() + " is already installed");
		}
	}
	
	/**
	 * Removes a component if this component cannot be removed
	 * 
	 * @returns false 
	 */
	public void remove(String componentName) {
		Component component = allComponents.get(componentName);	
		if (component.isInstalled())
		{
			for (Component parent: component.getParents()) {
				if (parent.isInstalled()) {
					System.out.println(INDENT + componentName + " is still needed.");
					return;
				}
			}
			System.out.println(INDENT + "Removing " + component.getName());
			component.setInstalled(false);
			uninstallOrphans(component.getDependencies());
		}
		else {
			System.out.println(INDENT + componentName + " is not installed.");
		}
	}
	
	/**
	 * Checks for orphaned dependencies that can be removed
	 * 
	 * @param components
	 */
	private void uninstallOrphans(Set<Component> components) {
		for (Component component: components) {
			boolean isOrphan = true;
			// see if this component has any installed parents
			for(Component parent: component.getParents())
			{
				if (parent.isInstalled()) {
					isOrphan = false;
				}
			}
			if (isOrphan) {
				remove(component.getName());
			}
			
		}
	}
	
	/**
	 * Add a component to the existing component map
	 * 
	 * @param componentName
	 * @return
	 */
	public Component addComponent(String componentName) {
		if (!allComponents.containsKey(componentName)) {
			Component newComponent = new Component(componentName);
			this.allComponents.put(componentName, newComponent);
		}
		return allComponents.get(componentName);
	}
	
	/**
	 * List all of the installed components
	 */
	public void list() {
		for (String componentName: allComponents.keySet()) {
			if (allComponents.get(componentName).isInstalled())
			{
				System.out.println(INDENT + componentName);
			}
		}
	}
	
	/**
	 * The main method to run the commnand line application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ManageDependencies manager = new ManageDependencies();
		try {
			FileInputStream fstream = new FileInputStream(args[0]);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
			  System.out.println(strLine);
			  String[] lineTokens = strLine.split("\\s+");
			  if (strLine.startsWith(DEPEND)) {
				  String componentName = lineTokens[1];
				  String[] dependencies = new String[lineTokens.length - 2];
				  for (int i = 0; i < dependencies.length; i++) {
					  dependencies[i] = lineTokens[i + 2];
				  }
				  manager.createDependency(componentName, dependencies);
			  }
			  else if (strLine.startsWith(INSTALL)) {
				  manager.install(lineTokens[1], true);
			  }
			  else if (strLine.startsWith(REMOVE)) {
				  manager.remove(lineTokens[1]);
			  }
			  else if (strLine.startsWith(LIST)) {
				  manager.list();
			  }
			}
				
		} catch (FileNotFoundException e) {
			System.out.println("File " + args[0] + " not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	
}

package com.aaron.cli;

import org.testng.annotations.Test;

public class ManageDependenciesTest {
	@Test public void testManageDependencies() {
		String[] args = {"proga.dat"};
		ManageDependencies.main(args);
	}
}

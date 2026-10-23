package com.osoterra.ososense.acceptance;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Runs the Gherkin acceptance criteria of section 3.1 found in {@code src/test/resources/features}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
class RunCucumberTest {
}

package io.github.rdlopes.kafka;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "io.github.rdlopes.kafka")
public class CucumberTest {
}

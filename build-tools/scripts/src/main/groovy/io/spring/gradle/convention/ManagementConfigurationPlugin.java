/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package io.spring.gradle.convention;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaTestFixturesPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.VariantVersionMappingStrategy;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

import org.springframework.gradle.propdeps.PropDepsPlugin;

/**
 * Creates a {@literal Management} Gradle {@link Configuration} that is appropriate for adding a platform
 * that it is not exposed externally.
 *
 * If the {@link JavaPlugin} is applied, then the {@literal compileClasspath}, {@literal runtimeClasspath},
 * {@literal testCompileClasspath}, and {@literal testRuntimeClasspath} will extend from it.
 *
 * @author Rob Winch
 * @author John Blum
 * @see Plugin
 * @see Project
 */
public class ManagementConfigurationPlugin implements Plugin<Project> {

	public static final String MANAGEMENT_CONFIGURATION_NAME = "management";

	// TODO: Understand why we don't want certain Configurations to be consumed, resolved or visible???
	@Override
	public void apply(Project project) {

		ConfigurationContainer configurations = project.getConfigurations();

		configurations.create(MANAGEMENT_CONFIGURATION_NAME, management -> {

			management.setCanBeConsumed(false);
			management.setCanBeResolved(false);
			management.setVisible(false);

			PluginContainer plugins = project.getPlugins();

			plugins.withType(JavaPlugin.class, javaPlugin -> {
				configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
				configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
				configurations.getByName(JavaPlugin.TEST_COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
				configurations.getByName(JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(management);
			});

			plugins.withType(JavaTestFixturesPlugin.class, javaTestFixturesPlugin -> {
				configurations.getByName("testFixturesCompileClasspath").extendsFrom(management);
				configurations.getByName("testFixturesRuntimeClasspath").extendsFrom(management);
			});

			plugins.withType(MavenPublishPlugin.class, mavenPublishPlugin -> {

				PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);

				publishingExtension.getPublications().withType(MavenPublication.class, mavenPublication ->
					mavenPublication.versionMapping(versions ->
						versions.allVariants(VariantVersionMappingStrategy::fromResolutionResult)));
			});

			plugins.withType(PropDepsPlugin.class, propDepsPlugin -> {
				configurations.getByName("optional").extendsFrom(management);
				configurations.getByName("provided").extendsFrom(management);
			});
		});
	}
}

/*******************************************************************************
 * Black Duck Software Suite SDK
 * Copyright (C) 2016 Black Duck Software, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *******************************************************************************/
package com.blackducksoftware.integration.hub.teamcity.agent.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jetbrains.buildServer.agent.AgentBuildFeature;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildInterruptReason;
import jetbrains.buildServer.agent.BuildParametersMap;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.ResolvedParameters;
import jetbrains.buildServer.agent.UnresolvedParameters;
import jetbrains.buildServer.artifacts.ArtifactDependencyInfo;
import jetbrains.buildServer.parameters.ValueResolver;
import jetbrains.buildServer.util.Option;
import jetbrains.buildServer.vcs.VcsChangeInfo;
import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vcs.VcsRootEntry;

public class TestAgentRunningBuild implements AgentRunningBuild {
	private TestBuildProgressLogger testLogger;
	private BuildAgentConfiguration config;

	public void setLogger(final TestBuildProgressLogger testLogger) {
		this.testLogger = testLogger;
	}

	@Override
	public BuildProgressLogger getBuildLogger() {
		return testLogger;
	}

	@Override
	public Collection<AgentBuildFeature> getBuildFeatures() {
		final ArrayList<AgentBuildFeature> buildFeatures = new ArrayList<AgentBuildFeature>();
		return buildFeatures;
	}

	@Override
	public Collection<AgentBuildFeature> getBuildFeaturesOfType(final String arg0) {
		final ArrayList<AgentBuildFeature> buildFeatures = new ArrayList<AgentBuildFeature>();

		return buildFeatures;
	}

	@Override
	public String getAccessCode() {
		return null;
	}

	@Override
	public String getAccessUser() {
		return null;
	}

	@Override
	public BuildAgentConfiguration getAgentConfiguration() {
		return config;
	}

	public void setAgentConfiguration(final BuildAgentConfiguration config) {
		this.config = config;
	}

	@Override
	public File getAgentTempDirectory() {
		return null;
	}

	@Override
	public List<ArtifactDependencyInfo> getArtifactDependencies() {
		return null;
	}

	@Override
	public String getBuildCurrentVersion(final VcsRoot arg0) {
		return null;
	}

	@Override
	public long getBuildId() {
		return 0;
	}

	@Override
	public String getBuildPreviousVersion(final VcsRoot arg0) {
		return null;
	}

	@Override
	public File getBuildTempDirectory() {
		return null;
	}

	@Override
	public String getBuildTypeId() {
		return null;
	}

	@Override
	public String getBuildTypeName() {
		return null;
	}

	@Override
	public <T> T getBuildTypeOptionValue(final Option<T> arg0) {
		return null;
	}

	@Override
	public File getDefaultCheckoutDirectory() {
		return null;
	}

	@Override
	public long getExecutionTimeoutMinutes() {
		return 0;
	}

	@Override
	public List<VcsChangeInfo> getPersonalVcsChanges() {
		return null;
	}

	@Override
	public String getProjectName() {
		return null;
	}

	@Override
	public List<VcsChangeInfo> getVcsChanges() {
		return null;
	}

	@Override
	public List<VcsRootEntry> getVcsRootEntries() {
		return null;
	}

	@Override
	public boolean isCheckoutOnAgent() {
		return false;
	}

	@Override
	public boolean isCheckoutOnServer() {
		return false;
	}

	@Override
	public boolean isCleanBuild() {
		return false;
	}

	@Override
	public boolean isCustomCheckoutDirectory() {
		return false;
	}

	@Override
	public boolean isPersonal() {
		return false;
	}

	@Override
	public boolean isPersonalPatchAvailable() {
		return false;
	}

	@Override
	public void addSharedConfigParameter(final String arg0, final String arg1) {
	}

	@Override
	public void addSharedEnvironmentVariable(final String arg0, final String arg1) {
	}

	@Override
	public void addSharedSystemProperty(final String arg0, final String arg1) {
	}

	@Override
	public String getArtifactsPaths() {
		return null;
	}

	@Override
	public String getBuildNumber() {
		return "5678";
	}

	@Override
	public BuildParametersMap getBuildParameters() {
		return null;
	}

	@Override
	public File getCheckoutDirectory() {
		return null;
	}

	@Override
	public boolean getFailBuildOnExitCode() {
		return false;
	}

	@Override
	public BuildInterruptReason getInterruptReason() {
		return null;
	}

	@Override
	public BuildParametersMap getMandatoryBuildParameters() {
		return null;
	}

	@Override
	public ResolvedParameters getResolvedParameters() {
		return null;
	}

	@Override
	public String getRunType() {
		return null;
	}

	@Override
	public Map<String, String> getRunnerParameters() {
		return null;
	}

	@Override
	public BuildParametersMap getSharedBuildParameters() {
		return null;
	}

	@Override
	public Map<String, String> getSharedConfigParameters() {
		return null;
	}

	@Override
	public ValueResolver getSharedParametersResolver() {
		return null;
	}

	@Override
	public UnresolvedParameters getUnresolvedParameters() {
		return null;
	}

	@Override
	public File getWorkingDirectory() {
		return null;
	}

	@Override
	public void stopBuild(final String arg0) {
	}

}

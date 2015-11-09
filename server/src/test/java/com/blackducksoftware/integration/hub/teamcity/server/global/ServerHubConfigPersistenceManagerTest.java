package com.blackducksoftware.integration.hub.teamcity.server.global;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jetbrains.buildServer.log.LogInitializer;
import jetbrains.buildServer.serverSide.ServerPaths;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blackducksoftware.integration.hub.teamcity.common.beans.HubCredentialsBean;
import com.blackducksoftware.integration.hub.teamcity.common.beans.HubProxyInfo;
import com.blackducksoftware.integration.hub.teamcity.common.beans.ServerHubConfigBean;
import com.blackducksoftware.integration.hub.teamcity.mocks.MockServerPaths;

public class ServerHubConfigPersistenceManagerTest {

    private final static String parentDir = "config";

    private static Properties testProperties;

    @BeforeClass
    public static void startup() {
        LogInitializer.setUnitTest(true);
        LogInitializer.initServerLogging();

        testProperties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("test.properties");
        try {
            testProperties.load(is);
        } catch (IOException e) {
            System.err.println("reading test.properties failed!");
        }
    }

    @AfterClass
    public static void tearDown() {
        String persistedConfig = MockServerPaths.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        persistedConfig = persistedConfig.substring(0, persistedConfig.indexOf("/target"));
        persistedConfig = persistedConfig + "/test-workspace/config/PersistConfig/hub-config.xml";
        File persistedFile = new File(persistedConfig);
        persistedFile.delete();
    }

    private ServerHubConfigPersistenceManager getPersistenceManager(final String configDir) {
        ServerPaths mockedPaths = MockServerPaths.getMockedServerPaths(parentDir, configDir);
        return new ServerHubConfigPersistenceManager(mockedPaths);
    }

    @Test
    public void testConstructor() throws Exception {
        ServerPaths mockedPaths = MockServerPaths.getMockedServerPaths(parentDir, null);

        ServerHubConfigPersistenceManager persistenceManager = new ServerHubConfigPersistenceManager(mockedPaths);
        assertNotNull(persistenceManager);

        File configFile = persistenceManager.getConfigFile();

        assertTrue(configFile.getCanonicalPath().contains("/test-workspace/config"));
    }

    @Test
    public void testLoadSettingsFromEmptyDirectory() throws Exception {
        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("NoConfig");

        persistenceManager.loadSettings();

        ServerHubConfigBean globalConfig = persistenceManager.getConfiguredServer();

        assertNotNull(globalConfig.getHubUrl());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getGlobalCredentials().getHubUser());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getGlobalCredentials().getEncryptedPassword());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getProxyInfo().getHost());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNull(globalConfig.getProxyInfo().getPort());

        assertNotNull(globalConfig.getProxyInfo().getIgnoredProxyHosts());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getProxyInfo().getProxyUsername());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getProxyInfo().getProxyPassword());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

    }

    @Test
    public void testLoadSettingsFromEmptyConfig() throws Exception {
        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("EmptyConfig");

        persistenceManager.loadSettings();

        ServerHubConfigBean globalConfig = persistenceManager.getConfiguredServer();

        assertNotNull(globalConfig.getHubUrl());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getGlobalCredentials().getHubUser());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getGlobalCredentials().getEncryptedPassword());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getProxyInfo().getHost());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNull(globalConfig.getProxyInfo().getPort());

        assertNotNull(globalConfig.getProxyInfo().getIgnoredProxyHosts());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getProxyInfo().getProxyUsername());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));

        assertNotNull(globalConfig.getProxyInfo().getProxyPassword());
        assertTrue(StringUtils.isBlank(globalConfig.getHubUrl()));
    }

    @Test
    public void testLoadSettingsFromValidConfig() throws Exception {
        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("ValidConfig");

        persistenceManager.loadSettings();

        ServerHubConfigBean globalConfig = persistenceManager.getConfiguredServer();

        assertNotNull(globalConfig.getHubUrl());
        assertEquals(testProperties.getProperty("TEST_HUB_SERVER_URL"), globalConfig.getHubUrl());

        assertNotNull(globalConfig.getGlobalCredentials().getHubUser());
        assertEquals(testProperties.getProperty("TEST_USERNAME"), globalConfig.getGlobalCredentials().getHubUser());

        assertNotNull(globalConfig.getGlobalCredentials().getDecryptedPassword());
        assertEquals(testProperties.getProperty("TEST_PASSWORD"), globalConfig.getGlobalCredentials().getDecryptedPassword());

        assertNotNull(globalConfig.getProxyInfo().getHost());
        assertEquals(testProperties.getProperty("TEST_PROXY_HOST_BASIC"), globalConfig.getProxyInfo().getHost());

        assertNotNull(globalConfig.getProxyInfo().getPort());
        assertEquals(Integer.valueOf(testProperties.getProperty("TEST_PROXY_PORT_BASIC")), globalConfig.getProxyInfo().getPort());

        assertNotNull(globalConfig.getProxyInfo().getProxyUsername());
        assertEquals(testProperties.getProperty("TEST_PROXY_USER_BASIC"), globalConfig.getProxyInfo().getProxyUsername());

        assertNotNull(globalConfig.getProxyInfo().getProxyPassword());
        assertEquals(testProperties.getProperty("TEST_PROXY_PASSWORD_BASIC"), globalConfig.getProxyInfo().getProxyPassword());

        assertEquals("testIgnore", globalConfig.getProxyInfo().getIgnoredProxyHosts());
    }

    @Test
    public void testPersistConfig() throws Exception {
        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("PersistConfig");

        HubCredentialsBean credentials = new HubCredentialsBean("FakeUser", "EncryptedPassword");
        HubProxyInfo proxyInfo = new HubProxyInfo();
        proxyInfo.setHost("testProxyHost");
        proxyInfo.setPort(4567);
        proxyInfo.setProxyUsername("proxyusername");
        proxyInfo.setProxyPassword("proxypassword");
        proxyInfo.setIgnoredProxyHosts("Ignore proxy");

        persistenceManager.getConfiguredServer().setGlobalCredentials(credentials);
        persistenceManager.getConfiguredServer().setProxyInfo(proxyInfo);
        persistenceManager.getConfiguredServer().setHubUrl("http://hubUrl");

        persistenceManager.persist();

        assertTrue(persistenceManager.getConfigFile().exists());

        persistenceManager.getConfiguredServer().setGlobalCredentials(null);
        persistenceManager.getConfiguredServer().setProxyInfo(null);
        persistenceManager.getConfiguredServer().setHubUrl(null);

        persistenceManager.loadSettings();

        assertEquals(persistenceManager.getConfiguredServer().getGlobalCredentials(), credentials);
        assertEquals(persistenceManager.getConfiguredServer().getProxyInfo(), proxyInfo);
        assertEquals(persistenceManager.getConfiguredServer().getHubUrl(), "http://hubUrl");

    }

    @Test
    public void testPersistConfigUpdateExisting() throws Exception {

        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("UpdateConfig");

        persistenceManager.loadSettings();

        ServerHubConfigBean originalConfig = new ServerHubConfigBean();
        originalConfig.setGlobalCredentials(persistenceManager.getConfiguredServer().getGlobalCredentials());
        originalConfig.setHubUrl(persistenceManager.getConfiguredServer().getHubUrl());
        originalConfig.setProxyInfo(persistenceManager.getConfiguredServer().getProxyInfo());

        try {
            HubCredentialsBean credentials = new HubCredentialsBean("FakeUser", "EncryptedPassword");
            HubProxyInfo proxyInfo = new HubProxyInfo();
            proxyInfo.setHost("testProxyHost");
            proxyInfo.setPort(4567);
            proxyInfo.setProxyUsername("proxyusername");
            proxyInfo.setProxyPassword("proxypassword");
            proxyInfo.setIgnoredProxyHosts("Ignore proxy");

            persistenceManager.getConfiguredServer().setGlobalCredentials(credentials);
            persistenceManager.getConfiguredServer().setProxyInfo(proxyInfo);
            persistenceManager.getConfiguredServer().setHubUrl("http://hubUrl");

            persistenceManager.persist();

            persistenceManager.getConfiguredServer().setGlobalCredentials(null);
            persistenceManager.getConfiguredServer().setProxyInfo(null);
            persistenceManager.getConfiguredServer().setHubUrl(null);

            persistenceManager.loadSettings();

            assertTrue(!persistenceManager.getConfiguredServer().getGlobalCredentials().equals(originalConfig.getGlobalCredentials()));
            assertTrue(!persistenceManager.getConfiguredServer().getProxyInfo().equals(originalConfig.getProxyInfo()));
            assertTrue(!persistenceManager.getConfiguredServer().getHubUrl().equals(originalConfig.getHubUrl()));

        } finally {

            // reset the configuration to the original
            persistenceManager.getConfiguredServer().setGlobalCredentials(originalConfig.getGlobalCredentials());
            persistenceManager.getConfiguredServer().setProxyInfo(originalConfig.getProxyInfo());
            persistenceManager.getConfiguredServer().setHubUrl(originalConfig.getHubUrl());
            persistenceManager.persist();
        }
    }

    @Test
    public void testGetHexEncodedPublicKey() throws Exception {
        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("NoConfig");

        String hexKey = persistenceManager.getHexEncodedPublicKey();
        assertTrue(StringUtils.isNotBlank(hexKey));
    }

    @Test
    public void testGetRandom() throws Exception {
        ServerHubConfigPersistenceManager persistenceManager = getPersistenceManager("NoConfig");

        String random = persistenceManager.getRandom();
        assertTrue(StringUtils.isNotBlank(random));
    }

}
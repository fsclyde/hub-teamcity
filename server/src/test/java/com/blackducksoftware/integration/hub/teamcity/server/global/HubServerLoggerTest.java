package com.blackducksoftware.integration.hub.teamcity.server.global;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import jetbrains.buildServer.log.LogInitializer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blackducksoftware.integration.suite.sdk.logging.LogLevel;

public class HubServerLoggerTest {

    private static PrintStream orgStream = null;

    private static PrintStream orgErrStream = null;

    private static ByteArrayOutputStream byteOutput = null;

    private static PrintStream currStream = null;

    @BeforeClass
    public static void startup() {
        orgStream = System.out;
        orgErrStream = System.err;
        byteOutput = new ByteArrayOutputStream();
        currStream = new PrintStream(byteOutput);
        System.setOut(currStream);
        System.setErr(currStream);

        LogInitializer.setUnitTest(true);
        LogInitializer.addConsoleAppender();
        LogInitializer.initServerLogging();
    }

    @Before
    public void testSetup() throws Exception {
        currStream.flush();
        byteOutput.flush();
        byteOutput.reset();
    }

    @AfterClass
    public static void tearDown() {
        System.setOut(orgStream);
        System.setErr(orgErrStream);
    }

    @Test
    public void testConstructor() {
        assertNotNull(new HubServerLogger());
    }

    @Test
    public void testSetLoggerLevel() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.WARN);
        assertEquals(LogLevel.WARN, logger.getLogLevel());
        logger.setLogLevel(LogLevel.INFO);
        assertEquals(LogLevel.INFO, logger.getLogLevel());
        logger.setLogLevel(LogLevel.DEBUG);
        assertEquals(LogLevel.DEBUG, logger.getLogLevel());
        logger.setLogLevel(LogLevel.ERROR);
        assertEquals(LogLevel.ERROR, logger.getLogLevel());
        logger.setLogLevel(LogLevel.TRACE);
        assertEquals(LogLevel.TRACE, logger.getLogLevel());
    }

    @Test
    public void testInfo() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.info("Should not be logged");

        logger.setLogLevel(LogLevel.INFO);
        logger.info("Should be logged");

        logger.setLogLevel(LogLevel.TRACE);
        logger.info("Definitely should be logged");

        String output = byteOutput.toString();

        assertTrue(output, !output.contains("Should not be logged"));
        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("Definitely should be logged"));

    }

    @Test
    public void testErrorMessageOnly() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.error("Should be logged");

        logger.setLogLevel(LogLevel.INFO);
        logger.error("Totally got logged");

        logger.setLogLevel(LogLevel.TRACE);
        logger.error("Definitely should be logged");

        String output = byteOutput.toString();

        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("Totally got logged"));
        assertTrue(output, output.contains("Definitely should be logged"));

    }

    @Test
    public void testErrorExceptionOnly() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.error(new Exception("Should be logged"));

        logger.setLogLevel(LogLevel.INFO);
        logger.error(new Exception("Totally got logged"));

        logger.setLogLevel(LogLevel.TRACE);
        logger.error(new Exception("Definitely should be logged"));

        String output = byteOutput.toString();

        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("Totally got logged"));
        assertTrue(output, output.contains("Definitely should be logged"));

    }

    @Test
    public void testErrorMessageAndException() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.error("Should be logged", new Exception("Error Should be logged"));

        logger.setLogLevel(LogLevel.INFO);
        logger.error("Totally got logged", new Exception("This error Should be logged"));

        logger.setLogLevel(LogLevel.TRACE);
        logger.error("Definitely should be logged", new Exception("This error definitely Should be logged"));

        String output = byteOutput.toString();

        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("Error Should be logged"));

        assertTrue(output, output.contains("Totally got logged"));
        assertTrue(output, output.contains("This error Should be logged"));

        assertTrue(output, output.contains("Definitely should be logged"));
        assertTrue(output, output.contains("This error definitely Should be logged"));

    }

    @Test
    public void testWarn() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.warn("Should not be logged");

        logger.setLogLevel(LogLevel.WARN);
        logger.warn("Should be logged");

        logger.setLogLevel(LogLevel.INFO);
        logger.warn("Definitely should be logged");

        String output = byteOutput.toString();

        assertTrue(output, !output.contains("Should not be logged"));
        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("Definitely should be logged"));

    }

    @Test
    public void testDebugMessageOnly() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.debug("Should not be logged");

        logger.setLogLevel(LogLevel.INFO);
        logger.debug("Should be logged either");

        logger.setLogLevel(LogLevel.DEBUG);
        logger.debug("Should be logged");

        logger.setLogLevel(LogLevel.TRACE);
        logger.debug("Definitely should be logged");

        String output = byteOutput.toString();

        assertTrue(output, !output.contains("Should not be logged"));
        assertTrue(output, !output.contains("Should not be logged either"));
        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("Definitely should be logged"));

    }

    @Test
    public void testDebugMessageAndException() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.debug("Should not be logged", new Exception("Error Should not be logged"));

        logger.setLogLevel(LogLevel.INFO);
        logger.debug("Should not be logged either", new Exception("This error Should not be logged either"));

        logger.setLogLevel(LogLevel.DEBUG);
        logger.debug("Should be logged", new Exception("This error Should be logged"));

        logger.setLogLevel(LogLevel.TRACE);
        logger.debug("Definitely should be logged", new Exception("This error definitely Should be logged"));

        String output = byteOutput.toString();

        assertTrue(output, !output.contains("Should not be logged"));
        assertTrue(output, !output.contains("Error Should not be logged"));

        assertTrue(output, !output.contains("Should not be logged either"));
        assertTrue(output, !output.contains("This error Should not be logged either"));

        assertTrue(output, output.contains("Should be logged"));
        assertTrue(output, output.contains("This error Should be logged"));

        assertTrue(output, output.contains("Definitely should be logged"));
        assertTrue(output, output.contains("This error definitely Should be logged"));

    }

    @Test
    public void testTraceMessageOnly() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.trace("Should not be logged");

        logger.setLogLevel(LogLevel.DEBUG);
        logger.trace("Should not be logged either");

        logger.setLogLevel(LogLevel.TRACE);
        logger.trace("Definitely should be logged");

        String output = byteOutput.toString();

        assertTrue(output, !output.contains("Should not be logged"));
        assertTrue(output, !output.contains("Should not be logged either"));
        assertTrue(output, output.contains("Definitely should be logged"));

    }

    @Test
    public void testTraceMessageAndException() {
        HubServerLogger logger = new HubServerLogger();
        logger.setLogLevel(LogLevel.ERROR);
        logger.trace("Should not be logged", new Exception("Error Should not be logged"));

        logger.setLogLevel(LogLevel.DEBUG);
        logger.trace("Should not be logged either", new Exception("This error Should not be logged either"));

        logger.setLogLevel(LogLevel.TRACE);
        logger.trace("Definitely should be logged", new Exception("This error definitely Should be logged"));

        String output = byteOutput.toString();

        assertTrue(output, !output.contains("Should not be logged"));
        assertTrue(output, !output.contains("Error Should not be logged"));

        assertTrue(output, !output.contains("Should not be logged either"));
        assertTrue(output, !output.contains("This error Should not be logged either"));

        assertTrue(output, output.contains("Definitely should be logged"));
        assertTrue(output, output.contains("This error definitely Should be logged"));

    }

}
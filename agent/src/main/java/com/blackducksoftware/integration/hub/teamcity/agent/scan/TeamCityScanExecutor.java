package com.blackducksoftware.integration.hub.teamcity.agent.scan;

import static java.lang.ProcessBuilder.Redirect.PIPE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.blackducksoftware.integration.hub.ScanExecutor;
import com.blackducksoftware.integration.hub.exception.HubIntegrationException;

public class TeamCityScanExecutor extends ScanExecutor {

    protected TeamCityScanExecutor(String hubUrl, String hubUsername, String hubPassword, List<String> scanTargets, Integer buildNumber) {
        super(hubUrl, hubUsername, hubPassword, scanTargets, buildNumber);
    }

    @Override
    protected boolean isConfiguredCorrectly(String scanExec, String oneJarPath, String javaExec) {
        if (getLogger() == null) {
            System.out.println("Could not find a logger");
            return false;
        }
        if (scanExec == null) {
            getLogger().error("Please provide the Hub scan CLI.");
            return false;
        }
        else {
            File scanExecRemote = new File(scanExec);
            if (!scanExecRemote.exists()) {
                getLogger().error("The Hub scan CLI provided does not exist.");
                return false;
            }
        }

        if (oneJarPath == null) {
            getLogger().error("Please provide the path for the CLI cache.");
            return false;
        }

        if (javaExec == null) {
            getLogger().error("Please provide the java home directory.");
            return false;
        }
        else {
            File javaExecRemote = new File(javaExec);
            if (!javaExecRemote.exists()) {
                getLogger().error("The Java home provided does not exist.");
                return false;
            }
        }

        if (getScanMemory() <= 0) {
            getLogger().error("No memory set for the HUB CLI. Will use the default memory, " + DEFAULT_MEMORY);
            setScanMemory(DEFAULT_MEMORY);
        }
        return true;
    }

    @Override
    protected String getLogDirectoryPath() throws IOException {
        File logDirectory = new File(getWorkingDirectory());
        logDirectory = new File(logDirectory, "HubScanLogs");
        logDirectory = new File(logDirectory, String.valueOf(getBuildNumber()));
        logDirectory.mkdirs();

        return logDirectory.getAbsolutePath();
    }

    @Override
    protected Result executeScan(List<String> cmd, String logDirectoryPath) throws HubIntegrationException, InterruptedException {
        try {

            // ////////////////////// Code to mask the password in the logs
            List<String> cmdToOutput = new ArrayList<String>();
            cmdToOutput.addAll(cmd);

            ArrayList<Integer> indexToMask = new ArrayList<Integer>();
            // The User's password will be at the next index
            indexToMask.add(cmdToOutput.indexOf("--password") + 1);

            for (int i = 0; i < cmdToOutput.size(); i++) {
                if (cmdToOutput.get(i).contains("-Dhttp") && cmdToOutput.get(i).contains("proxyPassword")) {
                    indexToMask.add(i);
                }
            }
            for (Integer index : indexToMask) {
                maskIndex(cmdToOutput, index);

            }

            // ///////////////////////
            getLogger().info("Hub CLI command :");
            for (String current : cmdToOutput) {
                getLogger().info(current);
            }

            // Should use the split stream for the process

            Process hubCliProcess = new ProcessBuilder(cmd).redirectError(PIPE).redirectOutput(PIPE).start();
            int returnCode = hubCliProcess.waitFor();

            String outputString = readStream(hubCliProcess.getInputStream());
            outputString = outputString + System.getProperty("line.separator") + readStream(hubCliProcess.getErrorStream());

            if (outputString.contains("Illegal character in path")
                    && (outputString.contains("Finished in") && outputString.contains("with status FAILURE"))) {
                // This version of the CLI can not handle spaces in the log directory
                // Not sure which version of the CLI this issue was fixed

                int indexOfLogOption = cmd.indexOf("--logDir") + 1;

                String logPath = cmd.get(indexOfLogOption);
                logPath = logPath.replace(" ", "%20");
                cmd.remove(indexOfLogOption);
                cmd.add(indexOfLogOption, logPath);

                hubCliProcess = new ProcessBuilder(cmd).redirectError(PIPE).start();
                returnCode = hubCliProcess.waitFor();

                outputString = readStream(hubCliProcess.getInputStream());
                outputString = outputString + System.getProperty("line.separator") + readStream(hubCliProcess.getErrorStream());
            } else if (outputString.contains("Illegal character in opaque")
                    && (outputString.contains("Finished in") && outputString.contains("with status FAILURE"))) {

                int indexOfLogOption = cmd.indexOf("--logDir") + 1;

                String logPath = cmd.get(indexOfLogOption);

                File logFile = new File(logPath);

                logPath = logFile.toURI().toString();
                cmd.remove(indexOfLogOption);
                cmd.add(indexOfLogOption, logPath);

                hubCliProcess = new ProcessBuilder(cmd).redirectError(PIPE).start();
                returnCode = hubCliProcess.waitFor();

                outputString = readStream(hubCliProcess.getInputStream());
                outputString = outputString + System.getProperty("line.separator") + readStream(hubCliProcess.getErrorStream());
            }

            getLogger().info("Hub CLI return code : " + returnCode);
            getLogger().info(outputString);

            if (logDirectoryPath != null) {
                File logDirectory = new File(logDirectoryPath);
                if (logDirectory.exists() && doesHubSupportLogOption()) {

                    getLogger().info("You can view the BlackDuck Scan CLI logs at : '"
                            + logDirectory.getAbsolutePath() + "'");

                    getLogger().info("");
                }
            }

            if (outputString.contains("Finished in") && outputString.contains("with status SUCCESS")) {
                return Result.SUCCESS;
            } else {
                return Result.FAILURE;
            }
        } catch (MalformedURLException e) {
            throw new HubIntegrationException("The server URL provided was not a valid", e);
        } catch (IOException e) {
            throw new HubIntegrationException(e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new HubIntegrationException(e.getMessage(), e);
        }
    }

    private void maskIndex(List<String> cmd, int indexToMask) {
        String cmdToMask = cmd.get(indexToMask);
        String[] maskedArray = new String[cmdToMask.length()];
        Arrays.fill(maskedArray, "*");
        StringBuilder stringBuilder = new StringBuilder();
        for (String current : maskedArray) {
            stringBuilder.append(current);
        }
        String maskedCmd = stringBuilder.toString();

        cmd.remove(indexToMask);
        cmd.add(indexToMask, maskedCmd);
    }

    private String readStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + System.lineSeparator());
        }
        return stringBuilder.toString();
    }

}
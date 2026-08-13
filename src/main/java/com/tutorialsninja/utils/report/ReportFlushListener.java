package com.tutorialsninja.utils.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class ReportFlushListener implements ISuiteListener {

    private static final Logger log = LogManager.getLogger(ReportFlushListener.class);

    @Override
    public void onStart(ISuite suite) {
        log.info("Suite started: {}", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("Suite finished. Flushing reports...");
        ExtentReportManager.flushReports();
    }
}

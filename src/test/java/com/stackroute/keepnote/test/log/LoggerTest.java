package com.stackroute.keepnote.test.log;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;

import com.stackroute.keepnote.config.ApplicationContextConfig;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@WebAppConfiguration
public class LoggerTest {

	@Test
	public void logBackXMLFileCreatedOrNotTestCase() {
		ClassLoader classLoader = getClass().getClassLoader();
		File logFile = new File(classLoader.getResource("logback.xml").getFile());
		Assert.assertTrue("You have to create logback.xml file in resources folder", logFile.exists());

	}

	@Test
	public void logFileGeneratedOrNotTestCase() throws ParserConfigurationException, SAXException, IOException {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		File logFile = ConfigurationWatchListUtil.getConfigurationWatchList(context).getCopyOfFileWatchList().get(0);
		Assert.assertTrue("You have to create logback.xml file in resources folder", logFile.exists());
		File clientLogFile;
		FileAppender<?> fileAppender = null;

		for (Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
				Object appender = index.next();
				if (appender instanceof FileAppender) {
					fileAppender = (FileAppender<?>) appender;
				}
			}
		}
		assertNotNull("File appender is not proper.  please check logback.xml file", fileAppender);
		clientLogFile = new File(fileAppender.getFile());
		assertNotNull("Log file not generated.  please check logback.xml file", clientLogFile);
	}

	@Test
	public void logggedTheMessagesInFileOrNotTestCase() throws ParserConfigurationException, SAXException, IOException {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		File logFile = ConfigurationWatchListUtil.getConfigurationWatchList(context).getCopyOfFileWatchList().get(0);
		Assert.assertTrue("You have to create logback.xml file in resources folder", logFile.exists());
		File clientLogFile;
		FileAppender<?> fileAppender = null;

		for (Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
				Object appender = index.next();
				if (appender instanceof FileAppender) {
					fileAppender = (FileAppender<?>) appender;
				}
			}
		}
		assertNotNull("File appender is not proper.  please check logback.xml file", fileAppender);
		clientLogFile = new File(fileAppender.getFile());
		assertNotNull("Log file not generated.  please check logback.xml file", clientLogFile);

		Assert.assertNotEquals("Log file is created but it is empty", 0, clientLogFile.length());
	}
}

package kanbannow.logback;


import java.io.IOException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


public class HealthCheckErrorRecordingAppender extends AppenderBase<ILoggingEvent> {

    PatternLayoutEncoder encoder;
    boolean warningOrErrorWasLogged = false;


    @Override
    public void start() {
        if (this.encoder == null) {
            addError("No encoder set for the appender named ["+ name +"].");
            return;
        }

        try {
            encoder.init(System.out);
        } catch (IOException e) {
        }
        super.start();
    }

    public void append(ILoggingEvent event) {

        Level eventLevel = event.getLevel();
        if( eventLevel.isGreaterOrEqual(Level.WARN)) {
            warningOrErrorWasLogged = true;
            //throw new RuntimeException(event.getFormattedMessage());
        }

    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public boolean wasWarningOrErrorLogged() {
        return this.warningOrErrorWasLogged;
    }
}

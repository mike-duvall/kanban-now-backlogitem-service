package kanbannow.logback;


import java.io.IOException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


public class LogLevelTripwireAppender extends AppenderBase<ILoggingEvent> {

    private PatternLayoutEncoder encoder;
    private boolean thresholdWasReached = false;
    private Level logLevelThreshold;


    public LogLevelTripwireAppender(Level aLogLevel) {
        this.logLevelThreshold = aLogLevel;
    }


    @Override
    public void start() {
        if( !isEncoderValid())
            return;
        try {
            encoder.init(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.start();
    }

    private boolean isEncoderValid() {
        boolean isValid = true;
        if (this.encoder == null) {
            addError("No encoder set for the appender named ["+ name +"].");
            isValid = false;
        }
        return isValid;
    }

    public void append(ILoggingEvent event) {
        Level eventLevel = event.getLevel();
        if( eventLevel.isGreaterOrEqual(this.logLevelThreshold)) {
            thresholdWasReached = true;
        }
    }


    public boolean wasThresholdReached() {
        return this.thresholdWasReached;
    }

    public void setEncoder(PatternLayoutEncoder anEncoder) {
        this.encoder = anEncoder;
    }
}
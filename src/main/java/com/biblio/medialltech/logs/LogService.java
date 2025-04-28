package com.biblio.medialltech.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    // Log de niveau WARN
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    // Log de niveau ERROR
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    // Log de niveau INFO
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    // Log de niveau DEBUG
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }
}

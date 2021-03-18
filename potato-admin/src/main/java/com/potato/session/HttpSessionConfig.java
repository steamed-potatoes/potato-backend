package com.potato.session;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24)
public class HttpSessionConfig {

}

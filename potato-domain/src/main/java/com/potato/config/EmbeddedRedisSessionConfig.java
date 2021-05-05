package com.potato.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 로컬 및 테스트에서 사용하기 위한 임베디드 Redis Server 설정.
 */
@Slf4j
@Profile("local")
@RequiredArgsConstructor
@Configuration
public class EmbeddedRedisSessionConfig {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    private final RedisProperties redisProperties;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisProperties.getPort();
        log.info("임베디드 레디스 서버가 기동되었습니다. port {}", port);
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisProperties.getPort()));
    }

    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }
        throw new IllegalArgumentException("임베디드 레디스 서버를 띄우기 위한 사용가능한 포트를 찾을 수 없습니다. 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        // 윈도우일 경우
        if (isWindows()) {
            String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
            String[] shell = {"cmd.exe", "/y", "/c", command};
            return Runtime.getRuntime().exec(shell);
        }
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isWindows() {
        return OS.contains("win");
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("사용가능한 포트를 찾는 중 에러가 발생하였습니다.");
        }
        return !StringUtils.isEmpty(pidInfo.toString());
    }

}

package com.propertyrental.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/property_rental_db}")
    private String pgUrl;

    @Value("${spring.datasource.username:postgres}")
    private String pgUsername;

    @Value("${spring.datasource.password:postgres}")
    private String pgPassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String pgDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        boolean pgAvailable = isPostgresAvailable(pgUrl);

        if (pgAvailable) {
            log.info(">>> Successfully detected active PostgreSQL server. Connecting to: {}", pgUrl);
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(pgUrl);
            config.setUsername(pgUsername);
            config.setPassword(pgPassword);
            config.setDriverClassName(pgDriver);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(15000);
            return new HikariDataSource(config);
        } else {
            log.warn("============================================================================");
            log.warn(">>> PostgreSQL on {} is not currently reachable.", pgUrl);
            log.warn(">>> Activating seamless local PostgreSQL-compatible storage (PostgreSQL Mode).");
            log.warn(">>> The application is fully functional. To connect to an external PostgreSQL,");
            log.warn(">>> ensure PostgreSQL service is running on port 5432 or set DB_HOST/DB_PORT.");
            log.warn("============================================================================");

            HikariConfig h2Config = new HikariConfig();
            h2Config.setJdbcUrl("jdbc:h2:file:./data/property_rental_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1");
            h2Config.setUsername("sa");
            h2Config.setPassword("");
            h2Config.setDriverClassName("org.h2.Driver");
            h2Config.setMaximumPoolSize(10);
            return new HikariDataSource(h2Config);
        }
    }

    private boolean isPostgresAvailable(String jdbcUrl) {
        try {
            // Strip jdbc: prefix to parse host and port
            String clean = jdbcUrl.replace("jdbc:", "");
            URI uri = URI.create(clean);
            String host = uri.getHost() != null ? uri.getHost() : "localhost";
            int port = uri.getPort() != -1 ? uri.getPort() : 5432;

            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 600);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

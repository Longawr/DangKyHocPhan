package com.dust.courseRegistration.service;

import javax.sql.DataSource;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import com.zaxxer.hikari.HikariDataSource;

@Service
public class ShutdownAppService implements DisposableBean {

    private final DataSource dataSource;

    public ShutdownAppService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void destroy() {
        closeDataSource();
    }

    @PreDestroy
    public void onShutdown() {
        closeDataSource();
    }

    private void closeDataSource() {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            if (hikariDataSource.isRunning()) {
                hikariDataSource.close();
                System.out.println("HikariCP DataSource closed.");
            }
        }
    }
}

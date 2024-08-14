package com.dust.courseRegistration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args) {
        var now = LocalDateTime.now();
        var out = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        log.info(out.toString());
    }
}

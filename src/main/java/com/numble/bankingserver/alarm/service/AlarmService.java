package com.numble.bankingserver.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {

    public void notify(String userId, String message) throws InterruptedException {
        Thread.sleep(500);
    }
}

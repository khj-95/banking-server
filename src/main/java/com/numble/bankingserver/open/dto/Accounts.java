package com.numble.bankingserver.open.dto;

public class Accounts {

    public static String generateAccountNumber() {
        // 계좌번호 : 4자리-3자리-6자리 형태
        long temp = (long) ((Math.random() + 1) * 1_000_000_000_000L);
        String randomNumber = String.valueOf(temp);

        return randomNumber.substring(0, 4)
            + "-"
            + randomNumber.substring(4, 7)
            + "-"
            + randomNumber.substring(7);
    }
}

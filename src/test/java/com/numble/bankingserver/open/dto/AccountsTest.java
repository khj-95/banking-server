package com.numble.bankingserver.open.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountsTest {

    @Test
    public void generateAccountNumber_정상작동() {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{3}-\\d{6}");
        Matcher matcher = pattern.matcher(Accounts.generateAccountNumber());
        assertTrue(matcher.matches());
    }
}

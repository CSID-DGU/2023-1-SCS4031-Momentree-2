package com.dateBuzz.backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class TelegramNotifierTests {

    @Autowired
    TelegramNotifier telegramNotifier;

//    @Test
//    public void test() throws Exception{
//        telegramNotifier.sendErrorMessage("test message");
//    }
}

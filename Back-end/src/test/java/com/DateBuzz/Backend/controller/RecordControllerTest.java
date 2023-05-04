package com.DateBuzz.Backend.controller;

import com.DateBuzz.Backend.service.RecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void 게시글_목록_받아오기(){
//
//        //Todo: mocking 데이터 삽입
////
////        mockMvc.perform(get("/community")
////                .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk());
//    }


}

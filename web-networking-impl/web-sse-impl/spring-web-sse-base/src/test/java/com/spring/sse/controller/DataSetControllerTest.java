package com.spring.sse.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.JsonPathExpectationsHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import springmvc.SpringBootSseEmitterApplication;
import springmvc.controller.DataSetController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @RunWith(SpringRunner.class)
// @WebMvcTest(DataSetController.class)
// @SpringBootTest(classes = SpringBootSseEmitterApplication.class)
public class DataSetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 标准的测试Sse Event Stream的返回数据
    // @Test
    public void foo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/emit-data-sets"))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/event-stream;charset=UTF-8"));

        String event = mvcResult.getResponse().getContentAsString();
        System.out.println("event: " + event);
        event = event.replaceAll("data:", "");
        event = event.replaceAll("\\n", "");

        new JsonPathExpectationsHelper("$.id").assertValue(event, "1");
        new JsonPathExpectationsHelper("$.name").assertValue(event, "data");
    }
}
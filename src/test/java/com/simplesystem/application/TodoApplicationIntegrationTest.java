package com.simplesystem.application;

import com.simplesystem.ApiTestBase;
import com.simplesystem.service.TodoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TodoApplicationIntegrationTest extends ApiTestBase {

    String baseUrl = "/v1/todo/";

    @Autowired
    TodoService todoService;

    @BeforeAll
    static void setup() {
    }

    @Test
    void test01_createValidTodo() throws Exception {
        Map<Object, Object> createMap = new HashMap<>();
        createMap.put("status", "NOT_DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");

        String inputJson = super.mapToJson(createMap);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
    }

    @Test
    void test02_createInValidTodo() throws Exception {
        Map<Object, Object> createMap = new HashMap<>();
        createMap.put("status", "N_DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");

        String inputJson = super.mapToJson(createMap);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());

        createMap.put("status", "PAST_DUE");
        inputJson = super.mapToJson(createMap);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void test03_updateValidTodo() throws Exception {
        Map<Object, Object> createMap = new HashMap<>();
        createMap.put("status", "NOT_DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");
        String inputJson = super.mapToJson(createMap);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        Map<String, String> outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), Map.class);
        String id = outputJson.get("uuid").toString();

        Map<Object, Object> updateMap = new HashMap<>();
        updateMap.put("status", "DONE");
        inputJson = super.mapToJson(updateMap);

        mvcResult = mvc.perform(MockMvcRequestBuilders.patch(baseUrl + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    void test04_updateInValidTodo() throws Exception {
        Map<Object, Object> createMap = new HashMap<>();
        createMap.put("status", "NOT_DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");
        String inputJson = super.mapToJson(createMap);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        Map<String, String> outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), Map.class);
        String id = outputJson.get("uuid").toString();

        Map<Object, Object> updateMap = new HashMap<>();
        updateMap.put("status", "N_DONE");
        inputJson = super.mapToJson(updateMap);

        mvcResult = mvc.perform(MockMvcRequestBuilders.patch(baseUrl + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void test05_getTodoWithValidId() throws Exception {
        Map<Object, Object> createMap = new HashMap<>();
        createMap.put("status", "NOT_DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");
        String inputJson = super.mapToJson(createMap);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());
        Map<String, String> outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), Map.class);
        String id = outputJson.get("uuid").toString();

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUrl + id))
                .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), Map.class);
        assertEquals("not done todo", outputJson.get("description").toString());
        assertEquals("NOT_DONE", outputJson.get("status").toString());
    }

    @Test
    void test06_getTodoWithInvalidId() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUrl + UUID.randomUUID().toString()))
                .andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void test07_getAllNotDoneTodo() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andReturn();
        List<Map<String, String>> outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(3, outputJson.size());

        Map<Object, Object> createMap = new HashMap<>();
        createMap.put("status", "DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");
        String inputJson = super.mapToJson(createMap);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andReturn();
        outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(3, outputJson.size());

        createMap = new HashMap<>();
        createMap.put("status", "NOT_DONE");
        createMap.put("description", "Not done todo");
        createMap.put("dueDate", "2021-11-12 13:23:44");
        inputJson = super.mapToJson(createMap);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson)).andReturn();
        assertEquals(201, mvcResult.getResponse().getStatus());

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andReturn();
        outputJson = super.mapFromJson(mvcResult.getResponse().getContentAsString(), List.class);
        assertEquals(4, outputJson.size());
    }
}

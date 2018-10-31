package com.node.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.node.resource.NodeResource;
import com.node.vo.NodeVO;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(NodeResource.class)
public class NodeResourceIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NodeResource nodeResource;

    @Test
    public void createNode() throws Exception {

        NodeVO node = new NodeVO();
        node.setDetail("Det");
        node.setDescription("Desc");
        node.setCode("Code");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("api/app/node")
                .content(new ObjectMapper().writeValueAsString(node))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        MatcherAssert.assertThat(response.getStatus(), Matchers.equalTo(201));
    }

}

package com.node.app.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.node.app.AppApplication;
import com.node.entity.Node;
import com.node.repository.NodeRepository;
import com.node.vo.NodeVO;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NodeResourceIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private NodeRepository repository;

    @Test
    public void createNode() throws Exception {
        RequestBuilder requestBuilder = buildPostRequest(buildNodeVO("NodeCode", "Node desc", "No details"));
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertThat(response.getStatus(), equalTo(201));
        assertThat(response.getContentAsString(), Matchers.containsString("1"));
    }

    @Test
    public void findNodes() throws Exception {
        repository.save(buildNode("Code1", "Test description", "Simple detail", null));

        RequestBuilder findAllNodesRequest = MockMvcRequestBuilders
                .get("/node")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(findAllNodesRequest).andReturn().getResponse();

        List<NodeVO> nodes = new ObjectMapper().readValue(response.getContentAsString(), new ObjectMapper().getTypeFactory().constructCollectionType(List.class, NodeVO.class));
        assertThat(response.getStatus(), equalTo(200));
        assertThat(nodes, Matchers.hasSize(1));
        assertThat(nodes.get(0).getCode(), equalTo("Code1"));
        assertThat(nodes.get(0).getId(), equalTo(1));
    }

    @Test
    public void findNodesWithChildren() throws Exception {
        Node first = repository.save(buildNode("Code1", "Desc1", "Detail1", null));
        Node parent = repository.findById(first.getId()).get();
        Node second = repository.save(buildNode("Code2", "Desc2", "Detail2", parent));

        RequestBuilder findAllNodesRequest = MockMvcRequestBuilders
                .get("/node")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(findAllNodesRequest).andReturn().getResponse();

        List<NodeVO> nodes = new ObjectMapper().readValue(response.getContentAsString(), new ObjectMapper().getTypeFactory().constructCollectionType(List.class, NodeVO.class));
        assertThat(response.getStatus(), equalTo(200));
        assertThat(nodes, Matchers.hasSize(2));
        assertThat(nodes.get(0).getCode(), equalTo("Code1"));
        assertThat(nodes.get(0).getId(), equalTo(1));
        assertThat(nodes.get(0).getChildren(), Matchers.hasSize(1));
        assertThat(nodes.get(0).getChildren().get(0).getCode(), equalTo(second.getCode()));
        assertThat(nodes.get(0).getChildren().get(0).getId(), equalTo(second.getId()));
    }

    private Node buildNode(String code, String description, String detail, Node parent) {
        Node node = new Node();
        node.setCode(code);
        node.setDescription(description);
        node.setDetail(detail);
        node.setParent(parent);
        return node;
    }

    private NodeVO buildNodeVO(String code, String description, String detail) {
        NodeVO node = new NodeVO();
        node.setCode(code);
        node.setDescription(description);
        node.setDetail(detail);
        return node;
    }

    private MockHttpServletRequestBuilder buildPostRequest(NodeVO node) throws JsonProcessingException {
        return MockMvcRequestBuilders
                .post("/node")
                .content(new ObjectMapper().writeValueAsString(node))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
    }

}

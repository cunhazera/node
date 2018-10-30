package com.node.app.jpa;

import com.node.entity.Node;
import com.node.repository.NodeRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NodeRepository repository;

    @Test
    public void testCreateNode() {
        Node node = new Node();
        node.setCode("Code");
        node.setDescription("Desc");
        node.setDetail("Detail");
        repository.findAll();
    }

}

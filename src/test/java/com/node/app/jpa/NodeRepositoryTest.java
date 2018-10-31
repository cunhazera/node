package com.node.app.jpa;

import com.node.entity.Node;
import com.node.repository.NodeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = NodeRepository.class)
@EntityScan(basePackageClasses = Node.class)
public class NodeRepositoryTest {

    @Autowired
    private NodeRepository repository;

    @Test
    public void testFindByParent() {
        Node node = createNode("Code1", "Description1", "Detail1");
        Node parent = createNode("Parent", "Desc Parent", "Detail Parent");
        repository.save(parent);
        node.setParent(parent);
        repository.save(node);
        List<Node> children = repository.findByParentId(parent.getId());
        assertThat(children, hasSize(1));
        Node createChild = children.get(0);
        assertThat(createChild.getId(), equalTo(2));
        assertThat(createChild.getParent().getId(), equalTo(1));
        assertThat(createChild.getCode(), equalTo("Code1"));
        assertThat(createChild.getDescription(), equalTo("Description1"));
        assertThat(createChild.getDetail(), equalTo("Detail1"));
    }

    private Node createNode(String code, String desc, String detail) {
        Node node = new Node();
        node.setCode(code);
        node.setDescription(desc);
        node.setDetail(detail);
        return node;
    }

}

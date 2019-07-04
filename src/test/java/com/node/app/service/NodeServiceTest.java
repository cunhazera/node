package com.node.app.service;

import com.node.entity.Node;
import com.node.exception.NodeNotFoundException;
import com.node.repository.NodeRepository;
import com.node.service.NodeService;
import com.node.vo.NodeVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = NodeRepository.class)
@EntityScan(basePackageClasses = {Node.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NodeServiceTest {

    @Autowired
    private NodeService service;

    @Autowired
    private NodeRepository repository;

    @TestConfiguration
    static class NodeServiceConfiguration {
        @Bean
        public NodeService nodeService() {
            return new NodeService();
        }
    }

    @Test
    public void testCreateNode() throws NodeNotFoundException {
        NodeVO node = createNodeVO("Code", "Desc", "Detail", null);
        NodeVO created = service.createNode(node);
        assertThat(created.getId(), equalTo(1));
    }

    @Test(expected = NodeNotFoundException.class)
    public void testCreateNodeWithNonExistingParent() throws NodeNotFoundException {
        NodeVO node = createNodeVO("Code", "Desc", "Detail", null);
        node.setParentId(23);
        service.createNode(node);
    }

    @Test
    public void testUpdateNode() throws NodeNotFoundException {
        NodeVO node = createNodeVO("Code", "Desc", "Detail", null);
        service.createNode(node);
        NodeVO newNode = new NodeVO(1, "NewCode", "Desc2", null, "Detail2");
        service.update(newNode);
        Node updated = repository.findById(newNode.getId()).get();
        assertThat(updated.getCode(), equalTo("NewCode"));
        assertThat(updated.getDetail(), equalTo("Detail2"));
        assertThat(updated.getDescription(), equalTo("Desc2"));
        assertThat(updated.getId(), equalTo(1));
    }

    private NodeVO createNodeVO(String code, String desc, String detail, Integer parentId) {
        NodeVO node = new NodeVO();
        node.setCode(code);
        node.setDescription(desc);
        node.setDetail(detail);
        node.setParentId(parentId);
        return node;
    }

}

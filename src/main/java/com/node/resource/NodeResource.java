package com.node.resource;

import com.node.entity.Node;
import com.node.service.NodeService;
import com.node.vo.NodeChildrenVO;
import com.node.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class NodeResource {

    @Autowired
    private NodeService service;

    @PostMapping(value = "/node", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody NodeVO node) throws Exception {
        Integer id = service.createNode(node);
        return new ResponseEntity(id, HttpStatus.CREATED);
    }

    @GetMapping(value = "/node/{parentId}", produces = "application/json")
    public ResponseEntity<NodeChildrenVO> findByParentId(@PathVariable("parentId") Integer id) {
        return new ResponseEntity(service.findAllByParent(id), HttpStatus.OK);
    }

    @GetMapping(value = "/node", produces = "application/json")
    public ResponseEntity<NodeChildrenVO> findAllNodes() {
        return new ResponseEntity(service.findAllNodes(), HttpStatus.OK);
    }

}

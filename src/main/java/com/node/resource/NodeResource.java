package com.node.resource;

import com.node.exception.NodeNotFoundException;
import com.node.service.NodeService;
import com.node.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("node")
public class NodeResource {

    @Autowired
    private NodeService service;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity create(@RequestBody NodeVO node) throws NodeNotFoundException {
        return new ResponseEntity(service.createNode(node).getId(), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity findAllNodes() {
        return new ResponseEntity(service.findAllNodes(), HttpStatus.OK);
    }

    @GetMapping(value = "/{parentId}", produces = "application/json")
    public ResponseEntity findByParentId(@PathVariable("parentId") Integer id) {
        return new ResponseEntity(service.findAllByParent(id), HttpStatus.OK);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity updateNode(@RequestBody NodeVO node) throws NodeNotFoundException {
        return new ResponseEntity(service.update(node).getId(), HttpStatus.OK);
    }

}

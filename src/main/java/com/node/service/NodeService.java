package com.node.service;

import com.node.entity.Node;
import com.node.exception.NodeNotFoundException;
import com.node.repository.NodeRepository;
import com.node.vo.NodeChildrenVO;
import com.node.vo.NodeVO;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NodeService {

    @Autowired
    private NodeRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    public NodeVO createNode(NodeVO nodeVO) throws NodeNotFoundException {
        Node node = new Node();
        node.setCode(nodeVO.getCode());
        node.setDescription(nodeVO.getDescription());
        node.setDetail(nodeVO.getDetail());
        if (nodeVO.getParentId() != null) {
            Optional<Node> optionalNode = repository.findById(nodeVO.getParentId());
            if (optionalNode.isPresent()) {
                Node parent = optionalNode.get();
                node.setParent(parent);
            } else {
                throw new NodeNotFoundException(nodeVO.getParentId());
            }
        }
        Node nodeResult = repository.save(node);
        return buildNodeVO(nodeResult);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public NodeVO update(NodeVO nodeVO) throws NodeNotFoundException {
        Optional<Node> optionalNode = repository.findById(nodeVO.getId());
        if (!optionalNode.isPresent()) {
            throw new NodeNotFoundException(nodeVO.getId());
        }
        Node node = optionalNode.get();
        node.setDetail(nodeVO.getDetail());
        node.setDescription(nodeVO.getDescription());
        node.setCode(nodeVO.getCode());
        if (node.getParent() != null) {
            Optional<Node> parent = repository.findById(node.getParent().getId());
            node.setParent(parent.get());
        }
        Node nodeResult = repository.save(node);
        return buildNodeVO(nodeResult);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Iterable<NodeChildrenVO> findAllByParent(Integer parentId) {
        List<Node> nodes = repository.findByParentId(parentId);
        List<NodeChildrenVO> vos = new ArrayList<>();
        for (Node node : nodes) {
            NodeChildrenVO vo = new NodeChildrenVO();
            vo.setCode(node.getCode());
            vo.setDescription(node.getDescription());
            vo.setDetail(node.getDetail());
            vo.setId(node.getId());
            vo.setParentId(node.getParent().getId());
            List<Node> res = repository.findByParentId(node.getId());
            vo.setHasChildren(!res.isEmpty());
            vos.add(vo);
        }
        return vos;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Iterable<NodeVO> findAllByParentRecursively(Integer parentId) {
        List<NodeVO> nodesVO = new ArrayList<>();
        Iterable<NodeChildrenVO> nodesChildren = this.findAllByParent(parentId);
        for (NodeChildrenVO child : nodesChildren) {
            NodeVO node = buildNodeVOFromChild(child);
            if (child.getHasChildren()) {
                Iterable<NodeVO> children = this.findAllByParentRecursively(child.getId());
                node.addAllChild(Lists.newArrayList(children));
            }
            nodesVO.add(node);
        }
        return nodesVO;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<NodeVO> findAllNodes() {
        List<NodeVO> vos = new ArrayList<>();
        Iterable<Node> all = repository.findAll();
        for (Node node : all) {
            NodeVO nodeVO = buildNodeVO(node);
            Iterable<NodeVO> children = this.findAllByParentRecursively(nodeVO.getId());
            for (NodeVO child : children) {
                nodeVO.addChild(child);
            }
            vos.add(nodeVO);
        }
        return vos;
    }

    private NodeVO buildNodeVO(Node node) {
        NodeVO vo = new NodeVO();
        vo.setDescription(node.getDescription());
        vo.setCode(node.getCode());
        vo.setDetail(node.getDetail());
        vo.setId(node.getId());
        if (node.getParent() != null) {
            vo.setParentId(node.getParent().getId());
        }
        return vo;
    }

    private NodeVO buildNodeVOFromChild(NodeChildrenVO node) {
        NodeVO vo = new NodeVO();
        vo.setDescription(node.getDescription());
        vo.setCode(node.getCode());
        vo.setDetail(node.getDetail());
        vo.setId(node.getId());
        vo.setParentId(node.getParentId());
        return vo;
    }

}

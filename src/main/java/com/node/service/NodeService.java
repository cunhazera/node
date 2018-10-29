package com.node.service;

import com.node.entity.Node;
import com.node.repository.NodeRepository;
import com.node.vo.NodeChildrenVO;
import com.node.vo.NodeVO;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class NodeService {

    @Autowired
    private NodeRepository repository;

    public Integer createNode(NodeVO nodeVO) throws Exception {
        Node node = new Node();
        node.setCode(nodeVO.getCode());
        node.setDescription(nodeVO.getDescription());
        node.setDetail(nodeVO.getDetail());
        if (nodeVO.getParentId() != null) {
            Optional<Node> opNode = repository.findById(nodeVO.getParentId());
            if (opNode.isPresent()) {
                Node parent = opNode.get();
                node.setParent(parent);
            } else {
                throw new Exception("Não existe node com id " + nodeVO.getParentId());
            }
        }
        Node nodeResult = repository.save(node);
        return nodeResult.getId();
    }

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
            if (vo.getHasChildren()) {
                this.findAllByParent(vo.getId());
            }
        }
        return vos;
    }


    public Iterable<NodeVO> findAllByParentRecur(Integer parentId) {
        List<NodeVO> vos = new ArrayList<>();
        Iterable<NodeChildrenVO> cc = this.findAllByParent(parentId);
        for (NodeChildrenVO voc : cc) {
            vos.add(buildNodeVOFromChild(voc));
            if  (voc.getHasChildren()) {
                this.findAllByParentRecur(voc.getId());
            }
        }
//
//
//        List<Node> nodes = repository.findByParentId(parentId);
//        for (Node node : nodes) {
//            vos.add(buildNodeVO(node));
//        }
//
//        for (NodeVO node : vos) {
//            NodeChildrenVO vo = new NodeChildrenVO();
//            vo.setCode(node.getCode());
//            vo.setDescription(node.getDescription());
//            vo.setDetail(node.getDetail());
//            vo.setId(node.getId());
//            vo.setParentId(node.getId());
//            List<Node> res = repository.findByParentId(node.getId());
//            vo.setHasChildren(!res.isEmpty());
//            node.addChild(node);
//            if (vo.getHasChildren()) {
//                this.findAllByParentRecur(vo.getId());
//            }
//        }
        return vos;
    }


    public Iterable<NodeVO> findAllNodes() {
        List<NodeVO> vos = new ArrayList<>();
        Iterable<Node> all = repository.findAll();
        for (Node node : all) {
            NodeVO nodeVO = buildNodeVO(node);
            Iterable<NodeVO> children = this.findAllByParentRecur(nodeVO.getId());
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

package com.node.service;

import com.node.entity.Node;
import com.node.repository.NodeRepository;
import com.node.vo.NodeChildrenVO;
import com.node.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        }
        return vos;
    }

    public Iterable<NodeVO> findAllNodes() {
        Iterable<Node> nodes = repository.findAll();
        List<NodeVO> vos = new ArrayList<>();
        for (Node node : nodes) {
            NodeVO vo = new NodeVO();
            vo.setId(node.getId());
            if (node.getParent() != null) {
                vo.setParentId(node.getParent().getId());
            }
            vo.setCode(node.getCode());
            vo.setDescription(node.getDescription());
            vo.setDetail(node.getDetail());
            Iterable<NodeChildrenVO> children = this.findAllByParent(node.getId());

            for (NodeChildrenVO child : children) {
                while (child.getHasChildren()) {
                    vos.add(new NodeVO())
                }
            }




            vo.setChildren(children);
            vos.add(vo);
        }
        return vos;
    }

}

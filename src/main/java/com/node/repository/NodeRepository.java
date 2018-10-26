package com.node.repository;

import com.node.entity.Node;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends CrudRepository<Node, Integer> {

    List<Node> findByParentId(Integer id);

}

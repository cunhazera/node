package com.node.vo;

import java.util.ArrayList;
import java.util.List;

public class NodeVO {

    private Integer id;
    private String code;
    private String description;
    private Integer parentId;
    private String detail;
    private List<NodeVO> children = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<NodeVO> getChildren() {
        return children;
    }

    public void setChildren(List<NodeVO> children) {
        this.children = children;
    }

    public void addChild(NodeVO child) {
        this.children.add(child);
    }
}

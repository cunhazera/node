package com.node.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NodeNotFoundException extends Exception {

    public NodeNotFoundException() {
        super();
    }

    public NodeNotFoundException(Integer id) {
        super("There is no node with id " + id);
    }

}

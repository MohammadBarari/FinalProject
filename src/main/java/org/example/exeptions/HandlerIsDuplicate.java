package org.example.exeptions;

import org.hibernate.tool.schema.spi.ExceptionHandler;

public class HandlerIsDuplicate extends RuntimeException {
    public HandlerIsDuplicate() {
        super("this name of handler is duplicated");
    }
}

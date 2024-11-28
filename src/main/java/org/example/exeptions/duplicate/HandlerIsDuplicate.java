package org.example.exeptions.duplicate;

public class HandlerIsDuplicate extends DuplicateException {
    public HandlerIsDuplicate() {
        super("this name of handler is duplicated");
    }
}

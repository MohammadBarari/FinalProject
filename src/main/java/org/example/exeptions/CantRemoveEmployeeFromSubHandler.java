package org.example.exeptions;

public class CantRemoveEmployeeFromSubHandler extends RuntimeException {
    public CantRemoveEmployeeFromSubHandler() {
        super("can not remove employee from subHandler");
    }
}

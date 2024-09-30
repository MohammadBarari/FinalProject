package org.example.exeptions;

public class CantRemoveEmployeeFromSubHandler extends Exception {
    public CantRemoveEmployeeFromSubHandler() {
        super("can not remove employee from subHandler");
    }
}

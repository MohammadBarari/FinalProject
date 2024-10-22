package org.example.exeptions;

    public class NotFoundCustomer extends RuntimeException {
    public NotFoundCustomer(String message) {
        super(message);
    }
        public NotFoundCustomer(String message,Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundCustomer() {
        super("Not Found");
        }
    }

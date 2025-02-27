package org.example.exeptions.NotFoundException;

    public class NotFoundCustomer extends NotFoundException {
    public NotFoundCustomer(String message) {
        super(message);
    }
        public NotFoundCustomer(String message,Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundCustomer() {
        super("Not Found customer");
        }
    }

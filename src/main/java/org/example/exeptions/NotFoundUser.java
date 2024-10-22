package org.example.exeptions;

    public class NotFoundUser extends RuntimeException {
    public NotFoundUser(String message) {
        super(message);
    }
        public NotFoundUser(String message, Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundUser() {
        super("Not Found");
        }
    }

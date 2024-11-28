package org.example.exeptions.NotFoundException;

    public class NotFoundUser extends NotFoundException {
    public NotFoundUser(String message) {
        super(message);
    }
        public NotFoundUser(String message, Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundUser() {
        super("Not Found User");
        }
    }

package org.example.exeptions;

    public class NotFoundSubHandler extends RuntimeException {
        public NotFoundSubHandler(String message) {
            super(message);
        }
        public NotFoundSubHandler(String message, Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundSubHandler() {
            super("Not Found");
        }
}

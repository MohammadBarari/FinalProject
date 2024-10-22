package org.example.exeptions;

    public class NotFoundEmployee extends RuntimeException {
        public NotFoundEmployee(String message) {
            super(message);
        }
        public NotFoundEmployee(String message,Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundEmployee() {
            super("Not Found");
        }
}

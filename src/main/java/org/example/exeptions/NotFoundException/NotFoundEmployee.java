package org.example.exeptions.NotFoundException;

    public class NotFoundEmployee extends NotFoundException {
        public NotFoundEmployee(String message) {
            super(message);
        }
        public NotFoundEmployee(String message,Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundEmployee() {
            super("Not Found employee");
        }
}

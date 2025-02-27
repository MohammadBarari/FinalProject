package org.example.exeptions.NotFoundException;

    public class NotFoundSubHandler extends NotFoundException {
        public NotFoundSubHandler(String message) {
            super(message);
        }
        public NotFoundSubHandler(String message, Throwable throwable) {
            super(message ,throwable);
        }

        public NotFoundSubHandler() {
            super("Not Found subHandler");
        }
}

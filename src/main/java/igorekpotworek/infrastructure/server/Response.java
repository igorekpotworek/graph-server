package igorekpotworek.infrastructure.server;

import lombok.Value;

public interface Response {
    static Response ok(String body) {
        return new SuccessResponse(body);
    }

    static Response error(String body) {
        return new ErrorResponse(body);
    }

    String body();

    @Value
    class SuccessResponse implements Response {

        String body;

        @Override
        public String body() {
            return body;
        }
    }

    @Value
    class ErrorResponse implements Response {
        String body;

        @Override
        public String body() {
            return body;
        }
    }
}

package org.example.advice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Integer status;
    public ErrorResponse(Integer status,String message) {
        this.status = status;
        this.message = message;
    }
}

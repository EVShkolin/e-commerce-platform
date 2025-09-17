package org.project.orderservice.exceptionhandler.exception;

public class EmptyOrderException extends RuntimeException {

    public EmptyOrderException(String userId) {
        super("User " + userId + " doesn't have any items in cart");
    }

}

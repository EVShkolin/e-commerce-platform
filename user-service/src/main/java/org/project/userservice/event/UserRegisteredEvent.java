package org.project.userservice.event;

public record UserRegisteredEvent(String userId, String email, String username) {
}

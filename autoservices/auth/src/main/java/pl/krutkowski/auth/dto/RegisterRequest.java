package pl.krutkowski.auth.dto;

public record RegisterRequest(
        String username,
        String password
) {
}

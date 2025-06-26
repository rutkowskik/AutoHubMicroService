package pl.krutkowski.auth.dto;

public record AuthRequest(
        String username,
        String password
) {
}

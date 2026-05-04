package _31.Shvetsov.dto;

public record ModerationResponse(
        Integer comment_id,
        Integer movie_id,
        Integer user_id,
        String message_text,
        Integer score,
        String status,
        String moderation_reason
) {
}

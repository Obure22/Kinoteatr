package _31.Shvetsov.service;

import _31.Shvetsov.document.CommentDocument;
import _31.Shvetsov.dto.ModerationResponse;
import _31.Shvetsov.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommentAnalyzeService {

    private static final List<String> BLOCKED_WORDS = List.of(
            "spam", "scam", "idiot", "durak", "oskorb"
    );

    private final CommentRepository commentRepository;

    public ModerationResponse moderate(Integer commentId) {
        CommentDocument comment = findComment(commentId);
        String text = comment.getMessage_text() == null ? "" : comment.getMessage_text().toLowerCase(Locale.ROOT);
        boolean blocked = BLOCKED_WORDS.stream().anyMatch(text::contains);

        if (blocked) {
            comment.setStatus("REJECTED");
            comment.setModeration_reason("blocked word");
        } else {
            comment.setStatus("APPROVED");
            comment.setModeration_reason("comment accepted");
        }
        return toResponse(commentRepository.save(comment));
    }

    public ModerationResponse approve(Integer commentId) {
        CommentDocument comment = findComment(commentId);
        comment.setStatus("APPROVED");
        comment.setModeration_reason("approved manually");
        return toResponse(commentRepository.save(comment));
    }

    public ModerationResponse reject(Integer commentId, String reason) {
        CommentDocument comment = findComment(commentId);
        comment.setStatus("REJECTED");
        comment.setModeration_reason(reason == null || reason.isBlank() ? "rejected manually" : reason);
        return toResponse(commentRepository.save(comment));
    }

    public List<ModerationResponse> pending() {
        return commentRepository.findByStatus("PENDING").stream().map(this::toResponse).toList();
    }

    public List<ModerationResponse> byMovie(Integer movieId) {
        return commentRepository.findByMovieId(movieId).stream().map(this::toResponse).toList();
    }

    private CommentDocument findComment(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("comment not found"));
    }

    private ModerationResponse toResponse(CommentDocument comment) {
        return new ModerationResponse(comment.getComment_id(), comment.getMovie_id(), comment.getUser_id(),
                comment.getMessage_text(), comment.getScore(), comment.getStatus(), comment.getModeration_reason());
    }
}

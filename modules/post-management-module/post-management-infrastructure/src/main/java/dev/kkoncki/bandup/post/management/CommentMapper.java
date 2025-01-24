package dev.kkoncki.bandup.post.management;

public class CommentMapper {

    public static Comment toDomain(CommentEntity entity) {
        return Comment.builder()
                .id(entity.getId())
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public static CommentEntity toEntity(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .timestamp(comment.getTimestamp())
                .build();
    }
}

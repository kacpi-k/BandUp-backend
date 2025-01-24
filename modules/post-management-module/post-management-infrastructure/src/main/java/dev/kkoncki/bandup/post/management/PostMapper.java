package dev.kkoncki.bandup.post.management;

public class PostMapper {

    public static Post toDomain(PostEntity entity) {
        return Post.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .content(entity.getContent())
                .mediaUrl(entity.getMediaUrl())
                .mediaType(entity.getMediaType())
                .timestamp(entity.getTimestamp())
                .likesCount(entity.getLikesCount())
                .commentsCount(entity.getCommentsCount())
                .build();
    }

    public static PostEntity toEntity(Post post) {
        return PostEntity.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .content(post.getContent())
                .mediaUrl(post.getMediaUrl())
                .mediaType(post.getMediaType())
                .timestamp(post.getTimestamp())
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .build();
    }
}

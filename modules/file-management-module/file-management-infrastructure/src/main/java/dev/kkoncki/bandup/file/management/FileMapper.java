package dev.kkoncki.bandup.file.management;

public class FileMapper {

    public static File toDomain(FileEntity entity) {
        return File.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .extension(entity.getExtension())
                .size(entity.getSize())
                .url(entity.getUrl())
                .uploadedAt(entity.getUploadedAt())
                .build();
    }

    public static FileEntity toEntity(File file) {
        return FileEntity.builder()
                .id(file.getId())
                .userId(file.getUserId())
                .extension(file.getExtension())
                .size(file.getSize())
                .url(file.getUrl())
                .uploadedAt(file.getUploadedAt())
                .build();
    }
}

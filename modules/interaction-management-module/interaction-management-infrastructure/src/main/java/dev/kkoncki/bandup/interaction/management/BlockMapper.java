package dev.kkoncki.bandup.interaction.management;

public class BlockMapper {

    public static Block toDomain(BlockEntity blockEntity) {
        return Block.builder()
                .id(blockEntity.getId())
                .blockerId(blockEntity.getBlockerId())
                .blockedId(blockEntity.getBlockedId())
                .timestamp(blockEntity.getTimestamp())
                .build();
    }

    public static BlockEntity toEntity(Block block) {
        return BlockEntity.builder()
                .id(block.getId())
                .blockerId(block.getBlockerId())
                .blockedId(block.getBlockedId())
                .timestamp(block.getTimestamp())
                .build();
    }
}

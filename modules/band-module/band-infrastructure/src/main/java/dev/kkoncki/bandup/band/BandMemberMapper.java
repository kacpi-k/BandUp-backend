package dev.kkoncki.bandup.band;

public class BandMemberMapper {

    public static BandMember toDomain(BandMemberEntity entity) {
        return BandMember.builder()
                .id(entity.getId())
                .bandId(entity.getBand().getId())
                .userId(entity.getUserId())
                .role(entity.getRole())
                .joinedOn(entity.getJoinedOn())
                .build();
    }

    public static BandMemberEntity toEntity(BandMember bandMember, BandEntity bandEntity) {
        return BandMemberEntity.builder()
                .id(bandMember.getId())
                .band(bandEntity)
                .userId(bandMember.getUserId())
                .role(bandMember.getRole())
                .joinedOn(bandMember.getJoinedOn())
                .build();
    }
}

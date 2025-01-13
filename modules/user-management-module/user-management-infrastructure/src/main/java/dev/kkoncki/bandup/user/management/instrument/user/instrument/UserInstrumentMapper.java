package dev.kkoncki.bandup.user.management.instrument.user.instrument;

import dev.kkoncki.bandup.user.management.UserEntity;
import dev.kkoncki.bandup.user.management.instrument.InstrumentEntity;

public class UserInstrumentMapper {

    public static UserInstrument toDomain(UserInstrumentEntity entity) {
        return UserInstrument.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .instrumentId(entity.getInstrument().getId())
                .skillLevel(entity.getSkillLevel())
                .build();
    }

    public static UserInstrumentEntity toEntity(UserInstrument userInstrument) {
        return UserInstrumentEntity.builder()
                .id(userInstrument.getId())
                .user(UserEntity.builder().id(userInstrument.getUserId()).build())
                .instrument(InstrumentEntity.builder().id(userInstrument.getInstrumentId()).build())
                .skillLevel(userInstrument.getSkillLevel())
                .build();
    }
}

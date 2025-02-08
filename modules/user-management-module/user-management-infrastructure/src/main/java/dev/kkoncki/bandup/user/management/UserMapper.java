package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.commons.genre.GenreEntity;
import dev.kkoncki.bandup.user.management.instrument.InstrumentEntity;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrumentEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        System.out.println("Mapping user: " + entity.getId());

        List<String> instruments = entity.getInstruments().stream()
                .map(i -> i.getInstrument().getId())
                .collect(Collectors.toList());

        System.out.println("Instruments: " + instruments);

        return User.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .createdOn(entity.getCreatedOn())
                .isBlocked(entity.isBlocked())
                .bio(entity.getBio())
                .genres(entity.getGenres().stream().map(GenreEntity::getName).collect(Collectors.toList()))
                .instruments(instruments)
                .imageUrl(entity.getImageUrl())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .city(entity.getCity())
                .country(entity.getCountry())
                .build();
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdOn(user.getCreatedOn())
                .isBlocked(user.isBlocked())
                .bio(user.getBio())
                .imageUrl(user.getImageUrl())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .city(user.getCity())
                .country(user.getCountry())
                .build();

        if (user.getGenres() != null) {
            entity.setGenres(user.getGenres().stream()
                    .map(name -> {
                        GenreEntity genre = new GenreEntity();
                        genre.setName(name);
                        return genre;
                    })
                    .toList());
        }

        if (user.getInstruments() != null) {
            entity.setInstruments(user.getInstruments().stream()
                    .map(instrumentId -> {
                        UserInstrumentEntity userInstrument = new UserInstrumentEntity();
                        InstrumentEntity instrument = new InstrumentEntity();
                        instrument.setId(instrumentId);
                        userInstrument.setInstrument(instrument);
                        return userInstrument;
                    })
                    .toList());
        }

        return entity;
    }
}


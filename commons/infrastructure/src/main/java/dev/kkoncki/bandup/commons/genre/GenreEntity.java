package dev.kkoncki.bandup.commons.genre;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreEntity {

    @Id
    private String id;

    private String name;
}

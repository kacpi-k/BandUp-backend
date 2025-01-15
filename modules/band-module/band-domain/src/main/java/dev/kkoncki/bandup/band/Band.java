package dev.kkoncki.bandup.band;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Band {
    private String id;
    private String name;
    private String description;
    private Instant createdOn;
    private List<BandMember> members = new ArrayList<>();
    private List<String> genres = new ArrayList<>();

    public void addMember(BandMember member) {
        members.add(member);
    }

    public void removeMember(String memberId) {
        members.removeIf(member -> member.getUserId().equals(memberId));
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updateGenres(List<String> newGenres) {
        this.genres = newGenres;
    }
}

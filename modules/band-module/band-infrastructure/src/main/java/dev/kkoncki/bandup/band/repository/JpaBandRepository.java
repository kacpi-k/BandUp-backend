package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JpaBandRepository extends JpaRepository<BandEntity, String>, JpaSpecificationExecutor<BandEntity> {

    @Query("SELECT b FROM BandEntity b JOIN b.members m WHERE m.userId = :userId")
    List<BandEntity> findAllByMemberUserId(@Param("userId") String userId);

}

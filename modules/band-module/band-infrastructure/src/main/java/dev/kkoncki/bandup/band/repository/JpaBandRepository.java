package dev.kkoncki.bandup.band.repository;

import dev.kkoncki.bandup.band.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaBandRepository extends JpaRepository<BandEntity, String>, JpaSpecificationExecutor<BandEntity> {
}

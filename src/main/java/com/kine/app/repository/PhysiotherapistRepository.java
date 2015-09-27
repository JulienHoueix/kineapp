package com.kine.app.repository;

import com.kine.app.domain.Physiotherapist;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Physiotherapist entity.
 */
public interface PhysiotherapistRepository extends JpaRepository<Physiotherapist,Long> {

    @Query("select distinct physiotherapist from Physiotherapist physiotherapist left join fetch physiotherapist.specialties")
    List<Physiotherapist> findAllWithEagerRelationships();

    @Query("select physiotherapist from Physiotherapist physiotherapist left join fetch physiotherapist.specialties where physiotherapist.id =:id")
    Physiotherapist findOneWithEagerRelationships(@Param("id") Long id);

}

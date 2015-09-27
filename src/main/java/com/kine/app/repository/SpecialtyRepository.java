package com.kine.app.repository;

import com.kine.app.domain.Specialty;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {

}

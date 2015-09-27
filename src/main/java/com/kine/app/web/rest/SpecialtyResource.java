package com.kine.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kine.app.domain.Specialty;
import com.kine.app.repository.SpecialtyRepository;
import com.kine.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Specialty.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    @Inject
    private SpecialtyRepository specialtyRepository;

    /**
     * POST  /specialtys -> Create a new specialty.
     */
    @RequestMapping(value = "/specialtys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Specialty> createSpecialty(@RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to save Specialty : {}", specialty);
        if (specialty.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new specialty cannot already have an ID").body(null);
        }
        Specialty result = specialtyRepository.save(specialty);
        return ResponseEntity.created(new URI("/api/specialtys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("specialty", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /specialtys -> Updates an existing specialty.
     */
    @RequestMapping(value = "/specialtys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Specialty> updateSpecialty(@RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to update Specialty : {}", specialty);
        if (specialty.getId() == null) {
            return createSpecialty(specialty);
        }
        Specialty result = specialtyRepository.save(specialty);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("specialty", specialty.getId().toString()))
                .body(result);
    }

    /**
     * GET  /specialtys -> get all the specialtys.
     */
    @RequestMapping(value = "/specialtys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Specialty> getAllSpecialtys() {
        log.debug("REST request to get all Specialtys");
        return specialtyRepository.findAll();
    }

    /**
     * GET  /specialtys/:id -> get the "id" specialty.
     */
    @RequestMapping(value = "/specialtys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Specialty> getSpecialty(@PathVariable Long id) {
        log.debug("REST request to get Specialty : {}", id);
        return Optional.ofNullable(specialtyRepository.findOne(id))
            .map(specialty -> new ResponseEntity<>(
                specialty,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /specialtys/:id -> delete the "id" specialty.
     */
    @RequestMapping(value = "/specialtys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        log.debug("REST request to delete Specialty : {}", id);
        specialtyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("specialty", id.toString())).build();
    }
}

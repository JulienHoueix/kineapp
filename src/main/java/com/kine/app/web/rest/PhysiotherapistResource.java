package com.kine.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.kine.app.domain.Physiotherapist;
import com.kine.app.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Physiotherapist.
 */
@RestController
@RequestMapping("/api")
public class PhysiotherapistResource {

	private final Logger log = LoggerFactory.getLogger(PhysiotherapistResource.class);

	@Inject
	private PhysiotherapistService physiotherapistService;

	/**
	 * POST /physiotherapists -> Create a new physiotherapist.
	 */
	@RequestMapping(value = "/physiotherapists", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Physiotherapist> createPhysiotherapist(@RequestBody Physiotherapist physiotherapist)
			throws URISyntaxException {
		log.debug("REST request to save Physiotherapist : {}", physiotherapist);
		if (physiotherapist.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new physiotherapist cannot already have an ID")
					.body(null);
		}
		Physiotherapist result = physiotherapistService.save(physiotherapist);
		return ResponseEntity.created(new URI("/api/physiotherapists/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("physiotherapist", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /physiotherapists -> Updates an existing physiotherapist.
	 */
	@RequestMapping(value = "/physiotherapists", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Physiotherapist> updatePhysiotherapist(@RequestBody Physiotherapist physiotherapist)
			throws URISyntaxException {
		log.debug("REST request to update Physiotherapist : {}", physiotherapist);
		if (physiotherapist.getId() == null) {
			return createPhysiotherapist(physiotherapist);
		}
		Physiotherapist result = physiotherapistService.save(physiotherapist);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("physiotherapist", physiotherapist.getId().toString()))
				.body(result);
	}

	/**
	 * GET /physiotherapists -> get all the physiotherapists.
	 */
	@RequestMapping(value = "/physiotherapists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Physiotherapist> getAllPhysiotherapists() {
		log.debug("REST request to get all Physiotherapists");
		return physiotherapistService.findAllWithEagerRelationships();
	}

	/**
	 * GET /physiotherapists/:id -> get the "id" physiotherapist.
	 */
	@RequestMapping(value = "/physiotherapists/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Physiotherapist> getPhysiotherapist(@PathVariable Long id) {
		log.debug("REST request to get Physiotherapist : {}", id);
		return Optional.ofNullable(physiotherapistService.findOneWithEagerRelationships(id))
				.map(physiotherapist -> new ResponseEntity<>(physiotherapist, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /physiotherapists/:id -> delete the "id" physiotherapist.
	 */
	@RequestMapping(value = "/physiotherapists/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deletePhysiotherapist(@PathVariable Long id) {
		log.debug("REST request to delete Physiotherapist : {}", id);
		physiotherapistService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("physiotherapist", id.toString()))
				.build();
	}

	@RequestMapping(value = "/physiotherapists/updatelocalization", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Physiotherapist> updateLocalization(@RequestBody Physiotherapist physiotherapist)
			throws URISyntaxException {
		log.debug("REST request to update localization of Physiotherapist : {}", physiotherapist.getId());
		physiotherapistService.updateLocalization(physiotherapist);
		return new ResponseEntity<>(physiotherapist, HttpStatus.OK);
	}
}

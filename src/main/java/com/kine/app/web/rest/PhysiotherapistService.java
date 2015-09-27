package com.kine.app.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kine.app.domain.Physiotherapist;
import com.kine.app.repository.PhysiotherapistRepository;
import com.kine.app.web.rest.googlemaps.GoogleMapGeocodeResponse;
import com.kine.app.web.rest.googlemaps.Result;

@Service
public class PhysiotherapistService {

	@Value("${google.maps.geocode.url}")
	private String googleMapGeocodeUrl;

	@Value("${google.api.key}")
	private String googleApiKey;

	@Inject
	private PhysiotherapistRepository physiotherapistRepository;

	public Physiotherapist save(Physiotherapist physiotherapist) {
		updateLocalization(physiotherapist);
		physiotherapist = physiotherapistRepository.save(physiotherapist);
		return physiotherapist;
	}

	public Physiotherapist updateLocalization(Physiotherapist physiotherapist) {
		RestTemplate restTemplate = new RestTemplate();
		GoogleMapGeocodeResponse googleMapGeocodeResponse = restTemplate.getForObject(googleMapGeocodeUrl,
				GoogleMapGeocodeResponse.class, physiotherapist.getFullAddress(), googleApiKey);
		if (googleMapGeocodeResponse != null && googleMapGeocodeResponse.getResults() != null
				&& googleMapGeocodeResponse.getResults().size() > 0) {
			Result result = googleMapGeocodeResponse.getResults().stream().findFirst().get();
			physiotherapist.setLatitude(result.getGeometry().getLocation().getLat());
			physiotherapist.setLongitude(result.getGeometry().getLocation().getLng());
		} else {
			physiotherapist.setLatitude(null);
			physiotherapist.setLongitude(null);
		}
		return physiotherapist;
	}

	public List<Physiotherapist> findAllWithEagerRelationships() {
		return physiotherapistRepository.findAllWithEagerRelationships();
	}

	public Physiotherapist findOneWithEagerRelationships(Long id) {
		return physiotherapistRepository.findOneWithEagerRelationships(id);
	}

	public void delete(Long id) {
		physiotherapistRepository.delete(id);
	}

}

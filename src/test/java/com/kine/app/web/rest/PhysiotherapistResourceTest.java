package com.kine.app.web.rest;

import com.kine.app.Application;
import com.kine.app.domain.Physiotherapist;
import com.kine.app.repository.PhysiotherapistRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PhysiotherapistResource REST controller.
 *
 * @see PhysiotherapistResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhysiotherapistResourceTest {

    private static final String DEFAULT_FIRST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_FIRST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LAST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_LAST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_STREET = "SAMPLE_TEXT";
    private static final String UPDATED_STREET = "UPDATED_TEXT";

    private static final Integer DEFAULT_POSTAL_CODE = 1;
    private static final Integer UPDATED_POSTAL_CODE = 2;
    private static final String DEFAULT_CITY = "SAMPLE_TEXT";
    private static final String UPDATED_CITY = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";

    @Inject
    private PhysiotherapistRepository physiotherapistRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhysiotherapistMockMvc;

    private Physiotherapist physiotherapist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhysiotherapistResource physiotherapistResource = new PhysiotherapistResource();
        ReflectionTestUtils.setField(physiotherapistResource, "physiotherapistRepository", physiotherapistRepository);
        this.restPhysiotherapistMockMvc = MockMvcBuilders.standaloneSetup(physiotherapistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        physiotherapist = new Physiotherapist();
        physiotherapist.setFirstName(DEFAULT_FIRST_NAME);
        physiotherapist.setLastName(DEFAULT_LAST_NAME);
        physiotherapist.setStreet(DEFAULT_STREET);
        physiotherapist.setPostalCode(DEFAULT_POSTAL_CODE);
        physiotherapist.setCity(DEFAULT_CITY);
        physiotherapist.setCountry(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createPhysiotherapist() throws Exception {
        int databaseSizeBeforeCreate = physiotherapistRepository.findAll().size();

        // Create the Physiotherapist

        restPhysiotherapistMockMvc.perform(post("/api/physiotherapists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(physiotherapist)))
                .andExpect(status().isCreated());

        // Validate the Physiotherapist in the database
        List<Physiotherapist> physiotherapists = physiotherapistRepository.findAll();
        assertThat(physiotherapists).hasSize(databaseSizeBeforeCreate + 1);
        Physiotherapist testPhysiotherapist = physiotherapists.get(physiotherapists.size() - 1);
        assertThat(testPhysiotherapist.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPhysiotherapist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPhysiotherapist.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testPhysiotherapist.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testPhysiotherapist.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPhysiotherapist.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllPhysiotherapists() throws Exception {
        // Initialize the database
        physiotherapistRepository.saveAndFlush(physiotherapist);

        // Get all the physiotherapists
        restPhysiotherapistMockMvc.perform(get("/api/physiotherapists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(physiotherapist.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
                .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void getPhysiotherapist() throws Exception {
        // Initialize the database
        physiotherapistRepository.saveAndFlush(physiotherapist);

        // Get the physiotherapist
        restPhysiotherapistMockMvc.perform(get("/api/physiotherapists/{id}", physiotherapist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(physiotherapist.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhysiotherapist() throws Exception {
        // Get the physiotherapist
        restPhysiotherapistMockMvc.perform(get("/api/physiotherapists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhysiotherapist() throws Exception {
        // Initialize the database
        physiotherapistRepository.saveAndFlush(physiotherapist);

		int databaseSizeBeforeUpdate = physiotherapistRepository.findAll().size();

        // Update the physiotherapist
        physiotherapist.setFirstName(UPDATED_FIRST_NAME);
        physiotherapist.setLastName(UPDATED_LAST_NAME);
        physiotherapist.setStreet(UPDATED_STREET);
        physiotherapist.setPostalCode(UPDATED_POSTAL_CODE);
        physiotherapist.setCity(UPDATED_CITY);
        physiotherapist.setCountry(UPDATED_COUNTRY);
        

        restPhysiotherapistMockMvc.perform(put("/api/physiotherapists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(physiotherapist)))
                .andExpect(status().isOk());

        // Validate the Physiotherapist in the database
        List<Physiotherapist> physiotherapists = physiotherapistRepository.findAll();
        assertThat(physiotherapists).hasSize(databaseSizeBeforeUpdate);
        Physiotherapist testPhysiotherapist = physiotherapists.get(physiotherapists.size() - 1);
        assertThat(testPhysiotherapist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPhysiotherapist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPhysiotherapist.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testPhysiotherapist.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testPhysiotherapist.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPhysiotherapist.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void deletePhysiotherapist() throws Exception {
        // Initialize the database
        physiotherapistRepository.saveAndFlush(physiotherapist);

		int databaseSizeBeforeDelete = physiotherapistRepository.findAll().size();

        // Get the physiotherapist
        restPhysiotherapistMockMvc.perform(delete("/api/physiotherapists/{id}", physiotherapist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Physiotherapist> physiotherapists = physiotherapistRepository.findAll();
        assertThat(physiotherapists).hasSize(databaseSizeBeforeDelete - 1);
    }
}

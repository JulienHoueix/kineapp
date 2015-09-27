package com.kine.app.web.rest;

import com.kine.app.Application;
import com.kine.app.domain.Specialty;
import com.kine.app.repository.SpecialtyRepository;

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
 * Test class for the SpecialtyResource REST controller.
 *
 * @see SpecialtyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpecialtyResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private SpecialtyRepository specialtyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpecialtyResource specialtyResource = new SpecialtyResource();
        ReflectionTestUtils.setField(specialtyResource, "specialtyRepository", specialtyRepository);
        this.restSpecialtyMockMvc = MockMvcBuilders.standaloneSetup(specialtyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        specialty = new Specialty();
        specialty.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSpecialty() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty

        restSpecialtyMockMvc.perform(post("/api/specialties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specialty)))
                .andExpect(status().isCreated());

        // Validate the Specialty in the database
        List<Specialty> specialties = specialtyRepository.findAll();
        assertThat(specialties).hasSize(databaseSizeBeforeCreate + 1);
        Specialty testSpecialty = specialties.get(specialties.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllspecialties() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialties
        restSpecialtyMockMvc.perform(get("/api/specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

		int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty
        specialty.setName(UPDATED_NAME);
        

        restSpecialtyMockMvc.perform(put("/api/specialties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specialty)))
                .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialties = specialtyRepository.findAll();
        assertThat(specialties).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialties.get(specialties.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

		int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

        // Get the specialty
        restSpecialtyMockMvc.perform(delete("/api/specialties/{id}", specialty.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Specialty> specialties = specialtyRepository.findAll();
        assertThat(specialties).hasSize(databaseSizeBeforeDelete - 1);
    }
}

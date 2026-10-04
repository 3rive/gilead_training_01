package com.gilead.medicalinventory.web.rest;

import com.gilead.medicalinventory.repository.StorageLocationRepository;
import com.gilead.medicalinventory.service.StorageLocationService;
import com.gilead.medicalinventory.service.dto.StorageLocationDTO;
import com.gilead.medicalinventory.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gilead.medicalinventory.domain.StorageLocation}.
 */
@RestController
@RequestMapping("/api/storage-locations")
public class StorageLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(StorageLocationResource.class);

    private static final String ENTITY_NAME = "storageLocation";

    @Value("${jhipster.clientApp.name:medicalinventory}")
    private String applicationName;

    private final StorageLocationService storageLocationService;

    private final StorageLocationRepository storageLocationRepository;

    public StorageLocationResource(StorageLocationService storageLocationService, StorageLocationRepository storageLocationRepository) {
        this.storageLocationService = storageLocationService;
        this.storageLocationRepository = storageLocationRepository;
    }

    /**
     * {@code POST  /storage-locations} : Create a new storageLocation.
     *
     * @param storageLocationDTO the storageLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageLocationDTO, or with status {@code 400 (Bad Request)} if the storageLocation already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StorageLocationDTO> createStorageLocation(@Valid @RequestBody StorageLocationDTO storageLocationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StorageLocation : {}", storageLocationDTO);
        if (storageLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new storageLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        storageLocationDTO = storageLocationService.save(storageLocationDTO);
        return ResponseEntity.created(new URI("/api/storage-locations/" + storageLocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, storageLocationDTO.getId().toString()))
            .body(storageLocationDTO);
    }

    /**
     * {@code PUT  /storage-locations/:id} : Updates an existing storageLocation.
     *
     * @param id the id of the storageLocationDTO to save.
     * @param storageLocationDTO the storageLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageLocationDTO,
     * or with status {@code 400 (Bad Request)} if the storageLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StorageLocationDTO> updateStorageLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StorageLocationDTO storageLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StorageLocation : {}, {}", id, storageLocationDTO);
        if (storageLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        storageLocationDTO = storageLocationService.update(storageLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageLocationDTO.getId().toString()))
            .body(storageLocationDTO);
    }

    /**
     * {@code PATCH  /storage-locations/:id} : Partial updates given fields of an existing storageLocation, field will ignore if it is null
     *
     * @param id the id of the storageLocationDTO to save.
     * @param storageLocationDTO the storageLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageLocationDTO,
     * or with status {@code 400 (Bad Request)} if the storageLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storageLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageLocationDTO> partialUpdateStorageLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StorageLocationDTO storageLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update StorageLocation : {}, {}", id, storageLocationDTO);
        if (storageLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageLocationDTO> result = storageLocationService.partialUpdate(storageLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, storageLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-locations} : get all the Storage Locations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Storage Locations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StorageLocationDTO>> getAllStorageLocations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of StorageLocations");
        Page<StorageLocationDTO> page = storageLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /storage-locations/:id} : get the "id" storageLocation.
     *
     * @param id the id of the storageLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StorageLocationDTO> getStorageLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StorageLocation : {}", id);
        Optional<StorageLocationDTO> storageLocationDTO = storageLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageLocationDTO);
    }

    /**
     * {@code DELETE  /storage-locations/:id} : delete the "id" storageLocation.
     *
     * @param id the id of the storageLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StorageLocation : {}", id);
        storageLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

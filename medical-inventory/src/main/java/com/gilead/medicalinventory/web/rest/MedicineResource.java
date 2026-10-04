package com.gilead.medicalinventory.web.rest;

import com.gilead.medicalinventory.repository.MedicineRepository;
import com.gilead.medicalinventory.service.MedicineService;
import com.gilead.medicalinventory.service.dto.MedicineDTO;
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
 * REST controller for managing {@link com.gilead.medicalinventory.domain.Medicine}.
 */
@RestController
@RequestMapping("/api/medicines")
public class MedicineResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicineResource.class);

    private static final String ENTITY_NAME = "medicine";

    @Value("${jhipster.clientApp.name:medicalinventory}")
    private String applicationName;

    private final MedicineService medicineService;

    private final MedicineRepository medicineRepository;

    public MedicineResource(MedicineService medicineService, MedicineRepository medicineRepository) {
        this.medicineService = medicineService;
        this.medicineRepository = medicineRepository;
    }

    /**
     * {@code POST  /medicines} : Create a new medicine.
     *
     * @param medicineDTO the medicineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicineDTO, or with status {@code 400 (Bad Request)} if the medicine already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicineDTO> createMedicine(@Valid @RequestBody MedicineDTO medicineDTO) throws URISyntaxException {
        LOG.debug("REST request to save Medicine : {}", medicineDTO);
        if (medicineDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicineDTO = medicineService.save(medicineDTO);
        return ResponseEntity.created(new URI("/api/medicines/" + medicineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicineDTO.getId().toString()))
            .body(medicineDTO);
    }

    /**
     * {@code PUT  /medicines/:id} : Updates an existing medicine.
     *
     * @param id the id of the medicineDTO to save.
     * @param medicineDTO the medicineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicineDTO,
     * or with status {@code 400 (Bad Request)} if the medicineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicineDTO> updateMedicine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicineDTO medicineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Medicine : {}, {}", id, medicineDTO);
        if (medicineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicineDTO = medicineService.update(medicineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicineDTO.getId().toString()))
            .body(medicineDTO);
    }

    /**
     * {@code PATCH  /medicines/:id} : Partial updates given fields of an existing medicine, field will ignore if it is null
     *
     * @param id the id of the medicineDTO to save.
     * @param medicineDTO the medicineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicineDTO,
     * or with status {@code 400 (Bad Request)} if the medicineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicineDTO> partialUpdateMedicine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicineDTO medicineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Medicine : {}, {}", id, medicineDTO);
        if (medicineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicineDTO> result = medicineService.partialUpdate(medicineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medicines} : get all the Medicines.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Medicines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicineDTO>> getAllMedicines(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Medicines");
        Page<MedicineDTO> page;
        if (eagerload) {
            page = medicineService.findAllWithEagerRelationships(pageable);
        } else {
            page = medicineService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicines/:id} : get the "id" medicine.
     *
     * @param id the id of the medicineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Medicine : {}", id);
        Optional<MedicineDTO> medicineDTO = medicineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicineDTO);
    }

    /**
     * {@code DELETE  /medicines/:id} : delete the "id" medicine.
     *
     * @param id the id of the medicineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Medicine : {}", id);
        medicineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

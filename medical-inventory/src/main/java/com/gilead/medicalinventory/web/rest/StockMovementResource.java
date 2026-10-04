package com.gilead.medicalinventory.web.rest;

import com.gilead.medicalinventory.repository.StockMovementRepository;
import com.gilead.medicalinventory.service.StockMovementService;
import com.gilead.medicalinventory.service.dto.StockMovementDTO;
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
 * REST controller for managing {@link com.gilead.medicalinventory.domain.StockMovement}.
 */
@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockMovementResource.class);

    private static final String ENTITY_NAME = "stockMovement";

    @Value("${jhipster.clientApp.name:medicalinventory}")
    private String applicationName;

    private final StockMovementService stockMovementService;

    private final StockMovementRepository stockMovementRepository;

    public StockMovementResource(StockMovementService stockMovementService, StockMovementRepository stockMovementRepository) {
        this.stockMovementService = stockMovementService;
        this.stockMovementRepository = stockMovementRepository;
    }

    /**
     * {@code POST  /stock-movements} : Create a new stockMovement.
     *
     * @param stockMovementDTO the stockMovementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockMovementDTO, or with status {@code 400 (Bad Request)} if the stockMovement already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockMovementDTO> createStockMovement(@Valid @RequestBody StockMovementDTO stockMovementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StockMovement : {}", stockMovementDTO);
        if (stockMovementDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockMovement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockMovementDTO = stockMovementService.save(stockMovementDTO);
        return ResponseEntity.created(new URI("/api/stock-movements/" + stockMovementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, stockMovementDTO.getId().toString()))
            .body(stockMovementDTO);
    }

    /**
     * {@code PUT  /stock-movements/:id} : Updates an existing stockMovement.
     *
     * @param id the id of the stockMovementDTO to save.
     * @param stockMovementDTO the stockMovementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockMovementDTO,
     * or with status {@code 400 (Bad Request)} if the stockMovementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockMovementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockMovementDTO> updateStockMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockMovementDTO stockMovementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StockMovement : {}, {}", id, stockMovementDTO);
        if (stockMovementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockMovementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockMovementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stockMovementDTO = stockMovementService.update(stockMovementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockMovementDTO.getId().toString()))
            .body(stockMovementDTO);
    }

    /**
     * {@code PATCH  /stock-movements/:id} : Partial updates given fields of an existing stockMovement, field will ignore if it is null
     *
     * @param id the id of the stockMovementDTO to save.
     * @param stockMovementDTO the stockMovementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockMovementDTO,
     * or with status {@code 400 (Bad Request)} if the stockMovementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockMovementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockMovementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockMovementDTO> partialUpdateStockMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockMovementDTO stockMovementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update StockMovement : {}, {}", id, stockMovementDTO);
        if (stockMovementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockMovementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockMovementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockMovementDTO> result = stockMovementService.partialUpdate(stockMovementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockMovementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-movements} : get all the Stock Movements.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Stock Movements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockMovementDTO>> getAllStockMovements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of StockMovements");
        Page<StockMovementDTO> page;
        if (eagerload) {
            page = stockMovementService.findAllWithEagerRelationships(pageable);
        } else {
            page = stockMovementService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-movements/:id} : get the "id" stockMovement.
     *
     * @param id the id of the stockMovementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockMovementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockMovementDTO> getStockMovement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StockMovement : {}", id);
        Optional<StockMovementDTO> stockMovementDTO = stockMovementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockMovementDTO);
    }

    /**
     * {@code DELETE  /stock-movements/:id} : delete the "id" stockMovement.
     *
     * @param id the id of the stockMovementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StockMovement : {}", id);
        stockMovementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

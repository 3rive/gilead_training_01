package com.gilead.medicalinventory.web.rest;

import com.gilead.medicalinventory.repository.StockLotRepository;
import com.gilead.medicalinventory.service.StockLotService;
import com.gilead.medicalinventory.service.dto.StockLotDTO;
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
 * REST controller for managing {@link com.gilead.medicalinventory.domain.StockLot}.
 */
@RestController
@RequestMapping("/api/stock-lots")
public class StockLotResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockLotResource.class);

    private static final String ENTITY_NAME = "stockLot";

    @Value("${jhipster.clientApp.name:medicalinventory}")
    private String applicationName;

    private final StockLotService stockLotService;

    private final StockLotRepository stockLotRepository;

    public StockLotResource(StockLotService stockLotService, StockLotRepository stockLotRepository) {
        this.stockLotService = stockLotService;
        this.stockLotRepository = stockLotRepository;
    }

    /**
     * {@code POST  /stock-lots} : Create a new stockLot.
     *
     * @param stockLotDTO the stockLotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockLotDTO, or with status {@code 400 (Bad Request)} if the stockLot already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockLotDTO> createStockLot(@Valid @RequestBody StockLotDTO stockLotDTO) throws URISyntaxException {
        LOG.debug("REST request to save StockLot : {}", stockLotDTO);
        if (stockLotDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockLot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockLotDTO = stockLotService.save(stockLotDTO);
        return ResponseEntity.created(new URI("/api/stock-lots/" + stockLotDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, stockLotDTO.getId().toString()))
            .body(stockLotDTO);
    }

    /**
     * {@code PUT  /stock-lots/:id} : Updates an existing stockLot.
     *
     * @param id the id of the stockLotDTO to save.
     * @param stockLotDTO the stockLotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockLotDTO,
     * or with status {@code 400 (Bad Request)} if the stockLotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockLotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockLotDTO> updateStockLot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockLotDTO stockLotDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StockLot : {}, {}", id, stockLotDTO);
        if (stockLotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockLotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockLotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stockLotDTO = stockLotService.update(stockLotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockLotDTO.getId().toString()))
            .body(stockLotDTO);
    }

    /**
     * {@code PATCH  /stock-lots/:id} : Partial updates given fields of an existing stockLot, field will ignore if it is null
     *
     * @param id the id of the stockLotDTO to save.
     * @param stockLotDTO the stockLotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockLotDTO,
     * or with status {@code 400 (Bad Request)} if the stockLotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stockLotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockLotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockLotDTO> partialUpdateStockLot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockLotDTO stockLotDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update StockLot : {}, {}", id, stockLotDTO);
        if (stockLotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockLotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockLotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockLotDTO> result = stockLotService.partialUpdate(stockLotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockLotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-lots} : get all the Stock Lots.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Stock Lots in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockLotDTO>> getAllStockLots(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of StockLots");
        Page<StockLotDTO> page;
        if (eagerload) {
            page = stockLotService.findAllWithEagerRelationships(pageable);
        } else {
            page = stockLotService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-lots/:id} : get the "id" stockLot.
     *
     * @param id the id of the stockLotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockLotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockLotDTO> getStockLot(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StockLot : {}", id);
        Optional<StockLotDTO> stockLotDTO = stockLotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockLotDTO);
    }

    /**
     * {@code DELETE  /stock-lots/:id} : delete the "id" stockLot.
     *
     * @param id the id of the stockLotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockLot(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StockLot : {}", id);
        stockLotService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

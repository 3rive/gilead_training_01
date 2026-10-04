package com.gilead.medicalinventory.service.impl;

import com.gilead.medicalinventory.domain.StockLot;
import com.gilead.medicalinventory.repository.StockLotRepository;
import com.gilead.medicalinventory.service.StockLotService;
import com.gilead.medicalinventory.service.dto.StockLotDTO;
import com.gilead.medicalinventory.service.mapper.StockLotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gilead.medicalinventory.domain.StockLot}.
 */
@Service
@Transactional
public class StockLotServiceImpl implements StockLotService {

    private static final Logger LOG = LoggerFactory.getLogger(StockLotServiceImpl.class);

    private final StockLotRepository stockLotRepository;

    private final StockLotMapper stockLotMapper;

    public StockLotServiceImpl(StockLotRepository stockLotRepository, StockLotMapper stockLotMapper) {
        this.stockLotRepository = stockLotRepository;
        this.stockLotMapper = stockLotMapper;
    }

    @Override
    public StockLotDTO save(StockLotDTO stockLotDTO) {
        LOG.debug("Request to save StockLot : {}", stockLotDTO);
        StockLot stockLot = stockLotMapper.toEntity(stockLotDTO);
        stockLot = stockLotRepository.save(stockLot);
        return stockLotMapper.toDto(stockLot);
    }

    @Override
    public StockLotDTO update(StockLotDTO stockLotDTO) {
        LOG.debug("Request to update StockLot : {}", stockLotDTO);
        StockLot stockLot = stockLotMapper.toEntity(stockLotDTO);
        stockLot = stockLotRepository.save(stockLot);
        return stockLotMapper.toDto(stockLot);
    }

    @Override
    public Optional<StockLotDTO> partialUpdate(StockLotDTO stockLotDTO) {
        LOG.debug("Request to partially update StockLot : {}", stockLotDTO);

        return stockLotRepository
            .findById(stockLotDTO.getId())
            .map(existingStockLot -> {
                stockLotMapper.partialUpdate(existingStockLot, stockLotDTO);

                return existingStockLot;
            })
            .map(stockLotRepository::save)
            .map(stockLotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockLotDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StockLots");
        return stockLotRepository.findAll(pageable).map(stockLotMapper::toDto);
    }

    public Page<StockLotDTO> findAllWithEagerRelationships(Pageable pageable) {
        return stockLotRepository.findAllWithEagerRelationships(pageable).map(stockLotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockLotDTO> findOne(Long id) {
        LOG.debug("Request to get StockLot : {}", id);
        return stockLotRepository.findOneWithEagerRelationships(id).map(stockLotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StockLot : {}", id);
        stockLotRepository.deleteById(id);
    }
}

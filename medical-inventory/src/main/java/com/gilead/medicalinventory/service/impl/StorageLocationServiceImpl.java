package com.gilead.medicalinventory.service.impl;

import com.gilead.medicalinventory.domain.StorageLocation;
import com.gilead.medicalinventory.repository.StorageLocationRepository;
import com.gilead.medicalinventory.service.StorageLocationService;
import com.gilead.medicalinventory.service.dto.StorageLocationDTO;
import com.gilead.medicalinventory.service.mapper.StorageLocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.gilead.medicalinventory.domain.StorageLocation}.
 */
@Service
@Transactional
public class StorageLocationServiceImpl implements StorageLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageLocationServiceImpl.class);

    private final StorageLocationRepository storageLocationRepository;

    private final StorageLocationMapper storageLocationMapper;

    public StorageLocationServiceImpl(StorageLocationRepository storageLocationRepository, StorageLocationMapper storageLocationMapper) {
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationMapper = storageLocationMapper;
    }

    @Override
    public StorageLocationDTO save(StorageLocationDTO storageLocationDTO) {
        LOG.debug("Request to save StorageLocation : {}", storageLocationDTO);
        StorageLocation storageLocation = storageLocationMapper.toEntity(storageLocationDTO);
        storageLocation = storageLocationRepository.save(storageLocation);
        return storageLocationMapper.toDto(storageLocation);
    }

    @Override
    public StorageLocationDTO update(StorageLocationDTO storageLocationDTO) {
        LOG.debug("Request to update StorageLocation : {}", storageLocationDTO);
        StorageLocation storageLocation = storageLocationMapper.toEntity(storageLocationDTO);
        storageLocation = storageLocationRepository.save(storageLocation);
        return storageLocationMapper.toDto(storageLocation);
    }

    @Override
    public Optional<StorageLocationDTO> partialUpdate(StorageLocationDTO storageLocationDTO) {
        LOG.debug("Request to partially update StorageLocation : {}", storageLocationDTO);

        return storageLocationRepository
            .findById(storageLocationDTO.getId())
            .map(existingStorageLocation -> {
                storageLocationMapper.partialUpdate(existingStorageLocation, storageLocationDTO);

                return existingStorageLocation;
            })
            .map(storageLocationRepository::save)
            .map(storageLocationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StorageLocationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all StorageLocations");
        return storageLocationRepository.findAll(pageable).map(storageLocationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StorageLocationDTO> findOne(Long id) {
        LOG.debug("Request to get StorageLocation : {}", id);
        return storageLocationRepository.findById(id).map(storageLocationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StorageLocation : {}", id);
        storageLocationRepository.deleteById(id);
    }
}

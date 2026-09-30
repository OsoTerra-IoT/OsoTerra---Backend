package com.osoterra.ososense.farmmanagement.infrastructure.persistence.jpa;

import com.osoterra.ososense.farmmanagement.domain.model.Device;
import com.osoterra.ososense.farmmanagement.domain.model.DeviceId;
import com.osoterra.ososense.farmmanagement.domain.repositories.DeviceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaDeviceRepository implements DeviceRepository {

    private final SpringDataDeviceJpaRepository springDataRepository;

    JpaDeviceRepository(SpringDataDeviceJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Device save(Device device) {
        DeviceJpaEntity saved = springDataRepository.save(DeviceMapper.toEntity(device));
        return DeviceMapper.toDomain(saved);
    }

    @Override
    public Optional<Device> findById(DeviceId id) {
        return springDataRepository.findById(id.value()).map(DeviceMapper::toDomain);
    }

    @Override
    public Optional<Device> findByActivationCode(String activationCode) {
        return springDataRepository.findByActivationCode(activationCode).map(DeviceMapper::toDomain);
    }

    @Override
    public boolean existsByActivationCode(String activationCode) {
        return springDataRepository.existsByActivationCode(activationCode);
    }
}

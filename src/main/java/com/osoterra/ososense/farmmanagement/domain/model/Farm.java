package com.osoterra.ososense.farmmanagement.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root for a farm. A farm belongs to a single owner (an IAM user account,
 * referenced only by its raw id, never by the IAM domain type) and groups the plots
 * conducted by that owner.
 */
public final class Farm extends AggregateRoot<FarmId> {

    private final Long ownerId;
    private final String name;
    private final String department;
    private final String province;
    private final String district;
    private final LocalDateTime createdAt;

    private Farm(
            FarmId id, Long ownerId, String name, String department, String province, String district,
            LocalDateTime createdAt) {
        super(id);
        this.ownerId = ownerId;
        this.name = name;
        this.department = department;
        this.province = province;
        this.district = district;
        this.createdAt = createdAt;
    }

    public static Farm register(Long ownerId, String name, String department, String province, String district) {
        Objects.requireNonNull(ownerId, "ownerId");
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("A farm's name must not be blank");
        }
        return new Farm(null, ownerId, name, department, province, district, LocalDateTime.now());
    }

    public static Farm reconstruct(
            FarmId id, Long ownerId, String name, String department, String province, String district,
            LocalDateTime createdAt) {
        return new Farm(id, ownerId, name, department, province, district, createdAt);
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

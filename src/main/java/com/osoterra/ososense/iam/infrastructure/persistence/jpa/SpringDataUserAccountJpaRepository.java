package com.osoterra.ososense.iam.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataUserAccountJpaRepository extends JpaRepository<UserAccountJpaEntity, Long> {

    Optional<UserAccountJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}

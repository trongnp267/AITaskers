package com.aitasker.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aitasker.backend.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
package com.icia.fproject.admin.dao;

import com.icia.fproject.admin.dto.SignupRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignupRequestRepository extends JpaRepository<SignupRequestEntity, Long> {
}

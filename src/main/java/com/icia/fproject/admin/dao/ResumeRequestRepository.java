package com.icia.fproject.admin.dao;

import com.icia.fproject.admin.dto.ResumeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRequestRepository extends JpaRepository<ResumeRequestEntity, Long> {
}

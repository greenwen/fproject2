package com.icia.fproject.admin.dao;

import com.icia.fproject.admin.dto.AdminInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInfoRepository extends JpaRepository<AdminInfoEntity, String> {
}

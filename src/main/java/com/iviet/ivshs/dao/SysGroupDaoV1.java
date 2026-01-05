package com.iviet.ivshs.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.iviet.ivshs.entities.SysGroupV1;

@Repository
public class SysGroupDaoV1 extends BaseAuditEntityDaoV1<SysGroupV1> {
    
    public SysGroupDaoV1() {
        super(SysGroupV1.class);
    }

    public Optional<SysGroupV1> findByCode(String code) {
        return findOne(root -> entityManager.getCriteriaBuilder()
            .equal(root.get("groupCode"), code));
    }

    public boolean existsByCode(String code) {
        return exists(root -> entityManager.getCriteriaBuilder()
            .equal(root.get("groupCode"), code));
    }
}

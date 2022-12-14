package com.christmasboy_.todo.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.christmasboy_.todo.member.entity.MemberInfoEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberInfoEntity, Long> {
    public Integer countByEmail(String email);
    public MemberInfoEntity findByEmailAndPwd(String email, String pwd);
}

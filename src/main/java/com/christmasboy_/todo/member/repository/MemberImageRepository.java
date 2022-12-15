package com.christmasboy_.todo.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.christmasboy_.todo.member.entity.MemberImageEntity;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImageEntity, Long> {
    public List<MemberImageEntity> findByMiSeq(Long miSeq);
    // select * from todo_images_info where tii_uri = uri order by tii_seq desc limit 1;
    public List<MemberImageEntity> findTop1ByUriOrderBySeqDesc(String uri);
}


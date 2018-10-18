package com.rustedbrain.study.course.service.repository;

import com.rustedbrain.study.course.model.persistence.authorization.Member;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberRepository extends UserRepository<Member> {

}

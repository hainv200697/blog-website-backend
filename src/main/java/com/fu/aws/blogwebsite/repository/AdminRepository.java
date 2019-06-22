package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.Admin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin> {
    @Query(value = "SELECT u FROM Admin u WHERE LOWER(u.email) LIKE CONCAT('%',LOWER(:email),'%')")
    List<Admin> findAllByNameLike(@Param("email") String email);

    Optional<Admin> findByEmail(String email);

    static Specification<Admin> filterByName(String email) {
        return (root, cq, cb) -> {
            return cb.like(root.get("email"), "%" + email + "%");
        };
    }
}

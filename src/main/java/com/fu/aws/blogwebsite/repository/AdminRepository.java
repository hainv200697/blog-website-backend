package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query(value = "SELECT u FROM User u WHERE LOWER(u.email) LIKE CONCAT('%',LOWER(:email),'%')")
    List<User> findAllByNameLike(@Param("email") String email);

    Optional<User> findByEmail(String email);

    static Specification<User> filterByName(String email) {
        return (root, cq, cb) -> {
            return cb.like(root.get("email"), "%" + email + "%");
        };
    }
}

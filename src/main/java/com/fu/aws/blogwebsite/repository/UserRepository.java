package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query(value = "SELECT u FROM User u WHERE LOWER(u.name) LIKE CONCAT('%',LOWER(:name),'%')")
    List<User> findAllByNameLike(@Param("name") String name);

    Optional<User> findByName(String name);

    static Specification<User> filterByName(String name) {
        return (root, cq, cb) -> {
            return cb.like(root.get("name"), "%" + name + "%");
        };
    }
}

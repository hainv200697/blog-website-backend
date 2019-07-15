package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.ExternalUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExternalRepository extends PagingAndSortingRepository<ExternalUser, Long> {
    @Query(value = "SELECT e FROM ExternalUser e WHERE LOWER(e.email) LIKE CONCAT('%',LOWER(:email),'%')")
    List<ExternalUser> findAllByEmailLike(@Param("email") String email, Pageable pageable);

    ExternalUser findByEmail(String email);
}

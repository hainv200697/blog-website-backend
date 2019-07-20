package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query(value = "SELECT e FROM Post e WHERE e.status = 'APPROVE'")
    List<Post> findPost(Pageable pageable);

    @Query(value = "SELECT e FROM Post e")
    List<Post> getAllAndPaging(Pageable pageable);

    @Query(value = "SELECT e FROM Post e WHERE e.status <> 'DELETE' AND (e.user.email) = :email")
    List<Post> findPostByEmail(@Param("email") String email, Pageable pageable);
}

package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query(value = "SELECT e FROM Post e")
    List<Post> findPost(Pageable pageable);

    @Query(value = "SELECT e FROM Post e WHERE LOWER(e.status) LIKE CONCAT('%',LOWER(:status),'%') AND LOWER(e.title) LIKE CONCAT('%',LOWER(:title),'%') AND LOWER(e.type) LIKE CONCAT('%',LOWER(:type),'%') AND LOWER(e.user.fullName) LIKE CONCAT('%',LOWER(:fullName),'%')")
    Page<Post> findPost(@Param("status") String status, @Param("title") String title, @Param("type") String type, @Param("fullName") String fullName, Pageable pageable);

    @Query(value = "SELECT e FROM Post e WHERE LOWER(e.status) LIKE CONCAT('%',LOWER(:status),'%')")
    List<Post> findPostApprove(@Param("status") String status, Pageable pageable);

    @Query(value = "SELECT e FROM Post e")
    List<Post> getAllAndPaging(Pageable pageable);

    @Query(value = "SELECT e FROM Post e WHERE e.status <> 'DELETE' AND (e.user.email) = :email")
    List<Post> findPostByEmail(@Param("email") String email, Pageable pageable);
}

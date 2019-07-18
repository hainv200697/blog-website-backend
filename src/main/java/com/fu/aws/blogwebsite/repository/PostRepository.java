package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query(value = "SELECT e FROM Post e WHERE e.status = 'APPROVE'")
    List<Post> findPost(Pageable pageable);

    @Query(value = "SELECT e FROM Post e")
    List<Post> getAllAndPaging(Pageable pageable);
}

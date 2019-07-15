package com.fu.aws.blogwebsite.repository;

import com.fu.aws.blogwebsite.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}

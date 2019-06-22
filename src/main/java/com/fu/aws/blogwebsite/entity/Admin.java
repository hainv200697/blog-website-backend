package com.fu.aws.blogwebsite.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_date")
    private Timestamp createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp updatedDate;
}

package com.fu.aws.blogwebsite.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "external_user")
@NoArgsConstructor
@Data
public class ExternalUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;
    @Column(name = "active")
    private boolean active;
    @CreationTimestamp
    @Column(name = "created_date")
    private Timestamp createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp updatedDate;
    @Column(name = "fullName")
    private String fullName;
}

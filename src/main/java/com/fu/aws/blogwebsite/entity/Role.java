package com.fu.aws.blogwebsite.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Data
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();
}

package com.fu.aws.blogwebsite.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdminRequest {
    private String email;
    private String fullName;
    private String password;
    private String newPassword;
}

package com.fu.aws.blogwebsite.service;

import com.fu.aws.blogwebsite.entity.ExternalUser;
import com.fu.aws.blogwebsite.repository.ExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalService {
    @Autowired
    private ExternalRepository externalRepository;

    public List<ExternalUser> searchLikeEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName"));
        return externalRepository.findAllByEmailLike(email, pageable);
    }

    public ExternalUser register(ExternalUser externalUser) {
        ExternalUser find = findByEmail(externalUser.getEmail());
        if (find == null) {
            return externalRepository.save(externalUser);
        }
        if (find.getFullName().compareTo(externalUser.getFullName()) != 0) {
            find.setFullName(externalUser.getFullName());
            return externalRepository.save(find);
        }
        if (!find.isActive()) {
            return null;
        }
        return find;
    }

    public ExternalUser findByEmail(String email) {
        return externalRepository.findByEmail(email);
    }

    public ExternalUser editActive(String email, boolean active) {
        ExternalUser find = findByEmail(email);
        if (find == null) return null;
        find.setActive(active);
        return externalRepository.save(find);
    }
}

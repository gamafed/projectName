package com.companyName.projectName.login.service;

import com.companyName.projectName.exception.NotFoundException;
import com.companyName.projectName.exception.UnprocessableEntityException;
import com.companyName.projectName.login.converter.AppUserConverter;
import com.companyName.projectName.login.entity.AppUser;
import com.companyName.projectName.login.entity.AppUserRequest;
import com.companyName.projectName.login.entity.AppUserResponse;
import com.companyName.projectName.login.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {
    private AppUserRepository repository;
    private BCryptPasswordEncoder passwordEncoder;
    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> existingUser = repository.findByEmailAddress(request.getEmailAddress());
        if (existingUser.isPresent()) {
            throw new UnprocessableEntityException("This email address has been used.");
        }

        AppUser user = AppUserConverter.toAppUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = repository.insert(user);
        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUserResponse getUserResponseById(String id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));

        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUser getUserByEmail(String email) throws NotFoundException{
        return repository.findByEmailAddress(email)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public List<AppUserResponse> getUserResponses() {
        List<AppUser> users = repository.findAll();
        return AppUserConverter.toAppUserResponses(users);
    }
}

package com.example.spring_boot_example.service;

import com.example.spring_boot_example.entity.User;
import com.example.spring_boot_example.entity.UserRole;
import com.example.spring_boot_example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void save(User user){
        userRepository.save(user);
    }

    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.
                findByEmailIgnoreCase(email).
                orElseThrow(() -> new IllegalArgumentException("User with email = " + email +" not found"));
    }

    public List<User> findAllByRole(Iterable<UserRole> roles){
        return userRepository.findAllByRoleInOrderById(roles);
    }

    public void deleteById(int id){
        userRepository.deleteById(id);
    }

    public Optional<User> findById(int id){
        return userRepository.findById(id);
    }

    public void updateRole(int id, UserRole newRole){
        userRepository.updateRole(id, newRole);
    }
}

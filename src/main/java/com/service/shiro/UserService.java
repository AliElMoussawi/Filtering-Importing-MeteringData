package com.service.shiro;

import com.model.GroupRole;
import com.model.User;
import com.repository.GroupRoleRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRoleRepository groupRoleRepository;
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<String> getCustomerAccountIdByUsername(String username) {
        return Optional.ofNullable(userRepository.getCustomerAccountIdByUsername(username));
    }
    public Optional<User> getById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }
    public Optional<GroupRole> getGroupRoleByCode(String code) {
        return groupRoleRepository.findByCode(code);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Boolean isCustomerAccountIdExist(UUID id){
        return userRepository.existsByCustomerAccountId(id);
    }
    public User save(User user) {
        return userRepository.save(user);
    }
}

package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.RoleDTO;
import com.example.DoAnCN.Entity.Role;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Repository.RoleRepository;
import com.example.DoAnCN.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> getAll(){
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }

    @Autowired
    private UserRepository userRepository;

    public void updateUserRole(Long userId, List<RoleDTO> roles){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();

        Set<Role> currentRoles = user.getRoles();

        for (RoleDTO roleDTO : roles) {
            Role role = roleRepository.findById(roleDTO.getId()).orElseThrow(() -> new RuntimeException("Role not found"));
            currentRoles.add(role);
        }

        user.setRoles(currentRoles);
        userRepository.save(user);
    }
}

package com.example.DoAnCN.Security;

import com.example.DoAnCN.Entity.Role;
import com.example.DoAnCN.Repository.RoleCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    @Autowired
    private RoleCheckRepository role;

    @Override
    public void run(String... args) {
        if (role.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            role.save(adminRole);
        }

        if (role.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            role.save(userRole);
        }
    }
}


package com.softwebdevelopers.ecommerce.bootstrap;

import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.category.Category;
import com.softwebdevelopers.ecommerce.models.product.Product;
import com.softwebdevelopers.ecommerce.models.role.Privilege;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import com.softwebdevelopers.ecommerce.models.user.UserRetailer;
import com.softwebdevelopers.ecommerce.repository.*;
import com.softwebdevelopers.ecommerce.models.role.Role;
import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bootstrap  implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UUserRepository userRepository;

    @Autowired
    private RRoleRepository roleRepository;

    @Autowired
    private PCategoryRepository categoryRepository;

    @Autowired
    private PBrandRepository brandRepository;

    @Autowired
    private PProductRepository productRepository;

    @Autowired
    private RPrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup)
            return;

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege updatePrivilege = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> superadminPrivileges = Arrays.asList(readPrivilege, createPrivilege, updatePrivilege, deletePrivilege);
        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, createPrivilege, updatePrivilege);
        List<Privilege> staffPrivileges = Arrays.asList(readPrivilege, createPrivilege);

        Role adminRole = createRoleIfNotFound("ROLE_SUPERADMIN", superadminPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role wholesalerRole = createRoleIfNotFound("ROLE_WHOLESALER", staffPrivileges);
        Role salesRole = createRoleIfNotFound("ROLE_SALES", staffPrivileges);
        Role retailerRole = createRoleIfNotFound("ROLE_RETAILER", Arrays.asList(readPrivilege));
        createRoleIfNotFound("ROLE_GUEST", Arrays.asList(readPrivilege));

        User superadmin = createUserIfNotFound(new User().toBuilder()
                .name("Softweb Developers")
                .username("softweb")
                .email("info@softwebdevelopers.com")
                .password(passwordEncoder.encode("Password@1"))
                .enabled(true)
                .isEmailVerified(true)
                .userType("SUPERADMIN")
                .roles(Arrays.asList(adminRole))
                .build());

        User wholesaler = new User().toBuilder()
                .name("A2Z Supplier")
                .username("ishukla")
                .email("wholesaler@softwebdevelopers.com")
                .password(passwordEncoder.encode("Password@1"))
                .enabled(true)
                .isEmailVerified(true)
                .userType("EMPLOYEE")
                .roles(Arrays.asList(wholesalerRole))
                .build();
        UserEmployee info = new UserEmployee().toBuilder()
                .firstname("A2Z")
                .lastname("Supplier")
                .username("ishukla")
                .email("wholesaler@softwebdevelopers.com")
                .user(wholesaler)
                .build();
        wholesaler.setUserEmployee(info);
        wholesaler = createUserIfNotFound(wholesaler);

        User salesPerson = new User().toBuilder()
                .name("Sulav Paudel")
                .username("salesperson")
                .email("salesperson@softwebdevelopers.com")
                .password(passwordEncoder.encode("Password@1"))
                .enabled(true)
                .isEmailVerified(true)
                .userType("EMPLOYEE")
                .roles(Arrays.asList(salesRole))
                .ownerUser(wholesaler)
                .build();
        UserEmployee info1 = new UserEmployee().toBuilder()
                .firstname("Sulav")
                .lastname("Paudel")
                .username("salesperson")
                .email("salesperson@softwebdevelopers.com")
                .user(salesPerson)
                .build();
        salesPerson.setUserEmployee(info1);
        createUserIfNotFound(salesPerson);

        User retailer1 = new User().toBuilder()
                .name("Sanjay Khatri")
                .username("retailer")
                .email("retailer@softwebdevelopers.com")
                .password(passwordEncoder.encode("Password@1"))
                .enabled(true)
                .isEmailVerified(true)
                .userType("RETAILER")
                .roles(Arrays.asList(retailerRole))
                .build();
        UserRetailer kyc1 = new UserRetailer().toBuilder()
                .businessName("A2Z Enterprises")
                .email("retailer@softwebdevelopers.com")
                .status(true)
                .user(retailer1)
                .build();
        retailer1.setUserRetailer(kyc1);
        createUserIfNotFound(retailer1);

        User retailer2 = new User().toBuilder()
                .name("Prayas Sapkota")
                .username("retailer1")
                .email("retailer1@softwebdevelopers.com")
                .password(passwordEncoder.encode("Password@1"))
                .enabled(true)
                .isEmailVerified(true)
                .userType("RETAILER")
                .roles(Arrays.asList(retailerRole))
                .build();
        UserRetailer kyc2 = new UserRetailer().toBuilder()
                .businessName("Hardware Suppliers")
                .email("retailer1@softwebdevelopers.com")
                .status(true)
                .user(retailer2)
                .build();
        retailer2.setUserRetailer(kyc2);
        createUserIfNotFound(retailer2);

        Category cat1 = createCategoryIfNotFound("Shovels", superadmin);
        Category cat2 = createCategoryIfNotFound("Wheel Barrows", superadmin);
        Category cat3 = createCategoryIfNotFound("Door Frames", superadmin);
        Category cat4 = createCategoryIfNotFound("Roof Bolts", superadmin);

        Brand brand1 = createBrandIfNotFound("KPML", superadmin);

        Product product1 = createProductIfNotFound(new Product().toBuilder()
                .wholesaler(wholesaler)
                .name("Shovel")
                .category(cat1)
                .brand(brand1)
                .mrp(200f)
                .price(180f)
                .description("This is description for Shovel product.")
                .productCondition("New")
                .deal(false)
                .createdUser(superadmin)
                .build());

        Product product2 = createProductIfNotFound(new Product().toBuilder()
                .wholesaler(wholesaler)
                .name("Windows Frame")
                .category(cat3)
                .brand(brand1)
                .mrp(800f)
                .price(800f)
                .description("This is description for Windows product.")
                .productCondition("New")
                .deal(false)
                .createdUser(superadmin)
                .build());

    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege().toBuilder()
                    .name(name)
                    .build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Optional<Role> role = roleRepository.findByName(name);
        if (!role.isPresent()) {
            Role entity = new Role().toBuilder()
                    .name(name)
                    .privileges(privileges)
                    .build();
            roleRepository.save(entity);
            return entity;
        }
        return role.get();
    }

    @Transactional
    User createUserIfNotFound(User entity) {

        Optional<User> user = userRepository.findByUsername(entity.getUsername());

        if(user.isPresent())
            return user.get();

        return userRepository.save(entity);
    }

    @Transactional
    Category createCategoryIfNotFound(String name, User superadmin) {

        Optional<Category> category = categoryRepository.findByName(name);
        if (!category.isPresent()) {
            Category entity = new Category().toBuilder()
                    .name(name)
                    .createdUser(superadmin)
                    .build();
            categoryRepository.save(entity);
            return entity;
        }
        return category.get();
    }

    @Transactional
    Brand createBrandIfNotFound(String name, User superadmin) {

        Optional<Brand> brand = brandRepository.findByName(name);
        if (!brand.isPresent()) {
            Brand entity = new Brand().toBuilder()
                    .name(name)
                    .createdUser(superadmin)
                    .build();
            brandRepository.save(entity);
            return entity;
        }
        return brand.get();
    }

    @Transactional
    Product createProductIfNotFound(Product entity) {

        Optional<Product> product = productRepository.findByName(entity.getName());
        if (!product.isPresent()) {
            productRepository.save(entity);
            return entity;
        }
        return product.get();
    }
}

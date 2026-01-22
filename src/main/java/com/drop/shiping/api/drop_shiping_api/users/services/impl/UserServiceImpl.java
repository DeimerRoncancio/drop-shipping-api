package com.drop.shiping.api.drop_shiping_api.users.services.impl;

import com.drop.shiping.api.drop_shiping_api.auth.dtos.RegisterUserDTO;
import com.drop.shiping.api.drop_shiping_api.common.exceptions.InvalidPasswordException;
import com.drop.shiping.api.drop_shiping_api.common.exceptions.NotFoundException;
import com.drop.shiping.api.drop_shiping_api.common.exceptions.PasswordMatchException;
import com.drop.shiping.api.drop_shiping_api.images.services.ImageService;
import com.drop.shiping.api.drop_shiping_api.users.dtos.PasswordDTO;
import com.drop.shiping.api.drop_shiping_api.users.dtos.UserDTO;
import com.drop.shiping.api.drop_shiping_api.users.dtos.UserResponseDTO;
import com.drop.shiping.api.drop_shiping_api.users.enums.Field;
import com.drop.shiping.api.drop_shiping_api.users.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.drop.shiping.api.drop_shiping_api.images.entities.Image;
import com.drop.shiping.api.drop_shiping_api.users.entities.Role;
import com.drop.shiping.api.drop_shiping_api.users.entities.User;
import com.drop.shiping.api.drop_shiping_api.users.mappers.UserMapper;
import com.drop.shiping.api.drop_shiping_api.users.repositories.RoleRepository;
import com.drop.shiping.api.drop_shiping_api.users.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository,
    RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> users = repository.findAll(pageable);
        return users.map(UserMapper.MAPPER::userToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findByRole(Pageable pageable, boolean isAdmin) {
        Page<User> admins = isAdmin
                ? repository.findByAdminTrue(pageable)
                : repository.findByAdminFalse(pageable);

        return admins.map(UserMapper.MAPPER::userToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findOne(String id){
        Optional<User> user = repository.findById(id);
        return user.map(UserMapper.MAPPER::userToResponseDTO);
    }

    @Override
    @Transactional
    public RegisterUserDTO save(RegisterUserDTO userDTO) {
        User user = UserMapper.MAPPER.registerDTOtoUser(userDTO);

        List<Role> roles = new ArrayList<>();
        roleRepository.findByRole("ROLE_USER").ifPresent(roles::add);

        if (user.isAdmin())
            roleRepository.findByRole("ROLE_ADMIN").ifPresent(roles::add);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        repository.save(user);
        return UserMapper.MAPPER.userToRegisterDTO(user);
    }

    @Override
    @Transactional
    public Optional<UserDTO> update(String id, UserDTO userDTO) {
        return repository.findById(id).map(user -> {
            List<Role> roles = user.getRoles();

            if (userDTO.admin() && !user.isAdmin())
                roleRepository.findByRole("ROLE_ADMIN").ifPresent(roles::add);
            if (!userDTO.admin() && user.isAdmin())
                roleRepository.findByRole("ROLE_ADMIN").ifPresent(roles::remove);

            user.setRoles(roles);
            UserMapper.MAPPER.toUpdateUser(userDTO, user);

            repository.save(user);
            return UserMapper.MAPPER.userToUserDTO(user);
        });
    }

    @Override
    @Transactional
    public Optional<User> updatePassword(String id, PasswordDTO passwordInfo) {
        return repository.findById(id).map(userDb -> {
            String currentPassword = passwordInfo.currentPassword();
            String newPassword = passwordInfo.newPassword();

            if (!passwordEncoder.matches(currentPassword, userDb.getPassword()))
                throw new InvalidPasswordException("Invalid password");
            if (currentPassword.equals(newPassword))
                throw new PasswordMatchException("Both passwords match");

            userDb.setPassword(passwordEncoder.encode(newPassword));
            repository.save(userDb);

            return userDb;
        });
    }

    @Override
    @Transactional
    public Optional<User> delete(String id) {
        return repository.findById(id).map(user -> {
            repository.delete(user);
            return user;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> userStats(boolean isAdmin) {
        Map<String, Long> userStats = new HashMap<>();

        userStats.put("totalUsers", isAdmin
                ? repository.countByAdminTrue()
                : repository.countByAdminFalse());
        userStats.put("enabledUsers", isAdmin
                ? repository.countByAdminTrueAndEnabledTrue()
                : repository.countByAdminFalseAndEnabledTrue());
        userStats.put("disabledUsers", isAdmin
                ? repository.countByAdminTrueAndEnabledFalse()
                : repository.countByAdminFalseAndEnabledFalse());

        return userStats;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> userSearch(Pageable pageable, String identifier, boolean isAdmin, Boolean isEnabled,Field field) {
        Page<User> users = switch (field) {
            case EMAIL -> isEnabled != null
                ? repository.findByEmailEnabled(identifier, isAdmin, isEnabled, pageable)
                : repository.findByEmail(identifier, isAdmin, pageable);
            case NUMBER -> isEnabled != null
                ? repository.findByPhoneEnabled(identifier, isAdmin, isEnabled, pageable)
                : repository.findByPhone(identifier, isAdmin, pageable);
            default -> isEnabled != null
                ? repository.findByNameEnabled(identifier, isAdmin, isEnabled, pageable)
                : repository.findByName(identifier, isAdmin, pageable);
        };

        return users.map(UserMapper.MAPPER::userToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Long usersSize() {
        return repository.count();
    }
}

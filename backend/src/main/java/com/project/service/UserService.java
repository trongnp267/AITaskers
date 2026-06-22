package com.project.service;

import com.project.dto.UserDTO;
import com.project.exception.BaseException;
import com.project.exception.UserNotFoundException;
import com.project.model.Account;
import com.project.repository.IUserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        try {
            if (userDTO == null) {
                throw new IllegalArgumentException("User data is required");
            }

            Account user = toEntity(userDTO);
            user.setId(null);
            return toDTO(userRepository.save(user));
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Cannot create user: " + exception.getMessage());
        }
    }

    @Override
    public List<UserDTO> findAll() {
        try {
            return userRepository.findAll().stream().map(this::toDTO).toList();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Cannot get users: " + exception.getMessage());
        }
    }

    @Override
    public UserDTO findById(UUID id) {
        try {
            if (id == null) {
                throw new UserNotFoundException(id);
            }

            return toDTO(findEntity(id));
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Cannot get user: " + exception.getMessage());
        }
    }

    @Override
    public UserDTO update(UUID id, UserDTO userDTO) {
        try {
            if (id == null) {
                throw new UserNotFoundException(id);
            }
            if (userDTO == null) {
                throw new IllegalArgumentException("User data is required");
            }

            Account user = findEntity(id);
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setSubscribed(userDTO.getSubscribed());
            user.setUserRole(userDTO.getUserRole());
            return toDTO(userRepository.save(user));
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Cannot update user: " + exception.getMessage());
        }
    }

    Account findEntity(UUID id) {
        if (id == null) {
            throw new UserNotFoundException(id);
        }
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    UserDTO toDTO(Account user) {
        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSubscribed(user.getSubscribed());
        dto.setUserRole(user.getUserRole());
        return dto;
    }

    private Account toEntity(UserDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("User data is required");
        }

        Account user = new Account();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setSubscribed(dto.getSubscribed());
        user.setUserRole(dto.getUserRole());
        return user;
    }
}

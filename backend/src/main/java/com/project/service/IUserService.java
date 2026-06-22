package com.project.service;

import com.project.dto.UserDTO;
import java.util.List;
import java.util.UUID;

public interface IUserService {

    UserDTO create(UserDTO userDTO);

    List<UserDTO> findAll();

    UserDTO findById(UUID id);

    UserDTO update(UUID id, UserDTO userDTO);
}

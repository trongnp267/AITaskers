package com.project.service;

import com.project.dto.UserDTO;
import java.util.List;

public interface IUserService {

    UserDTO create(UserDTO userDTO);

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO update(Long id, UserDTO userDTO);
}

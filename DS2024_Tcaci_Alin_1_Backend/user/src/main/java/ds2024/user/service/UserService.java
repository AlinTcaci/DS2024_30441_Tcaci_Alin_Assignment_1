package ds2024.user.service;

import ds2024.user.dtos.UserDTO;
import ds2024.user.dtos.builder.UserBuilder;
import ds2024.user.entity.User;
import ds2024.user.repository.UserRepository;
import ds2024.user.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserBuilder.toUserDTO(user);
    }

    public void createUser(UserDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        UserBuilder.toUserDTO(savedUser);
    }

    public void updateUserById(UUID id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setRole(userDTO.getRole());

        User updatedUser = userRepository.save(existingUser);
        UserBuilder.toUserDTO(updatedUser);
    }

    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserBuilder.toUserDTO(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


    public String authenticate(User userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if(user != null && (user.getPassword().equals(userDTO.getPassword()))) {
            return jwtUtil.generateToken(user.getUsername());
        }
        return null;
    }
}
package vn.Tuan8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.Tuan8.dto.UserInput;
import vn.Tuan8.entity.User;
import vn.Tuan8.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @QueryMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @QueryMapping
    public Optional<User> findUserById(@Argument Long id) {
        return userRepository.findById(id);
    }

    @MutationMapping
    public User createUser(@Argument UserInput input) {
        User user = new User();
        user.setFullname(input.getFullname());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword()); // Cần mã hóa trong thực tế
        user.setPhone(input.getPhone());
        return userRepository.save(user);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument UserInput input) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        existingUser.setFullname(input.getFullname());
        existingUser.setEmail(input.getEmail());
        existingUser.setPhone(input.getPhone());

        return userRepository.save(existingUser);
    }

    @MutationMapping
    public boolean deleteUser(@Argument Long id) {
        userRepository.deleteById(id);
        return true;
    }
}

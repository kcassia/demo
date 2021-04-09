package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        LOG.info("Getting all users.");
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID: {}.", userId);
        return userRepository.findById(userId).orElse(null);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String userId) {
        LOG.info("Deleting user with ID: {}.", userId);
        userRepository.deleteById(userId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public User addNewUsers(@RequestBody User user)
    {
        LOG.info("Creating user with ID: {}.", user.getUserId());
        return userRepository.save(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User setUser(@PathVariable String userId, @RequestBody User user)
    {
        if (userId.equals(user.getUserId()))
        {
            LOG.info("Updating user with ID: {}.", user.getUserId());
            return userRepository.save(user);
        }
        else
        {
            LOG.info("Updating fail by mismatch {} vs {}", userId, user.getUserId());
            return userRepository.findById(userId).orElse(null);
        }
    }

    @RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
    public Object getAllUserSettings(@PathVariable String userId)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getUserSettings();
        } else {
            return "User not found.";
        }
    }

    @RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
    public String getUserSetting(@PathVariable String userId, @PathVariable String key)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getUserSettings().get(key);
        } else {
            return "User not found.";
        }
    }

    @RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
    public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.getUserSettings().put(key, value);
            userRepository.save(user);
            return "Key added";
        } else {
            return "User not found.";
        }
    }
}
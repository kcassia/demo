package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @RequestMapping(value = "", method = RequestMethod.GET)
    @GetMapping("")
    public List<User> getAllUsers() {
        LOG.info("Getting all users.");
        return userRepository.findAll();
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID: {}.", userId);
        return userRepository.findById(userId).orElse(null);
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        LOG.info("Deleting user with ID: {}.", userId);
        userRepository.deleteById(userId);
    }

    // @RequestMapping(value = "", method = RequestMethod.POST)
    @PostMapping("")
    public User addNewUsers(@RequestBody User user)
    {
        LOG.info("Creating user with ID: {}.", user.getUserId());
        return userRepository.save(user);
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    @PutMapping("/{userId}")
    public User setUser(@PathVariable String userId, @RequestBody User user)
    {
        if (userId.equals(user.getUserId()))
        {
            LOG.info("Updating user by PUT with ID: {}.", user.getUserId());
            return userRepository.save(user);
        }
        else
        {
            LOG.info("Updating user by PUT fail due to mismatch {} vs {}", userId, user.getUserId());
            return null;
        }
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    @PatchMapping("/{userId}")
    public User setUser(@PathVariable String userId, @RequestBody Map<String, Object> update)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
        {
            LOG.info("User not found");
            return null;
        }

        update.forEach(
                (key, value)->{
                    switch (key)
                    {
                        case "userName" :
                            LOG.info("Updating userName {} to {}", user.getUserName(), value.toString());
                            user.setUserName((String) value);
                            break;
                        case "creationDate" :
                            LOG.info("Updating createDate {} to {}", user.getCreationDate(), value.toString());
                            user.setCreationDate((Date) value);
                            break;
                        case "userSettings" :
                            LOG.info("Updating userSettings {} to {}", user.getUserSettings(), value.toString());
                            user.setUserSettings((Map<String, String>) value);
                            break;
                    }
                }
        );

        LOG.info("Updating user by PATCH with ID: {}.", user.getUserId());

        return userRepository.save(user);
    }

    // @RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
    @GetMapping("/settings/{userId}")
    public Object getAllUserSettings(@PathVariable String userId)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null)
        {
            return user.getUserSettings();
        }
        else
        {
            return "User not found.";
        }
    }

    // @RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
    @GetMapping("settings/{userId}/{key}")
    public String getUserSetting(@PathVariable String userId, @PathVariable String key)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null)
        {
            return user.getUserSettings().get(key);
        }
        else
        {
            return "User not found.";
        }
    }

    // @RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
    @GetMapping("/settings/{userId}/{key}/{value}")
    public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null)
        {
            user.getUserSettings().put(key, value);
            userRepository.save(user);
            return "Key added";
        }
        else {
            return "User not found.";
        }
    }
}
package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class UserController
{

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @RequestMapping(value = "", method = RequestMethod.GET)
    @GetMapping("")
    public ResponseEntity getAllUsers()
    {
        LOG.info("Getting all users.");
        List<User> users = userRepository.findAll();
        return (users.size() > 0) ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable String userId)
    {
        LOG.info("Getting user with ID: {}.", userId);
        User user = userRepository.findById(userId).orElse(null);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        LOG.info("Deleting user with ID: {}.", userId);
        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    // @RequestMapping(value = "", method = RequestMethod.POST)
    @PostMapping("")
    public ResponseEntity addNewUsers(@RequestBody User user)
    {
        LOG.info("Creating user with ID: {}.", user.getUserId());
        return ResponseEntity.created(URI.create("/" + user.getUserId())).body(userRepository.save(user));
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    @PutMapping("/{userId}")
    public ResponseEntity setUser(@PathVariable String userId, @RequestBody User user)
    {
        if (userId.equals(user.getUserId()))
        {
            LOG.info("Updating user by PUT with ID: {}.", user.getUserId());
            return (userRepository.existsById(userId)) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        }
        else
        {
            LOG.info("Updating user by PUT fail due to mismatch {} vs {}", userId, user.getUserId());
            return ResponseEntity.badRequest().build();
        }
    }

    // @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    @PatchMapping("/{userId}")
    public ResponseEntity setUser(@PathVariable String userId, @RequestBody Map<String, Object> update)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
        {
            LOG.info("User not found");
            return ResponseEntity.notFound().build();
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

        return ResponseEntity.ok(userRepository.save(user));
    }

    // @RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
    @GetMapping("/settings/{userId}")
    public ResponseEntity getAllUserSettings(@PathVariable String userId)
    {
        User user = userRepository.findById(userId).orElse(null);
        return (user != null) ? ResponseEntity.ok(user.getUserSettings()) : ResponseEntity.notFound().build();
    }

    // @RequestMapping(value = "/settings/{userId}/{key}", method = RequestMethod.GET)
    @GetMapping("settings/{userId}/{key}")
    public ResponseEntity getUserSetting(@PathVariable String userId, @PathVariable String key)
    {
        User user = userRepository.findById(userId).orElse(null);
        return (user != null) ? ResponseEntity.ok(user.getUserSettings().get(key)) : ResponseEntity.notFound().build();
    }

    // @RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.POST)
    @PutMapping("/settings/{userId}/{key}/{value}")
    public ResponseEntity addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value)
    {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null)
        {
            user.getUserSettings().put(key, value);
            return ResponseEntity.ok(userRepository.save(user));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
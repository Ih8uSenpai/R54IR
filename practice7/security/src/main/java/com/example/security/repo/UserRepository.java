package com.example.security.repo;

import com.example.security.model.UserDto;
import com.example.security.model.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;
    private final static String HASH_KEY = "User";
    public UserImpl save(UserImpl userImpl){
        UserDto userDto = new UserDto(userImpl.getName(), userImpl.getPassword(), userImpl.getRole());
        template.opsForHash().put(HASH_KEY, userImpl.getName(), userDto);
        findUserByUsername(userImpl.getName());
        return userImpl;
    }
    public List<UserImpl> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }
    public Object findUserById(int id){

        return (UserImpl) template.opsForHash().get(HASH_KEY, id);
    }
    public UserDto findUserByUsername(String username){
        Object object = template.opsForHash().get(HASH_KEY, username);
        String parse = object.toString();
        String [] temp = parse.split(",");
        String name = temp[0].substring(temp[0].indexOf("name=") + 5);
        String password = temp[1].substring(temp[1].indexOf("$") );
        String role = temp[2].substring(temp[2].indexOf("=")+1, temp[2].length() -1);
        return new UserDto(name, password, role);
    }
    public String deleteUser(int id){
        template.opsForHash().delete(HASH_KEY, id);
        return "User removed";
    }
}

package com.biblio.medialltechv2.security;

import com.biblio.medialltechv2.model.Users;
import com.biblio.medialltechv2.service.UsersService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomizedUserManagement implements UserDetailsService {

    private UsersService usersService;

    public CustomizedUserManagement(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userFound =  usersService.getUserByUsername(username);
        return new UserSpringSecurity(userFound);
    }
}

package com.biblio.medialltech.security;

import com.biblio.medialltech.users.UserRepository;
import com.biblio.medialltech.users.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
        User user = userRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + pseudo));

        return new org.springframework.security.core.userdetails.User(
                user.getPseudo(),
                user.getPassword(),
                user.getAccountNonExpired() != null ? user.getAccountNonExpired() : true,
                user.getCredentialsNonExpired() != null ? user.getCredentialsNonExpired() : true,
                user.getAccountNonLocked() != null ? user.getAccountNonLocked() : true,
                true, // enabled
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getAuthorities().name()));
    }
}
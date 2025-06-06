package com.biblio.medialltech.security;

import com.biblio.medialltech.users.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si l'utilisateur est déjà authentifié (pas anonyme), le rediriger vers la page d'accueil
        if (auth != null && auth.isAuthenticated() &&
                !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/";
        }
        return "login";
    }
}

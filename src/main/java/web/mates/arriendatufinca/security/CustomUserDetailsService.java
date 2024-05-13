package web.mates.arriendatufinca.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.model.user.User;
import web.mates.arriendatufinca.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@Getter
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private User userDetail;

    CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new UsernameNotFoundException("user not found");

        userDetail = user.get();
        return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
    }
}

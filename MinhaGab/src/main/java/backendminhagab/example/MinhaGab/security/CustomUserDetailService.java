package backendminhagab.example.MinhaGab.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backendminhagab.example.MinhaGab.models.UserModel;
import backendminhagab.example.MinhaGab.repositories.UserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    private final UserRepository repository;

    public CustomUserDetailService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String cpfcnpj) throws UsernameNotFoundException {
        logger.debug("Tentando carregar usuário com CPF: {}", cpfcnpj);
        
        UserModel user = repository.findByCpfcnpj(cpfcnpj)
            .orElseThrow(() -> {
                logger.warn("Usuário não encontrado com CPF: {}", cpfcnpj);
                return new UsernameNotFoundException("Usuário não encontrado com CPF: " + cpfcnpj);
            });

        logger.debug("Usuário encontrado: {}", user.getCpfcnpj());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}

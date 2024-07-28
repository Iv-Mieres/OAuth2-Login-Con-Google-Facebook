package com.prueba.login.serviceImp;

import com.prueba.login.dto.request.CustomerUserDtoReq;
import com.prueba.login.dto.response.CustomerUserDtoRes;
import com.prueba.login.model.CustomerUser;
import com.prueba.login.modelExceptions.EmailExistsException;
import com.prueba.login.modelExceptions.RoleNameNotFoundException;
import com.prueba.login.modelExceptions.UnconfirmedPasswordException;
import com.prueba.login.modelExceptions.UserIdNotFoundException;
import com.prueba.login.repository.ICustomerUserRepository;
import com.prueba.login.repository.IRoleRepository;
import com.prueba.login.service.ICustomerUserService;
import com.prueba.login.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class UserService implements ICustomerUserService {

    @Autowired
    private ICustomerUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;


    /**
     * Guardar datos de Usuario de Google o Facebook
     *
     * @param oAuth2User datos de usuario OAuth2
     * @param provider  nombre del cliente : google o facebook
     * @exception RuntimeException provider null
     * @exception RoleNameNotFoundException rolename no encontrado
     * @return CustomerUser
     */
    @Override
    public CustomerUser saveOAuth2User(OAuth2User oAuth2User, String provider) {
        String socialId;
        switch (provider) {
            case "google" -> {
                socialId = "sub";
                return this.saveSocialUser(oAuth2User, provider, socialId);
            }
            case "facebook" -> {
                socialId = "id";
                return this.saveSocialUser(oAuth2User, provider, socialId);
            }
            default -> throw new RuntimeException("Usuario de Google o Facebook invalido.");
        }
    }

    /**
     * Guardar datos del formulario de registro de usuarios
     *
     * @param user datos del usuario registrado
     * @exception UnconfirmedPasswordException confirmación de password incorrecta
     * @exception EmailExistsException email ya registrado
     * @return CustomerUserDtoRes
     */
    @Override
    public CustomerUserDtoRes saveUser(CustomerUserDtoReq user) {
        this.validatePasswordAndUserEmail(user.password(), user.repeatPassword(), user.email());
        CustomerUser userDb = userRepository.save(this.convertToCustomerUserDtoReq(user));
        return this.convertToCustomerUserDtoRes(userDb);
    }

    @Override
    public CustomerUserDtoRes getUserById(Long id) {
        return this.convertToCustomerUserDtoRes(this.getEntityById(id));
    }

    @Override
    public CustomerUserDtoRes getUserAuthenticated(String email) {
        var userDb = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserIdNotFoundException("El usuario : " + email + " no se encuentra registrado"));
        return this.convertToCustomerUserDtoRes(userDb);
    }

    @Override
    public Page<CustomerUserDtoRes> getAllUsers(Pageable pageable) {
        var usersDb = userRepository.findAll(pageable);
        var usersDTO = new ArrayList<CustomerUserDtoRes>();

        for (CustomerUser user : usersDb) {
            usersDTO.add(this.convertToCustomerUserDtoRes(user));
        }
        return new PageImpl<>(usersDTO, pageable, usersDb.getTotalElements());
    }

    @Override
    public CustomerUserDtoRes updateUserById(CustomerUserDtoReq user, Long id) {
        var userDb = this.getEntityById(id);
        if (!user.email().equals(userDb.getEmail()) && userRepository.existsByEmail(user.email())) {
            throw new EmailExistsException("El Email " + user.email() + " ya se encuentra registrado");
        }
        userDb.setName(user.name() + " " + user);
        userDb.setEmail(user.email());
        userDb.setPicture(user.picture());

        return this.convertToCustomerUserDtoRes(userRepository.save(userDb));
    }

    @Override
    public void deleteUserById(Long id) {
       var userDb = this.getEntityById(id);
       userDb.setIsEnabled(false);
       userRepository.save(userDb);
    }


    private CustomerUser saveSocialUser(OAuth2User oAuth2User, String provider, String socialId) {
        var userDb = userRepository.findByEmail(oAuth2User.getAttribute("email"));

        if (userDb.isEmpty()) {
            var saveNewUser = CustomerUser.builder()
                    .email(oAuth2User.getAttribute("email"))
                    .name(oAuth2User.getAttribute("name"))
                    .picture(oAuth2User.getAttribute("picture"))
                    .provider(provider)
                    .socialId(oAuth2User.getAttribute(socialId))
                    .roles(Set.of(roleRepository.findByRoleName("USER")
                            .orElseThrow(() -> new RoleNameNotFoundException("Debe crear el rol 'USER' antes de crear un usuario."))))
                    .isEnabled(true).build();
            return userRepository.save(saveNewUser);
        } else if (userDb.get().getSocialId().isEmpty()) {
            userDb.get().setProvider(provider);
            userDb.get().setSocialId(oAuth2User.getAttribute(socialId));
            return userRepository.save(userDb.get());
        } else {
            return userDb.get();
        }
    }

    private CustomerUser getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("El id: " + id + " no se encuentra registrado."));
    }


    private CustomerUser convertToCustomerUserDtoReq(CustomerUserDtoReq user) {
        return CustomerUser.builder()
                .email(user.email())
                .password(passwordEncoder.encode(user.password()))
                .name(user.name() + " " + user.surname())
                .isEnabled(true)
                .roles(Set.of(roleRepository.findByRoleName("USER")
                        .orElseThrow(() -> new RoleNameNotFoundException("Debe crear el rol 'USER' antes de crear un usuario."))))
                .build();
    }

    private CustomerUserDtoRes convertToCustomerUserDtoRes(CustomerUser user) {
        return new CustomerUserDtoRes(user.getId(), user.getEmail(), user.getName(), user.getPicture());
    }

    private void validatePasswordAndUserEmail(String password, String repeatPassword, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException("El email " + email + " Ya se encuentra registrado. Ingrese un nuevo email.");
        }
        if (!password.equals(repeatPassword)) {
            throw new UnconfirmedPasswordException("las contraseñas ingresadas deben coincidir.");
        }
    }


}

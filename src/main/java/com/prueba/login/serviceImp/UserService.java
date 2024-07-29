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
     * Guarda datos de Usuario de Google o Facebook
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
     * Guarda datos del formulario de registro de usuarios
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

    /**
     * Muestra usuario por Id
     *
     * @param id id de usuario registrado
     * @exception UserIdNotFoundException usuario no encotrado
     * @return CustomerUserDtoRes
     */
    @Override
    public CustomerUserDtoRes getUserById(Long id) {
        return this.convertToCustomerUserDtoRes(this.getEntityById(id));
    }


    /**
     * Muestra usuario autenticado
     *
     * @param email username del usuario autenticado
     * @exception UserIdNotFoundException usuario no encotrado
     * @return CustomerUserDtoRes
     */
    @Override
    public CustomerUserDtoRes getUserAuthenticated(String email) {
        var userDb = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserIdNotFoundException("El usuario : " + email + " no se encuentra registrado"));
        return this.convertToCustomerUserDtoRes(userDb);
    }


    /**
     * Muestra todos los usuarios paginados
     *
     * @param pageable datos de paginación
     * @return Page
     */
    @Override
    public Page<CustomerUserDtoRes> getAllUsers(Pageable pageable) {
        var usersDb = userRepository.findAll(pageable);
        var usersDTO = new ArrayList<CustomerUserDtoRes>();

        for (CustomerUser user : usersDb) {
            usersDTO.add(this.convertToCustomerUserDtoRes(user));
        }
        return new PageImpl<>(usersDTO, pageable, usersDb.getTotalElements());
    }

    /**
     * Actualiza datos de usuario
     *
     * @param user datos del usuario actualizados
     * @param id id del usuario a actualizar
     * @exception EmailExistsException email ya registrado por otro usuario
     * @return CustomerUserDtoRes
     */
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

    /**
     * Elimina lógico de usuario registrado
     *
     * @param id id del usuario a eliminar
     * @exception UserIdNotFoundException id de usuario no encontrado
     */
    @Override
    public void deleteUserById(Long id) {
       var userDb = this.getEntityById(id);
       userDb.setIsEnabled(false);
       userRepository.save(userDb);
    }


    /**
     * Guarda datos de la cuenta social autenticada
     *
     * @param oAuth2User datos del usuario actualizados
     * @param provider nombre de cliente: google o facebook
     * @param socialId id de la cuenta social del usuario logueado
     * @exception RoleNameNotFoundException el rolename no se encuentra registrado
     * @return CustomerUser
     */
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

    /**
     * Muestra usuario por id
     *
     * @param id id del usuario
     * @exception UserIdNotFoundException usuario no registrado
     * @return CustomerUser
     */
    private CustomerUser getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("El id: " + id + " no se encuentra registrado."));
    }

    /**
     * Convierte un UserDTO a una Entity
     *
     * @param user datos del DTO a convertir
     * @exception RoleNameNotFoundException el rolename no se encuentra registrado
     * @return CustomerUser
     */
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

    /**
     * Convierte una Entity a un UserDTO
     *
     * @param user datos de la Entity a convertir
     * @return CustomerUserDtoRes
     */
    private CustomerUserDtoRes convertToCustomerUserDtoRes(CustomerUser user) {
        return new CustomerUserDtoRes(user.getId(), user.getEmail(), user.getName(), user.getPicture());
    }

    /**
     * Valida email y password al registrar un usuario
     *
     * @param password contraseña de usuario
     * @param repeatPassword contraseña de confirmación
     * @param email username de usuario
     * @exception EmailExistsException email no disponible: registrado por otro usuario
     * @exception UnconfirmedPasswordException las contraseñas ingresadas no coinciden
     */
    private void validatePasswordAndUserEmail(String password, String repeatPassword, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException("El email " + email + " Ya se encuentra registrado. Ingrese un nuevo email.");
        }
        if (!password.equals(repeatPassword)) {
            throw new UnconfirmedPasswordException("las contraseñas ingresadas deben coincidir.");
        }
    }


}

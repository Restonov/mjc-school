package com.epam.esm.controller;

import com.epam.esm.dto.SimpleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserFormDto;
import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.security.JwtTokenProcessor;
import com.epam.esm.service.UserService;
import com.epam.esm.util.DtoConverter;
import com.epam.esm.util.EntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.epam.esm.entity.Constants.ALL_CERTIFICATES;
import static com.epam.esm.entity.Constants.AUTH_URL;
import static com.epam.esm.entity.Constants.CREATE_ORDER;
import static com.epam.esm.entity.Constants.GENERATED_JWT;
import static com.epam.esm.entity.Constants.TOKEN_GENERATED_MESSAGE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = AUTH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final UserService userService;
    private final JwtTokenProcessor tokenProcessor;

    /**
     * Register new User & generate JWT
     *
     * @param form Form with Login and Password
     * @return User DTO, JWT in Header
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<UserDto> registerUser(@RequestBody @Valid UserFormDto form,
                                                HttpServletResponse response) {
        User createdUser = userService.create(DtoConverter.convertUserFormToEntity(form));
        response.setHeader(GENERATED_JWT, tokenProcessor.generateToken(form));
        return EntityModel.of(
                EntityConverter.convertUserToDto(createdUser),
                linkTo(methodOn(UserController.class).findUserById(createdUser.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .createOrder(createdUser.getId(), new UserOrderDto()))
                        .withRel(CREATE_ORDER)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(GiftCertificateController.class)
                        .findAll(0, 10, new PagedResourcesAssembler<>(null, null)))
                        .withRel(ALL_CERTIFICATES)
        );
    }

    /**
     * Generate new User's JWT
     *
     * @param userForm Form with Login and Password
     * @return Answer text, JWT in Header
     */
    @GetMapping(value = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleDto> generateToken(@RequestBody @Valid UserFormDto userForm) {
        return new ResponseEntity<>(
                new SimpleDto(TOKEN_GENERATED_MESSAGE),
                generateTokenHeader(userForm),
                HttpStatus.OK
        );
    }

    /**
     * Generate JWT from User's credentials
     * and wrap it in HttpHeader
     *
     * @param form Form with Login and Password
     * @return header
     */
    private HttpHeaders generateTokenHeader(UserFormDto form) {
        final String authToken = tokenProcessor.generateToken(form);
        HttpHeaders header = new HttpHeaders();
        header.add(GENERATED_JWT, authToken);
        return header;
    }
}

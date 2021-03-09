package com.epam.esm.controller;

import com.epam.esm.dto.PagedModelDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.DtoConverter;
import com.epam.esm.util.EntityConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static com.epam.esm.entity.Constants.ALL_USERS;
import static com.epam.esm.entity.Constants.USERS_URL;
import static com.epam.esm.entity.Constants.USER_ORDER;
import static com.epam.esm.entity.Constants.USER_ORDERS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * User controller
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = USERS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final UserOrderService orderService;

    /**
     * Create new Order for User
     *
     * @param userId   User id
     * @param orderDto User order
     * @return ResponseEntity<UserOrder>
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "{userId}/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@userAccessVerification.verifyLoggedUser(@userServiceImpl.findById(#userId))")
    public EntityModel<UserOrderDto> createOrder(@PathVariable long userId,
                                                 @RequestBody @Valid UserOrderDto orderDto) {
        final UserOrder order = DtoConverter.convertOrderToEntity(orderDto);
        order.setUser(new User(userId));
        final UserOrder createdOrder = orderService.create(order);
        return EntityModel.of(
                EntityConverter.convertOrderToDto(createdOrder),
                linkTo(methodOn(UserController.class)
                        .findOrder(userId, order.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .findAllOrders(userId, 0, 10, new PagedResourcesAssembler<>(null, null)))
                        .withRel(USER_ORDERS)
        );
    }

    /**
     * Find all Users
     *
     * @param page pagination page
     * @return List of Users
     */
    @GetMapping(params = {"page"})
    public PagedModelDto findAllUsers(@RequestParam @Min(0) int page,
                                      @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                      PagedResourcesAssembler<User> assembler) {
        Page<User> users = userService.findAll(page, size);
        return new PagedModelDto(
                assembler.toModel(users),
                HttpStatus.FOUND
        );
    }

    /**
     * Find User by id
     * if not found return 404 error
     *
     * @param userId User id
     * @return User
     */
    @GetMapping(value = "/{userId}")
    public EntityModel<UserDto> findUserById(@PathVariable @Min(1) long userId) {
        User user = userService.findById(userId);
        return EntityModel.of(
                EntityConverter.convertUserToDto(user),
                linkTo(methodOn(UserController.class)
                        .findOrder(userId, NumberUtils.INTEGER_ONE)).withRel(USER_ORDER),
                linkTo(methodOn(UserController.class)
                        .findAllOrders(userId, NumberUtils.INTEGER_ZERO, 10, new PagedResourcesAssembler<>(null, null)))
                        .withRel(USER_ORDERS),
                linkTo(methodOn(UserController.class)
                        .findAllUsers(NumberUtils.INTEGER_ONE, 10, new PagedResourcesAssembler<>(null, null)))
                        .withRel(ALL_USERS)
        );
    }

    /**
     * Find all User orders
     *
     * @param userId User id
     * @param page pagination page
     * @return List of User orders
     */
    @GetMapping("/{userId}/orders")
    @PreAuthorize("@userAccessVerification.verifyLoggedUser(@userServiceImpl.findById(#userId))")
    public PagedModelDto findAllOrders(@PathVariable @Min(1) long userId,
                                                       @RequestParam @Min(0) int page,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) int size,
                                                       PagedResourcesAssembler<UserOrder> assembler) {
        Page<UserOrder> orders = orderService.findUserOrders(page,size, userId);
        return new PagedModelDto(
                assembler.toModel(orders),
                HttpStatus.FOUND
        );
    }

    /**
     * Find User order
     *
     * @param userId User id
     * @param orderId Order id
     * @return User order
     */
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{userId}/orders/{orderId}")
    @PreAuthorize("@userAccessVerification.verifyLoggedUser(@userServiceImpl.findById(#userId))")
    public EntityModel<UserOrderDto> findOrder(@PathVariable @Min(1) long userId,
                                               @PathVariable @Min(1) long orderId) {
        UserOrder order = orderService.findUserOrder(userId, orderId);
        return EntityModel.of(
                EntityConverter.convertOrderToDto(order),
                linkTo(methodOn(UserController.class)
                        .findAllOrders(userId, NumberUtils.INTEGER_ZERO,
                                10, new PagedResourcesAssembler<>(null, null)))
                        .withRel(USER_ORDERS)
        );
    }
}
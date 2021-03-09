package com.epam.esm.controller;

import com.epam.esm.entity.ParameterName;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.service.UserOrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * User controller
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ParameterName.USERS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(value = "{userId}/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserOrder> createOrder(@PathVariable @Min(1) long userId,
                                                 @RequestBody UserOrder orderDto) {
        orderDto.setUser(new User(userId));
        UserOrder order = orderService.create(orderDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ParameterName.USERS_URL + "/{id}")
                .buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }

    /**
     * Find all Users
     *
     * @param page pagination page
     * @return List of Users
     */
    @GetMapping()
    public ResponseEntity<List<User>> findAll(@RequestParam(defaultValue = "1") @Min(1) int page) {
        List<User> users = userService.findAll(page);
        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    /**
     * Find User by id
     * if not found return 404 error
     *
     * @param userId User id
     * @return User
     */
    @GetMapping(value = "/{userId}")
    public EntityModel<User> findById(@PathVariable @Min(1) long userId) {
        User user = userService.findById(userId);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class)
                        .findById(userId)).withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .createOrder(userId, new UserOrder()))
                        .withRel(ParameterName.CREATE_ORDER)
                        .withType(HttpMethod.POST.name()),
                linkTo(methodOn(UserController.class)
                        .findOrder(userId, NumberUtils.INTEGER_ONE)).withRel(ParameterName.USER_ORDER),
                linkTo(methodOn(UserController.class)
                        .findAllOrders(userId, NumberUtils.INTEGER_ONE)).withRel(ParameterName.USER_ORDERS),
                linkTo(methodOn(UserController.class)
                        .findAll(NumberUtils.INTEGER_ONE)).withRel(ParameterName.ALL_USERS)
        );
    }

    /**
     * Find all User orders
     *
     * @param id User id
     * @param page pagination page
     * @return List of User orders
     */
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<UserOrder>> findAllOrders(@PathVariable @Min(1) long id,
                                                         @RequestParam @Min(1) int page) {
        List<UserOrder> orders = userService.findAllOrders(page, id);
        return new ResponseEntity<>(orders, new HttpHeaders(), HttpStatus.FOUND);
    }

    /**
     * Find User order
     *
     * @param userId User id
     * @param orderId Order id
     * @return User order
     */
    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<UserOrder> findOrder(@PathVariable @Min(1) long userId,
                                               @PathVariable @Min(1) long orderId) {
        UserOrder order = userService.findOrder(userId, orderId);
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }
}

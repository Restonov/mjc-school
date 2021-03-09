package com.epam.esm.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.epam.esm.entity.Constants.SHUTDOWN_URL;

@RestController
@RequestMapping(value = SHUTDOWN_URL)
public class ShutdownController implements ApplicationContextAware {

    private ApplicationContext context;

    @PostMapping
    public void shutdownContext() {
        ((ConfigurableApplicationContext) context).close();
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }
}

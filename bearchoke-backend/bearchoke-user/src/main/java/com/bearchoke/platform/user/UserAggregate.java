/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bearchoke.platform.user;

import com.bearchoke.platform.api.user.RoleIdentifier;
import com.bearchoke.platform.api.user.UserAuthenticatedEvent;
import com.bearchoke.platform.api.user.UserCreatedEvent;
import com.bearchoke.platform.api.user.UserIdentifier;
import com.bearchoke.platform.user.document.Role;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/13/14
 * Time: 5:10 PM
 * Responsibility:
 */
@Component
@Slf4j
public class UserAggregate extends AbstractAnnotatedAggregateRoot {
    private PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    @AggregateIdentifier
    private UserIdentifier id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public UserAggregate() {
    }

    public UserAggregate(UserIdentifier id, String username, String password, String email, String firstName, String lastName, List<String> roles) {
        apply(new UserCreatedEvent(
                id,
                username.toLowerCase(),
                passwordEncryptor.encryptPassword(password),
                email.toLowerCase(),
                firstName,
                lastName,
                roles
        ));
    }

    public boolean authenticate(String password) {
        boolean success = passwordEncryptor.checkPassword(password, this.password);
        if (success) {
            apply(new UserAuthenticatedEvent(this.id));
        }
        return success;
    }

    @EventHandler
    public void onUserCreated(UserCreatedEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Caught: " + event.getClass().getSimpleName());
        }

        this.id = event.getUserId();
        this.username = event.getUsername();
        this.password = event.getPassword();
        this.email = event.getEmail();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.roles = event.getRoles();
    }

}
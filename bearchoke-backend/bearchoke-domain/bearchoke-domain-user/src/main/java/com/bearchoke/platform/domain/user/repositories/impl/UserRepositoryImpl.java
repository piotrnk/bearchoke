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

package com.bearchoke.platform.domain.user.repositories.impl;

import com.bearchoke.platform.domain.user.document.User;
import com.bearchoke.platform.domain.user.repositories.UserRepositoryCustom;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 1/9/14
 * Time: 8:05 PM
 * Responsibility:
 */
@Log4j2
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final MongoOperations mongoOperations;
    
    @Autowired
    public UserRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public boolean isEmailUnique(String email) {
        Query q = query(where("email").is(email));

        boolean result = !mongoOperations.exists(q, User.class);

        if (log.isDebugEnabled()) {
            log.debug("Is email unique: " + email + " - " + result);
        }

        return result;
    }

    @Override
    public boolean isUsernameUnique(String username) {

        Query q = query(where("username").is(username));

        boolean result = !mongoOperations.exists(q, User.class);

        if (log.isDebugEnabled()) {
            log.debug("Is username unique: " + username + " - " + result);
        }

        return result;
    }

}

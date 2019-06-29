/*
 * Copyright (c) 2017 Otávio Santana and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 * Contributors:
 *
 * Otavio Santana
 */

package org.jnosql.artemis.demo.se;


import org.jnosql.artemis.Param;
import org.jnosql.artemis.Query;
import jakarta.nosql.mapping.Repository;

import java.util.List;

public interface PersonRepository extends Repository<Person, Long> {

    List<Person> findByName(String name);

    @Query("select * from Person where age > @age")
    List<Person> findByAge(@Param("age") Integer age);
}

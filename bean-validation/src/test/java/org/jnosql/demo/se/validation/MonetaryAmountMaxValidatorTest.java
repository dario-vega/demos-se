/*
 * Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.demo.se.validation;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonetaryAmountMaxValidatorTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Mock
    private MonetaryMax monetaryMax;

    private MonetaryAmountMaxValidator monetaryValidator;

    private ConstraintValidatorContext context;

    private MonetaryAmount money;

    @BeforeEach
    public void setup(){
        monetaryValidator = new MonetaryAmountMaxValidator();
        money = Money.of(12, Monetary.getCurrency("USD"));
    }

    @Test
    public void shouldReturnsTrueWhenIsNull() {
        Assertions.assertTrue(monetaryValidator.isValid(null, context));
    }

    @Test
    public void shouldReturnsTrueWhenIsLesserThanMaximumValue() {
        when(monetaryMax.value()).thenReturn("20.00");
        monetaryValidator.initialize(monetaryMax);
        assertTrue(monetaryValidator.isValid(money, context));
    }

    @Test
    public void shouldReturnsTrueWhenIsEqualsThanMaximumValue() {
        when(monetaryMax.value()).thenReturn("12.00");
        monetaryValidator.initialize(monetaryMax);
        assertTrue(monetaryValidator.isValid(money, context));
    }

    @Test
    public void shouldReturnsFalseWhenIsGreaterThanMinimumValue() {
        when(monetaryMax.value()).thenReturn("11.00");
        monetaryValidator.initialize(monetaryMax);
        assertFalse(monetaryValidator.isValid(money, context));
    }


    @Test
    public void shouldReturnsConstrainsWhenMonetaryIsGreaterThanMaximum() {
        MonetaryAmountValidator currency = new MonetaryAmountValidator(Money.of(20, Monetary.getCurrency("BRL")));
        Set<ConstraintViolation<MonetaryAmountValidator>> constraintViolations =
                validator.validate(currency);

        assertTrue(constraintViolations.size() == 1);
        assertEquals("{org.javamoney.midas.constraints.monetaryMax}", constraintViolations.iterator().next().getMessageTemplate());
    }

    @Test
    public void shouldReturnsEmptyConstrainsWhenMonetaryIsNull() {
        MonetaryAmountValidator currency = new MonetaryAmountValidator(null);
        Set<ConstraintViolation<MonetaryAmountValidator>> constraintViolations =
                validator.validate(currency);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void shouldReturnsEmptyConstrainsWhenMonetaryIsLesserThanMaximum() {
        MonetaryAmountValidator currency = new MonetaryAmountValidator(Money.of(10, Monetary.getCurrency("BRL")));
        Set<ConstraintViolation<MonetaryAmountValidator>> constraintViolations =
                validator.validate(currency);
        assertTrue(constraintViolations.isEmpty());
    }
    private class MonetaryAmountValidator {

        @MonetaryMax("10.12")
        private MonetaryAmount money;

        MonetaryAmountValidator(MonetaryAmount money) {
            this.money = money;
        }

    }
}
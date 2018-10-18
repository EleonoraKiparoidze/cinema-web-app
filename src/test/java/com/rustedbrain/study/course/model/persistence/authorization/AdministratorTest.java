package com.rustedbrain.study.course.model.persistence.authorization;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class AdministratorTest {

    private static Administrator administrator;

    @BeforeClass
    public static void setUpBeforeClass() {
        administrator = new Administrator();
    }

    @Test
    public void testConstructor() {
        administrator = new Administrator("login", "password", "email@email.ukr");
        assertNotEquals(null, administrator);
    }

}

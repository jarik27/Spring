package com.sda.springmvc.example.repositories;


import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.sda.springmvc.example.entities.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Locale;
import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("dev")
public class UserRepositoryTest {

    @Autowired
    private EntityManager em;

    @Resource
    private UserRepository userRepository;

    @Test
    public void entity_manager_must_not_be_null() {
        org.assertj.core.api.Assertions.assertThat(em).isNotNull();
    }

    @Test
    public void should_save_new_user() {
        User user = new User("John", "box@mail.com", "CZ");

        userRepository.save(user);

        org.assertj.core.api.Assertions.assertThat(user.getId()).isNotNull();

        System.out.println(user);
    }

    @Test(expected = Exception.class)
    public void should_not_save_two_users_with_the_same_email() {
        final String email = "box@mail.com";
        userRepository.save(new User("John", email, "CZ"));
        userRepository.save(new User("Bob", email, "US"));

        em.flush();
    }

    @Test
    public void should_find_user_by_email() {
        String email = "alice@gmail.com";

        Optional<User> maybeAlice = userRepository.findByEmail(email);

        org.assertj.core.api.Assertions.assertThat(maybeAlice).isPresent();
    }

    @Test
    @Ignore
    public void dont_do_this() {

        final FakeValuesService fakeValuesService
                = new FakeValuesService(Locale.ENGLISH, new RandomService());

        final String longName = fakeValuesService.regexify("[a-zA-Z]{256}");
        System.out.println("Long name is: " + longName);

        User user = new User(longName, "box@mail.com", "CZ");

        userRepository.save(user);

        org.assertj.core.api.Assertions.assertThat(user.getId()).isNotNull();

        System.out.println(user);

        em.flush();

        // Value too long for column """NAME"" VARCHAR(255)":
    }

}
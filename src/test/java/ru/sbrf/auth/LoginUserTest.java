package ru.sbrf.auth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import ru.sbrf.AuthInfo;
import ru.sbrf.LoginUser;
import ru.sbrf.LoginUserImpl;
import ru.sbrf.dao.springdao.SpringJdbcUsersDao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

public class LoginUserTest {
    private EmbeddedDatabase db;

    @Before
    public void setUp() {
        // creates an H2 in-memory database populated from default scripts
        // classpath:schema.sql and classpath:data.sql
        db = new EmbeddedDatabaseBuilder()
                .setType(H2)
                .generateUniqueName(true)
                .addDefaultScripts()
                .build();
    }

    private final AuthInfo authInfo = mock(AuthInfo.class);

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void testCreate() throws Exception {
        when(authInfo.getEmail()).thenReturn("Ivan@mail1.ru");
        when(authInfo.getPassword()).thenReturn("123456");
        LoginUser dao = new LoginUserImpl(new SpringJdbcUsersDao(new JdbcTemplate(db), new NamedParameterJdbcTemplate(db)));

        assertEquals(2, dao.registerNewUser(authInfo).get().longValue());

        assertEquals(Optional.empty(), dao.registerNewUser(authInfo));

        assertTrue("Верный пароль", dao.logIn(authInfo).isPresent());

        when(authInfo.getPassword()).thenReturn("123456456");
        assertFalse("Неверный пароль", dao.logIn(authInfo).isPresent());

        when(authInfo.getEmail()).thenReturn("Nick");
        assertFalse("Неверный логин", dao.logIn(authInfo).isPresent());
    }

}

package ru.sbrf;

import java.util.Optional;

public interface LoginUser {
    Optional<Long> registerNewUser(AuthInfo authInfo);
    Optional<Long> logIn(AuthInfo authInfo);
}

package ru.sbrf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.sbrf.authexceptions.AuthenticationException;
import ru.sbrf.authexceptions.UserExistsException;
import ru.sbrf.dao.springdao.SpringJdbcUsersDao;

import java.util.Optional;

@Component
public class LoginUserImpl implements LoginUser {
    private final SpringJdbcUsersDao userDao;

    @Autowired
    public LoginUserImpl(SpringJdbcUsersDao userDao) {
        this.userDao = userDao;
    }

    public Optional<Long> registerNewUser(AuthInfo authInfo) {
        try {
            if (!userDao.findByEmail(authInfo.getEmail()).isEmpty())
                throw new UserExistsException("Пользователь с таким email существует");

            Long userID = userDao.create(authInfo);
            return Optional.of(userID);
        } catch (DataIntegrityViolationException e) {
            return Optional.empty();
        } catch (UserExistsException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }


    public Optional<Long> logIn(AuthInfo authInfo) {
        try {
            Long userID = userDao.read(authInfo);
            return Optional.of(userID);
        }catch (NullPointerException e) {
            System.out.println("Неверный логин или пароль"); //Вывод в интерфейс
            return Optional.empty();
        } catch (Exception e) {
          throw new AuthenticationException(e);
        }
    }
}

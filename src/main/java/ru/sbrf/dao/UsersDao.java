package ru.sbrf.dao;

import ru.sbrf.AuthInfo;

import java.util.List;


public interface UsersDao extends GenericDao<AuthInfo, Long>{
    Long read(AuthInfo authInfo);
    Long create (AuthInfo authInfo);
    List<Long> findByEmail(String email);
}

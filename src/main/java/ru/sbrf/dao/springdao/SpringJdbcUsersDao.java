package ru.sbrf.dao.springdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ru.sbrf.AuthInfo;
import ru.sbrf.dao.UsersDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SpringJdbcUsersDao implements UsersDao {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public SpringJdbcUsersDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Long read(AuthInfo authInfo) {
        String sql = "SELECT ID FROM USERSLOGIN WHERE EMAIL LIKE ? AND PASSWORD LIKE ?";
        Long results = jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                System.out.println(authInfo.getEmail() + " " + authInfo.getPassword());
                preparedStatement.setString(1, authInfo.getEmail());
                preparedStatement.setString(2, authInfo.getPassword());

            }
        }, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    return resultSet.getLong("ID");
                }
                return null;
            }
        });
        return results;
    }

    @Override
    public Long create(AuthInfo authInfo) {
        String sql = "INSERT INTO USERSLOGIN(ID, EMAIL, PASSWORD) VALUES(USRID_SEQ.NEXTVAL, :email, :password)";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(authInfo);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return (Long) keyHolder.getKey();
    }


    @Override
    public List<Long> findByEmail(String email) {
        String sql = "SELECT ID FROM USERSLOGIN WHERE EMAIL LIKE ?";
        return jdbcTemplate.queryForList(sql, Long.class, email);
    }
}

package net.petriv.dao;

import java.sql.SQLException;
import java.util.List;

public interface GeneralDAO<T> {

    void save(T v) throws SQLException;

    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    void delete(int id) throws SQLException;

    void update (T v) throws SQLException;

}

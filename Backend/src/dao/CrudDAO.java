package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{
    boolean add(Connection con, T t) throws SQLException, ClassNotFoundException;
    boolean update(Connection con, T t) throws SQLException, ClassNotFoundException;
    boolean delete(Connection con, String id) throws SQLException, ClassNotFoundException;
    T get(Connection c, String id) throws SQLException, ClassNotFoundException;
    ArrayList<T> getAll(Connection con) throws SQLException, ClassNotFoundException;
    String getId(Connection con) throws SQLException, ClassNotFoundException;
    ArrayList<String> getAllIds(Connection con) throws SQLException, ClassNotFoundException;
}
package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{
    boolean add(Connection c, T t) throws SQLException, ClassNotFoundException;
    boolean update(Connection c, T t) throws SQLException, ClassNotFoundException;
    boolean delete(Connection c, String id) throws SQLException, ClassNotFoundException;
    T get(Connection c, String id) throws SQLException, ClassNotFoundException;
    ArrayList<T> getAll(Connection c) throws SQLException, ClassNotFoundException;
    String getId(Connection c) throws SQLException, ClassNotFoundException;
    ArrayList<String> getAllIds(Connection c) throws SQLException, ClassNotFoundException;
}

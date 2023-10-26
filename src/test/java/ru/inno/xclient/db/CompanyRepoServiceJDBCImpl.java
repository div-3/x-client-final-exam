package ru.inno.xclient.db;


import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepoServiceJDBCImpl implements CompanyRepoService {
    Connection connection;

    public CompanyRepoServiceJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<CompanyEntity> getAll() throws SQLException {
        String getAllQuery = "select * from company;";
        ResultSet resultSet = connection.createStatement().executeQuery(getAllQuery);
        return getCompanyDBEntitiesFromResultSet(resultSet);
    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive) throws SQLException {
        String getAllQuery = "select * from company where \"is_active\" = true;";
        ResultSet resultSet = connection.createStatement().executeQuery(getAllQuery);
        return getCompanyDBEntitiesFromResultSet(resultSet);
    }

    @Override
    public CompanyEntity getLast() throws SQLException {
        String getAllQuery = "select * from company order by id desc limit 1;";
        ResultSet resultSet = connection.createStatement().executeQuery(getAllQuery);
        return getCompanyDBEntitiesFromResultSet(resultSet).get(0);
    }

    @Override
    public CompanyEntity getById(int id) throws SQLException {
        String getAllQuery = "select * from company where \"id\" = " + id + ";";
        ResultSet resultSet = connection.createStatement().executeQuery(getAllQuery);
        return getCompanyDBEntitiesFromResultSet(resultSet).get(0);
    }

    @Override
    public int create(String name) throws SQLException {
        String insertQuery = "insert into company (\"name\") values (?);";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS);    //Включение возврата созданной записи
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        ResultSet createdId = preparedStatement.getGeneratedKeys();
        createdId.next();
        return createdId.getInt(1);
    }

    @Override
    public int create(String name, String description) throws SQLException {
        int count = 0;
        while (count < 10) {
            String insertQuery = "insert into company (\"name\", \"description\", \"id\") values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                    Statement.RETURN_GENERATED_KEYS);    //Включение возврата созданной записи
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            int id = getLast().getId() + 1;     //Получение id последне компании, чтобы создать после неё
            preparedStatement.setInt(3, id);
            try {
                preparedStatement.executeUpdate();
                ResultSet createdId = preparedStatement.getGeneratedKeys();
                createdId.next();
                return createdId.getInt(1);
            } catch (Exception e) {
                count++;
            }
        }
        return 0;
    }

    @Override
    public int create(CompanyEntity company) throws SQLException {
        return 0;
    }

    @Override
    public void deleteById(int id) {
        try {
            String insertQuery = "DELETE FROM company c WHERE c.id =?;";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                    Statement.RETURN_GENERATED_KEYS);    //Включение возврата созданной записи
            preparedStatement.setInt(1, id);
            int count = preparedStatement.executeUpdate();
            System.out.println("Удалена компания с id = " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean clean(String prefix) throws SQLException {
        String insertQuery = "DELETE FROM company c WHERE c.name LIKE 'TS_%';";
//        String insertQuery = "SELECT * FROM company c WHERE c.name LIKE 'TS_%';";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS);    //Включение возврата созданной записи
        int count = preparedStatement.executeUpdate();
        return false;
    }

    @Override
    public void save(CompanyEntity company) {

    }

    private static List<CompanyEntity> getCompanyDBEntitiesFromResultSet(ResultSet resultSet) throws SQLException {
        List<CompanyEntity> companies = new ArrayList<>();
        while (resultSet.next()) {
            companies.add(new CompanyEntity(
                    resultSet.getInt("id"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getTimestamp("create_timestamp"),
                    resultSet.getTimestamp("change_timestamp"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getTimestamp("deleted_at")));
        }
        return companies;
    }
}

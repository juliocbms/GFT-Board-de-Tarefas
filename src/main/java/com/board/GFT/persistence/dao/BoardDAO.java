package com.board.GFT.persistence.dao;



import com.board.GFT.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;


@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;


    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS (name) VALUES (?)";

        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        }

        return entity;
    }

    public void delete(final  Long id) throws SQLException{
        var sql = "DELETE FROM BOARDS WHERE id = ?;";
        try(var statement  = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            statement.executeUpdate();
        }
    }

    public Optional<BoardEntity> findById (Long id) throws SQLException{
        var sql = "SELECT id,name FROM BOARDS WHERE id = ?;";
        try(var statement  = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()){
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    public   boolean exists (Long id) throws SQLException{
        var sql = "SELECT 1 FROM BOARDS WHERE id = ?;";
        try(var statement  = connection.prepareStatement(sql)){
            statement.setLong(1,id);
            statement.executeQuery();
            return statement.getResultSet().next();
        }
    }
}

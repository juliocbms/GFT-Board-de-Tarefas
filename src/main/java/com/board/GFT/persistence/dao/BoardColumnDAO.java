package com.board.GFT.persistence.dao;

import com.board.GFT.persistence.entity.BoardColumnEntity;
import com.board.GFT.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnDAO {

    private  final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException{
        var sql = "INSERT INTO BOARDS_COLUMNS (name, `order`,kind, board_id) VALUES(?, ?, ?, ?);";
        try(var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++,entity.getName());
            statement.setInt(i++,entity.getOrder());
            statement.setString(i++,entity.getKind().name());
            statement.setLong(i,entity.getBoard().getId());
            statement.executeUpdate();
            var resultSet = statement.getResultSet();
            if (resultSet.next()){
                entity.setId(resultSet.getLong("id"));
            }
            return entity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException {
        return null;
    }

}

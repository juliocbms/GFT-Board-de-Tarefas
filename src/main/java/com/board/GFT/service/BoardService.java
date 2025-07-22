package com.board.GFT.service;


import com.board.GFT.persistence.dao.BoardColumnDAO;
import com.board.GFT.persistence.dao.BoardDAO;
import com.board.GFT.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public boolean delete(final Long id) throws SQLException{
        var dao = new BoardDAO(connection);
        try {
            if (!dao.exists(id)){
                return false;
            }
            dao.delete(id);
            connection.commit();
            return true;
        }catch (SQLException e){
            connection.rollback();
            throw e;
        }
    }

    public BoardEntity insert(final BoardEntity entity)throws SQLException{
        var dao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);
        try {
            dao.insert(entity);
           var columns = entity.getBoardColumns().stream().map(c -> {
            c.setBoard(entity);
            return c;
            }).toList();
           for (var column : columns){
               boardColumnDao.insert(column);
           }
            connection.commit();
        }catch (SQLException e){
            connection.rollback();
            throw e;
        }
        return entity;
    }
}

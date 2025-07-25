package com.board.GFT.persistence.dao;

import com.board.GFT.dto.CardDetailsDTO;
import com.board.GFT.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.time.CalendarUtils.toOffsetDateTime;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, board_column_id) VALUES (?, ?, ?)";
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var i = 1;
            statement.setString(i++, entity.getTitle());
            statement.setString(i++, entity.getDescription());
            statement.setLong(i++, entity.getBoardColumn().getId());

            statement.executeUpdate();

            try (var rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        }
        return entity;
    }


    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException{
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setLong(i ++, columnId);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql = """
        SELECT c.id,
               c.title,
               c.description,
               b.blocked_at,
               b.block_reason,
               c.board_column_id,
               bc.name,
               (SELECT COUNT(sub_b.id)
                   FROM BLOCKS sub_b
                  WHERE sub_b.card_id = c.id) blocks_amount
          FROM CARDS c
          LEFT JOIN BLOCKS b
            ON c.id = b.card_id
           AND b.unblocked_at IS NULL
         INNER JOIN BOARDS_COLUMNS bc
            ON bc.id = c.board_column_id
          WHERE c.id = ?;
        """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var blockReason = resultSet.getString("b.block_reason");
                    var dto = new CardDetailsDTO(
                            resultSet.getLong("c.id"),
                            resultSet.getString("c.title"),
                            resultSet.getString("c.description"),
                            blockReason != null,
                            toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
                            blockReason,
                            resultSet.getInt("blocks_amount"),
                            resultSet.getLong("c.board_column_id"),
                            resultSet.getString("bc.name")
                    );
                    return Optional.of(dto);
                }
            }
        }

        return Optional.empty();
    }

    private OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        return timestamp != null
                ? timestamp.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime()
                : null;
    }

}

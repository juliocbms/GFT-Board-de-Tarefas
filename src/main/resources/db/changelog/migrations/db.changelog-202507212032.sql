--liquibase formatted sql

--changeset julio:202507191854
--comment: cards create
CREATE TABLE CARDS(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  board_column_id BIGINT NOT NULL,
  CONSTRAINT boards_columns__cards_fk FOREIGN KEY(board_column_id) REFERENCES BOARDS_COLUMNS(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE CARDS;

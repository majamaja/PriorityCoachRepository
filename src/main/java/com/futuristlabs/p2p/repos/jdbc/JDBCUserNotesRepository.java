package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.notes.UserNotes;
import com.futuristlabs.p2p.func.notes.UserNotesRepository;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class JDBCUserNotesRepository extends JDBCRepository implements UserNotesRepository {

    @Override
    public List<UserNotes> modifiedNotes(UUID userId, DateTime modifiedSince) {
        final String sql = onlyModifiedForUser("SELECT id, user_id, note_date, title, content FROM user_notes");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new RowMapper<UserNotes>() {
            @Override
            public UserNotes mapRow(ResultSet rs, int rowNum) throws SQLException {
                final UUID id = UUID.fromString(rs.getString("id"));
                final UUID userId = UUID.fromString(rs.getString("user_id"));
                final DateTime noteDate = new DateTime(rs.getTimestamp("note_date"));
                final String title = rs.getString("title");
                final String content = rs.getString("content");

                final UserNotes note = new UserNotes();
                note.setId(id);
                note.setUserId(userId);
                note.setDateAsDate(noteDate);
                note.setTitle(title);
                note.setContent(content);
                return note;
            }
        });
    }

    @Override
    public List<UUID> deletedNotes(UUID userId, DateTime modifiedSince) {
        final String sql = deletedSinceForUser("user_notes");

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("modifiedSince", modifiedSince != null ? modifiedSince.toDate() : null);
        params.addValue("userId", userId.toString());

        return db.returnList(sql, params, new UUIDMapper());
    }

    @Override
    public void modifyNotes(UUID userId, List<UserNotes> userNotes) {
        if (userNotes == null || userNotes.isEmpty()) {
            return;
        }

        final String sql =
                " INSERT INTO user_notes (id, user_id, note_date, title, content) " +
                " VALUES (:id, :userId, :noteDate, :title, :content) " +
                " ON DUPLICATE KEY UPDATE note_date = :noteDate, title = :title, content = :content ";

        for (UserNotes userNote : userNotes) {
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("id", userNote.getId().toString());
            params.addValue("userId", userId.toString());
            params.addValue("noteDate", userNote.getDateAsDate().toDate());
            params.addValue("title", userNote.getTitle());
            params.addValue("content", userNote.getContent());

            db.update(sql, params);
        }
    }

    @Override
    public void deleteNotes(UUID userId, List<UUID> userNotes) {
        deleteFromTable("user_notes", userId, userNotes);
    }

}

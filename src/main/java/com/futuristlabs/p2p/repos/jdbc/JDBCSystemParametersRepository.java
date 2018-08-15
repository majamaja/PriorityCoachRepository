package com.futuristlabs.p2p.repos.jdbc;

import com.futuristlabs.p2p.func.cfg.SystemParam;
import com.futuristlabs.p2p.func.cfg.SystemParametersRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JDBCSystemParametersRepository extends JDBCRepository implements SystemParametersRepository {
    private String getStringParameter(final String paramName) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("key", paramName);

        return db.returnStringSafe(
                "SELECT parameter_value FROM system_parameters WHERE parameter_key = :key",
                params);
    }

    private byte[] getBlobParameter(final String paramName) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("key", paramName);

        return db.returnSafe(
                "SELECT parameter_blob FROM system_parameters WHERE parameter_key = :key",
                params, new RowMapper<byte[]>() {
                    @Override
                    public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getBytes(1);
                    }
                });
    }

    @Override
    public String getApnPassword() {
        return getStringParameter("APN Password");
    }

    @Override
    public byte[] getApnCertificate() {
        return getBlobParameter("APN Certificate");
    }

    @Override
    public boolean isApnProduction() {
        return Boolean.parseBoolean(getStringParameter("APN Is Production"));
    }

    @Override
    public void setParameter(SystemParam param) {
        final String sql = "UPDATE system_parameters SET parameter_value = :stringValue, parameter_blob = :blobValue WHERE parameter_key = :key";

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("key", param.getName());
        params.addValue("stringValue", param.getStringValue());
        params.addValue("blobValue", param.getDataValue());

        db.update(sql,  params);
    }
}

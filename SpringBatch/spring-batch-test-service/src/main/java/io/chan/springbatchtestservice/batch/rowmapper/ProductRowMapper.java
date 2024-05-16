package io.chan.springbatchtestservice.batch.rowmapper;

import io.chan.springbatchtestservice.batch.domain.ProductVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<ProductVO> {
    @Override
    public ProductVO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return ProductVO.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .type(rs.getString("type"))
                .build();
    }
}

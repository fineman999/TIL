package io.chan.springbatchtestservice.batch.job.api;

import io.chan.springbatchtestservice.batch.domain.ProductVO;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class QueryGenerator {
  public static ProductVO[] getProductList(DataSource dataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate
        .query(
            "SELECT type FROM product group by type",
            (ResultSet rs, int rowNum) -> ProductVO.builder().type(rs.getString("type")).build())
        .toArray(new ProductVO[] {});
  }

  public static Map<String, Object> getParameterValues(final String parameter, final String value) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(parameter, value);
    return parameters;
  }
}

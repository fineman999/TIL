package io.chan.bookrentalservice.setup;

import io.chan.bookrentalservice.domain.model.vo.RentalCardNo;
import io.chan.bookrentalservice.feature.RentalCardNoFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@TestComponent
public class DBInitializer {

    private static final int OFF = 0;
    private static final int ON = 1;
    private static final int COLUMN_INDEX = 1;

    private final List<String> tableNames = new ArrayList<>();

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private void findDatabaseTableNames() {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                final String tableName = resultSet.getString(COLUMN_INDEX);
                tableNames.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void truncate() {
        setForeignKeyCheck(OFF);
        for (String tableName : tableNames) {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", tableName)).executeUpdate();
        }
        setForeignKeyCheck(ON);
        insertInitialData();
    }

    private void insertInitialData() {
        final RentalCardNo rentalCardNo = RentalCardNoFixture.createRentalCardNo();

        entityManager.createNativeQuery(
                "INSERT INTO rental_card (point, id, name, rent_status,no) VALUES (0, 'id-test', '황천길', 'RENT_AVAILABLE', :no)")
                .setParameter("no", rentalCardNo.getNo())
                .executeUpdate();
    }

    private void setForeignKeyCheck(int mode) {
        entityManager.createNativeQuery(String.format("SET FOREIGN_KEY_CHECKS = %d", mode)).executeUpdate();
    }

    @Transactional
    public void clear() {
        if (tableNames.isEmpty()) {
            findDatabaseTableNames();
        }
        entityManager.clear();
        truncate();
    }
}

package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UnCheckedAppTest {


    @Test
    void unChecked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request).isInstanceOf(RuntimeException.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new UnCheckedAppTest.Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new UnCheckedAppTest.Repository();
        NetworkClient networkClient = new UnCheckedAppTest.NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }


    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call(){

            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {

        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}

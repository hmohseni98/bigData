import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class JdbcBatchInsert2 {
    public static void main(String[] args) throws IOException {

            String sqlFilePath = "C:\\Users\\ha.mohseni\\Desktop\\InternetBank\\6-transfer_destination-part-1.sql";

            ArrayBlockingQueue<String> concurrentQueue = readSqlFile(sqlFilePath);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);


            executor.submit(() -> {
                for (int i = 0; i <concurrentQueue.size(); i++) {
                    Statement statement = null;
                    try {
                        statement = DataSource.getConnection().createStatement();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        statement.execute(concurrentQueue.peek());
                        System.out.println("statement: " + i + "run with thread: " + Thread.currentThread().getName() + "connection: " + statement.getConnection().toString());
                        Thread.sleep(5000);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }

    private static ArrayBlockingQueue<String> readSqlFile(String filePath) throws IOException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1000000);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                queue.add(line);
            }
        }
        return queue;
    }
}

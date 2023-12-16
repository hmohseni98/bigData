import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class JdbcBatchInsert {
    public static void main(String[] args) {

        try (Connection connection = JdbcConnectionSingleton.getInstance()) {
            String sqlFilePath = "C:\\Users\\ha.mohseni\\Desktop\\InternetBank\\6-transfer_destination-part-5.sql";
            String sqlStatementDelimiter = ";";

            String sqlStatements = readSqlFile(sqlFilePath);
            String[] queries = sqlStatements.split(sqlStatementDelimiter);

            long start = System.currentTimeMillis();
            long counter = 0;
            Statement statement = connection.createStatement();
            for (String query : queries) {
                query = query.trim();
                if (!query.isEmpty()) {
                    counter++;
                    statement.addBatch(query);
                }
            }

            System.out.println(counter);
            int[] insertCount = statement.executeBatch();
            System.out.println("insert count " + Arrays.toString(insertCount));

            long stop = System.currentTimeMillis();

            System.out.println("time execute " + (stop - start));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String readSqlFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}

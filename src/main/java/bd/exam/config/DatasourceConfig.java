package bd.exam.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatasourceConfig {

    @Bean(name = "writeDs")
    public DataSource writeDs(
            @Value("${write.url}") String url,
            @Value("${write.user}") String user,
            @Value("${write.pass}") String pass) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        ds.setMaximumPoolSize(20);
        return ds;
    }

    @Bean(name = "readDs")
    public DataSource readDs(
            @Value("${read.url}") String url,
            @Value("${read.user}") String user,
            @Value("${read.pass}") String pass) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(pass);
        ds.setMaximumPoolSize(50);
        return ds;
    }

    @Bean(name = "writeJdbc")
    public JdbcTemplate writeJdbc(@Qualifier("writeDs") DataSource writeDs) {
        return new JdbcTemplate(writeDs);
    }

    @Bean(name = "readJdbc")
    public JdbcTemplate readJdbc(@Qualifier("readDs") DataSource readDs) {
        return new JdbcTemplate(readDs);
    }
}

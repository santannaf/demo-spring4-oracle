package santannaf

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.DatabaseMetaData
import java.sql.SQLException
import javax.sql.DataSource

@SpringBootApplication
class DemoSpring4OracleApplication

fun main(args: Array<String>) {
    runApplication<DemoSpring4OracleApplication>(*args)
}

@RestController
class TestController(
    private val jdbcClient: JdbcClient,
    val datasource: DataSource
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/dual")
    fun myDual(): Int {
        logger.info("get from dual into database oracle ...")
        try {
            datasource.connection.use { conn ->
                val meta: DatabaseMetaData = conn.metaData
                logger.info("Database product: {}", meta.databaseProductName)
                logger.info("Database version: {}", meta.databaseProductVersion)
                logger.info("JDBC driver: {}", meta.driverName)
                logger.info("JDBC driver version: {}", meta.driverVersion)
            }
        } catch (e: SQLException) {
            logger.error("Failed to retrieve database metadata", e)
        }

        return jdbcClient.sql("select 1 from dual")
            .query(Int::class.java)
            .single()
    }
}
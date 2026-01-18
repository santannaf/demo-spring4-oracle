package santannaf

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@SpringBootApplication
class DemoSpring4OracleApplication

fun main(args: Array<String>) {
    runApplication<DemoSpring4OracleApplication>(*args)
}

@RestController
class TestController(
    private val jdbcClient: JdbcClient,
    val dataSource: DataSource
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/dual")
    fun myDual(): Int {
        logger.info("myDual get single connection from database Oracle 19C ...")

        try {
            dataSource.connection.use { conn ->
                val meta = conn.metaData
                logger.info("meta: ${meta.databaseProductName}")
                logger.info("meta: ${meta.databaseProductVersion}")
                logger.info("meta: ${meta.driverName}")
                logger.info("meta: ${meta.driverVersion}")
            }

        } catch (e: Exception) {
            logger.error("myDual error: ", e)
        }

        return jdbcClient.sql("select 1 from dual")
            .query(Int::class.java)
            .single()
    }
}
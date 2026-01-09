package santannaf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class DemoSpring4OracleApplication

fun main(args: Array<String>) {
    runApplication<DemoSpring4OracleApplication>(*args)
}

@RestController
class TestController(
    private val jdbcClient: JdbcClient
) {
    @GetMapping("/dual")
    fun myDual(): Int {
        return jdbcClient.sql("select 1 from dual")
            .query(Int::class.java)
            .single()
    }
}
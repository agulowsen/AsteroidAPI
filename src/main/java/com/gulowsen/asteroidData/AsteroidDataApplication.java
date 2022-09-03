package com.gulowsen.asteroidData;

import com.gulowsen.asteroidData.utils.DBCPDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

@EnableWebMvc
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class AsteroidDataApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
		loadJDBDriver();
		SpringApplication.run(AsteroidDataApplication.class, args);
	}

	private static void loadJDBDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DBCPDataSource.getConnection();
			System.out.println("Database connection OK");
			connection.close();
		} catch (SQLException | ClassNotFoundException throwables) {
			throwables.printStackTrace();
		}
	}
}

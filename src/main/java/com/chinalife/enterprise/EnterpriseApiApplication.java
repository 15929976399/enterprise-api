package com.chinalife.enterprise;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@MapperScan(basePackages={"com.chinalife.enterprise.dao"})
@SpringBootApplication
public class EnterpriseApiApplication {

	@Autowired
	DataSource dataSource;

	public static void main(String[] args)
	{
		SpringApplication.run(EnterpriseApiApplication.class, args);
	}

	public void run(String... arg0)
			throws Exception {
		System.out.println("DATASOURCE **********++++++++++********* " + this.dataSource.getClass().getName());
		System.out.println("DATASOURCE **********++++++++++********* " + this.dataSource);
	}
}

package liuhao.memo.mybatiesplus.demo;

import liuhao.memo.mybatiesplus.demo.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("liuhao.memo.mybatiesplus.demo.mapper")
public class MybatiesPlusDemoApplication {


	public static void main(String[] args) {

		SpringApplication.run(MybatiesPlusDemoApplication.class, args);
		System.out.println(("----- selectAll method test ------"));

	}

}

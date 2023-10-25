package ru.inno.xclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.db.CompanyRepository;
import ru.inno.xclient.db.CompanyRepositorySpringImpl;
import ru.inno.xclient.db.CompanyRepositorySpring;
import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class XClientApplicationTests {

//	@Autowired
//	private CompanyRepositorySpring repo;
	@Autowired
	private CompanyRepository service;

	@Test
	@Transactional
	@Commit
	void contextLoads() throws SQLException {
//		CompanyEntity ce = repo.findAll().get(0);
//		System.out.println(ce.getId() + " name " + ce.getName());
//		System.out.println(ce.getEmployees().toString());
//		int id = service.create("Simba");
//		ce = repo.findById(id).get();
//		System.out.println("Компания: " + ce);
//		System.out.println(ce.getId() + " name " + ce.getName());
//		System.out.println(ce.getEmployees().toString());

//		System.out.println(service.getAll(false));
		System.out.println(2 + 2);

	}

	@Test
	void contextLoads2() {
		assertEquals(4, 2 + 2 + 2);
	}

}

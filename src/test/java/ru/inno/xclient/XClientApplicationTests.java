package ru.inno.xclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.db.CompanyRepoService;
import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class XClientApplicationTests {

//	@Autowired
//	private CompanyRepositorySpring repo;
@Autowired
//	private CompanyRepoService service;
private CompanyRepoService service;

@AfterEach
public void clearData() throws SQLException {
	service.clean("");
}

	@Test
	@Transactional
	@Commit
//	void contextLoads(@Autowired CompanyRepoService service) throws SQLException {
//	void contextLoads(@Autowired CompanyRepoServiceSpringImpl service) throws SQLException {
	void contextLoads() throws SQLException {
		CompanyEntity ce;
//		CompanyEntity ce = repo.findAll().get(0);
//		System.out.println(ce.getId() + " name " + ce.getName());
//		System.out.println(ce.getEmployees().toString());
		int id = service.create("");
		ce = service.getById(id);
		System.out.println("Компания: " + ce);
		System.out.println(ce.getId() + " name " + ce.getName());
//		System.out.println(ce.getEmployees().toString());

		System.out.println(service.getAll(true));
//		System.out.println(service.getAll(false));
		System.out.println(2 + 2);
		service.clean("");
	}

	@Test
	void contextLoads2() {
		assertEquals(4, 2 + 2 + 2);
	}

}

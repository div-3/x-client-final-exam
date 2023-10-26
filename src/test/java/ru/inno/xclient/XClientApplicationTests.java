package ru.inno.xclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.db.CompanyRepoService;
import ru.inno.xclient.db.EmployeeRepoService;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class XClientApplicationTests {

//	@Autowired
//	private CompanyRepositorySpring repo;
@Autowired
//	private CompanyRepoService service;
private CompanyRepoService companyRepoService;

@Autowired
private EmployeeRepoService employeeRepoService;

@AfterEach
public void clearData() throws SQLException {
//	service.clean("");
}

	@Test
//	@Transactional
//	@Commit
//	void contextLoads(@Autowired CompanyRepoService service) throws SQLException {
//	void contextLoads(@Autowired CompanyRepoServiceSpringImpl service) throws SQLException {
	void contextLoads() throws SQLException {
		CompanyEntity ce;
//		CompanyEntity ce = repo.findAll().get(0);
//		System.out.println(ce.getId() + " name " + ce.getName());
//		System.out.println(ce.getEmployees().toString());
		int id = companyRepoService.create("");
		ce = companyRepoService.getById(id);
		System.out.println("Компания: " + ce);
		System.out.println(ce.getId() + " name " + ce.getName());



//		System.out.println(ce.getEmployees().toString());


//		System.out.println(service.getAll(true));
//		System.out.println(service.getAll(false));
//		companyRepoService.deleteById(ce.getId());
//		service.clean("");
	}

	@Test
	@Transactional
	@Commit
	void contextLoads2() throws SQLException {

		System.out.println("\n--------------------------------------\n");

		int id = companyRepoService.create("");
		CompanyEntity company = companyRepoService.getById(id);

		System.out.println("\n--------------------------------------\n");
		System.out.println(company);
		System.out.println("\n--------------------------------------\n");

		List<EmployeeEntity> employees = new ArrayList<>();
		EmployeeEntity employee = employeeRepoService.create(id);

		System.out.println("\n--------------------------------------\n");
		System.out.println(employee);
		System.out.println("\n--------------------------------------\n");
//		employees.add(employee);
//		company.setEmployees(employees);
//		companyRepoService.save(company);

		employees = companyRepoService.getById(id).getEmployees();

		System.out.println("\n--------------------------------------\n");
		System.out.println("Из компании: " + employees);
		System.out.println("\n--------------------------------------\n");

		System.out.println("\n--------------------------------------\n");
		System.out.println("Из БД: " + employeeRepoService.getById(employee.getId()) + " id " + employee.getId());
		System.out.println("\n--------------------------------------\n");


//	assertEquals(4, 2 + 2 + 2);
	}

}

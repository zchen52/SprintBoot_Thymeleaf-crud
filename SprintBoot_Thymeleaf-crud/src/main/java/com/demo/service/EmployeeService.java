package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.Entity.Employee;

public interface EmployeeService {
	public List<Employee> getEmployees();
	public Employee getEmployee(long id);
	public Employee saveOrUpdate (Employee employee);
	public void deleteEmployee(Employee employee);
	Page<Employee> findPaginated(int pageNo,int pageSize,String sortField, String sortDirection);
}

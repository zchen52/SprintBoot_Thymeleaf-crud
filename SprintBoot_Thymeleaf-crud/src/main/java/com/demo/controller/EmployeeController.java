package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Entity.Employee;
import com.demo.exception.ResourceNotFoundException;
import com.demo.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/")
	public String getEmployees(Model model){
		return this.findPaginated(1, "firstname","asc",model);
	}
	
	@GetMapping("show_new_employee_form")
	public String showform(Model model) {
		Employee employee=new Employee();
		model.addAttribute("employee",employee);
		return "addEmployee";
	}
	
	@GetMapping("show_update_employee_form/{id}")
	public String showupdateform(Model model,@PathVariable("id") long id) {
		Employee employee=employeeService.getEmployee(id);
		model.addAttribute("employee",employee);
		return "update_Employee";
	}
	
	@PostMapping("employees")
	public String saveEmployee(@ModelAttribute("employee") Employee emp,Model model) {
		employeeService.saveOrUpdate(emp);
		return "redirect:/";
	}
	
	@GetMapping("delete_employee/{id}")
	public String deleteEmployee(@PathVariable("id") long id) 
	throws ResourceNotFoundException{
		
		Employee employee = employeeService.getEmployee(id);
		if(employee==null) {
			throw new ResourceNotFoundException("Employee id " + id +" not found!");
		}
		employeeService.deleteEmployee(employee);
		return "redirect:/";
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo 
			,@RequestParam("sortField") String sortField
		    ,@RequestParam("sortDir") String sortDir
			,Model model) {
	    int pageSize = 5;
	    System.out.println(sortField);
	    if(sortField.equals("firtname")) {sortField="firstname";}
	    if(sortField.equals("email")) {sortField="emailaddress";}

	    Page < Employee > page = employeeService.findPaginated(pageNo, pageSize,sortField,sortDir);
	    List < Employee > listEmployees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", listEmployees);
	    return "index";
	}
	
}

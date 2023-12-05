/*
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 * 
 */

// Write your code here
package com.example.employee.service;

import java.util.*;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRowMapper;
import com.example.employee.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeH2Service implements EmployeeRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Employee> getAllEmployees() {
        List<Employee> employeeList = db.query("SELECT * FROM employeelist", new EmployeeRowMapper());
        ArrayList<Employee> employees = new ArrayList<>(employeeList);
        return employees;
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try {
            Employee employee = db.queryForObject("SELECT * FROM employeelist WHERE id =?", new EmployeeRowMapper(),
                    employeeId);
            return employee;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Employee addEmployee(Employee employee) {
        db.update("INSERT INTO employeelist(employeeName, email, department) VALUES (?, ?, ?)",
                employee.getEmployeeName(),
                employee.getEmail(), employee.getDepartment());
        Employee savedEmployee = db.queryForObject(
                "SELECT * FROM employeelist WHERE employeeName=? and email=? and department=?",
                new EmployeeRowMapper(), employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        return savedEmployee;
    }

    @Override
    public Employee updateEmployee(int employeeId, Employee employee) {
        if (employee.getEmployeeName() != null) {
            db.update("UPDATE employeelist SET employeeName = ? WHERE id = ?", employee.getEmployeeName(), employeeId);
        }
        if (employee.getEmail() != null) {
            db.update("UPDATE employeelist SET email = ? WHERE id = ?", employee.getEmail(), employeeId);
        }
        if (employee.getDepartment() != null) {
            db.update("UPDATE employeelist SET department = ? WHERE id = ?", employee.getDepartment(), employeeId);
        }

        return getEmployeeById(employeeId);
    }

    @Override
    public void deleteEmployee(int employeeId) {
        db.update("DELETE FROM employeelist WHERE id=?", employeeId);
    }

}

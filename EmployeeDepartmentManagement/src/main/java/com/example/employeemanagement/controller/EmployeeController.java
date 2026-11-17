
package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private EmployeeResponse convertToResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName()
        );
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setSalary(request.getSalary());

        Employee savedEmployee = employeeService.createEmployee(
                employee, request.getDepartmentId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToResponse(savedEmployee));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        List<EmployeeResponse> employees =
                employeeService.getAllEmployees()
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        Employee employee = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(convertToResponse(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        Employee updatedEmployee = new Employee();

        updatedEmployee.setName(request.getName());
        updatedEmployee.setEmail(request.getEmail());
        updatedEmployee.setSalary(request.getSalary());

        Employee result = employeeService.updateEmployee(
                id, updatedEmployee, request.getDepartmentId());

        return ResponseEntity.ok(convertToResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(
            @PathVariable Long departmentId) {

        List<EmployeeResponse> employees =
                employeeService.getEmployeesByDepartment(departmentId)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/search/salary")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesBySalary(
            @RequestParam BigDecimal amount) {

        List<EmployeeResponse> employees =
                employeeService.getEmployeesBySalary(amount)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<EmployeeResponse>> searchByName(
            @RequestParam String keyword) {

        List<EmployeeResponse> employees =
                employeeService.searchEmployeesByName(keyword)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/search/salary-range")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesBySalaryRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        List<EmployeeResponse> employees =
                employeeService.getEmployeesBySalaryRange(min, max)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<EmployeeResponse>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        if (!List.of("id", "name", "email", "salary").contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeResponse> employees =
                employeeService.getEmployeesWithPagination(pageable)
                        .map(this::convertToResponse);

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/jpql/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesUsingJpql(
            @PathVariable Long departmentId) {

        List<EmployeeResponse> employees =
                employeeService.getEmployeesUsingJpql(departmentId)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/native-query")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesUsingNativeSql(
            @RequestParam BigDecimal salary) {

        List<EmployeeResponse> employees =
                employeeService.getEmployeesUsingNativeSql(salary)
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(employees);
    }
}

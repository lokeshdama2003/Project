
package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DepartmentRequest;
import com.example.employeemanagement.dto.DepartmentResponse;
import com.example.employeemanagement.dto.DepartmentDetailsResponse;
import com.example.employeemanagement.dto.EmployeeResponse;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;

import com.example.employeemanagement.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private DepartmentResponse convertToResponse(Department department) {

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getLocation()
        );
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        Department department = new Department();

        department.setName(request.getName());
        department.setLocation(request.getLocation());

        Department savedDepartment =
                departmentService.createDepartment(department);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToResponse(savedDepartment));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {

        List<DepartmentResponse> departments =
                departmentService.getAllDepartments()
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(
            @PathVariable Long id) {

        Department department =
                departmentService.getDepartmentById(id);

        return ResponseEntity.ok(convertToResponse(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {

        Department updatedDepartment = new Department();

        updatedDepartment.setName(request.getName());
        updatedDepartment.setLocation(request.getLocation());

        Department result =
                departmentService.updateDepartment(id, updatedDepartment);

        return ResponseEntity.ok(convertToResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(
            @PathVariable Long id) {

        List<EmployeeResponse> employees =
                departmentService.getEmployeesByDepartment(id)
                        .stream()
                        .map(employee -> new EmployeeResponse(
                                employee.getId(),
                                employee.getName(),
                                employee.getEmail(),
                                employee.getSalary(),
                                employee.getDepartment().getId(),
                                employee.getDepartment().getName()
                        ))
                        .toList();

        return ResponseEntity.ok(employees);
    }
    @GetMapping("/{id}/details")
    public ResponseEntity<DepartmentDetailsResponse> getDepartmentDetails(
            @PathVariable Long id) {

        Department department =
                departmentService.getDepartmentById(id);

        List<EmployeeResponse> employees =
                departmentService.getEmployeesByDepartment(id)
                        .stream()
                        .map(employee -> new EmployeeResponse(
                                employee.getId(),
                                employee.getName(),
                                employee.getEmail(),
                                employee.getSalary(),
                                employee.getDepartment().getId(),
                                employee.getDepartment().getName()
                        ))
                        .toList();

        DepartmentDetailsResponse response =
                new DepartmentDetailsResponse(
                        department.getId(),
                        department.getName(),
                        department.getLocation(),
                        employees
                );

        return ResponseEntity.ok(response);
    }
}

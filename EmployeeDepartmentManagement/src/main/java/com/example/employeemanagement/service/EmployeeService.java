
package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.exception.DuplicateResourceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }
    public Employee createEmployee(Employee employee, Long departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + departmentId));

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException(
                    "Employee email already exists: " + employee.getEmail());
        }

        employee.setId(null);
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + id));
    }

    public Employee updateEmployee(Long id,
                                   Employee updatedEmployee,
                                   Long departmentId) {

        Employee employee = getEmployeeById(id);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + departmentId));

        if (!employee.getEmail().equalsIgnoreCase(updatedEmployee.getEmail())
                && employeeRepository.existsByEmail(updatedEmployee.getEmail())) {

            throw new DuplicateResourceException(
                    "Employee email already exists: " + updatedEmployee.getEmail());
        }

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setSalary(updatedEmployee.getSalary());
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }
    public void deleteEmployee(Long id) {

        Employee employee = getEmployeeById(id);

        employeeRepository.delete(employee);
    }
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(Long departmentId) {

        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException(
                    "Department not found with ID: " + departmentId);
        }

        return employeeRepository.findByDepartment_Id(departmentId);
    }

    @Transactional(readOnly = true)
    public List<Employee> getEmployeesBySalary(BigDecimal salary) {

        return employeeRepository.findBySalaryGreaterThan(salary);
    }
    @Transactional(readOnly = true)
    public List<Employee> searchEmployeesByName(String keyword) {

        return employeeRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Transactional(readOnly = true)
    public List<Employee> getEmployeesBySalaryRange(
            BigDecimal minSalary,
            BigDecimal maxSalary) {

        if (minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException(
                    "Minimum salary cannot exceed maximum salary");
        }

        return employeeRepository.findBySalaryBetween(minSalary, maxSalary);
    }
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesWithPagination(Pageable pageable) {

        return employeeRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesUsingJpql(Long departmentId) {

        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException(
                    "Department not found with ID: " + departmentId);
        }

        return employeeRepository.getEmployeesByDepartment(departmentId);
    }
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesUsingNativeSql(BigDecimal salary) {

        return employeeRepository.findEmployeesUsingNativeQuery(salary);
    }
}

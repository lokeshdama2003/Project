
package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;

import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceConflictException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {

        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public Department createDepartment(Department department) {

        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateResourceException(
                    "Department already exists: " + department.getName());
        }

        department.setId(null);

        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {

        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + id));
    }

    public Department updateDepartment(
            Long id,
            Department updatedDepartment) {

        Department department = getDepartmentById(id);

        if (!department.getName().equalsIgnoreCase(
                updatedDepartment.getName())
                && departmentRepository.existsByName(
                updatedDepartment.getName())) {

            throw new DuplicateResourceException(
                    "Department already exists: "
                            + updatedDepartment.getName());
        }

        department.setName(updatedDepartment.getName());
        department.setLocation(updatedDepartment.getLocation());

        return departmentRepository.save(department);
    }
    public void deleteDepartment(Long id) {

        Department department = getDepartmentById(id);

        if (!employeeRepository.findByDepartment_Id(id).isEmpty()) {

            throw new ResourceConflictException(
                    "Cannot delete department because employees are assigned to it");
        }

        departmentRepository.delete(department);
    }
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(Long departmentId) {

        if (!departmentRepository.existsById(departmentId)) {

            throw new ResourceNotFoundException(
                    "Department not found with ID: " + departmentId);
        }

        return employeeRepository.findByDepartment_Id(departmentId);
    }
}


package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    List<Employee> findByDepartment_Id(Long departmentId);
    List<Employee> findBySalaryGreaterThan(BigDecimal salary);
    List<Employee> findByNameContainingIgnoreCase(String keyword);
    List<Employee> findBySalaryBetween(
            BigDecimal minSalary,
            BigDecimal maxSalary);
    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> getEmployeesByDepartment(
            @Param("departmentId") Long departmentId);
    @Query(value = "SELECT * FROM employees WHERE salary >= :salary",
            nativeQuery = true)
    List<Employee> findEmployeesUsingNativeQuery(
            @Param("salary") BigDecimal salary);
}

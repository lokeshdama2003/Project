
package com.example.employeemanagement.dto;

import java.util.List;

public class DepartmentDetailsResponse {

    private Long id;
    private String name;
    private String location;
    private List<EmployeeResponse> employees;

    public DepartmentDetailsResponse() {
    }

    public DepartmentDetailsResponse(
            Long id,
            String name,
            String location,
            List<EmployeeResponse> employees) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.employees = employees;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<EmployeeResponse> getEmployees() {
        return employees;
    }
}

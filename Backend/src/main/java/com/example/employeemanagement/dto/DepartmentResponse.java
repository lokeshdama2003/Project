
package com.example.employeemanagement.dto;

public class DepartmentResponse {

    private Long id;
    private String name;
    private String location;

    public DepartmentResponse() {
    }

    public DepartmentResponse(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
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
}

package com.example.android.testemployees.screens.employees;

import com.example.android.testemployees.pojo.Employee;

import java.util.List;

public interface EmployeesListView {
    void showData(List<Employee> employees);
    void showError();
}

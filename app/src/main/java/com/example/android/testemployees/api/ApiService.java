package com.example.android.testemployees.api;

import com.example.android.testemployees.pojo.EmployeeResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {
    @GET("testTask.json")
    Observable<EmployeeResponse> getEmployees();
}

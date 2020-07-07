package com.example.android.testemployees.screens.employees;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.testemployees.api.ApiFactory;
import com.example.android.testemployees.api.ApiService;
import com.example.android.testemployees.data.AppDatabase;
import com.example.android.testemployees.pojo.Employee;
import com.example.android.testemployees.pojo.EmployeeResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EmployeeViewModel  extends AndroidViewModel {

    private static AppDatabase db;
    private CompositeDisposable compositeDisposable;
    private LiveData<List<Employee>> employees;
    private MutableLiveData<Throwable> errors;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        employees = db.employeeDao().getAllEmployees();
        errors = new MutableLiveData<>();
    }

    public LiveData<Throwable> getErrors() {
        return errors;
    }

    public LiveData<List<Employee>> getEmployees() {
        return employees;
    }

    public void clearErrors(){
        errors.setValue(null);
    }

    @SuppressWarnings("unchecked")
    private void insertEmployees(List<Employee> employees) {
        new InsertEmployeesTask().execute(employees);

    }

    private static class InsertEmployeesTask extends AsyncTask<List<Employee>, Void, Void>{

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Employee>... lists) {
            if (lists != null && lists.length > 0 ){
                db.employeeDao().insertEmployees(lists[0]);
            }
            return null;
        }
    }

    private void deleteAllEmployees(){
        new DeleteAllEmployeesTask().execute();

    }

    private static class DeleteAllEmployeesTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            db.employeeDao().deleteAllEmployees();
            return null;
        }
    }


    public void loadData(){
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        Disposable disposable = apiService.getEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EmployeeResponse>() {
                    @Override
                    public void accept(EmployeeResponse employeeResponse) throws Exception {
                        deleteAllEmployees();
                        insertEmployees(employeeResponse.getEmployees());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errors.setValue(throwable);
                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}

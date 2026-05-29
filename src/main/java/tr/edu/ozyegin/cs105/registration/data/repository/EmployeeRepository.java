package tr.edu.ozyegin.cs105.registration.data.repository;

import tr.edu.ozyegin.cs105.registration.data.Employee;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Staff;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, String> {

    List<Professor> findAllProfessors();

    List<Staff> findAllStaff();
}

package tr.edu.ozyegin.cs105.registration.data.repository.inmemory;

import tr.edu.ozyegin.cs105.registration.data.Employee;
import tr.edu.ozyegin.cs105.registration.data.Professor;
import tr.edu.ozyegin.cs105.registration.data.Staff;
import tr.edu.ozyegin.cs105.registration.data.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final LinkedHashMap<String, Employee> byId = new LinkedHashMap<>();

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(byId.values());
    }

    @Override
    public List<Professor> findAllProfessors() {
        List<Professor> result = new ArrayList<>();
        for (Employee e : byId.values()) {
            if (e instanceof Professor p) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<Staff> findAllStaff() {
        List<Staff> result = new ArrayList<>();
        for (Employee e : byId.values()) {
            if (e instanceof Staff s) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public Optional<Employee> findById(String employeeNumber) {
        return Optional.ofNullable(byId.get(employeeNumber));
    }

    @Override
    public Employee save(Employee employee) {
        byId.put(employee.getEmployeeNumber(), employee);
        return employee;
    }

    @Override
    public boolean delete(String employeeNumber) {
        return byId.remove(employeeNumber) != null;
    }

    @Override
    public int count() {
        return byId.size();
    }
}

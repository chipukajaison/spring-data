package io.techstreet.data.controller;

import io.techstreet.data.exception.ResourceNotFoundException;
import io.techstreet.data.model.Employee;
import io.techstreet.data.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return this.employeeRepository.findAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
        throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id::"+employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId, @Valid @RequestBody Employee employeeDetails)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id::"+employeeId));
        employee.setEmail(employeeDetails.getEmail());
        employee.setFirstname(employeeDetails.getFirstname());
        employee.setLastname(employeeDetails.getLastname());

        return ResponseEntity.ok(this.employeeRepository.save(employee));
    }

    @DeleteMapping(path = "{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id::"+employeeId));
        this.employeeRepository.delete(employee);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);

        return response;
    }
}

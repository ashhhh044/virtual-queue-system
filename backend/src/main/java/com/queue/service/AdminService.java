package com.queue.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.queue.data_transfer_object_dto.AnalyticsResponse;
import com.queue.model.Customer;
import com.queue.model.Services;
import com.queue.model.ServiceHistory;
import com.queue.model.Staff;
import com.queue.repository.CustomerRepository;
import com.queue.repository.ServiceHistoryRepository;
import com.queue.repository.ServiceRepository;
import com.queue.repository.StaffRepository;

@org.springframework.stereotype.Service
public class AdminService {
    
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceHistoryRepository serviceHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(CustomerRepository customerRepository, 
                        StaffRepository staffRepository, 
                        ServiceRepository serviceRepository, 
                        ServiceHistoryRepository serviceHistoryRepository,
                        PasswordEncoder passwordEncoder){
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
        this.serviceHistoryRepository = serviceHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Service CRUD

    @Transactional
    public Services createService(String name, String description, Integer estimatedDuration){
        if(serviceRepository.findByName(name).isPresent()){
            throw new RuntimeException("Service with name: " +name+ " already exists!");
        }

        Services service = new Services();
        service.setName(name);
        service.setDescription(description);
        service.setEstimatedDuration(estimatedDuration);
        service.setIsActive(true);

        return serviceRepository.save(service);
    }

    public List<Services> getAllServices(){
        return serviceRepository.findAll();
    }

    public List<Services> getActiveServices(){
        return serviceRepository.findByIsActiveTrue();
    }

    public Services getServiceById(Long Id){
        return serviceRepository.findById(Id).orElseThrow(() -> new RuntimeException("Service with Id: "+Id +" not found!"));
    }

    @Transactional
    public Services updateService(Long Id, String name, String description, Integer estimatedDuration, Boolean isActive){

        Services service = getServiceById(Id);

        if(name != null){
            // check if new name conflicts with newer
            if(!service.getName().equals(name) && serviceRepository.findByName(name).isPresent()){
                throw new RuntimeException("Service with name: " +name+ " already exists!");
            }
            service.setName(name);
        }

        if(description != null){
            service.setDescription(description);
        }

        if(estimatedDuration != null){
            service.setEstimatedDuration(estimatedDuration);
        }

        if(isActive != null){
            service.setIsActive(isActive);
        }

        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long Id){
        Services service = getServiceById(Id);
        serviceRepository.delete(service);
    }

    // Staff CRUD

    @Transactional
    public Staff createStaff(String name, String email, String phone, String password, String employeeId, String department, Integer counterNumber){

        // check is the email and employeeId already exists!

        if(staffRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Staff with email: " +email+ " already exists!");
        }

        if(staffRepository.findByEmployeeId(employeeId).isPresent()){
            throw new RuntimeException("Staff with employee ID: " +employeeId+ " already exists!");
        }

        Staff staff = new Staff();

        staff.setName(name);
        staff.setEmail(email);
        staff.setPhone(phone);
        staff.setPassword(passwordEncoder.encode(password));
        staff.setEmployeeId(employeeId);
        staff.setDepartment(department);
        staff.setCounterNumber(counterNumber);
        staff.setRole("STAFF");

        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff(){
        return staffRepository.findAll();
    }

    public Staff getStaffById(Long Id){
        return staffRepository.findById(Id).orElseThrow(() -> new RuntimeException("Staff with Id: "+Id +" not found!"));
    }

    @Transactional
    public Staff updateStaff(Long Id, String name, String email, String phone, String password, String employeeId, String department, Integer counterNumber){

        Staff staff = getStaffById(Id);

        if(name != null){
            staff.setName(name);
        }

        if (employeeId != null && !employeeId.equals(staff.getEmployeeId())) {
            if (staffRepository.findByEmployeeId(employeeId).isPresent()) {
                throw new RuntimeException("Staff with employee ID '" + employeeId + "' already exists");
            }
            staff.setEmployeeId(employeeId);
        }

        if (email != null && !email.equals(staff.getEmail())) {
            if (staffRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Staff with email '" + email + "' already exists");
            }
            staff.setEmail(email);
        }
        
        if (phone != null) {
            staff.setPhone(phone);
        }
        
        if (password != null) {
            staff.setPassword(passwordEncoder.encode(password));
        }
        
        if (department != null) {
            staff.setDepartment(department);
        }
        
        if (counterNumber != null) {
            staff.setCounterNumber(counterNumber);
        }

        return staffRepository.save(staff);
    }

    @Transactional
    public void deleteStaff(Long Id){
        Staff staff = getStaffById(Id);
        staffRepository.delete(staff);
    }

    // Analytics

    public AnalyticsResponse getAnalytics(){
        AnalyticsResponse response = new AnalyticsResponse();

        // Total customers
        response.setTotalCustomers(customerRepository.count());
        
        // Waiting customers
        long waitingCount = customerRepository.findByStatus("waiting").size() + 
            customerRepository.findByStatus("called").size();
        response.setWaitingCustomers(waitingCount);

        // Served today
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        response.setServedToday(serviceHistoryRepository.countServedToday(todayStart));

        // Service Distribution
        List<Services> services = serviceRepository.findAll();
        for(Services service : services){
            List<ServiceHistory> histories = serviceHistoryRepository.findByServiceId(service.getId());
            Long count = histories.stream().filter(h-> "completed".equals(h.getStatus())).count();
            if(count > 0){
                response.getServiceDistribution().put(service.getName(), count);
            }
        }

        // Average waiting time for customers

        List<Customer> waitingCustomers = customerRepository.findByStatus("waiting");
        if(!waitingCustomers.isEmpty()){
            double avgEta = waitingCustomers.stream()
                            .mapToInt(c -> c.getEta() != null ? c.getEta() : 0)
                            .average()
                            .orElse(0);
            response.setAverageWaitTime(avgEta);
        }
        else {
            response.setAverageWaitTime(0.0);
        }
        
        return response;
    }
}

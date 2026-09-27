package com.queue;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.queue.model.Admin;
import com.queue.model.Services;
import com.queue.model.Staff;
import com.queue.repository.AdminRepository;
import com.queue.repository.ServiceRepository;
import com.queue.repository.StaffRepository;

// Seeds the database with a default admin account, a default staff
// account, and a couple of sample services, ONLY if they don't already
// exist. This runs every time the app starts, but is safe to re-run.
@Component
public class DataLoader implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(AdminRepository adminRepository,
                       StaffRepository staffRepository,
                       ServiceRepository serviceRepository,
                       PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Default admin account
        if (adminRepository.findByEmail("admin@queue.com").isEmpty()) {
            Admin admin = new Admin("Admin User", "admin@queue.com", "0000000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            adminRepository.save(admin);
            System.out.println("Seeded default admin -> admin@queue.com / admin123");
        }

        // Default staff account
        if (staffRepository.findByEmail("staff@queue.com").isEmpty()) {
            Staff staff = new Staff("Staff User", "staff@queue.com", "0000000000", "EMP001", "General");
            staff.setPassword(passwordEncoder.encode("staff123"));
            staffRepository.save(staff);
            System.out.println("Seeded default staff -> staff@queue.com / staff123");
        }

        // Sample services
        if (serviceRepository.findByName("General").isEmpty()) {
            serviceRepository.save(new Services("General", "General enquiries", 5));
        }
        if (serviceRepository.findByName("Billing").isEmpty()) {
            serviceRepository.save(new Services("Billing", "Billing and payments", 10));
        }
    }
}

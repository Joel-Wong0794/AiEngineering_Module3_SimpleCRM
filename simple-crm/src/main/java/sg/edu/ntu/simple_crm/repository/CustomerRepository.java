package sg.edu.ntu.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.simple_crm.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Inherits a rich set of built-in methods for standard CRUD (Create, Read,
    // Update, Delete) operations, batch processing, pagination, and sorting.

}

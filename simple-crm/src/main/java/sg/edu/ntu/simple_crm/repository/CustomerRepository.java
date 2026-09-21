package sg.edu.ntu.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.ntu.simple_crm.model.Customer;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // JPQL — uses the entity name (Customer) and field name (jobTitle)
    // find all customers by firstName
    List<Customer> findByFirstName(String firstName);

    List<Customer> findByFirstNameContaining(String text);

    @Query("SELECT c FROM Customer c WHERE c.lastName = :lastName")
    List<Customer> findByLastNameJPQL(@Param("lastName") String lastName);

    // JPQL-
    @Query("SELECT c FROM Customer c WHERE c.jobTitle = :jobTitle")
    List<Customer> findByJobTitleJPQL(@Param("jobTitle") String jobTitle);

    // Native SQL — uses the table name (customer) and column name (job_title)
    @Query(value = "SELECT * FROM customer WHERE job_title = :jobTitle", nativeQuery = true)
    List<Customer> findByJobTitleNative(@Param("jobTitle") String jobTitle);
}

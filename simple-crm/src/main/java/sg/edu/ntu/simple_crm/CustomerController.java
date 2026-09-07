package sg.edu.ntu.simple_crm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private Customer customer;

    @GetMapping("/customer")
    public Customer getCustomer() {
        customer.setId("C001");
        customer.setFirstName("Jo");
        customer.setLastName("Wong");
        customer.setEmail("jo@example.com");
        customer.setContactNo("12345678");
        customer.setJobTitle("Developer");
        customer.setYearOfBirth(1995);
        return customer;
    }
}

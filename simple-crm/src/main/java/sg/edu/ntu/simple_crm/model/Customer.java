package sg.edu.ntu.simple_crm.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customer")
// @JsonPropertyOrder({ "id", "firstName", "lastName", "email", "contactNo",
// "jobTitle", "yearOfBirth" })
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_no")
    private String contactNo;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    // Keep this constructor — it has custom logic used for preloading data
    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
package sg.edu.ntu.simple_crm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode
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
    @Pattern(regexp = "\\d{8}", message = "Contact number must be exactly 8 digits")
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
// Places this class in the same model package as the other database entities.
package sg.edu.ntu.simple_crm.model;

// Represents a date without a time or time zone, such as 2026-09-14.
import java.time.LocalDate;

// Maps a Java field to a database column.
import jakarta.persistence.Column;
// Marks this class as an entity managed by JPA and Hibernate.
import jakarta.persistence.Entity;
// Tells JPA that the database generates the primary-key value.
import jakarta.persistence.GeneratedValue;
// Provides the available strategies for generating primary keys.
import jakarta.persistence.GenerationType;
// Marks a field as the entity's primary key.
import jakarta.persistence.Id;
// Maps this entity to a specific database table.
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

// Registers Interaction as a JPA entity.
@Entity
// Stores Interaction objects in the "interaction" table.
@Table(name = "interaction")
// Defines the data and behavior of one customer interaction.
public class Interaction {

    // Marks id as the primary key.
    @Id
    // Lets the database generate each id using an identity/auto-increment column.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Maps this field to the table's "id" column.
    @Column(name = "id")
    // Holds the unique database identifier for this interaction.
    private Long id;

    // Maps this field to the table's "remarks" column.
    @Column(name = "remarks")
    // Holds notes describing what happened during the interaction.
    private String remarks;

    // Maps this field to the table's "interaction_date" column.
    @Column(name = "interaction_date")
    // Holds the calendar date on which the interaction occurred.
    private LocalDate interactionDate;

    // Provides the no-argument constructor required by JPA.
    public Interaction() {
        // JPA creates an empty object and then populates its fields.
    }

    // Returns the interaction's generated identifier.
    public Long getId() {
        // Gives the caller the current id value.
        return id;
    }

    // Replaces the interaction's identifier with the supplied value.
    public void setId(Long id) {
        // "this.id" is the field; "id" is the method parameter.
        this.id = id;
    }

    // Returns the notes recorded for the interaction.
    public String getRemarks() {
        // Gives the caller the current remarks value.
        return remarks;
    }

    // Replaces the interaction's notes with the supplied text.
    public void setRemarks(String remarks) {
        // Stores the method parameter in this object's remarks field.
        this.remarks = remarks;
    }

    // Returns the date on which the interaction occurred.
    public LocalDate getInteractionDate() {
        // Gives the caller the current interaction date.
        return interactionDate;
    }

    // Replaces the interaction date with the supplied date.
    public void setInteractionDate(LocalDate interactionDate) {
        // Stores the method parameter in this object's interactionDate field.
        this.interactionDate = interactionDate;
    }
}
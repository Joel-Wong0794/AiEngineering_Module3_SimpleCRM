# Activity: Add a Validating Service Implementation

In your `simple-crm` project.

**1.** Create `InvalidCustomerException` in the `exceptions` folder. It extends `RuntimeException` and takes a message.

**2.** Create `CustomerServiceValidationImpl` in the `service` folder. It implements `CustomerService`, same as your other implementation.

Before creating or updating a customer, check:
- first name is not empty
- last name is not empty
- email contains an `@`

If any check fails, throw `InvalidCustomerException`.

**3.** Run the application. You will get the ambiguity error again.

**4.** Use `@Qualifier` in `CustomerController` to select your new validating implementation.

**5.** Update the `createCustomer` endpoint to catch `InvalidCustomerException` and return `400 Bad Request`.

**6.** Test in Postman:

| Payload | Expected |
|---|---|
| Valid customer | `201 Created` |
| Blank first name | `400 Bad Request` |
| Email with no `@` | `400 Bad Request` |

**7.** Change the `@Qualifier` back to `customerServiceImpl`, restart, and send the invalid customer again. It saves.

---

### Test payloads

```json
{
  "firstName": "Bruce",
  "lastName": "Banner",
  "email": "bruce@avengers.com",
  "contactNo": "12345678",
  "jobTitle": "Scientist",
  "yearOfBirth": 1975
}
```

```json
{
  "firstName": "",
  "lastName": "Banner",
  "email": "bruce@avengers.com"
}
```

```json
{
  "firstName": "Bruce",
  "lastName": "Banner",
  "email": "bruce-avengers-com"
}
```

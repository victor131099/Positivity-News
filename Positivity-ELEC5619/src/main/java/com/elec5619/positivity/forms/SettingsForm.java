package com.elec5619.positivity.forms;

public class SettingsForm {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String oldPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String password) {
        this.oldPassword = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "User(FirstName: " + this.firstName + ", LastName: " + this.lastName + ", Email: " + this.email + ", Password:" + this.password + ", ConfirmPassword" + this.confirmPassword + ")";
    }

}
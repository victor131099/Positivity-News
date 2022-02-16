package com.elec5619.positivity.forms;

public class SignInForm {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Login(Username: " + this.email + ", Password: " + this.password + ")";
    }
}

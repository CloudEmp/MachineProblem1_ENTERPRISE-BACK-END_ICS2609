/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud_machineprob1;

/**
 *
 * @author Blade Emperor
 */
public class User {

    private String Email;
    private String Password;
    private String UserRole;

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setUserRole(String UserRole) {
        this.UserRole = UserRole;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getUserRole() {
        return UserRole;
    }

}

package storage;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String username;
    private String password;
    private boolean logedIn=false;

    public User(String email, String username, String password){
        this.email=email;
        this.username=username;
        this.password=password;
    }


    public String getEmail() {
        return email;
    }


    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return logedIn;
    }

    public boolean tryPassword(String pass){
        if(pass.equalsIgnoreCase(this.password)){
            System.out.println("Log in successful.");
            logedIn=true;
            return true;
        }
        System.out.println("Log in failed.");
        return false;
    }

    public void logOut(){
        System.out.println("User loged out.");
        logedIn=false;
    }

    public void setPassword(String oldPassword, String newPassword){
        if(this.password.equalsIgnoreCase(oldPassword)){
            this.password=newPassword;
            System.out.println("Password changed successfully.");
        }else{
            System.out.println("Incorrect password entered.");
        }
    }

}

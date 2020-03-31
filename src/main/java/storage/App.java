package storage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App
{
    static Scanner in = new Scanner(System.in);

    public static void main( String[] args ) {

        boolean loop = true;

        while(loop) {
          User a = null;

          menu:
          while (a == null) {

              System.out.println("What action do you want to take?\n1. Create new account.\n2. See existing users.\n3. Log in to account.\n4. Exit program.");
              String action = in.nextLine();

              switch (action) {
                  case "1":
                      createNewUser();
                      break;
                  case "2":
                      printUserInfo();
                      break;
                  case "3":
                      a = loginToUser();
                      break;
                  case "4":
                      loop=false;
                      break menu;
                  default:
                      System.out.println("Incorrect input.");
              }
          }


          while (a != null) {
              System.out.println("Chose task to perform:\n1. Create folder.\n2. Create file.\n3. Move file.\n4. Delete file.\n5. Copy file\n6. Write to file.\n7. Read file.\n8. Clear file.\n9.Change password.\n10.Log out.");
              String action = in.nextLine();
              switch (action) {
                  case "1":
                      System.out.println("Enter path of folder to create.");
                      String path = in.nextLine();
                      createFolder(path, a);
                      break;
                  case "2":
                      System.out.println("Enter path of file to create.");
                      path = in.nextLine();
                      createFile(path, a);
                      break;
                  case "3":
                      System.out.println("Enter name of file.");
                      path = in.nextLine();
                      System.out.println("Enter name of folder the file is in ending with two \\");
                      String folder1 = in.nextLine();
                      System.out.println("Enter name of folder to move file to ending with two \\");
                      String folder2 = in.nextLine();
                      moveFile(path, folder1, folder2, a);
                      break;
                  case "4":
                      System.out.println("Enter filepath to delete.");
                      path = in.nextLine();
                      deleteFile(path, a);
                      break;
                  case "5":
                      System.out.println("Enter name of file.");
                      path = in.nextLine();
                      System.out.println("Enter name of folder the file is in ending with two \\");
                      folder1 = in.nextLine();
                      System.out.println("Enter name of folder to copy file to ending with two \\");
                      folder2 = in.nextLine();
                      copyFile(path, folder1, folder2, a);
                      break;
                  case "6":
                      System.out.println("Enter filepath to write in.");
                      path = in.nextLine();
                      writeFile(path, a);
                      break;
                  case "7":
                      System.out.println("Enter filepath to read.");
                      path = in.nextLine();
                      readFile(path, a);
                      break;
                  case "8":
                      System.out.println("Enter filepath to clear.");
                      path = in.nextLine();
                      clearFile(path, a);
                      break;
                  case "9":
                      changePassword(a);
                      break;
                  case "10":
                      a.logOut();
                      a = null;
                      break;
              }
          }
      }

        in.close();
    }

    public static void createNewUser() {

        //Skapar en lista för alla användare för att jämföra användarnamn och email
        List<User> a = new ArrayList<>();
        if (Files.exists(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))) {
            try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))))) {
                a = (List<User>) fileInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        String email="invalid";
        while (email.equalsIgnoreCase("invalid")){
            System.out.println("Type your email:");

            email = in.nextLine();
            for (User person : a) {
                if (person.getEmail().equalsIgnoreCase(email)) {
                    System.out.println("Email already in use.");
                    email="invalid";
                }
            }
        }

        String username="invalid";
        while (username.equalsIgnoreCase("invalid")){
            System.out.println("Type desired username:");

            username = in.nextLine();
            for (User person : a) {
                if (person.getUsername().equalsIgnoreCase(username)) {
                    System.out.println("Username already in use.");
                    username="invalid";
                }
            }
        }

        System.out.println("Type desired password:");
        String password = in.nextLine();

         a.add(new User(email,username,password));

        try {
            Files.createDirectory(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + username));
        }catch (IOException e){
            e.printStackTrace();
        }

        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))))){
            out.writeObject(a);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printUserInfo(){
        if (Files.exists(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))) {
            try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))))) {
                List<User> a = (List<User>) fileInput.readObject();
                for (User person: a) {
                    System.out.println(person.getEmail()+" "+person.getUsername());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static User loginToUser(){
        System.out.println("Type username:");
        String username = in.nextLine();
        User person = findUser(username);

        if(person!=null){
            System.out.println("Enter password.");
            String password= in.nextLine();
            if(person.tryPassword(password)){
                return person;
            }
        }

        return null;

    }

    public static User findUser(String username){

        if (Files.exists(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))) {
            try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\ListOfUsers.ser"))))) {
                List<User> a = (List<User>) fileInput.readObject();
                for (User person:a) {
                    if(username.equalsIgnoreCase(person.getUsername())){
                        return person;
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("User not found.");
        return null;
    }

    public static void createFile(String name, User person){
        if(person.isLoggedIn()){
            try {
                Files.createFile(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+name));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("You are not logged in.");
        }
    }

    public static void createFolder(String name, User person){
        if(person.isLoggedIn()){
            try {
                Files.createDirectory(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+name));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("You are not logged in.");
        }
    }

    public static void moveFile(String nameOfFile, String inFolder, String toFolder, User person){
        if(person.isLoggedIn()){
            try {
                Files.move(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+inFolder+"\\"+nameOfFile),
                        Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+toFolder+"\\"+nameOfFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("You are not logged in.");
        }
    }

    public static void copyFile(String nameOfFile, String inFolder, String toFolder, User person){

        StringBuilder name = new StringBuilder(nameOfFile);
        int i = name.length()-4;
        name.insert(i,"-copy");

        if(person.isLoggedIn()){
            try {
                Files.copy(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+inFolder+nameOfFile),
                        Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+toFolder+name.toString()));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("You are not logged in.");
        }
    }

    public static void deleteFile(String nameOfFile, User person){
        if(person.isLoggedIn()){
            try {
                Files.delete(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername()+"\\"+nameOfFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("You are not logged in.");
        }
    }

    public static void readFile(String nameOfFile, User person){

        if(person.isLoggedIn()) {
            try (BufferedReader fileInput = new BufferedReader(new FileReader(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername() + "\\" + nameOfFile))))) {
                String st;
                while ((st = fileInput.readLine()) != null)
                    System.out.println(st);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Not logged in as a user");
        }
    }

    public static void writeFile(String nameOfFile, User person){

        if(person.isLoggedIn()) {
            try (BufferedWriter fileInput = new BufferedWriter(new FileWriter(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername() + "\\" + nameOfFile)), true))) {
                System.out.println("Write text to file.");
                fileInput.write(in.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Not logged in as a user");
        }
    }

    public static void clearFile(String nameOfFile, User person){

        if(person.isLoggedIn()) {
            try (BufferedWriter fileInput = new BufferedWriter(new FileWriter(String.valueOf(Paths.get("C:\\Users\\tobbe\\Downloads\\JavaUtilFunctionPractice\\UserFileStorage\\src\\main\\java\\storage\\Users\\" + person.getUsername() + "\\" + nameOfFile))))) {
                fileInput.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Not logged in as a user");
        }
    }

    public static void changePassword(User a){
        if(a.isLoggedIn()) {
            System.out.println("Type old password:");
            String oldPass = in.nextLine();
            System.out.println("Type new password:");
            String newPass = in.nextLine();
            a.setPassword(oldPass, newPass);
        }else{
            System.out.println("Not logged in as a user");
        }
    }

}

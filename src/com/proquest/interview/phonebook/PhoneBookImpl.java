package com.proquest.interview.phonebook;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImpl implements PhoneBook {
	
	public List <Person> people;
	private Connection cn;
	
	
	public PhoneBookImpl(){
		this.people = new ArrayList<Person>();
		this.cn = null;
	}
	
	private void getConnectionToDb(){
		if (cn==null){
			try{
				cn = DatabaseUtil.getConnection();
			}catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		return;
	}
	
	@Override
	public void addPerson(Person newPerson) {
		this.people.add( newPerson );
		System.out.println(newPerson.toString() + " added to local phonebook");
	}
	
	public String persistPerson(Person person){
		getConnectionToDb();
		
		String name =person.getName();
		
		try{
			Statement stmt = this.cn.createStatement();
			stmt.execute("INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES('" 
				+ name + "', '"
				+ person.getPhoneNumber() + "', '"
				+ person.getAddress() + "')"
				);
			
			System.out.println("Sucessfully Persited Person: " + person.toString());
		
		}catch (Exception e){
			System.out.println("ERROR PERSISTING PERSON: " + e.getMessage());
		}
		return null;
	}
	
	@Override
	public Person findPerson( String first, String last) {
		String fullName = first + " " + last;
		for (Person p : people){
			if (p.getName().equals(fullName)){
				return p;
			}
		}
		return null;
	}
	
	public void printAllPeople(){
		System.out.println("Contents of Local Phonebook:");
		for (Person p :people){
			System.out.println(p.toString());
		}
	} 
	
	public static void main(String[] args) {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database
		
		PhoneBookImpl phoneBook = new PhoneBookImpl();
		
		/*create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		*/ 
		Person person1 = new Person("John Smith", 
				"(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		Person person2 = new Person("Cynthia Smith",
				"(824) 128-8758", "875 Main St, Ann Arbor, MI");

		phoneBook.addPerson(person1);
		phoneBook.persistPerson(person1);
		phoneBook.addPerson(person2);
		phoneBook.persistPerson(person2);
		
		//print the phone book out to System.out
		phoneBook.printAllPeople();
		
		//find Cynthia Smith and print out just her entry
		Person cynthiaSmith = phoneBook.findPerson("Cynthia", "Smith");
		System.out.println( "Successfully Found: " + cynthiaSmith.toString() );
		

		//TODO insert the new person objects into the database
		//-- this happens in step 1?--    
	}
}

package application;

public class User {
	private String UserName,Password;
	
	public User(String UN,String ps) {
		this.UserName=UN;
		this.Password=ps;
	}
	public String getUserName()
	{
		return this.UserName;
	}
	public String getPassword()
	{
		return this.Password;
	}
	
}

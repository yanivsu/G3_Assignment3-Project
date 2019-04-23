package Server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimerTask;



public class TaskCloser extends TimerTask{
/**
 * This class help to make takes with timer for example if we want to lock test automatically 
 * 
 */

	private String exeCode;
	public static String livetime;
	private String startTime;
	public TaskCloser(String exeCode,String liveTime) 
	{
		this.exeCode = exeCode;
		this.livetime = liveTime;
		this.startTime = liveTime;
	}
	
	@Override
	public void run() 
	{
		System.out.println("Timer task started at:"+new Date() + "the exeCode is: " + this.exeCode);
        completeTask();
        System.out.println("Timer task finished at:"+new Date());
		
	}
/*
 * This method sleep until the test time is over and after the thread wakes he lock the test .
 */
	private void completeTask() 
	{
	        try {
	            Thread.sleep(Integer.valueOf(livetime)*60000);
	            if((!startTime.equals(Integer.parseInt(livetime)))&& ((Integer.valueOf(this.livetime)) -(Integer.valueOf(this.startTime))) > 0) 
	            {
	            	Thread.sleep(60000*((Integer.valueOf(this.livetime)) -(Integer.valueOf(this.startTime))));
	            }
	            System.out.println("the time is + " + livetime + this.startTime);
	            System.out.println("Im Here And Update the test ");
				PreparedStatement ps;
				try {
					ps = ServerMain.con.prepareStatement("UPDATE Statistic SET status = '0' WHERE executionCode = ? ");
					ps.setString(1, exeCode);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("Timer task finished ");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return;
	}
	

}

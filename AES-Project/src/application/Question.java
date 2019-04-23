package application;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A serializable class that transfers all the question data from the server to the client
 * @author Yaniv
 *	
 */
@SuppressWarnings("serial")
public class Question implements Serializable
{
	private ArrayList<String> Answers;
	private String Text;
	private String corrAns;
	private String ID;
	private String TeacherName;
	private String instructions;
	public Question(String ID,String TeacherName,String Text,String a1,String a2,String a3,String a4,int corr,String instructions) /*Constructor*/
	{
		this.ID = ID;
		this.TeacherName = TeacherName;
		Answers = new ArrayList<String>();
		Answers.add(a1);
		Answers.add(a2);
		Answers.add(a3);
		Answers.add(a4);
		this.Text = Text;/*What the question ask*/
		this.corrAns = Integer.toString(corr);/*What is the corr answer*/
		this.instructions=instructions;
	}

	public String get_text() {
		return this.Text;
	}
	public String get_a1()
	{
		return this.Answers.get(0);
	}
	public String get_a2()
	{
		return this.Answers.get(1);
	}
	public String get_a3()
	{
		return this.Answers.get(2);
	}
	public String get_a4()
	{
		return this.Answers.get(3);
	}
	
	public ArrayList<String> get_answers(){
		return this.Answers;
	}
	public String get_ID() {
		return this.ID;
	}
	public String get_corr_answer() {
		return this.corrAns;
	}
	public String get_TeacherName(){
		return this.TeacherName;
	}
	
	public void update_corr_answer(int new_ans) 
	{
		this.corrAns = this.corrAns = Integer.toString(new_ans);
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	

	

}

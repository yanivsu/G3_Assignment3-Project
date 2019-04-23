package Server;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



public class ManulTest {

private ArrayList<String> answersList;
private ArrayList<String> instructionsList;
private ArrayList<String> scoreList;
private ArrayList<String> questionsIDList;
private ArrayList<String> textList;
private String testID;
private String courseName;
private String profName;

public ManulTest(ArrayList<String> ans,ArrayList<String> inst, 
					  ArrayList<String> score, ArrayList<String> qID,
					  ArrayList<String>text,String testid,String course,String prof)
{
	this.answersList=ans;
	this.instructionsList=inst;
	this.scoreList=score;
	this.questionsIDList=qID;
	this.questionsIDList.remove(0);
	this.textList=text;
	this.testID=testid;
	this.courseName=course;
	this.profName=prof;
}
	public void CreateManulTest() throws IOException {
		// Blank Document
		@SuppressWarnings("resource")
		//String path="C:/Users/Or Asus/Desktop";
		XWPFDocument document = new XWPFDocument();
		System.out.println("IM AM  NOW CREATING MANUAL TEST");
		for(int k=0;k<this.questionsIDList.size();k++)
		{	
			System.out.println("---------------------------------------");
			System.out.println("The quesiton is:");
			System.out.println("QID: "+this.questionsIDList.get(k)+" Text :"+this.textList.get(k));
			System.out.println("Instructions: "+this.instructionsList.get(k)+" Score: "+this.scoreList.get(k));
			System.out.println("The answers are: "+this.answersList.get(k));
		}
		System.out.println("TestID: "+this.testID+" Coursename: "+this.courseName+" Profname: "+this.profName);
		// Write the Document in file system
		//String filePath="C:/Users/Or Asus/git/Project/FirstVer/ManualTests/";
		String fileName=this.testID+".docx";
		FileOutputStream out = new FileOutputStream( new File(fileName));
		/*
		---------------------------------------------
						create new folder
		File checkFile= new File("C:/Users/Or Asus/git/Project/FirstVer/luatar");
		boolean check=checkFile.exists();
		if(!check)
		{
			checkFile.mkdir();
		}*/
	
		// create Paragraph
		
		XWPFParagraph IDparagraph = document.createParagraph();
		XWPFParagraph Titleparagraph = document.createParagraph();
		XWPFRun runID = IDparagraph.createRun();
		XWPFRun runTitle = Titleparagraph.createRun();
		
		runID.setText("Student ID:");
		runID.addBreak();
		runID.addBreak();

		runTitle.setBold(true);
		runTitle.setItalic(true);
		runTitle.setFontSize(14);
		Titleparagraph.setAlignment(ParagraphAlignment.CENTER);
		runTitle.setText(this.profName+" "+this.courseName +" "+this.testID);
		runTitle.addBreak();
		runTitle.addBreak();
		
			
		for (int i = 0; i < this.questionsIDList.size(); i++) {
			
			XWPFParagraph Questionparagraph = document.createParagraph();
			XWPFParagraph Instructionparagraph= document.createParagraph();
			XWPFParagraph Answersparagraph= document.createParagraph();
			
			
			XWPFRun runQuestions = Questionparagraph.createRun();
			XWPFRun runIntructions= Instructionparagraph.createRun();
			XWPFRun runAnswers = Answersparagraph.createRun();
			
			runIntructions.setItalic(true);
			String ansArr[]=this.answersList.get(i).split("@");
			int index=0;
			runQuestions.setText((i + 1)+". Score: ("+this.scoreList.get(i)+")");
			runQuestions.addBreak();
			runQuestions.setText(this.textList.get(i)+"?");
			runIntructions.setText("Instructions: ");
			runIntructions.setText(this.instructionsList.get(i));
			
			runAnswers.setText("A."+ansArr[index]);
			index++;
			runAnswers.addBreak();
			runAnswers.setText("B. "+ansArr[index]);
			index++;
			runAnswers.addBreak();
			runAnswers.setText("C. "+ansArr[index]);
			index++;
			runAnswers.addBreak();
			runAnswers.setText("D. "+ansArr[index]);
			index=0;
			runAnswers.addBreak();
		}

		document.write(out);
		out.close();


		System.out.println("createparagraph.docx written successfully");
	}
}
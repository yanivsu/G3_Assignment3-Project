package Server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**/
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class Server extends AbstractServer {

	Connection con;
	private Statement stmt;
	private ResultSet rs;
	public static int myFlag;
	private int task_req_array_pointer = 0;
	private HashMap<String, TaskCloser> task_req_array = new HashMap();
	private Map<String, Integer> StatusCon = new HashMap();
	private ArrayList<Request> req_Array_Server = new ArrayList<Request>();
	private int[] countarray = new int[3];
	private boolean temp = true;

	public Server(int port) {
		super(port);

	}

	public Map<String, Integer> getStatusCon() {
		return StatusCon;
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		con = ServerMain.con;
		// System.out.println((String)msg + "Im in the server");

		if (msg instanceof Request) {
			System.out.println("Add to ServerArray");
			req_Array_Server.add((Request) msg);
		}
		if (msg instanceof MyFile)// For now hashmap is only for Test Question!!!!!/
		{
			MyFile file = (MyFile) msg;
			try {
				File newFile = new File(file.getFileName());
				FileOutputStream out = new FileOutputStream(newFile);
				out.write(file.getMybytearray());
				out.close();
				System.out.println("NEW FILE HAVE BEEN CREATED");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (msg instanceof ArrayList) {
			ArrayList<String> myArrayList = (ArrayList<String>) msg;
			if (myArrayList.get(0).equals("AddToQuestionToQuestionInCourseTable")) {
				PreparedStatement ps;
				for (int i = 2; i < myArrayList.size(); i++) {
					try {
						ps = con.prepareStatement("INSERT INTO QuestionInCourse VALUES(?,?)");
						ps.setString(1, myArrayList.get(1));
						ps.setString(2, myArrayList.get(i));
						ps.executeUpdate();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				try {
					client.sendToClient("QuestionInCourseUpdatedSuccesfully");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		/**
		 * if (((String) msg).equals("importQuestions")) { try { Question temp;
		 * ArrayList<Question> Question_array = new ArrayList<Question>(); stmt =
		 * con.createStatement(); rs = stmt.executeQuery("SELECT * FROM questions WHERE
		 * teacherName=?;"); while (rs.next()) {
		 * 
		 * String Temp_ID = rs.getString(1); String Temp_TeacherName = rs.getString(2);
		 * String Temp_questionText = rs.getString(3); String Temp_Answer1 =
		 * rs.getString(4); String Temp_Answer2 = rs.getString(5); String Temp_Answer3 =
		 * rs.getString(6); String Temp_Answer4 = rs.getString(7); int Temp_corrAns =
		 * rs.getInt(8);
		 * 
		 * temp = new Question(Temp_ID, Temp_TeacherName, Temp_questionText,
		 * Temp_Answer1, Temp_Answer2, Temp_Answer3, Temp_Answer4, Temp_corrAns);
		 * Question_array.add(temp); } try { client.sendToClient(Question_array);
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } Question_array.clear();/* Clear Memory
		 */
		// } catch (SQLException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		else if (((String) msg).contains("updateQuestions")) {

			PreparedStatement ps;
			try {
				String str = (String) msg;
				String[] arr = str.split("@");
				ps = con.prepareStatement("UPDATE questions SET correctAnswer=? WHERE questionID=?;");
				ps.setInt(1, Integer.parseInt(arr[1]));
				ps.setString(2, arr[2]);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("CheckLogin")) {
			PreparedStatement ps;
			ResultSet rs;
			try {

				String sql = (String) msg;
				String[] arr = sql.split("@");
				ps = con.prepareStatement("SELECT * FROM Users WHERE UserName=? AND Password =?");
				ps.setString(1, arr[1]);
				ps.setString(2, arr[2]);
				rs = ps.executeQuery();
				if (rs.next())/* If exist */
				{
					if (checkActive(arr[1])) // check if the user already in the system
						client.sendToClient("LoginSucess");
					else {
						client.sendToClient("AlreadyConncecetd");
					}

				} else
					client.sendToClient("UserDoesntExist");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("lecturerID"))//// lecturerID@308283415////
		{
			String str = (String) msg;
			String strarr[] = str.split("@");
			String LoginID = strarr[1];
			PreparedStatement ps;
			ResultSet rs;
			String GetId;
			try {
				ps = con.prepareStatement("SELECT * FROM Project.Lecturer");
				rs = ps.executeQuery();
				while (rs.next()) {
					GetId = rs.getString(1);
					if (GetId.equals(LoginID)) {
						String lectname = rs.getString(2);
						try {
							client.sendToClient("LecturerName@" + lectname);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("LogOut")) {
			String str = (String) msg;
			String strarr[] = str.split("@");
			String logoutID = strarr[1];
			StatusCon.put(logoutID, 0);
			System.out.println("The User " + logoutID + " Disconnected");

		} else if (((String) msg).contains("getQuestionCountForEachProfession")) {
			int i = 0;
			PreparedStatement ps;
			ResultSet rs;
			String str = (String) msg;
			String[] ProfessionArr = str.split("@");
			String professionID = ProfessionArr[1];
			try {
				ps = con.prepareStatement("SELECT questionID FROM questions WHERE questionID LIKE ?");
				String sqlStr = professionID + "%";
				ps.setString(1, sqlStr);
				rs = ps.executeQuery();

				while (rs.next()) {
					String id = rs.getString(1).substring(2, 5);
					System.out.println("i=" + i + ", rs=" + id);
					if (Integer.parseInt(id) > (i + 1))
						break;
					i++;
				}
				i++;
				client.sendToClient("TotalCount@" + i);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("AddNewQuestion")) {
			PreparedStatement ps;
			ResultSet rs;
			String str = (String) msg;
			String[] Questions_String = str.split("@");

			try {
				ps = con.prepareStatement("INSERT INTO questions VALUES(?,?,?,?,?,?,?,?,?)");
				ps.setString(1, Questions_String[1]);
				ps.setString(2, Questions_String[2]);
				ps.setString(3, Questions_String[3]);
				ps.setString(4, Questions_String[4]);
				ps.setString(5, Questions_String[5]);
				ps.setString(6, Questions_String[6]);
				ps.setString(7, Questions_String[7]);
				ps.setString(8, Questions_String[8]);
				ps.setString(9, Questions_String[9]);
				ps.executeUpdate();
				System.out.println("Question:#" + Questions_String[1] + " has been uploaded to the DB");
				client.sendToClient("uploadedQuestionSuccesfully");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (((String) msg).contains("Status")) {
			PreparedStatement ps;
			ResultSet rs;
			String str = (String) msg;
			String[] Status = str.split("@");
			String userId = Status[1];
			String getID;
			String statusfromsql = null;
			try {
				ps = con.prepareStatement("SELECT * FROM Project.Users");
				rs = ps.executeQuery();
				while (rs.next()) {
					getID = rs.getString(1);
					if (getID.equals(userId)) {
						statusfromsql = rs.getString(3);
						System.out.println(statusfromsql);
					}
				}
				client.sendToClient("CheckStatus@" + statusfromsql);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (((String) msg).contains("proffesions")) {
			PreparedStatement ps;
			ResultSet rs;
			try {

				String sql = (String) msg;
				String[] arr = sql.split("@");
				ps = con.prepareStatement("SELECT ProffName FROM LecturerInCourse WHERE LecID=?");// select all the //
																									// given lecturer Id
				ps.setString(1, arr[1]);
				rs = ps.executeQuery();
				if (rs.next() == false) {
					client.sendToClient(null);

				} else {
					ArrayList<String> proff = new ArrayList<String>(); // list of professions
					proff.add("professions");

					while (temp == true)/* If exist */
					{
						if (!proff.contains(rs.getString(1)))
							proff.add(rs.getString(1)); // add all this lecturer professions to proff array list
						temp = rs.next();
					}
					temp = true;

					client.sendToClient(proff); // send back to client
				}
				client.sendToClient(null); // send back to client

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("Initialize")) {
			PreparedStatement ps;
			ResultSet rs;

			try {

				String sql = (String) msg;
				String[] arr = sql.split("@");
				ps = con.prepareStatement(
						"SELECT DISTINCT ProffName , ProffID FROM Project.LecturerInCourse WHERE LecID=?");// select all
				// the
				// professions for
				int i = 1; // given lecturer Id
				ps.setString(1, arr[1]);
				rs = ps.executeQuery();
				ArrayList<String> proff = new ArrayList<String>(); // list of professions
				proff.add("proffinitializelist");
				while (rs.next()) {
					proff.add(rs.getString(2) + "-" + rs.getString(1));
					System.out.println(proff.get(i));
				}
				client.sendToClient(proff); // send back to client

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("courses")) {
			PreparedStatement ps;
			ResultSet rs;
			try {

				String sql = (String) msg;
				String[] arr = sql.split("@");
				ps = con.prepareStatement("SELECT CourseName FROM LecturerInCourse WHERE LecID=? AND ProffName=?");// select
																													// //
																													// and
				// proff
				ps.setString(1, arr[1]);
				ps.setString(2, arr[2]);
				rs = ps.executeQuery();
				ArrayList<String> courses = new ArrayList<String>(); // list of courses
				courses.add("courses");
				while (rs.next())/* If exist */
				{
					if (!courses.contains(rs.getString(1)))
						courses.add(rs.getString(1)); // add all this lecturer courses to courses array list according
														// to the user prof choice

				}
				client.sendToClient(courses); // send back to client
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("questionByCourse")) {
			PreparedStatement ps;
			ResultSet rs;
			try {
				Question temp;
				ArrayList<Question> Question_array = new ArrayList<Question>();

				String sql = (String) msg;
				String[] arr = sql.split("@");
				/*
				 * Select questionText FROM QuestionInCourse S, questions X WHERE S.Course
				 * ='Algebra 2' AND S.questionID = X.questionID;
				 */
				ps = con.prepareStatement(
						"Select distinct questionText,S.questionID FROM QuestionInCourse S, questions X WHERE S.Course =? AND S.questionID = X.questionID;");// select
				// given // course
				ps.setString(1, arr[1]); // course name
				rs = ps.executeQuery();

				if (!rs.next()) {
					client.sendToClient(null);
				} else {
					rs.previous();
					while (rs.next())/* If exist */
					{
						String Temp_ID = rs.getString(2);
						String Temp_questionText = rs.getString(1);

						temp = new Question(Temp_ID, null, Temp_questionText, null, null, null, null, 1, null);
						Question_array.add(temp);
					}

					client.sendToClient(Question_array); // send back to client
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("GetCourses")) {
			PreparedStatement ps;
			ResultSet rs;
			ArrayList<String> courseNameList = new ArrayList<String>();
			courseNameList.add("CourseList");
			String str = (String) msg;
			String[] strArr = str.split("@");
			String profName = strArr[1];
			// System.out.println(profName);

			try {
				ps = con.prepareStatement("SELECT Coursename FROM CourseInProfession WHERE proffName=?");
				ps.setString(1, profName); // profession name // course name
				rs = ps.executeQuery();
				while (rs.next()) {
					courseNameList.add(rs.getString(1));
				}

				client.sendToClient(courseNameList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("GetTestsID")) {
			PreparedStatement ps;
			ResultSet rs;

			String sql = (String) msg;
			String[] arr = sql.split("@");// prof=1 course=2//
			ArrayList<String> profCourseID = new ArrayList<String>();
			ArrayList<String> testsList = new ArrayList<String>();
			testsList.add("ProfandCourseID");

			try {

				int i = 0;
				ps = con.prepareStatement(
						"SELECT DISTINCT ProffId,idCourse FROM Project.CourseInProfession WHERE ProffName=? AND Coursename=?");
				ps.setString(1, arr[1]);
				ps.setString(2, arr[2]);
				rs = ps.executeQuery();
				while (rs.next()) {
					profCourseID.add(rs.getString(1) + rs.getString(2));

				}

				ps = con.prepareStatement("SELECT distinct TestID FROM Project.Test WHERE TestID LIKE ?");
				String sqlStr = profCourseID.get(0) + "%";
				ps.setString(1, sqlStr);
				rs = ps.executeQuery();
				while (rs.next()) {
					testsList.add(rs.getString(1));
					System.out.println(testsList.get(i));
					i++;
				}
				client.sendToClient(testsList);

			}

			catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("addTest")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String[] arr = sql.split("@");
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i]);
			}
			try {

				ps = con.prepareStatement(
						"INSERT INTO Test (TestID,questionID,QuestionScore,teachername,durationTIme,studentText,lecturerText) VALUE(?,?,?,?,?,?,?)");
				ps.setString(1, arr[1]);
				ps.setString(2, arr[2]);
				ps.setString(3, arr[3]);
				ps.setString(4, arr[4]);
				ps.setString(5, arr[5]);
				ps.setString(6, arr[6]);
				if (arr[6].equals("null")) {
					ps.setNull(6, java.sql.Types.VARCHAR);

				} else {
					ps.setString(6, arr[6]);
				}
				if (arr[7].equals("null")) {
					ps.setNull(7, java.sql.Types.VARCHAR);

				} else {
					ps.setString(7, arr[7]);
				}

				/*
				 * if (!arr[8].isEmpty()) {
				 * 
				 * } else { ps.setString(8, " "); } if (!arr[9].isEmpty()) {
				 * 
				 * } else { ps.setString(9, " "); }
				 */

				ps.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				client.sendToClient(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else if (((String) msg).contains("idByName")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String testID = "";
			String[] arr = sql.split("@");
			try {
				ps = con.prepareStatement("SELECT idProffesion FROM Project.Proffesion WHERE ProffName=?");
				ps.setString(1, arr[1]); // prof name
				rs = ps.executeQuery();
				rs.next();
				testID = rs.getString(1);
				ps = con.prepareStatement("SELECT idCourse FROM CourseInProfession WHERE Coursename=?");
				ps.setString(1, arr[2]); // course name
				rs = ps.executeQuery();
				rs.next();
				testID = testID + "@" + rs.getString(1);
				// System.out.println(testID);
				client.sendToClient("idCoursePro@" + testID);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("testCount")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			System.out.println("meow meow meow");
			String[] arr = sql.split("@");
			int i = 0;
			try {
				ps = con.prepareStatement("SELECT distinct TestID FROM Test  WHERE TestID LIKE ?");
				String sqlStr = arr[1] + arr[2] + "%";
				ps.setString(1, sqlStr);
				rs = ps.executeQuery();
				while (rs.next()) {

					String id = rs.getString(1).substring(4, 6);
					System.out.println(id);
					int countTest = Integer.parseInt(id);
					if (countTest > (i + 1)) {
						break;
					}
					i++;
				}
				System.out.println("I IS :" + i);
				i++;
				System.out.println("blablalbla" + i);
				client.sendToClient("testCount@" + i);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("isExist")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String testID = "";
			String[] arr = sql.split("@");
			try {
				ps = con.prepareStatement("SELECT TestID FROM Statistic WHERE executionCode=?");
				ps.setString(1, arr[1]);
				rs = ps.executeQuery();
				if (rs.next()) {
					client.sendToClient("isexist@true");
				} else
					client.sendToClient("isexist@false");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("isExeCodeMatch")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String[] arr = sql.split("@");
			try {
				ps = con.prepareStatement("SELECT testType FROM Statistic WHERE executionCode=? AND testID=?");
				ps.setString(1, arr[1]);
				ps.setString(2, arr[2]);
				rs = ps.executeQuery();
				if (rs.next()) {
					client.sendToClient("isexist@true");
				} else
					client.sendToClient("isexist@false");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("Updateteststatus")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String[] strArr = sql.split("@");// str[0]=statemane,str[1]=active/notactive, str[2]=manul/comp,
												// str[3]=testID//
			int flag = Integer.parseInt(strArr[1]);
			System.out.println("THE FLAGE IS: " + flag);
			try {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();

				DateFormat dateFormattime = new SimpleDateFormat("HH:mm");
				Date datetime = new Date();
				ps = con.prepareStatement(
						"INSERT INTO Statistic (testID, date, executionCode,actualTime ,testType, status, amountOfStudent,time)"
								+ "VALUES (?,?,?,?,?,?,?,?)");
				ps.setString(1, strArr[3]);
				ps.setString(2, dateFormat.format(date));
				ps.setString(3, strArr[4]);
				ps.setString(4, strArr[5]);
				ps.setString(5, strArr[2]);
				ps.setString(6, "1");
				ps.setString(7, "0");
				ps.setString(8, dateFormattime.format(datetime));
				ps.executeUpdate();
				client.sendToClient("changeteststatus");
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("ShowTestDurationTime")) {

			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String statusAndDuration = "";
			String[] strArr = sql.split("@");// strArr[0]=statment, strArr[1]=testID/
			try {
				ps = con.prepareStatement("SELECT DISTINCT durationtime FROM Test WHERE TestID=?");
				ps.setString(1, strArr[1]);
				// ps.setString(2, strArr[2]);
				rs = ps.executeQuery();
				while (rs.next()) {
					statusAndDuration = "DurationOfTest@" + rs.getString(1);

				}
				client.sendToClient(statusAndDuration);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		/* Hai */
		else if (((String) msg).contains("getAllQuestionsPerLecturer")) {
			try {

				String[] arr = ((String) msg).split("@");
				ArrayList<QuestionTable> Question_array = new ArrayList<QuestionTable>();
				PreparedStatement ps;
				ResultSet rs;
				ps = con.prepareStatement("SELECT * FROM questions WHERE teacherName=?");
				ps.setString(1, arr[1]);
				rs = ps.executeQuery();
				if (rs.next()) {
					
					do {
						String questionID = rs.getString(1);
						String questionText = rs.getString(3);
						String ans1 = rs.getString(4);
						String ans2 = rs.getString(5);
						String ans3 = rs.getString(6);
						String ans4 = rs.getString(7);
						int correctAnswer = rs.getInt(8);
						String instructions = rs.getString(9);
						QuestionTable tempQuestion = new QuestionTable(questionID, questionText, ans1, ans2, ans3, ans4,
								correctAnswer, instructions, null, null);
						Question_array.add(tempQuestion);
					}while(rs.next());
				
				}
				else
					client.sendToClient(null);
				try {
					client.sendToClient(Question_array);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Question_array.clear();/* Clear Memory */
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (((String) msg).contains("deleteSelectedQuestion")) {
			try {
				String[] arr = ((String) msg).split("@");
				PreparedStatement ps;
				ps = con.prepareStatement(
						"DELETE a.*, b.*  FROM Project.questions a LEFT JOIN Project.QuestionInCourse b ON b.questionID = a.questionID  WHERE a.questionID = ? ");
				ps.setString(1, arr[1]);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllCoursesByQuestionCode")) {
			String[] arr = ((String) msg).split("@");
			ArrayList<String> coursesArr = new ArrayList<String>();
			PreparedStatement ps;
			ResultSet rs;
			try {
				ps = con.prepareStatement("SELECT Course FROM QuestionInCourse WHERE questionID=? ");

				ps.setString(1, arr[1]);

				rs = ps.executeQuery();
				coursesArr.add("AllCoursesByQuestionCodeArray");
				while (rs.next())
					coursesArr.add(rs.getString(1));
				for (int i = 0; i < coursesArr.size(); i++)
					System.out.println(coursesArr.get(i));
				client.sendToClient(coursesArr);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("TestCombobox")) {
			PreparedStatement ps;
			ResultSet rs;
			String sql = (String) msg;
			String[] strArr = sql.split("@");
			ArrayList<String> activeCourses = new ArrayList<String>();
			activeCourses.add("activeCourses");
			try {
				ps = con.prepareStatement(
						"SELECT distinct S.CourseName,STC.testID,STC.testType FROM CourseInProfession C,Project.Statistic STC, Project.StudentInCourse S WHERE S.StudentID= ? AND S.CourseName=C.Coursename AND STC.status = '1' AND substring(STC.testID,1,4) = C.CourseInProfID AND STC.testID NOT IN (SELECT R.Testid FROM Project.TestResults R )");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					activeCourses.add(rs.getString(1) + " (id: " + rs.getString(2) + " " + rs.getString(3) + ")");
				}

				client.sendToClient(activeCourses);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("GetQuestionForTest")) {
			PreparedStatement ps;
			ResultSet rs;
			Question temp_Question;
			String sql = (String) msg;
			String[] strArr = sql.split("@");
			try {
				HashMap<Question, String> Question_array = new HashMap<Question, String>();

				ps = con.prepareStatement(
						"SELECT Q.*,T.QuestionScore FROM Project.Test T , Project.questions Q  Where T.TestID = ? And Q.questionID = T.questionID ;");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					String Temp_ID = rs.getString(1);
					String Temp_TeacherName = rs.getString(2);
					String Temp_questionText = rs.getString(3);
					String Temp_Answer1 = rs.getString(4);
					String Temp_Answer2 = rs.getString(5);
					String Temp_Answer3 = rs.getString(6);
					String Temp_Answer4 = rs.getString(7);
					int Temp_corrAns = rs.getInt(8);
					String instraction = rs.getString(9);
					String Score = rs.getString(10);
					temp_Question = new Question(Temp_ID, Temp_TeacherName, Temp_questionText, Temp_Answer1,
							Temp_Answer2, Temp_Answer3, Temp_Answer4, Temp_corrAns, instraction);
					Question_array.put(temp_Question, Score);
				}
				try {
					client.sendToClient(Question_array);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("UpdateQuestionsTableAfterEdit")) {
			String[] arr = ((String) msg).split("@");
			PreparedStatement ps;
			try {
				ps = con.prepareStatement(
						"UPDATE questions SET questionText=?,answer1=?,answer2=?,answer3=?,answer4=?,correctAnswer=?,instructions=? WHERE questionID=?");
				ps.setString(8, arr[1]);
				ps.setString(1, arr[2]);
				ps.setString(2, arr[3]);
				ps.setString(3, arr[4]);
				ps.setString(4, arr[5]);
				ps.setString(5, arr[6]);
				ps.setString(6, arr[7]);
				ps.setString(7, arr[8]);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("DeleteQuestionInCourseAfterEdit")) {
			String[] arr = ((String) msg).split("@");
			PreparedStatement ps;
			try {
				ps = con.prepareStatement("DELETE FROM QuestionInCourse WHERE questionID=?");
				ps.setString(1, arr[1]);
				ps.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (((String) msg).contains("GetDurationTimeForTest")) {
			ResultSet rs;
			PreparedStatement ps;
			String timedur = "GetTestDuration@";
			String sql = (String) msg;
			String[] strArr = sql.split("@");
			try {
				ps = con.prepareStatement("Select distinct T.durationTIme From Test T Where T.TestID = ?");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					timedur += rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				client.sendToClient(timedur);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("AddTestResults")) {
			ResultSet rs;
			PreparedStatement ps;
			String sql = (String) msg;
			String[] strArr = sql.split("@");
			int j = 0;
			String ans = "", status = "";

			try {
				for (int i = 0; i < Integer.parseInt(strArr[3]); i++) {
					ps = con.prepareStatement("INSERT INTO TestResults VALUES (?,?,?,?,?,?)");
					ps.setString(1, strArr[4]);
					ps.setString(2, strArr[5]);
					ps.setString(3, strArr[j + 6]);
					ps.setInt(4, Integer.parseInt(strArr[j + 7]));
					ans += strArr[j + 7] + ",";
					ps.setString(5, strArr[j + 8]);
					ps.setString(6, strArr[1]); // exeCode
					j += 3;
					ps.executeUpdate();
				}

				ps = con.prepareStatement(
						"SELECT studentID FROM Grades WHERE answers=? and testID=? and executionCode=?");
				ps.setString(1, ans);
				ps.setString(2, strArr[4]);
				ps.setString(3, strArr[1]);
				
				rs = ps.executeQuery();
				if (rs.next()) {
					status += "Suspicion of copying from students id: ";
					do {
						status += rs.getString(1) + ", ";
					} while (rs.next());
					ps = con.prepareStatement("INSERT INTO Grades VALUES (?,?,?,?,?,?,?,?)");
					ps.setString(1, strArr[4]);
					ps.setString(2, strArr[5]);
					ps.setString(3, strArr[strArr.length - 1]);
					ps.setString(4, ans);
					ps.setString(5, status);
					ps.setString(6, "-----");
					ps.setString(7, strArr[1]);
					ps.setString(8, strArr[2]); // testTime
					ps.executeUpdate();

				} else {
					status = "waiting for approval";
					ps = con.prepareStatement("INSERT INTO Grades VALUES (?,?,?,?,?,?,?,?)");
					ps.setString(1, strArr[4]);
					ps.setString(2, strArr[5]);
					ps.setString(3, strArr[strArr.length - 1]);
					ps.setString(4, ans);
					ps.setString(5, status);
					ps.setString(6, "-----");
					ps.setString(7, strArr[1]);
					ps.setString(8, strArr[2]); // testTime
					ps.executeUpdate();

				}

				client.sendToClient(null); // send back to client

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("CreateManualTest")) {

			ArrayList<String> ansArrayList = new ArrayList<String>();
			ArrayList<String> insArrayList = new ArrayList<String>();
			ArrayList<String> scoreArrayList = new ArrayList<String>();
			ArrayList<String> questiondIDArrList = new ArrayList<String>();
			ArrayList<String> txtArrayList = new ArrayList<String>();
			String str = (String) msg;
			String[] strArr = str.split("@");
			String profName = "";
			String courseName = "";
			int k = 1;
			PreparedStatement ps;
			ResultSet rs;
			try {
				questiondIDArrList.add("QuestionsToManulTest");
				ps = con.prepareStatement("Select distinct questionID from Test where TestID=?");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					questiondIDArrList.add(rs.getString(1));
					System.out.println("QuestionID " + questiondIDArrList.get(k));
					k++;
				}
				ps = con.prepareStatement("Select questionText from questions where questionID=?");
				System.out.println(questiondIDArrList.size());
				for (int i = 1; i < questiondIDArrList.size(); i++) {
					System.out.println(questiondIDArrList.get(i));
					ps.setString(1, questiondIDArrList.get(i));
					rs = ps.executeQuery();
					if (rs.next()) {
						txtArrayList.add(rs.getString(1));
						System.out.println(txtArrayList.get(i - 1));
						System.out
								.println("TEXT Question: " + questiondIDArrList.get(i) + " " + txtArrayList.get(i - 1));
					}
				}
				System.out.println("got all texf questions,send all of them to client");
				ps = con.prepareStatement("Select questionScore from Test where questionID=?");
				for (int i = 1; i < questiondIDArrList.size(); i++) {
					ps.setString(1, questiondIDArrList.get(i));
					rs = ps.executeQuery();
					if (rs.next()) {
						scoreArrayList.add(rs.getString(1));
						System.out.println("Score Question" + i + " " + scoreArrayList.get(i - 1));
					}
				}
				System.out.println("got all score for all questions");
				ps = con.prepareStatement("Select instructions from questions where questionID=?");
				for (int i = 1; i < questiondIDArrList.size(); i++) {
					ps.setString(1, questiondIDArrList.get(i));
					rs = ps.executeQuery();
					if (rs.next()) {
						insArrayList.add(rs.getString(1));
						System.out.println("Instructions for Question" + i + " " + insArrayList.get(i - 1));
					}
				}
				System.out.println("got all INSTRUCTIONS for all questions");
				String ansStr = "";
				String prof = "";
				String course = "";
				ps = con.prepareStatement("Select answer1,answer2,answer3,answer4 from questions where questionID=?");
				for (int i = 1; i < questiondIDArrList.size(); i++) {
					ps.setString(1, questiondIDArrList.get(i));
					rs = ps.executeQuery();
					if (rs.next()) {
						ansStr += (rs.getString(1));
						ansStr += "@";
						ansStr += (rs.getString(2));
						ansStr += "@";
					
						ansStr += (rs.getString(3));
						ansStr += "@";
						ansStr += (rs.getString(4));
						System.out.println("Answers for Question: " + (i) + " " + ansStr);
						ansArrayList.add(ansStr);
						ansStr = "";
					}
				}
				System.out.println("here");
				ps = con.prepareStatement(
						"SELECT ProffName,Coursename FROM CourseInProfession where ProffId =? AND idCourse=?");
				System.out.println("AFTER");
				prof = strArr[1].substring(0, 2);
				course = strArr[1].substring(2, 4);
				System.out.println(course + "----------" + prof);
				ps.setString(1, prof);
				ps.setString(2, course);
				rs = ps.executeQuery();
				rs.next();
				System.out.println("3");
				courseName = rs.getString(2);
				profName = rs.getString(1);

				System.out.println(courseName + "  " + profName);
				ManulTest cd = new ManulTest(ansArrayList, insArrayList, scoreArrayList, questiondIDArrList,
						txtArrayList, strArr[1], courseName, profName);
				cd.CreateManulTest();
				client.sendToClient("QuestionsToManulTest@" + strArr[1]);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("openManualTest")) {
			String str = (String) msg;
			String[] strArr;
			strArr = str.split("@");// strArr[0]= statment strArr[1]=testID

			MyFile message = new MyFile(strArr[1] + ".docx");

			try {
				File newFile = new File(strArr[1] + ".docx");
				byte[] mybytearray = new byte[(int) newFile.length()];
				FileInputStream fis = new FileInputStream(newFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				message.initArray(mybytearray.length);
				message.setSize(mybytearray.length);
				bis.read(message.getMybytearray(), 0, mybytearray.length);
				client.sendToClient(message);
			} catch (Exception e) {
				System.out.println("Error send (Files)msg) to Server");
			}
		} else if (((String) msg).contains("getTestTimeAndExecutionCode")) {
			ResultSet rs;
			PreparedStatement ps;
			String sql = (String) msg;
			String[] strArr = sql.split("@");
			try {
				ps = con.prepareStatement(
						"Select distinct T.durationTIme,T.studentText,T.lecturerText From Test T Where T.TestID = ?");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					sql = "testTimeAndExecutionCode@" + rs.getString(1) + "@" + rs.getString(2) + "@" + rs.getString(3);
				}
				System.out.println(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				client.sendToClient(sql);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("testToApprove")) {
			String str = (String) msg;
			String[] strArr;
			strArr = str.split("@");
			ResultSet rs;
			PreparedStatement ps;
			String sql = (String) msg;
			ArrayList<String> TestToApprove = new ArrayList<String>();
			TestToApprove.add("TestToApprove");
			try {
				ps = con.prepareStatement(
						"SELECT * FROM Grades WHERE  testID=? AND executionCode=? and (status='waiting for approval' OR substring(status,1,9) = 'Suspicion')");
				ps.setString(1, strArr[1]);
				ps.setString(2, strArr[2]);
				rs = ps.executeQuery();
				if (rs.next()) {
					do {
						String studInfo = "";
						studInfo += rs.getString(1) + "@" + rs.getString(2) + "@" + rs.getString(3) + "@"
								+ rs.getString(5) + "@" + rs.getString(7);
						TestToApprove.add(studInfo);
					} while (rs.next());
				}

				client.sendToClient(TestToApprove);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (((String) msg).contains("removeTestFromDB")) {

			String str = (String) msg;
			String[] strArr = str.split("@");
			System.out.println("removeTestFromDBasdl  SERVER " + strArr[1]);
			PreparedStatement ps;
			ResultSet rs;

			try {
				ps = con.prepareStatement("delete from Test where TestID=? and Status=?");
				ps.setString(1, strArr[1]);
				ps.setInt(2, 0);
				myFlag = ps.executeUpdate();
				System.out.println("the flage valie is: " + myFlag);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("importRequests")) {/**/
			if (req_Array_Server.size() == 0) {
				try {
					client.sendToClient(null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					System.out.println("send to client");
					client.sendToClient(req_Array_Server);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (((String) msg).contains("RemoveReq")) {
			req_Array_Server.remove(0);
			try {
				client.sendToClient("RemoveReqFromClientArray");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("ActiveTestTime")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			String newtime = strArr[1];
			String testCode = strArr[2];
			String exeCode = strArr[3];
			System.out.println(strArr[1] + " " + strArr[2] + " ");

			this.sendToAllClients(("ApproveTime@" + newtime + "@" + testCode + "@" + exeCode));
			try {

				PreparedStatement ps;
				ps = con.prepareStatement("UPDATE Statistic SET actualTime = ? WHERE executionCode = ?");
				ps.setString(1, newtime);
				ps.setString(2, exeCode);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("getAllQuestionsPerManager")) {
			try {

				ArrayList<ViewQuestionTable> Question_array = new ArrayList<ViewQuestionTable>();
				PreparedStatement ps;
				ResultSet rs;
				ps = con.prepareStatement("SELECT * FROM questions");
				rs = ps.executeQuery();
				while (rs.next()) {
					String questionID = rs.getString(1);
					String lecturer = rs.getString(2);
					String questionText = rs.getString(3);
					String ans1 = rs.getString(4);
					String ans2 = rs.getString(5);
					String ans3 = rs.getString(6);
					String ans4 = rs.getString(7);
					int correctAnswer = rs.getInt(8);
					String instructions = rs.getString(9);
					ViewQuestionTable tempQuestion = new ViewQuestionTable(questionID, questionText, ans1, ans2, ans3,
							ans4, correctAnswer, instructions, lecturer);
					System.out.println("questionID " + tempQuestion.get_qID());
					System.out.println("lecturer " + tempQuestion.getLecturer());
					System.out.println("questionText" + tempQuestion.get_qText());
					System.out.println("ans1 " + tempQuestion.getAns1());
					System.out.println("ans2 " + tempQuestion.getAns2());
					System.out.println("ans3 " + tempQuestion.getAns3());
					System.out.println("ans4 " + tempQuestion.getAns4());
					System.out.println("correctAnswer " + tempQuestion.getCorrectAnswer());
					System.out.println("Instructions" + tempQuestion.get_Instructions());
					Question_array.add(tempQuestion);
					System.out.println("----------------------------");
				}
				try {
					client.sendToClient(Question_array);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Question_array.clear();/* Clear Memory */
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllTestsPerManager")) {
			ArrayList<ViewTestTable> Test_array = new ArrayList<ViewTestTable>();
			PreparedStatement ps;
			ResultSet rs;
			try {
				ps = con.prepareStatement(
						"SELECT distinct TestID,teachername,durationTIme,studentText,lecturerText FROM Test");

				rs = ps.executeQuery();
				while (rs.next()) {
					String TestID = rs.getString(1);
					String teachername = rs.getString(2);
					String studentText = rs.getString(4);
					String durationTIme = rs.getString(3);
					String lecturerText = rs.getString(5);
					ViewTestTable tempTest = new ViewTestTable(TestID, lecturerText, null, teachername, studentText,
							durationTIme);

					Test_array.add(tempTest);
				}
				client.sendToClient(Test_array);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("GetTestQuestion")) {
			ArrayList<TestForManager> arrayToSend = new ArrayList<TestForManager>();
			PreparedStatement ps;
			ResultSet rs;
			String[] arr = ((String) msg).split("@");

			try {
				ps = con.prepareStatement(
						"SELECT t.questionID,t.QuestionScore,q.questionText,q.answer1,q.answer2,q.answer3,q.answer4,q.correctAnswer,q.instructions FROM Project.Test t LEFT JOIN Project.questions q on t.questionID = q.questionID WHERE t.TestID=?");
				ps.setString(1, arr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					String questionID = rs.getString(1);
					String questionScore = rs.getString(2);
					String questionText = rs.getString(3);
					String answer1 = rs.getString(4);
					String answer2 = rs.getString(5);
					String answer3 = rs.getString(6);
					String answer4 = rs.getString(7);
					String correctAnswer = rs.getString(8);
					String instructions = rs.getString(9);
					TestForManager temp = new TestForManager(questionID, questionScore, questionText, answer1, answer2,
							answer3, answer4, correctAnswer, instructions);
					arrayToSend.add(temp);

				}
				client.sendToClient(arrayToSend);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getFinishedTestsPerLecturer")) {
			try {
				ArrayList<String> testIDArr = new ArrayList<String>();
				String[] arr = ((String) msg).split("@");
				PreparedStatement ps;
				ResultSet rs;

				ps = con.prepareStatement(
						"SELECT DISTINCT S.testID FROM Project.Statistic S  LEFT JOIN Project.Test T on S.testID = T.testID WHERE T.teachername=? AND status='0';");
				ps.setString(1, arr[1]);
				testIDArr.add(0, "FinishedTestsPerLecturer");
				rs = ps.executeQuery();
				while (rs.next()) {
					System.out.println(rs.getString(1));
					testIDArr.add(rs.getString(1));
				}
				System.out.println("@@@@@@@@@@@");
				try {
					client.sendToClient(testIDArr);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("getFinishedTestNumberPerLecturer")) {
			try {
				ArrayList<String> execArr = new ArrayList<String>();
				String[] arr = ((String) msg).split("@");
				PreparedStatement ps;
				ResultSet rs;
				ps = con.prepareStatement("SELECT DISTINCT executionCode FROM Statistic WHERE testID=?");
				ps.setString(1, arr[1]);
				execArr.add(0, "FinishedTestNumber");
				rs = ps.executeQuery();
				while (rs.next()) {
					System.out.println(rs.getString(1));
					execArr.add(rs.getString(1));
				}
				System.out.println("@");
				try {
					client.sendToClient(execArr);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllGradesByTestID")) {
			try {
				StatisticData sd = null;
				String[] arr = ((String) msg).split("@");
				PreparedStatement ps;
				ResultSet rs;
				ps = con.prepareStatement(
						"SELECT DISTINCT avreage,median,0to9,10to19,20to29,30to39,40to49,50to59,60to69,70to79,80to89,90to100 FROM Statistic WHERE testID=? AND executionCode=?");
				ps.setString(1, arr[1]);
				ps.setString(2, arr[2]);

				rs = ps.executeQuery();
				while (rs.next()) {
					sd = new StatisticData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
							rs.getString(10), rs.getString(11), rs.getString(12), arr[1], arr[2]);
				}
				System.out.println("@@@@@@@@@@@");
				try {
					client.sendToClient(sd);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("approveTests")) {
			String str = (String) msg; // [0]=testID,[1]=student,[2]=status,[3]=explanation,[4]=grade
			String[] strArr = str.split("@");
			PreparedStatement ps;
			ResultSet rs;
			int tarr[] = new int[10];
			int temp;
			double avg;
			String testID = null;
			ArrayList<Integer> gradesToMedian = new ArrayList<Integer>();
			// String []tstr={"0to9",
			// "10to19","20to29","30to39","40to49","50to59","60to69","70to79","80to89","90to100"};

			try {
				for (int i = 1; i < strArr.length; i++) {
					String[] Arr = strArr[i].split("~");
					testID = Arr[0];
					ps = con.prepareStatement(
							"UPDATE Grades SET status=? , explaination=?, grade=? WHERE testID= ? AND studentID=?");
					if (Arr[3].equals("null"))
						ps.setNull(2, java.sql.Types.VARCHAR);
					else
						ps.setString(2, Arr[3]);
					ps.setString(4, Arr[0]); // testID
					ps.setString(1, Arr[2]); // status
					ps.setString(5, Arr[1]); // student ID
					ps.setInt(3, Integer.parseInt(Arr[4])); // grade
					System.out.println("1-" + Arr[2] + " 2-" + Arr[3] + " 3-" + Arr[0] + " 4-" + Arr[1]);
					ps.executeUpdate();
					temp = Integer.parseInt(Arr[4]);
					temp = temp / 10;
					tarr[temp]++;

				}
				strArr = str.split(" ");
				/*
				 * ps = con.
				 * prepareStatement("SELECT COUNT(*) FROM Grades WHERE status='Approve' AND executionCode = ? and testID=?"
				 * ); ps.setString(1, strArr[1]); ps.setString(2, testID);
				 * 
				 * rs=ps.executeQuery(); rs.next(); String count=rs.getString(1); ps = con.
				 * prepareStatement("UPDATE Statistic SET amountOfStudent = ? WHERE executionCode = ? and testID=?"
				 * ); ps.setString(1, count); ps.setString(2, strArr[1]); ps.setString(3,
				 * testID);
				 * 
				 * ps.executeUpdate();
				 */
				/*
				 * for(int i=0;i<10;i++) { ps =
				 * con.prepareStatement("UPDATE Statistic SET "+tstr[i]+" = "+
				 * tstr[i]+" + "+tarr[i]+" WHERE executionCode = ?"); ps.setString(1,
				 * String.valueOf(tarr[i])); ps.setString(2,strArr[1]); ps.executeUpdate(); }
				 */

				if (tarr[0] > 0) {
					ps = con.prepareStatement(
							"UPDATE Statistic SET 0to9 = 0to9 + " + tarr[0] + " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[1] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 10to19 = 10to19 + " + tarr[1]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[2] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 20to29 = 20to29 + " + tarr[2]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[3] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 30to39 = 30to39 + " + tarr[3]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[4] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 40to49 = 40to49 + " + tarr[4]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[5] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 50to59 = 50to59 + " + tarr[5]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[6] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 60to69 = 60to69 + " + tarr[6]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[7] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 70to79 = 70to79 + " + tarr[7]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[8] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 80to89 = 80to89 + " + tarr[8]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}
				if (tarr[9] > 0) {
					ps = con.prepareStatement("UPDATE Statistic SET 90to100 = 90to100 + " + tarr[9]
							+ " WHERE executionCode = ? and testID=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, testID);
					ps.executeUpdate();
				}

				ps = con.prepareStatement(
						"SELECT AVG(grade) FROM Grades WHERE testID=? and executionCode=? and status='Approve'");
				ps.setString(2, strArr[1]);
				ps.setString(1, testID);

				rs = ps.executeQuery();
				rs.next();
				avg = rs.getDouble(1);
				String savg = String.format("%.2f", avg);

				ps = con.prepareStatement("update Statistic set avreage=? where testID=? and executionCode=?");
				ps.setString(3, strArr[1]);
				ps.setString(1, savg);
				ps.setString(2, testID);
				ps.executeUpdate();

				ps = con.prepareStatement("select grade from Project.Grades where testID=? and executionCode=?");
				ps.setString(1, testID);
				ps.setString(2, strArr[1]);
				rs = ps.executeQuery();

				while (rs.next())
					gradesToMedian.add(rs.getInt(1));

				Collections.sort(gradesToMedian);
				int middle = gradesToMedian.size() / 2;
				middle = middle % 2 == 0 ? middle - 1 : middle;
				int median = gradesToMedian.get(middle);

				ps = con.prepareStatement("update Statistic set median=? where testID=? and executionCode=?");
				ps.setString(1, Integer.toString(median));
				ps.setString(2, testID);
				ps.setString(3, strArr[1]);
				ps.executeUpdate();

				client.sendToClient(null);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("stuAnsAndCorrAns")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			ResultSet rs;

			try {
				ps = con.prepareStatement(
						"SELECT t.questionID,t.score,t.answer,q.questionText,q.answer1,q.answer2,q.answer3,q.answer4,q.correctAnswer,q.instructions FROM Project.TestResults t LEFT JOIN Project.questions q on t.questionID = q.questionID WHERE t.Testid=? AND t.Studentid=?");
				ps.setString(1, strArr[1]);
				ps.setString(2, strArr[2]);
				rs = ps.executeQuery();
				ArrayList<TestOfStudent> toSend = new ArrayList<TestOfStudent>();
				while (rs.next()) {
					String qustionID = rs.getString(1);
					String questionScore = rs.getString(2);
					String stuAns = String.valueOf(rs.getInt(3));
					String questionText = rs.getString(4);
					String ans1 = rs.getString(5);
					String ans2 = rs.getString(6);
					String ans3 = rs.getString(7);
					String ans4 = rs.getString(8);
					String corrAns = String.valueOf(rs.getInt(9));
					String instruction = rs.getString(10);

					TestOfStudent temp = new TestOfStudent(qustionID, questionScore, questionText, ans1, ans2, ans3,
							ans4, corrAns, instruction, stuAns);

					toSend.add(temp);
				}

				client.sendToClient(toSend);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("ViewGrades")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			ResultSet rs;

			try {
				ps = con.prepareStatement(
						"SELECT testID,grade,status,explaination FROM Project.Grades WHERE studentID=?");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				ArrayList<GradesOfStudent> toSend = new ArrayList<GradesOfStudent>();
				while (rs.next()) {
					String testID = rs.getString(1);
					String grade = rs.getString(2);
					String status = rs.getString(3);
					String explanation = rs.getString(4);

					GradesOfStudent temp = new GradesOfStudent(testID, grade, status, null, explanation);

					toSend.add(temp);
				}

				client.sendToClient(toSend);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("TimeForCheck")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			ResultSet rs;
			String toSent = "";
			try {
				ps = con.prepareStatement("Select time FROM Statistic Where executionCode = ? ");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				if (rs.next()) {
					toSent = rs.getString(1);
				}

				client.sendToClient("TimeChange@" + toSent);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("StartTimerForTest")) {
			String str = (String) msg;
			String[] strArr = str.split("@");

			TaskCloser taskrun = new TaskCloser(strArr[1], strArr[2]);
			task_req_array.put(strArr[1], taskrun);
			System.out.println("Array Task size : " + task_req_array.size());
			Timer mission = new Timer();
			mission.schedule(task_req_array.get(strArr[1]), 0);
		} else if (((String) msg).contains("CloseAutoExam")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			try {
				ps = con.prepareStatement("UPDATE Statistic SET status = '0' WHERE executionCode = ? ");
				ps.setString(1, strArr[1]);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (((String) msg).contains("UpdateTestStatusForMan")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			try {
				ps = con.prepareStatement("UPDATE Statistic SET status = '0' WHERE executionCode = ? ");
				ps.setString(1, strArr[1]);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (((String) msg).contains("updateTestWithData")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;

			try {
				ps = con.prepareStatement("UPDATE Test SET durationTIme=?,studentText=?,lecturerText=? WHERE testID=?");

				if (strArr[3].equals("null")) {
					ps.setNull(2, java.sql.Types.VARCHAR);

				} else {
					ps.setString(2, strArr[3]);
				}
				if (strArr[4].equals("null")) {
					ps.setNull(3, java.sql.Types.VARCHAR);

				} else {
					ps.setString(3, strArr[4]);
				}

				ps.setString(1, strArr[2]);

				ps.setString(4, strArr[1]);
				ps.executeUpdate();
				try {
					client.sendToClient("updatedTestData");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("IncreceAmountOfStudent")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			try {
				ps = con.prepareStatement(
						"UPDATE Statistic SET amountOfStudent = amountOfStudent + 1 WHERE executionCode = ?");
				ps.setString(1, strArr[1]);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (((String) msg).contains("UpdateAcTestTime")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			String newtime = strArr[1];
			String testCode = strArr[2];
			String exeCode = strArr[3];
			TaskCloser temp = task_req_array.get(strArr[3]);
			temp.livetime = strArr[1];
			System.out.println(temp.livetime);
			System.out.println(strArr[1] + " " + strArr[2] + " ");
			this.sendToAllClients(("ApproveTime@" + newtime + "@" + testCode + "@" + exeCode));
			try {
				PreparedStatement ps;
				ps = con.prepareStatement("UPDATE Statistic SET actualTime = ? WHERE executionCode = ?");
				ps.setString(1, newtime);
				ps.setString(2, exeCode);
				ps.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getStudentTextArea")) {

			String str = (String) msg;
			String[] strArr = str.split("@");
			String studentText = "";
			System.out.println("TEST ID IS: " + strArr[1]);
			PreparedStatement ps;
			ResultSet rs;
			try {
				ps = con.prepareStatement("Select DISTINCT studentText FROM Test Where TestID = ? ");

				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					studentText = rs.getString(1);
					System.out.println(studentText);
				}
				try {
					client.sendToClient("StudentText@" + studentText);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (((String) msg).contains("testApproveManager")) {
			ResultSet rs;
			PreparedStatement ps;
			String sql = (String) msg;
			ArrayList<checkTestBox> TestToApprove = new ArrayList<checkTestBox>();

			try {
				ps = con.prepareStatement(
						"SELECT G.*,S.testType FROM   Project.Grades G  LEFT JOIN Project.Statistic S on S.testID = G.testID  AND S.executionCode=G.executionCode");
				rs = ps.executeQuery();
				while (rs.next()) {
					checkTestBox toAdd = new checkTestBox(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(5), null, rs.getString(7), rs.getString(9));
					TestToApprove.add(toAdd);
				}

				client.sendToClient(TestToApprove);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("GivemeActiTest")) {
			ResultSet rs;
			PreparedStatement ps;
			ArrayList<String> activeTest = new ArrayList<String>();
			activeTest.add("AllactivetestForLock");
			try {
				ps = con.prepareStatement("SELECT executionCode FROM Project.Statistic Where status = '1'");
				rs = ps.executeQuery();
				while (rs.next()) {
					String toAdd = rs.getString(1);
					activeTest.add(toAdd);
				}
				try {
					client.sendToClient(activeTest);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("ForceLockTest")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			PreparedStatement ps;
			try {
				ps = con.prepareStatement("UPDATE Statistic SET status = '0' WHERE executionCode = ?");
				ps.setString(1, strArr[1]);
				ps.executeUpdate();
				System.out.println("Status Updated!");
				this.sendToAllClients("ForceLockTest@" + strArr[1]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("its_ok")) {
			String str = (String) msg;
			String[] strArr = str.split("@");
			ResultSet rs;
			PreparedStatement ps;
			int active_Flag = 0;
			try {
				ps = con.prepareStatement("SELECT * FROM Project.Statistic Where status = '1' AND executionCode = ?;");
				ps.setString(1, strArr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					active_Flag = 1;
				}
				if (active_Flag == 1)// is exist
				{
					try {
						client.sendToClient("ExeCodeisOk@" + "1");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						client.sendToClient("ExeCodeisOk@" + "0");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("testApproveBySelection")) {
			String str = (String) msg;
			String[] strArr;
			strArr = str.split("@");
			ResultSet rs;
			PreparedStatement ps;
			String sql = (String) msg;
			String status;
			ArrayList<String> TestToApprove = new ArrayList<String>();
			TestToApprove.add("TestToApprove");
			try {
				status = strArr[3];
				if (status.equals("waiting for approval")) {
					ps = con.prepareStatement(
							"SELECT * FROM Grades WHERE  testID=? AND executionCode=? and (status='waiting for approval' OR substring(status,1,9) = 'Suspicion')");
					ps.setString(1, strArr[1]);
					ps.setString(2, strArr[2]);
					rs = ps.executeQuery();
				} else {
					ps = con.prepareStatement("SELECT * FROM Grades WHERE  testID=? AND executionCode=? and status=?");
					ps.setString(1, strArr[1]);
					ps.setString(2, strArr[2]);
					ps.setString(3, status);
					rs = ps.executeQuery();
				}
				if (rs.next()) {
					do {
						String studInfo = "";
						studInfo += rs.getString(1) + "@" + rs.getString(2) + "@" + rs.getString(3) + "@"
								+ rs.getString(5) + "@" + rs.getString(7);
						TestToApprove.add(studInfo);
					} while (rs.next());
				}

				client.sendToClient(TestToApprove);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllProffesions")) {
			PreparedStatement ps;
			ResultSet rs;
			try {

				String sql = (String) msg;
				ps = con.prepareStatement("SELECT ProffName FROM LecturerInCourse");// select all the //
				rs = ps.executeQuery();
				if (rs.next() == false) {
					client.sendToClient(null);;

				} else {
					ArrayList<String> proff = new ArrayList<String>(); // list of professions
					proff.add("professions");

					while (temp == true)/* If exist */
					{
						if (!proff.contains(rs.getString(1)))
							proff.add(rs.getString(1)); // add all this lecturer professions to proff array list
						temp = rs.next();
					}
					temp = true;

					client.sendToClient(proff); // send back to client
				}
				client.sendToClient(null); // send back to client

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllCourseNames")) {
			PreparedStatement ps;
			ResultSet rs;
			try {

				String sql = (String) msg;
				String[] arr = sql.split("@");
				ps = con.prepareStatement("SELECT CourseName FROM LecturerInCourse WHERE ProffName=?");// select

				ps.setString(1, arr[1]);
				rs = ps.executeQuery();
				ArrayList<String> courses = new ArrayList<String>(); // list of courses
				courses.add("courses");
				while (rs.next())/* If exist */
				{
					if (!courses.contains(rs.getString(1)))
						courses.add(rs.getString(1)); // add all this lecturer courses to courses array list according
														// to the user prof choice

				}
				client.sendToClient(courses); // send back to client
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getFinishedTestsPerCourse")) {
			ArrayList<String> testIDArr = new ArrayList<String>();
			String[] arr = ((String) msg).split("@");
			PreparedStatement ps;
			PreparedStatement ps2;
			ResultSet rs;
			String sql = (String) msg;
			ArrayList<String> profCourseID = new ArrayList<String>();

			try {
				ps2 = con.prepareStatement(
						"SELECT DISTINCT ProffId,idCourse FROM Project.CourseInProfession WHERE ProffName=? AND Coursename=?");

				ps2.setString(1, arr[1]);
				ps2.setString(2, arr[2]);
				rs = ps2.executeQuery();
				while (rs.next()) {
					profCourseID.add(rs.getString(1) + rs.getString(2));
				}

				ps = con.prepareStatement(
						"SELECT DISTINCT S.testID FROM Project.Statistic S  WHERE status='0' AND S.TestID LIKE ?;");
				String sqlStr = profCourseID.get(0) + "%";
				ps.setString(1, sqlStr);
				testIDArr.add(0, "FinishedTestsPerCourse");
				rs = ps.executeQuery();
				while (rs.next()) {
					testIDArr.add(rs.getString(1));
				}
				client.sendToClient(testIDArr);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (((String) msg).contains("getAllStudents")) {
			ArrayList<String> students = new ArrayList<String>();
			PreparedStatement ps;
			ResultSet rs;
			students.add("studentsList");
			try {
				ps = con.prepareStatement("SELECT UserName FROM Project.Users WHERE Status='Stu'");
				rs = ps.executeQuery();
				while (rs.next()) {
					students.add(rs.getString(1));
				}
				client.sendToClient(students);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllGradesByStudentID")) {
			String str = (String) msg;
			String strarr[] = str.split("@");
			ArrayList<StudentGrade> students = new ArrayList<StudentGrade>();
			PreparedStatement ps;
			ResultSet rs;
			try {
				ps = con.prepareStatement(
						"SELECT grade,TestID,executionCode FROM Grades WHERE studentID=? and status=\"Approve\";");
				ps.setString(1, strarr[1]);
				rs = ps.executeQuery();
				while (rs.next()) {
					students.add(new StudentGrade(rs.getInt(1), rs.getString(2), rs.getString(3)));
				}
				client.sendToClient(students);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (((String) msg).contains("getAllLecturerNames")) {
			String str = (String) msg;
			String strarr[] = str.split("@");
			ArrayList<Lecturer> lecturers = new ArrayList<Lecturer>();
			PreparedStatement ps;
			ResultSet rs;
			try {
				ps = con.prepareStatement("SELECT * FROM Project.Lecturer");
				rs = ps.executeQuery();
				while (rs.next()) {
					lecturers.add(new Lecturer(rs.getString(1), rs.getString(2)));
				}
				client.sendToClient(lecturers);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean checkActive(String id) {
		if (!(StatusCon.containsKey(id))) // check if the user is in the table
		{
			StatusCon.put(id, 1);
			return true;
		} else {
			if (StatusCon.get(id) == 0) // check if the user in the system
			{
				StatusCon.put(id, 1);
				return true;
			} else {
				System.out.println("already connected");
			}
		}
		return false;
	}
}

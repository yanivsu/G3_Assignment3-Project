package application;
import java.io.Serializable;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import Server.GradesOfStudent;
import Server.checkTestBox;
import Server.Lecturer;
import Server.ManulTest;
import Server.MyFile;
import Server.QuestionTable;
import Server.Question;
import Server.Request;
import Server.StatisticData;
import Server.StudentGrade;
import Server.TaskCloser;
import Server.TestForManager;
import Server.TestOfStudent;
import Server.ViewQuestionTable;

import Server.ViewTestTable;
import javafx.scene.image.ImageView;
import ocsf.client.AbstractClient;
import Server.QuestionTable;

public class Client extends AbstractClient {
	public static ArrayList<String> testsOfLecturer = new ArrayList<String>();
	public static ArrayList<String> testsOfcourse = new ArrayList<String>();
	public static ArrayList<String> finishedTestNumber = new ArrayList<String>();
	public static ArrayList<String> activeTest = new ArrayList<String>();
	public static ArrayList<Request> req_Array_Client = new ArrayList<Request>();
	public static HashMap<Question, String> test_question_map = new HashMap<Question, String>();
	public static ArrayList<Question> test_question;
	public ArrayList<StudentGrade> studentGradesData = new ArrayList<StudentGrade>();
	public static ArrayList<String> testToApprove = new ArrayList<String>();
	public static ArrayList<String> profCourseIDList = new ArrayList<String>();
	public static ArrayList<String> myProffinitialize = new ArrayList<String>();
	public static ArrayList<String> courseList = new ArrayList<String>();
	public ArrayList<Question> questions = new ArrayList<Question>();
	public static ArrayList<checkTestBox> managerViewsCheckedTests = new ArrayList<checkTestBox>();
	public static ArrayList<String> studentsList;
	public static ArrayList<String> myProff = new ArrayList<String>();
	public static ArrayList<Lecturer> lecturers = new ArrayList<Lecturer>();
	public static int its_ok = 0;
	public static boolean gotmsg = false, lecTemp = false;;
	public static int QuestionCounter;
	public static String Lectname;
	public static String StuId;
	public static ArrayList<String> coursesByCode = new ArrayList<String>();
	public static int loginFlag = 2;
	public static int already_conn = 1;
	public static String UserStatus;
	public static String idCourse, idProf;
	public static String testCounter = "";
	public static boolean isExist = false;
	public static int testStatus;
	public static String testDuration = "";
	public static String timeforLivetest = "";
	public static String testDuration2 = "";
	public static String executionCode = "";
	public static String studentTextArea = "";
	public static String addlivetime = "0";
	public static StatisticData gradesOfTest;
	public static String studentText = "";
	public static String lecturerText = "";
	public static ArrayList<QuestionTable> questionTable = new ArrayList<QuestionTable>();
	public static ArrayList<ViewTestTable> managerTestTable = new ArrayList<ViewTestTable>();
	public static ArrayList<ViewQuestionTable> managerQuestionTable = new ArrayList<ViewQuestionTable>();
	public static ArrayList<TestForManager> testForManager = new ArrayList<TestForManager>();
	public static ArrayList<TestOfStudent> testOfStudent = new ArrayList<TestOfStudent>();
	public static ArrayList<GradesOfStudent> studentGrades = new ArrayList<GradesOfStudent>();

	public Client(String host, int port) {
		super(host, port);
	}

	/**
	 * The function handle with requests/questions from the user
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg == null) {
			lecTemp = true;
		} else if (msg instanceof StatisticData) {
			gradesOfTest = ((Server.StatisticData) msg);
			gotmsg = true;
		}
		if (msg instanceof HashMap)/* For now hashmap is only for Test Question!!!!! */
		{
			test_question_map = (HashMap<Question, String>) msg;
			test_question = new ArrayList<>(test_question_map.keySet());/* Move all question intro array list */
			gotmsg = true;
		}
		if (msg instanceof ArrayList) {
			
			if (((ArrayList<Object>) msg).get(0).getClass().equals(QuestionTable.class)) 
			{
				//sSystem.out.println(QuestionTable.class.toString());
				questionTable = ((ArrayList<QuestionTable>) msg);
				for (QuestionTable q : questionTable) {
					q.setEditImage(new ImageView("/resources/edit.png"));
					q.setDelImage(new ImageView("/resources/delete.png"));
				}
				gotmsg = true;
			}
			if (((ArrayList<Object>) msg).get(0) instanceof Request) {
				System.out.println("Client array is: " + req_Array_Client.size());
				req_Array_Client = ((ArrayList<Request>) msg);
				lecTemp = true;
			}
			if (((ArrayList<Object>) msg).get(0).getClass().equals(Question.class)) {
				questions = (ArrayList<Question>) msg;
				gotmsg = true;
			} 
			else if (((ArrayList<Object>) msg).get(0).getClass().equals(TestForManager.class)) {
				testForManager = (ArrayList<TestForManager>) msg;
				gotmsg = true;
			}
			else if (((ArrayList<Object>) msg).get(0).getClass().equals(ViewQuestionTable.class)) 
			{
				ArrayList<ViewQuestionTable> myArrayList = (ArrayList<ViewQuestionTable>) msg;
				for (int i = 0; i < myArrayList.size(); i++) {
					managerQuestionTable.add(myArrayList.get(i));
				}
				gotmsg = true;
			} else if (((ArrayList<Object>) msg).get(0).getClass().equals(ViewTestTable.class)) 
			{
				ArrayList<ViewTestTable> myArrayList = (ArrayList<ViewTestTable>) msg;
				for (int i = 0; i < myArrayList.size(); i++) {
					myArrayList.get(i).setViewImage(new ImageView("/resources/view.png"));
					managerTestTable.add(myArrayList.get(i));
				}
				gotmsg = true;
			} else if (((ArrayList<Object>) msg).get(0).getClass().equals(TestOfStudent.class)) 
			{
				testOfStudent = (ArrayList<TestOfStudent>) msg;
				lecTemp = true;
			} else if (((ArrayList<Object>) msg).get(0).getClass().equals(GradesOfStudent.class))
			{
				studentGrades = (ArrayList<GradesOfStudent>) msg;
				gotmsg = true;
			} else if (((ArrayList<Object>) msg).get(0).getClass().equals(StudentGrade.class)) 
			{

				studentGradesData = (ArrayList<StudentGrade>) msg;
				gotmsg = true;
			} else if (((ArrayList<Object>) msg).get(0).getClass().equals(Lecturer.class)) 
			{
				lecturers = (ArrayList<Lecturer>) msg;
				gotmsg = true;
			}

			if (((ArrayList<Object>) msg).get(0) instanceof checkTestBox) {
				System.out.println("i got a message");
				managerViewsCheckedTests = ((ArrayList<checkTestBox>) msg);
				for (checkTestBox ctb : managerViewsCheckedTests)
					ctb.setViewImage(new ImageView("/resources/view.png"));
				gotmsg = true;
			}

			else if (((ArrayList<Object>) msg).get(0) instanceof String) {

				ArrayList<String> myArrayList = (ArrayList<String>) msg;
				if (myArrayList.get(0).equals("CourseList")) {
					for (int i = 1; i < myArrayList.size(); i++) {
						courseList.add(myArrayList.get(i));
					}
					gotmsg = true;
				}

				else if (myArrayList.get(0).equals("profflist")) {
					myProff = (ArrayList<String>) msg;
					lecTemp = true;
					for (int i = 0; i < myProff.size(); i++) {
						System.out.println(myProff.get(i));
					}
				} else if (((ArrayList) msg).get(0).equals("courses")
						|| ((ArrayList) msg).get(0).equals("professions")) {
					myProff = (ArrayList<String>) msg;
					lecTemp = true;
					myProff.remove(0);
				} else if (myArrayList.get(0).equals("proffinitializelist")) {
					myProffinitialize = myArrayList;
					myProffinitialize.remove(0);
					gotmsg = true;

				} else if (myArrayList.get(0).equals("ProfandCourseID")) {
					profCourseIDList = myArrayList;
					profCourseIDList.remove(0);
					gotmsg = true;
				} else if (myArrayList.get(0).equals("AllCoursesByQuestionCodeArray")) {
					coursesByCode = myArrayList;
					coursesByCode.remove(0);
					gotmsg = true;
				} else if (myArrayList.get(0).equals("activeCourses")) {
					courseList = myArrayList;
					courseList.remove(0);
					gotmsg = true;
				} else if (myArrayList.get(0).equals("TestToApprove")) {
					testToApprove = myArrayList;
					testToApprove.remove(0);
					gotmsg = true;

				} else if (myArrayList.get(0).equals("FinishedTestsPerLecturer")) {
					testsOfLecturer = myArrayList;
					testsOfLecturer.remove(0);
					gotmsg = true;

				} else if (myArrayList.get(0).equals("FinishedTestNumber")) {
					finishedTestNumber = myArrayList;
					finishedTestNumber.remove(0);
					gotmsg = true;
				} else if (myArrayList.get(0).equals("AllactivetestForLock")) {
					activeTest = myArrayList;
					activeTest.remove(0);
					gotmsg = true;
				} else if (myArrayList.get(0).equals("FinishedTestsPerCourse")) {
					testsOfcourse = myArrayList;
					testsOfcourse.remove(0);
					for (String tests : testsOfcourse)
						System.out.println(tests);
					gotmsg = true;
				} else if (myArrayList.get(0).equals("studentsList")) {
					studentsList = myArrayList;
					studentsList.remove(0);
					gotmsg = true;
				}

			}

		} else if (msg instanceof String) {
			if (((String) msg).contains("StudentText")) {
				String str = (String) msg;
				String[] strArr = str.split("@");
				studentTextArea = strArr[1];
				;
				gotmsg = true;
			}
			if (((String) msg).equals("AlreadyConncecetd")) {
				already_conn = 0;
				loginFlag = -1;
			}
			if (((String) msg).equals("LoginSucess")) {
				loginFlag = 1;
			}
			if (((String) msg).equals("UserDoesntExist")) {
				loginFlag = 0;
			}
			if (((String) msg).equals("changeteststatus")) {
				gotmsg = true;
			}

			if (((String) msg).contains("LecturerName")) {
				String str = (String) msg;
				String[] strArr = str.split("@");
				Lectname = strArr[1];
				gotmsg = true;
			}
			if (((String) msg).contains("TotalCount")) {
				String str = (String) msg;
				String[] countArr = str.split("@");
				QuestionCounter = Integer.parseInt(countArr[1]);
				System.out.println(QuestionCounter + " HAI EFES");
				gotmsg = true;
			}
			if (((String) msg).contains("CheckStatus")) {
				String str = (String) msg;
				String[] statusArr = str.split("@");
				UserStatus = statusArr[1];
				gotmsg = true;

			}

			if (((String) msg).contains("CourseList")) {

			}
			if (((String) msg).contains("idCoursePro")) {
				String[] arr = ((String) msg).split("@");
				idProf = arr[1];
				idCourse = arr[2];
				lecTemp = true;
			}
			if (((String) msg).contains("testCount")) {
				String[] arr = ((String) msg).split("@");
				testCounter = arr[1];
				System.out.println(arr[1]);
				if (testCounter.length() == 1) {
					testCounter = "0" + testCounter;
				}
				System.out.println("xxxx" + testCounter);
				lecTemp = true;
			}
			if (((String) msg).contains("isexist")) {
				if (((String) msg).contains("true"))
					isExist = true;
				else
					isExist = false;
				gotmsg = true;
			}
			if (((String) msg).contains("DurationOfTest")) {
				String[] arr = ((String) msg).split("@");
				testDuration = arr[1];
				gotmsg = true;

			}
			if (((String) msg).contains("uploadedQuestionSuccesfully")) {
				gotmsg = true;
			}

			if (((String) msg).contains("QuestionInCourseUpdatedSuccesfully")) {
				gotmsg = true;
			}
			if (((String) msg).contains("GetTestDuration")) {
				String[] arr = ((String) msg).split("@");
				timeforLivetest = arr[1];
				gotmsg = true;
			}
			if (((String) msg).contains("QuestionsToManulTest")) {
				String str = (String) msg;
				String[] strArr = str.split("@");
				System.out.println("Test " + strArr[1] + " Created successfully");
				str = "Test " + strArr[1] + " created successfully";
				gotmsg = true;
			}
			if (((String) msg).equals("XXXXXXX")) {
				gotmsg = true;
				System.out.println("asdoasd");
			}
			if (((String) msg).contains("testTimeAndExecutionCode")) {
				String[] arr = ((String) msg).split("@");
				testDuration2 = arr[1];
				studentText = arr[2];
				lecturerText = arr[3];

				gotmsg = true;
			}
			if (((String) msg).contains("ApproveTime")) {
				gotmsg = true;
				if (LoginController.userClass.equals("Stu")) {
					String[] arr = ((String) msg).split("@");
					System.out.println(arr[3]);
					if (arr[3].equals(MainPageForSudentTestController.Ecode)) {
						timeforLivetest = arr[1];
					}
				}
				if (LoginController.userClass.equals("Lec")) {
					String[] arr = ((String) msg).split("@");
					if (arr[2].equals(LoginController.userid)) {
						Client.addlivetime = arr[1];
					}
				}

			}
			if (((String) msg).contains("ForceLockTest")) {
				gotmsg = true;
				if (LoginController.userClass.equals("Stu")) {
					String[] arr = ((String) msg).split("@");
					if (arr[1].equals(MainPageForSudentTestController.Ecode)) {
						timeforLivetest = "0";
					}
				}

			}
			if (((String) msg).equals("RemoveReqFromClientArray")) {
				req_Array_Client.remove(0);
				lecTemp = true;
			}
			if (((String) msg).contains("TimeChange")) {
				String[] arr = ((String) msg).split("@");
				StudentTestStart.realtime = arr[1];
				UploadManulTestController.realtime = arr[1];
				gotmsg = true;
			}
			if (((String) msg).equals("updatedTestData")) {
				gotmsg = true;

			}
			if (((String) msg).contains("ExeCodeisOk")) {
				String[] arr = ((String) msg).split("@");
				if (arr[1].equals("1")) {
					its_ok = 1;
				} else {
					its_ok = 0;
				}
				gotmsg = true;
			}

		} else if (msg instanceof MyFile)// For now hashmap is only for Test Question!!!!!/
		{
			MyFile file = (MyFile) msg;
			try {
				File newFile = new File(file.getFileName());
				FileOutputStream out = new FileOutputStream(newFile);
				out.write(file.getMybytearray());
				out.close();

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(newFile);
				}

			} catch (Exception e) {

			}
		}

	}

	public void handleMessageFromClientUI(String msg) throws IOException {
		if (msg instanceof String) {
			if (msg.contains("uploadManualTestToServer")) {
				String str = (String) msg;
				String[] strArr = str.split("@");
				String fileName = strArr[1].substring(0, 6) + "-" + LoginController.userid + ".docx";
				MyFile messasge = new MyFile(fileName);

				try {
					File newFile = new File(strArr[1]);
					byte[] mybytearray = new byte[(int) newFile.length()];
					FileInputStream fis = new FileInputStream(newFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					messasge.initArray(mybytearray.length);
					messasge.setSize(mybytearray.length);
					bis.read(messasge.getMybytearray(), 0, mybytearray.length);
					sendToServer(messasge);
				} catch (Exception e) {
					System.out.println(strArr[1]);
					System.out.println("Error send (Files)msg) to Server");
				}

			} else if (msg.contains("CreateManualTest")) {
				sendToServer(msg);
			} else if (msg.contains("removeTestFromDB")) {
				sendToServer(msg);

			} else if (msg.contains("getAllQuestionsPerManager")) {
				sendToServer(msg);
			}
		}
	}

}
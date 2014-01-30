import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class TextBuddy {
      
	private static File outputFile; 
	
	//the line number of the output file, used for add/delete
	private static int fileLineNumber=0;
	
	//temp file to store operation
	private static List <String> tempFile = new ArrayList<String>();
    
	private static String[] cmdSet={"add","display","delete","clear","exit"};                 
    private static String EXIT_CMD,CLEAR_CMD,DELETE_CMD,DISPLAY_CMD,ADD_CMD;
   	
   
   
public static void main(String[] args) throws Exception {
	try{
	     setEnvrnt(args);
	     initiateCommands();
	     processCmd();
	}
	catch (Exception e)
	{
		
		throw new Exception ("Cannot initialize the environment");
	}	 }

private static void setEnvrnt(String[] args ) throws IOException
{

	setIOForm(args);
    setOutputFileName(args);

}

private static void setOutputFileName(String[] args) throws IOException {
	
	String fileName= args[0];
	
	outputFile= new File(fileName);
	if (outputFile.exists())
	 {
		outputFile= new File("TextBuddyOutput-"+fileName);
	System.out.println("Welcome to TextBuddy."+ "TextBuddyOutput-"+fileName+"is ready for use.")	; 
	 }
	 
	else System.out.println("Welcome to TextBuddy."+ fileName+" is ready for use.")	; 
		
     try {
		outputFile.createNewFile();
	
     } catch (IOException e) {
	
		throw new IOException ("Cannot create output file");
	}
}

private static void setIOForm(String[] args) throws FileNotFoundException {
	//if input args has two elements, the second one will be the input file
	if (args.length==2)
	{  
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(args[1]);
			System.setIn(fstream);
			PrintStream out = new PrintStream(new FileOutputStream("output.txt"),true);
			System.setOut(out);
		
		
		} catch (FileNotFoundException e) {
			 throw new FileNotFoundException ("Cannot find the input file");
		}
	}
}
private static void initiateCommands()
{
    EXIT_CMD=cmdSet[4];
    CLEAR_CMD=cmdSet[3];
    DELETE_CMD=cmdSet[2];
    DISPLAY_CMD=cmdSet[1];
    ADD_CMD=cmdSet[0];

}
private static void processCmd() throws Exception
{
	
	Scanner sc = new Scanner(System.in);
	 while (sc.hasNextLine())
	{
	     System.out.print("command: ");
	     parseCmd(sc);
	}
}

public static void parseCmd(Scanner sc) throws IOException
{
	 String sentence=sc.nextLine();
        System.out.println(sentence+"\n");
	 String[]  content = sentence.split(" ", 2);
    	 executeCmd(content);
}

public static void exitProgram()
{
	System.out.println("Exit");	
    System.exit(0);

}


private static void executeCmd(String[] content) throws IOException {
	 String cmd= "";
	cmd = content[0];
	
  if (cmd.equals(DISPLAY_CMD))
  { displayContent();
      return;
  }
 
  if (cmd.equals(ADD_CMD))  
  { addContent(content[1]);
    return;
  }
  if (cmd.equals(DELETE_CMD))
  { 
    deleteContent(Integer.parseInt(content[1]));
    return;
  }
  if (cmd.equals(CLEAR_CMD))
  { 
    deleteAllContent(true);
    return;
  }
  if (cmd.equals(EXIT_CMD))
  { exitProgram();
    return;
  }
}
public static int countFileLineNumber() throws IOException
{    
	String line = null;
	fileLineNumber =0;
	try {
		FileReader fileReader = new FileReader(outputFile.getAbsoluteFile());

		 BufferedReader br = new BufferedReader(fileReader);

		
		
		 while ((line = br.readLine()) != null) {
		
		     fileLineNumber++;
		 }

		br.close();
	} catch (IOException e) {
		throw new IOException("Cannot access the file");
	}

return fileLineNumber;
}

public static void addContent(String content) throws IOException
{
    //append content with line number to the last line   
	try {
		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile(),true);
		
		BufferedWriter bw = new BufferedWriter(fw);

		
			bw.append((countFileLineNumber()+1)+". "+ content);
		
			System.out.println("added to "+outputFile.getName()+": \""+content+"\"\n");
		
			bw.newLine();
		
		
			bw.close();
	} catch (IOException e) {
		
	throw new IOException("Cannot add content");
	}
}
// delete all contents in the output file, with option to print message
public static void deleteAllContent(boolean ifPrintMessage) throws IOException
{

    try {
		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		bw.close();
		if (ifPrintMessage)
		System.out.println("all content deleted from "+outputFile.getName());
	} catch (IOException e) {
		throw new IOException("Cannot delete contents");
	}


}
public static void displayContent() throws IOException
{
	
	FileReader fileReader = new FileReader(outputFile.getAbsoluteFile());

	 BufferedReader br = new BufferedReader(fileReader);
	
	 if (countFileLineNumber()==0)
	 {
		 System.out.println(outputFile.getName()+" is empty");
		 
	 }
	 else { String line = null;
	
	 while ((line = br.readLine()) != null) {
	     System.out.println(line);
     
	 
	 }}

	br.close();


}
public static void deleteContent(int lineNumber) throws IOException
{
	//write contents to temp file except the deleted line
	wrtiteTempFile(lineNumber);

    deleteAllContent(false);

	//rewrite all remaining contents to the output file
    writeTempToOutputFile();


}

private static void writeTempToOutputFile() throws IOException {
	try {
		FileWriter fw = new FileWriter(outputFile.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		int newLineNumber=1;
			for (String  content : tempFile)
				{ bw.append(newLineNumber+". "+content);
			      newLineNumber++;
				}
		
			
			bw.close();
	} catch (IOException e) {
		throw new IOException("Cannot write to output file");
	}
}

private static void wrtiteTempFile(int lineNumber)throws FileNotFoundException, IOException {
	
	int countLine=1;
	
	String line = null;
	//store contents line by line except for the deleted line
	try {
		FileReader fileReader = new FileReader(outputFile.getAbsoluteFile());
		BufferedReader br = new BufferedReader(fileReader);

		 while ((line = br.readLine()) != null) {
			 String[]  content = line.split(" ", 2);
			 if (countLine!=lineNumber)
		       {
		         	
		    	tempFile.add(content[1]+"\n");

		       }
			 else System.out.println("deleted from "+outputFile.getName()+": "+content[1] );
			 
			 
			 countLine++; 
		
		 
		 }
		fileReader.close();
		br.close();
	} catch (IOException e) {
	
		throw new IOException ("Operation cannot be stored correctly");
	}
}

}

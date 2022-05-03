package il.ac.tau.cs.sw1.ex4;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Random;

public class WordPuzzle {
	public static final String alphabet ="abcdefghijklmnopqrstuvwxyz" ;
	public static final char HIDDEN_CHAR = '_';
	public static final char[] empty = {};
	/*
	 * @pre: template is legal for word
	 */
	public static char[] createPuzzleFromTemplate(String word, boolean[] template) { // Q - 1
		char [] arr=new char[word.length()];
		for(int i=0; i<word.length();i++) {
			if (template[i]) 
				arr[i]=HIDDEN_CHAR;
			else
				arr[i]=word.charAt(i);
		}
		return arr;
	}

	public static boolean checkLegalTemplate(String word, boolean[] template) { // Q - 2
		if (word.length()!=template.length||word.length()==1)
			return false;
		return (checkLegalTemplate_2(template)&&checkLegalTemplate_1(template, word) );
	}
	private static boolean checkLegalTemplate_1(boolean[]arr,String word) {
		for(int j=0;j<26;j++) {
			int cnt_t=0;
			int cnt_f=0;
			for(int i=0;i<word.length();i++) {
				if  (word.charAt(i)==alphabet.charAt(j)) { 
					if (arr[i]) 
						cnt_t+=1;
					if (!arr[i])
						cnt_f+=1;		
					if ((cnt_f!=0) && (cnt_t!=0))
						return false;
				}		
			}
		}
		return true;
	}
	private static boolean checkLegalTemplate_2(boolean[]template){
		boolean exist_hidden_char=false;
		boolean exist_aparent_char=false;
		for (boolean x : template) {
			if (x)
				 exist_hidden_char=true;
			if (!x)
				exist_aparent_char=true;
			if (exist_hidden_char && 	exist_aparent_char)
				return true;
		}
		return false;
	}
	
	/*
	 * @pre: 0 < k < word.lenght(), word.length() <= 10
	 */
	public static boolean[][] getAllLegalTemplates(String word, int k){  // Q - 3
		int x= factorial(word.length())/(factorial(k)*factorial(word.length()-k));
		boolean[][] res=new boolean[x][word.length()];
 		boolean[]arr=new boolean[word.length()];
 		int cnt=0;
 		get_all_num(word,k,res);
		for (boolean[] b : res) {
			if (Arrays.equals(b, arr))
				cnt++;
		}
		return Arrays.copyOf(res, res.length-cnt);
	}
	private static int factorial(int number) {
		int fact=1;
		for(int i=1;i<=number;i++){    
		      fact=fact*i; }
		return fact;
	}
	private static void get_all_num(String word,int k,boolean[][]res) {
		int cnt= 0;
		for (int i = 0; i < Math.pow(2,word.length()); i++) {
			if (has_k_one(Integer.toBinaryString(i),k)) {
				boolean arr[]=convert_to_arr(Integer.toBinaryString(i),word.length());
				if(checkLegalTemplate(word,arr)) {
					res[cnt]=arr;
					cnt++;}
		}
			}
	}
	private static boolean[] convert_to_arr(String binaryString,int n){
		boolean []arr=new boolean[n];
		for (int i = 0; i < binaryString.length(); i++) {
			if (binaryString.charAt(i)=='1')
				arr[i+n-binaryString.length()]=true;
	}
		return arr;
	}

	private static boolean has_k_one(String binaryString,int k) {
		int cnt=0;
		for (int i = 0; i < binaryString.length(); i++) {
			if (binaryString.charAt(i)=='1')
				cnt++;
		}
		if (cnt==k)
			return true;
		return false;
	}

	/*
	 * @pre: puzzle is a legal puzzle constructed from word, guess is in [a...z]
	 */
	public static int applyGuess(char guess, String word, char[] puzzle) { // Q - 4
		int cnt=0;
		for (int i = 0; i < puzzle.length; i++) {
			if (word.charAt(i)==guess) {
				if (puzzle[i]== guess)
					return 0;
				else {
					puzzle[i]=guess;
					cnt+=1;
				}
			}
		}
		return cnt;
	}
		

	/*
	 * @pre: puzzle is a legal puzzle constructed from word
	 * @pre: puzzle contains at least one hidden character. 
	 * @pre: there are at least 2 letters that don't appear in word, and the user didn't guess
	 */
	public static char[] getHint(String word, char[] puzzle, boolean[] already_guessed) { // Q - 5
			char x=rand(already_guessed,false,word,puzzle);
			char y=rand(already_guessed,true,word,puzzle);
			if (Character.compare(x,y)<=0) {
				char[] m={x,y};
				return(m);}
			else {
				char[] m={y,x};
				return(m);
			}
				
	}
	
	private static char rand(boolean[]already_guessed,boolean exist,String word,char[] puzzle) {
		 Random rand = new Random(); 
		 int int_random =0;
		 char x;
		 if (exist) 
			 while(true) {
			 {int_random = rand.nextInt(word.length()-1);
			 if (puzzle[int_random]==HIDDEN_CHAR)
				 return word.charAt(int_random);
			 }}
		 
		 else {
			 while(true) {
		    	 int_random = rand.nextInt(25);
		    	 x=alphabet.charAt(int_random);
		    	 if (!already_guessed[int_random]&&!word.contains(Character.toString(x)) ) 
					 return x;
		    	 
		    			
		     }
		}
		 }
	    
	
	

	public static char[] mainTemplateSettings(String word, Scanner inputScanner) { // Q - 6
		
		printSettingsMessage();
		char[]ch= {};
		while (ch.length==0) {
			printSelectTemplate();
			int choose =inputScanner.nextInt();
			if (choose==1) 
				ch=choose_1(word,inputScanner);
			if (choose==2)  {
				ch=choose_2(word,inputScanner);
			}
			if (ch.length==0) {
				printWrongTemplateParameters();
				}
			else
				break;
			
		}
		return ch;}
	private static char[] choose_1(String word, Scanner inputScanner) {
		printSelectNumberOfHiddenChars();
		int num =inputScanner.nextInt();
		boolean[][] Templates = getAllLegalTemplates(word, num);
		if (Templates.length==0) 
			return (empty);
		else {
			int rnd = new Random().nextInt(Templates.length);
			boolean[] x =(Templates [rnd]);
			return(createPuzzleFromTemplate(word,x));
		}
	}
	private static char[] choose_2(String word, Scanner inputScanner) {
		 printEnterPuzzleTemplate();
		 String fragments=inputScanner.next();
		String[] str=fragments.split(",");
		int fragments_length=str.length;
		boolean [] arr=new boolean[str.length];
		for (int i=0;i<fragments_length;i++) {
			if (str[i].equals("_")) 
				arr[i]=true;
			else
				arr[i]=false;}
		if (checkLegalTemplate(word, arr)) 
			return (createPuzzleFromTemplate(word, arr));
		else 
			return(empty);
		}
	

	public static void mainGame(String word, char[] puzzle, Scanner inputScanner){ // Q - 7
		printGameStageMessage();
		int x;
		char h[] ;
		boolean win=false;
		boolean [] already_guessed= new boolean[26];
		for (int i = hidden_cahr_cnt(puzzle)+3; i >0; i--) {
			printPuzzle(puzzle);
			printEnterYourGuessMessage();
			char guess=inputScanner.next().charAt(0);			
			if (guess==('H')) {
					h=getHint(word,puzzle,already_guessed);
					printHint(h);
					i++;
					}
			
			else
				{already_guessed[(int)guess-97]=true;
				x= applyGuess(guess,word,puzzle);
				if (x>0) {
					if (hidden_cahr_cnt(puzzle)==0) {
						printWinMessage();
						win=true;
						break;
					}
					else  printCorrectGuess(i-1);					}
				
				else printWrongGuess(i-1);
		}}
		if(!win)
			printGameOver();
	}
	private static int hidden_cahr_cnt(char[] puzzle) {
		int cnt=0;
		for (char c :  puzzle) {
			if (c== HIDDEN_CHAR)
				cnt+=1;
		}
		return cnt;
	}
		
	
	
				


/*************************************************************/
/********************* Don't change this ********************/
/*************************************************************/

	public static void main(String[] args) throws Exception { 
		if (args.length < 1){
			throw new Exception("You must specify one argument to this program");
		}
		String wordForPuzzle = args[0].toLowerCase();
		if (wordForPuzzle.length() > 10){
			throw new Exception("The word should not contain more than 10 characters");
		}
		Scanner inputScanner = new Scanner(System.in);
		char[] puzzle = mainTemplateSettings(wordForPuzzle, inputScanner);
		mainGame(wordForPuzzle, puzzle, inputScanner);
		inputScanner.close();
	}


	public static void printSettingsMessage() {
		System.out.println("--- Settings stage ---");
	}

	public static void printEnterWord() {
		System.out.println("Enter word:");
	}
	
	public static void printSelectNumberOfHiddenChars(){
		System.out.println("Enter number of hidden characters:");
	}
	public static void printSelectTemplate() {
		System.out.println("Choose a (1) random or (2) manual template:");
	}
	
	public static void printWrongTemplateParameters() {
		System.out.println("Cannot generate puzzle, try again.");
	}
	
	public static void printEnterPuzzleTemplate() {
		System.out.println("Enter your puzzle template:");
	}


	public static void printPuzzle(char[] puzzle) {
		System.out.println(puzzle);
	}


	public static void printGameStageMessage() {
		System.out.println("--- Game stage ---");
	}

	public static void printEnterYourGuessMessage() {
		System.out.println("Enter your guess:");
	}

	public static void printHint(char[] hist){
		System.out.println(String.format("Here's a hint for you: choose either %s or %s.", hist[0] ,hist[1]));

	}
	public static void printCorrectGuess(int attemptsNum) {
		System.out.println("Correct Guess, " + attemptsNum + " guesses left.");
	}

	public static void printWrongGuess(int attemptsNum) {
		System.out.println("Wrong Guess, " + attemptsNum + " guesses left.");
	}

	public static void printWinMessage() {
		System.out.println("Congratulations! You solved the puzzle!");
	}

	public static void printGameOver() {
		System.out.println("Game over!");
	}

}

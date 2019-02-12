package Assign1;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Created using eclipse
 * Stud. #5931456
 *	09-09-16
 * @author Dom's Computer
 *
 */
public class Assignment1 {
	public static void main(String[] args) throws IOException {
		new Assignment1();
	}

	public Assignment1() throws IOException{

		int nStrands=0,nBases = 0;//Number of Strand/Number of Bases in strand
		File text = chooseFile();//choose the file to read from
		
		if(text==null)//if no file was selected end program
			return;
	
		Scanner sc = new Scanner(new FileReader(text));//create an instance of scanner to read the file
		
		
		nStrands = Integer.parseInt(sc.next());//read from text file
		
		for(int i = 0;i<nStrands;i++){//print the information for each read strand
			nBases = Integer.parseInt(sc.next());
						
			Node<Character> str = new Node<Character>(sc.next().charAt(0),null);//create initial node for strand
			Node<Character> st = str;//create second pointer to head
			
			for(int j = 0;j<nBases-1;j++){//add a node for the number of bases
				st.next = new Node<Character>(sc.next().charAt(0),null);
				st = st.next;
			}
			
			print(str);//print out strand
			System.out.print(" GC content: "+gcContent(str,0));//print gc content of strand
			System.out.print("   Reverse Complement: ");//print out the reverse complement
			print(reverseComplement(str));
			System.out.println();
		}
		int length = Integer.parseInt(sc.next());
		int rate = Integer.parseInt(sc.next());
		
		sc.close();//close the scanner stream
		
		int seq = 0;
		
		seq = 0;
		for(int i=0;i<length;i++){//set up the sequence to create bases
			seq+=((int)Math.pow(10, i));
		}
		
		Node <Character> strand = generateStrands(length,strand = null, rate, 0,seq);//calls method to generate all strands with the desired length,gc content, and non self complementry
	}

	public Node<Character> generateStrands(int n, Node<Character> k, int rate, int length,int seq) {
		
		if (n == length) {//base case for creating nodes
			return null;
		} else {
			k = new Node<Character>(choose(Character.getNumericValue(Integer.toString(seq).charAt(length))),null);//create a node based on the which nodes have already been created
			
			k.next = generateStrands(n, k.next, rate, ++length,seq);//recursively create the next node in the strand
			
			if (length > 1)//return the node unless its at the bottom of the stack
				return k;
		}
		boolean b1 = false,b2= false,last = check(seq,n)!=0;
		if(last){//check if the strands null
			b1 = (gcContent(k, 0) == rate);//checks if the strands got correct gc content
			b2 = compare(k, reverseComplement(copy(k)));//checks if the strand is the same reverse complemented
		}
		 if(b1&&!b2&&last){//if strands got correct gc content and complementary and not the last node to be generated
			 print(k);//print the strand
			 System.out.println();
			 if(last){
				 k = null;//reset the strand
				 seq = incr(seq,n);//increment the sequence to produce the strands
				 generateStrands(n,k,rate,0,seq);//create new strand with the new sequence
			 }
			 return k;
		 }
		 else{//if the strand doesn't have the right gc content or is self complementary then create next strand
			 if(last){
				 k = null;
				 seq = incr(seq,n);//increment sequence
				 generateStrands(n,k,rate,0,seq);
			 }
		 }

		return k;
	}
	int incr(int seq,int len){
		
		seq++;//add one to the sequence
	
		int ch = check(seq,len);//checks if any of the numbers in the sequence is greater then 4, returns the length of the strand if nothings over 4
		
		if(ch==len)//if nothing is over 4 then return that sequence
			return seq;
		else{
			do{
				seq+=Math.pow(10, len-ch);//add one to the next column that was over
				seq-=(4*Math.pow(10, len-ch-1));//remove 4 from the column that was over
				
				ch = check(seq,len);//check if the sequence is within boundaries
			}while(ch!=len&&ch!=0);//if not remove the next one out of bounds
			return seq;
		}
}
	int check(int seq,int l){//check if any of the columns are out of bounds, returns the length if nothing is out of bounds
		for(int i = 0;i<l;i++){
			if(Integer.toString(seq).charAt(i)=='5'){
				return i;
			}
		}
		return l;
	}
	public Character choose(int x) {//choose which base to add to the strand
		switch (x) {
		case 1:
			return 'A';
		case 2:
			return 'C';
		case 3:
			return 'G';
		case 4:
			return 'T';
		}
		return null;
	}

	public void print(Node<Character> l) {//print the strand
		if(l!=null){
			System.out.print(l.item);
			print(l.next);
		}
	}

	public Node<Character> copy(Node<Character> l) {//copy the strand and return it
		if (l == null)
			return null;
		else
			return new Node<Character>(l.item, copy(l.next));
	}

	public boolean compare(Node<Character> l1, Node<Character> l2) {//compares two strands and returns true if they are the same
		boolean same = false, fYet = false;

		if (l1 == null ^ l2 == null) {//XOR the lists, if one of them is null then the lists aren't the same, but if neither or both are then they can be the same.
			return false;
		} else if (l1 == null && l2 == null) {//if they're both null then they are the same length
			return true;
		} else if (l1.item == l2.item) {//if the two bases match and the strand isn't false yet check the next node
			if (!fYet) {
				same = compare(l1.next, l2.next);
			}
		} else if (l1.item != l2.item) {//if the bases don't match return false
			return false;
		}

		return same;

	}

	public Node<Character> reverseComplement(Node<Character> list) {
		if (list != null)//if the node isn't empty then change the item to the complement of itself
			list.item = comp(list.item);
		if (list == null)//if the strand is empty then there's no reverse complement
			return null;
		if (list.next == null)//if the next node is null then the current node is the end of the strand
			return list;
		Node<Character> rL = reverseComplement(list.next);//recursively call reverse complement for the next node

		list.next.next = list;
		list.next = null;
		return rL;
	}

	public char comp(char x) {//returns the complement of the bases given
		char comp = ' ';

		switch (x) {
		case 'A':
			comp = 'T';
			break;
		case 'T':
			comp = 'A';
			break;
		case 'G':
			comp = 'C';
			break;
		case 'C':
			comp = 'G';
			break;
		}
		return comp;
	}

	public int gcContent(Node<Character> list, int cont) {//returns the number of times g or c is in the strand
		if (list != null) {//base case if list is empty

			if (list.item == 'G' || list.item == 'C') {//if the base of the strand is either g or c add one to the tally and look at the next item
				return gcContent(list.next, cont + 1);
			} else {
				return gcContent(list.next, cont);
			}
		}
		return cont;
	}
	
	public File chooseFile(){//method to open up a file dialog and return the file. returns null if no file is chosen
		File f = null;
		JFileChooser fc = new JFileChooser();//opens file dialog using swing
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files","txt","text");//filter to choose which files to look for
	    fc.setFileFilter(filter);
	    int rVal = fc.showOpenDialog(null);//gets the button pressed
	    if(rVal == JFileChooser.APPROVE_OPTION) {//if the button pressed is the open button
	    	f = fc.getSelectedFile();//set the file to the file chosen
	    }
	    return f;
	}
}

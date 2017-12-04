package com.intellectualcloud.intellectualcloud.parseuri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ParseUri {

	// title
	private static final String pt       =   "<pt>";
	private static final String pt_close =   "</pt>";
	
	// description
	private static final String pd       =   "<pd>";
	private static final String pd_close =   "</pd>";
	
	// content
	private static final String pc       =   "<pc>";
	private static final String pc_close =   "</pc>";
	
	// image
	private static final String pi       =   "<pi>";
	private static final String pi_close =   "</pi>";
	
	public static final ArrayList<String> tagsList = 
				new ArrayList<String> (Collections.unmodifiableList(
						Arrays.asList( pt, pd, pc, pi, pi_close, 
								pc_close, pd_close, pt_close )));
	
	private String unstructuredURI;
	
	private StructuredURI structuredURI;

	private static boolean isOpenBracket (String br) {
		return   br.equals(pt)  ||  br.equals(pd)  || 
			     br.equals(pc)  ||  br.equals(pi);
	}
	
	private static boolean isCloseBracket (String br) {
		return br.equals(pt_close)  ||  br.equals(pd_close)  || 
			   br.equals(pc_close)  ||  br.equals(pi_close);
	}
	
	private static boolean checkOpenCloseBracketPair (String opBr, String clBr ) {
		
		return  (opBr.equals(pt) && clBr.equals(pt_close)) ||
				(opBr.equals(pd) && clBr.equals(pd_close)) ||
				(opBr.equals(pc) && clBr.equals(pc_close)) ||
				(opBr.equals(pi) && clBr.equals(pi_close));
	}
	
	
	private static ArrayList <String> splitWords (String message) {
		
		String curWord = new String();
		ArrayList <String> result = new ArrayList <String>();
		
		boolean nonSpaceTrig = false;
		
		for (int i = 0; i < message.length(); i++) {
			
			if (message.charAt(i) != ' ') {
				
				// if not a space and curWord is not empty.
 				if ( (!curWord.isEmpty()) && (!nonSpaceTrig) ) {
					result.add(curWord);
					curWord = "";
				}
				
				curWord += message.charAt(i);
				nonSpaceTrig = true;
				
			// if space and non space trigger is true.	
			} else if (nonSpaceTrig){ 
				nonSpaceTrig = false;
				result.add(curWord);
				curWord = "";
			} else {
				curWord += " ";
			}
		}
		
		return result;
	}
	
	public StructuredURI getStructuredURI () {
		return structuredURI;
	}
	
	
	public String getUnstructuredURI () {
		return unstructuredURI;
	}
	
	public void setUnstructuredURI (String uri) {
		unstructuredURI = uri;
	}
	
	public void setStructuredURI (StructuredURI uri) {
		structuredURI = uri;
	}
	
	
	public void structuredToUnstructured () {
		unstructuredURI = new String ();
		
		unstructuredURI +=   " " + pt +  " " + structuredURI.title       + " " + pt_close + " "
						   + " " + pd +  " " + structuredURI.description + " " + pd_close + " "
						   + " " + pc +  " " + structuredURI.content     + " " + pc_close + " "
						   + " " + pi +  " " + structuredURI.pictureURL  + " " + pi_close + " ";
	}

	
	public void unstructuredToStructured () throws ParserException {
		
		structuredURI = new StructuredURI ();
		
		Map <String, Boolean> alreadyParsed = new HashMap <String, Boolean> ();
		
		ArrayList <String> splitup  = splitWords (unstructuredURI);
		Stack <String > symbolStack = new Stack <String > ();
		
		String word = new String ();
		
		for (int i = 0; i < splitup.size(); i++) {

			
			
			if ( isOpenBracket (splitup.get(i)) ) {
				word = "";
				symbolStack.push(splitup.get(i));
				
				if(!alreadyParsed.containsKey(splitup.get(i))) {
					alreadyParsed.put(splitup.get(i), true);
				} else {
					throw new ParserException ("Error parsing, multiple declaration of "
							+ splitup.get(i) + " tag is forbidden." );
				}
				
				
			} else if (isCloseBracket (splitup.get(i))) {
				
				if (symbolStack.isEmpty() || 
						!checkOpenCloseBracketPair (symbolStack.peek(), splitup.get(i))) {
					throw new ParserException ("Error parsing, Expected open tag before close tag "+ 
						splitup.get(i));
				}
				
				alreadyParsed.put(splitup.get(i), true);
				
				switch (symbolStack.peek()) {
				case pi:
					structuredURI.pictureURL  = word;
					break;
				case pd:
					structuredURI.description = word;
					break;
				case pc:
					structuredURI.content     = word;
					break;
				case pt:
					structuredURI.title       = word;
					break;
				}
				
				symbolStack.pop();
			} else {
				word += splitup.get(i);
			}
						
		}
		
		for (int i = 0; i < tagsList.size(); i++) {
			if  ( !alreadyParsed.containsKey(tagsList.get(i)) ) {
				throw new ParserException ("Error parsing, unused tag "
						+ tagsList.get(i) +". usage of every tag is mandatory ");
			}
		}
		
		
	}
	
}

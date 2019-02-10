package com.example.demo;

import java.util.Date;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i=0;i<200;i++)
		getSaltString(false,10);
		//generateRandomPin();
		String ss =String.valueOf(System.currentTimeMillis());
		System.out.println("ss"+ss.substring(3, ss.length()));

	}
	public static String getSaltString(boolean onlyChar,int maxLengath) {
		String SALTCHARS=null;
     	 String saltStr="";
     	 int length;
     	StringBuilder salt = new StringBuilder();
     	Random rnd = new Random();
     	if(onlyChar) {
     		SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
     	}else {
     		SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
     	}
     	
     	length=maxLengath-1;
         while (salt.length() <length) { // length of the random string.
             int index = (int) (rnd.nextFloat() * SALTCHARS.length());
             salt.append(SALTCHARS.charAt(index));
         }
         if(onlyChar) {
       	   saltStr = salt.toString();
         }else {
        	saltStr = salt.toString().concat("0");
         }
         
         System.out.println(saltStr);
         return saltStr;


   }
	public static  void generateRandomPin(){

	   
	    createRandomInteger(1000000000, 5555555555L);

	}
	public static void createRandomInteger(int aStart, long aEnd){
		 Random random = new Random();
	    if ( aStart > aEnd ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = aEnd - (long)aStart + 1;
	    //System.out.println(range+" range");
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * random.nextDouble());
	    long randomNumber =  fraction + (long)aStart;    
	    System.out.println("Generated : " + randomNumber);

	  }

}

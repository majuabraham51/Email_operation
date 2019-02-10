package com.example.demo;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

public class ApachePOIExcelRead {

    private static final String FILE_NAME = "/tmp/MyFirstExcel.xlsx";

       public static void main(String[] args) {

        try {
        	File file = ResourceUtils.getFile("classpath:TestData.xlsx");

            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            
           int sheetCount= workbook.getNumberOfSheets();
           System.out.println("sheetCount -- "+sheetCount);
           ArrayList<Object> finalArray =new ArrayList<>();
           for(int i=0;i<sheetCount;i++) {
		        	   System.out.println("Num of Execution "+i);
		        	 Sheet datatypeSheet = workbook.getSheetAt(i);
		            Iterator<Row> iterator = datatypeSheet.iterator();
		            // ------------------------------------ Logic
		            Map<String, Integer> map = new HashMap<String,Integer>(); //Create map
		            Row currentRow2 = iterator.next();
		            short minColIx = currentRow2.getFirstCellNum(); //get the first column index for a row
		            short maxColIx = currentRow2.getLastCellNum(); //get the last column index for a row
		            System.out.println("minColIx"+minColIx +" maxColIx"+maxColIx);
		            
		            for(short colIx=minColIx; colIx<maxColIx; colIx++) { //loop from first to last index
		            	   Cell cell = currentRow2.getCell(colIx); //get the cell
		            	   System.out.println(cell.getStringCellValue() +"--"+cell.getColumnIndex());
		            	   map.put(cell.getStringCellValue(),cell.getColumnIndex()) ;//add the cell contents (name of column) and cell index to the map
		            	 }
		            //----------------------- Maju Logic end----
		            
		           // int idxForColumn1 = map.get("TRAN_ID");
		            
		            /*String enterpriseCode= enterprisesCreation();
		            String agentId=  agentActivity(enterpriseCode);
		            String customerCode=customerCreationActivity(agentId,enterpriseCode);
		            beneficiaryCreation(customerCode,enterpriseCode);
		            */
		            ArrayList<Object> sheetName = new ArrayList<Object>();
		            retrieveExcelSheet(iterator,sheetName);
		            finalArray.add(sheetName);
		            workbook.close();
		            excelFile.close();
		            System.out.println("---- Excel Operation Completed ---- ");
           }
           List<Object> enterprice = null;
           List<Object> agent= null;
           List<Object> remitter= null;
           for(int i=0;i<finalArray.size();i++) {
        	   if(i==0) {
        		   enterprice=(List)finalArray.get(0);
        		   System.out.println("enterprice "+enterprice.size());
        	   }
        	   else if(i==1) {
        		   agent=(List)finalArray.get(i);
        		   System.out.println("agent "+agent.size());
        	   }else {
        		  remitter=(List)finalArray.get(i);
        		   System.out.println("remitter "+remitter.size());
        	   }
               
           }
           
           for(Object enterprice1 : enterprice) {
        	   List<Object> list=(List)enterprice1;
        	   ExcelSheetEnterpriceDTO enterpriceList =new ExcelSheetEnterpriceDTO();
    			   enterpriceList.setColumnNo1(list.get(0).toString());
    			   enterpriceList.setColumnNo2(list.get(1).toString());
    			   enterpriceList.setColumnNo3(list.get(2).toString());
    			   enterpriceList.setColumnNo4(list.get(3).toString());
        	   String enterpriseCode= enterprisesCreation(enterpriceList);
        	  
        	   for(Object agentList : agent) {
        		   ExcelSheetAgentDTO agnt =new ExcelSheetAgentDTO();
        		   List<Object> agntlist=(List)agentList;
        		   agnt.setColumnNo1(agntlist.get(0).toString());
        		   agnt.setColumnNo2(agntlist.get(1).toString());
        		   agnt.setColumnNo3(agntlist.get(2).toString());
        		   agnt.setColumnNo4(agntlist.get(3).toString());
        		   String agentId=  agentActivity(enterpriseCode,agnt);
        		   
        		   /*for(Object remitterIteration : remitter) {
        			   ExcelSheetRemittertDTO remitterDTO =new ExcelSheetRemittertDTO();
            		   List<Object> remitterList=(List)remitterIteration;
            		   remitterDTO.setColumnNo1(remitterList.get(0).toString());
            		   remitterDTO.setColumnNo2(remitterList.get(1).toString());
            		   String customerCode=customerCreationActivity(agentId,enterpriseCode,remitterDTO);
            		   
        		   }*/
        	   }
        	   
           }
           
           /*for(Object finalList :finalArray) {
        	   List<Object> m=(List)finalList;
        	   for(Object ss : m) {
        		   List<Object> list=(List)ss;
        		   System.out.println("Size "+list.size());
        		   for(int i=0;i<list.size();i++) {
        			   System.out.println(list.get(i));
        		   }
        		   System.out.println("=========================");
        	   }
           }*/
           
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public static boolean isCollection(Object ob) {
    	  return ob instanceof Collection || ob instanceof Map;
    	}
    
	private static void retrieveExcelSheet(Iterator<Row> iterator,ArrayList<Object> arrayList) {
		// TODO Auto-generated method stub
    	 while (iterator.hasNext()) {
         	System.out.println("Inside");
         	ArrayList<String> obj =new ArrayList<String>();
             Row currentRow = iterator.next();
             Iterator<Cell> cellIterator = currentRow.iterator();
             while (cellIterator.hasNext()) {
                 Cell currentCell = cellIterator.next();
                  addColumnDataIntoPojo(currentCell,obj);
             }
             arrayList.add(obj);
         }
		System.out.println("-----Done-----");
	}


	private static void addColumnDataIntoPojo(Cell currentCell,ArrayList<String> obj) {
		// TODO Auto-generated method stub
		DataFormatter objDefaultFormat = new DataFormatter();
		String value=null;
		if (currentCell.getCellTypeEnum() == CellType.STRING) {
            System.out.print(currentCell.getStringCellValue() + "--");
            value=currentCell.getStringCellValue();
        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
            System.out.print(currentCell.getNumericCellValue() + "--");
            value= objDefaultFormat.formatCellValue(currentCell);
            System.out.println("value=============== "+value);
        }
		
		obj.add(value);
	}



	private static String enterprisesCreation(ExcelSheetEnterpriceDTO obj) {
		// TODO Auto-generated method stub
		String ss =String.valueOf(System.currentTimeMillis());
		System.out.println("ss"+ss.substring(3, ss.length()));
    	String enterprisesCreationJson="{  \r\n" + 
    			"   \"userCode\":null,\r\n" + 
    			"   \"addresses\":[  \r\n" + 
    			"      {  \r\n" + 
    			"         \"addressType\":{  \r\n" + 
    			"            \"codeName\":\"ADDRESS_TYPE_PRIMARY\",\r\n" + 
    			"            \"codeValue\":1,\r\n" + 
    			"            \"displayValue\":\"Primary\"\r\n" + 
    			"         },\r\n" + 
    			"         \"addressLine1\":\"Davinta Tech\",\r\n" + 
    			"         \"addressLine2\":\"ETV\",\r\n" + 
    			"         \"addressLine3\":\"Bellandur\",\r\n" + 
    			"         \"village\":\"Whitefield\",\r\n" + 
    			"         \"city\":\"GB\",\r\n" + 
    			"         \"subDistrict\":null,\r\n" + 
    			"         \"district\":\"Bangalore\",\r\n" + 
    			"         \"postalCode\":\"685608\",\r\n" + 
    			"         \"stateCode\":\"KA\",\r\n" + 
    			"         \"countryCode\":\"IN\",\r\n" + 
    			"         \"censusCode\":null,\r\n" + 
    			"         \"isVerified\":null,\r\n" + 
    			"         \"documentCollected\":null,\r\n" + 
    			"         \"effectiveDate\":null,\r\n" + 
    			"         \"enterpriseCode\":null\r\n" + 
    			"      }\r\n" + 
    			"   ],\r\n" + 
    			"   \"contacts\":[  \r\n" + 
    			"      {  \r\n" + 
    			"         \"contactType\":{  \r\n" + 
    			"            \"codeName\":\"CONTACT_TYPE_PRIMARY\",\r\n" + 
    			"            \"codeValue\":1,\r\n" + 
    			"            \"displayValue\":\"Primary\"\r\n" + 
    			"         },\r\n" + 
    			"         \"namePrefix\":\"Mr.\",\r\n" + 
    			"         \"firstName\":\""+obj.getColumnNo2()+"\",\r\n" + 
    			"         \"middleName\":null,\r\n" + 
    			"         \"lastName\":\"P\",\r\n" + 
    			"         \"jobTitle\":\"Admin\",\r\n" + 
    			"         \"mobileNo\":\""+obj.getColumnNo3()+"\",\r\n" + 
    			"         \"phoneNo\":null,\r\n" + 
    			"         \"faxNo\":null,\r\n" + 
    			"         \"emailAddress\":\""+obj.getColumnNo4()+"\",\r\n" + 
    			"         \"otherContact\":null,\r\n" + 
    			"         \"effectiveDate\":\"1970-01-01T00:00:00.000+0000\",\r\n" + 
    			"         \"enterpriseCode\":null\r\n" + 
    			"      }\r\n" + 
    			"   ],\r\n" + 
    			"   \"bankAccounts\":[  \r\n" + 
    			"      {  \r\n" + 
    			"         \"userCode\":null,\r\n" + 
    			"         \"enterpriseCode\":null,\r\n" + 
    			"         \"state\":null,\r\n" + 
    			"         \"bankAccountCode\":\"AC000001\",\r\n" + 
    			"         \"entityType\":2,\r\n" + 
    			"         \"agentCode\":null,\r\n" + 
    			"         \"accountName\":\"Settlement Account\",\r\n" + 
    			"         \"accountClassifier\":1,\r\n" + 
    			"         \"ifscCode\":\"SYNB0005000\",\r\n" + 
    			"         \"accountNumber\":\""+ss.substring(2, ss.length()-1)+"\",\r\n" + 
    			"         \"accountType\":2,\r\n" + 
    			"         \"name\":\"Davinta\",\r\n" + 
    			"         \"address\":null,\r\n" + 
    			"         \"bankName\":\"ABHY\",\r\n" + 
    			"         \"branchName\":null,\r\n" + 
    			"         \"effectiveDate\":null,\r\n" + 
    			"         \"documentCollected\":null,\r\n" + 
    			"         \"isVerified\":null\r\n" + 
    			"      }\r\n" + 
    			"   ],\r\n" + 
    			"   \"enterpriseType\":{  \r\n" + 
    			"      \"codeValue\":4\r\n" + 
    			"   },\r\n" + 
    			"   \"enterpriseName\":\""+obj.getColumnNo1()+"\",\r\n" + 
    			"   \"legalStatus\":{  \r\n" + 
    			"      \"codeValue\":1\r\n" + 
    			"   },\r\n" + 
    			"   \"taxId1\":\"TAX009\",\r\n" + 
    			"   \"taxId2\":\"TAX060\",\r\n" + 
    			"   \"doingBusinessAs\":\"DAVINTA\",\r\n" + 
    			"   \"financialYearStartMonth\":11,\r\n" + 
    			"   \"financialYearEndMonth\":12,\r\n" + 
    			"   \"glIdentifier\":\""+getSaltString(false,5)+"\",\r\n" + 
    			"   \"businessType\":{  \r\n" + 
    			"      \"codeValue\":2\r\n" + 
    			"   },\r\n" + 
    			"   \"establishmentDate\":\"2018-01-15T07:13:29.000+0000\",\r\n" + 
    			"   \"taxResidenceCountryCode\":\"IN\",\r\n" + 
    			"   \"registrationCode\":\""+ss.substring(3, ss.length())+"\",\r\n" + 
    			"   \"registrationDate\":\"2018-03-38T07:15:09.000+0000\",\r\n" + 
    			"   \"parentEnterpriseCode\":\"DAVINTA\",\r\n" + 
    			"   \"otherBcnm\":false,\r\n" + 
    			"   \"createdBy\":\"SYSTEM\",\r\n" + 
    			"   \"creationDate\":\"2018-03-28T07:15:29.000+0000\"\r\n" + 
    			"}";
    	String url = "http://localhost:9300/enterprises";
    	System.out.println("enterprisesCreationJson :"+enterprisesCreationJson);
    	String enterprisesCode=  callURLMethod(url,enterprisesCreationJson,"message.enterpriseCode",false);
    	String url2 = "http://localhost:9300//enterprises/"+enterprisesCode+"/approve";
    	String approvalenterprises ="{\r\n" + 
    			"  \"channelId\": 0,\r\n" + 
    			"  \"enterpriseCode\": \""+enterprisesCode+"\",\r\n" + 
    			"  \"reviewComments\": \"Approved\",\r\n" + 
    			"  \"state\": 0,\r\n" + 
    			"  \"userCode\": \"string\"\r\n" + 
    			"}\r\n" + 
    			"";
    	System.out.println("approvalenterprises :"+approvalenterprises);
    	String enterprisesActivation=  callURLMethod(url2,approvalenterprises,"status.statusCode",true);
    	return enterprisesCode;
	}


	private static void beneficiaryCreation(String customerCode,String enterpriseCode,int numofBene) {
		// TODO Auto-generated method stub
    	for(int i=0;i<numofBene;i++) {
    		
    	
    	String beneficiaryCreation="{  \r\n" + 
    			"   \"account\":{  \r\n" + 
    			"      \"accountName\":\""+getSaltString(true,5)+"\",\r\n" + 
    			"      \"accountType\":\"CURRENT\",\r\n" + 
    			"      \"bankIdentifier\":\"SYNB0003125\",\r\n" + 
    			"      \"identifierType\":\"ACCOUNT\",\r\n" + 
    			"      \"identifierValue\":\"60428023700\"\r\n" + 
    			"   },\r\n" + 
    			"   \"address\":\"maharatstra\",\r\n" + 
    			"   \"emailAddress\":\""+getSaltString(true,5)+"@gmail.com\",\r\n" + 
    			"   \"enterpriseCode\":\""+enterpriseCode+"\",\r\n" + 
    			"   \"mobileNumber\":\""+createRandomInteger(1000000000, 6666666666L)+"\",\r\n" + 
    			"   \"name\":\""+getSaltString(true,7)+"\",\r\n" + 
    			"   \"page\":0,\r\n" + 
    			"   \"pageable\":true,\r\n" + 
    			"   \"size\":0,\r\n" + 
    			"   \"state\":0\r\n" + 
    			"}";
    	System.out.println("beneficiaryCreation :"+beneficiaryCreation);

    	String url = "http://localhost::9500/customers/"+customerCode+"/beneficiaries/preAuthorised";
    	String customerCodeStatus=  callURLMethod(url,beneficiaryCreation,"status.statusCode",false);
    	if(customerCodeStatus.equalsIgnoreCase("1")) {
       	 System.out.println("-- beneficiaryCreation : "+customerCode + " customer : "+customerCode);
       	 // OTP need to get it from DB
        }
    	}
	}


	private static String customerCreationActivity(String agentId,String enterpriseCode,ExcelSheetRemittertDTO remitterDTO ) {
		// TODO Auto-generated method stub
		int n=Integer.parseInt(remitterDTO.getColumnNo1());
		 String customerCode=null;
		 int i;
		for( i=0;i<n;i++) {
			
		
    	String customerCreation="{  \r\n" + 
    			"   \"address\":{  \r\n" + 
    			"      \"addressLine1\":\"Bangalore\",\r\n" + 
    			"      \"addressLine2\":\"Bangalore\",\r\n" + 
    			"      \"city\":\"Bangalore\",\r\n" + 
    			"      \"pinCode\":\"560100\",\r\n" + 
    			"      \"stateCode\":\"KA\"\r\n" + 
    			"   },\r\n" + 
    			"   \"firstName\":\""+getSaltString(true,6)+"\",\r\n" + 
    			"   \"lastName\":\""+getSaltString(true,2)+"\",\r\n" + 
    			"   \"emailAddress\":\""+getSaltString(true,4)+"@gmail.com\",\r\n" + 
    			"   \"mobileNumber\":\""+ createRandomInteger(1000000000, 5445555555L)+"\",\r\n" + 
    			"   \"enterpriseCode\":\""+enterpriseCode+"\",\r\n" + 
    			"   \"agentCode\":\""+agentId+"\",\r\n" + 
    			"   \"dateOfBirth\":\"1984-06-09\",\r\n" + 
    			"   \"page\":0,\r\n" + 
    			"   \"pageable\":true,\r\n" + 
    			"   \"size\":0,\r\n" + 
    			"   \"state\":0\r\n" + 
    			"}";
    	System.out.println("customerCreation :"+customerCreation);
    	
    	  String url = "http://localhost:9500/customers";
    	   customerCode=  callURLMethod(url,customerCreation,"message.customerCode",false);
    	  //Bene Creation
    	  System.out.println("url done --"+ url + " customerCode : -"+ customerCode);
    	  System.out.println("---- Start Bene Creation ----");
    	  beneficiaryCreation(customerCode,enterpriseCode,Integer.parseInt(remitterDTO.getColumnNo2()));
    	  //return customerCode;
    	  System.out.println("Execution count "+ i);
		}
		return customerCode;
	}


	private static  String agentActivity(String enterpriseCode,ExcelSheetAgentDTO agentDto) {
    	 System.out.println("Before ----/agents");
    	 String ss =String.valueOf(System.currentTimeMillis());
         String url = "http://localhost:9300/agents";
         String jsonReq = "{  \r\n" + 
         		"   \"aadhaarNumber\":\""+getSaltString(false,10)+"\",\r\n" + 
         		"   \"agentType\":{  \r\n" + 
         		"      \"codeValue\":1\r\n" + 
         		"   },\r\n" + 
         		"   \"addresses\":[  \r\n" + 
         		"      {  \r\n" + 
         		"         \"addressLine1\":\"Banglore\",\r\n" + 
         		"         \"addressLine2\":\"Banglore\",\r\n" + 
         		"         \"stateCode\":\"AN\",\r\n" + 
         		"         \"district\":\"Banglore\",\r\n" + 
         		"         \"city\":\"Banglore\",\r\n" + 
         		"         \"postalCode\":\"515408\",\r\n" + 
         		"         \"addressType\":{  \r\n" + 
         		"            \"codeValue\":4\r\n" + 
         		"         },\r\n" + 
         		"         \"countryCode\":\"IN\"\r\n" + 
         		"      }\r\n" + 
         		"   ],\r\n" + 
         		"   \"bankAccounts\":[  \r\n" + 
         		"      {  \r\n" + 
         		"         \"accountNumber\":\""+ss.substring(4, ss.length())+"\",\r\n" + 
         		"         \"ifscCode\":\"HDFC0000001\",\r\n" + 
         		"         \"effectiveDate\":\"2018-03-28T18:30:00.000Z\",\r\n" + 
         		"         \"accountName\":\"Settlement Account\",\r\n" + 
         		"         \"name\":\"Savings\",\r\n" + 
         		"         \"accountClassifier\":1,\r\n" + 
         		"         \"accountType\":2\r\n" + 
         		"      }\r\n" + 
         		"   ],\r\n" + 
         		"   \"businessType\":{  \r\n" + 
         		"      \"codeValue\":\"\"\r\n" + 
         		"   },\r\n" + 
         		"   \"externalReference\":\"\",\r\n" + 
         		"   \"firstName\":\""+agentDto.getColumnNo1()+"\",\r\n" + 
         		"   \"lastName\":\""+agentDto.getColumnNo1()+"\",\r\n" + 
         		"   \"emailAddress\":\""+agentDto.getColumnNo4()+"\",\r\n" + 
         		"   \"legalStatus\":{  \r\n" + 
         		"      \"codeValue\":1\r\n" + 
         		"   },\r\n" + 
         		"   \"middleName\":\"\",\r\n" + 
         		"   \"mobileNo\":\""+agentDto.getColumnNo3()+"\",\r\n" + 
         		"   \"namePrefix\":\"TITLE_MR\",\r\n" + 
         		"   \"taxLocation\":\"IN\",\r\n" + 
         		"   \"kycCompleted\":true,\r\n" + 
         		"   \"enterpriseCode\":\""+enterpriseCode+"\",\r\n" + 
         		"   \"registrationCode\":\""+createRandomInteger(10000000, 99999999L)+"\",\r\n" + 
         		"   \"identifications\":[  \r\n" + 
         		"\r\n" + 
         		"   ]\r\n" + 
         		"}";
         System.out.println("agentCreation :"+jsonReq);
         String agentCode=  callURLMethod(url,jsonReq,"message.agentCode",false);
         System.out.println("After ----/agents");
         String jsonReq2="{\r\n" + 
         		"  \"channelId\": 2,\r\n" + 
         		"  \"enterpriseCode\": \""+enterpriseCode+"\",\r\n" + 
         		"  \"reviewComments\": \"Approved Agent\",\r\n" + 
         		"  \"state\": 0,\r\n" + 
         		"  \"userCode\": \"string\"\r\n" + 
         		"}";
         System.out.println("agentActivationStatus :"+jsonReq2);
         url="http://localhost:9300/agents/"+agentCode+"/approve";
         String agentActivationStatus= callURLMethod(url,jsonReq2,"status.statusCode",true);
         if(agentActivationStatus.equalsIgnoreCase("1")) {
        	 System.out.println("-- agentActivationStatus : "+agentActivationStatus + " agent : "+agentCode);
        	 // OTP need to get it from DB
         }
         return agentCode;
    	
    }
  private static String callURLMethod(String posturl,String jsonReq,String field,boolean put) {
	  String xx = null;
	  CloseableHttpClient httpclient = HttpClients.custom().build();
	  String url = posturl;
	  HttpPut request = null ;
	  HttpPost request1= null ;
      HttpResponse response;
      String responseAsString = null;
	  try {
		  StringEntity stringEntity = new StringEntity(jsonReq,"UTF-8");
	  if(put){
		   request = new HttpPut(url);
		   request.setEntity(stringEntity);
		   request.setHeader("Content-Type","application/json");
		   response = httpclient.execute(request);
			
	  }else {
		  request1 = new HttpPost(url);
		  request1.setEntity(stringEntity);
		  request1.setHeader("Content-Type","application/json");
	      response = httpclient.execute(request1);
		
	  }
      
       responseAsString = EntityUtils.toString(response.getEntity());
       System.out.println("responseAsString---"+responseAsString);
       JSONObject jsonObj = new JSONObject(responseAsString);
        xx= getValue(jsonObj,field);
       System.out.println("value ---"+xx);
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      return xx;
	}
/*  public static  File fileLocation() {
    	File file = new File(getClass().getResource("MyFirstExcel.xlsx").getFile());
    	return file;
    }*/
    
    public static String getValue(JSONObject inputJson, String field) {
        String resultValue = null;
        try {
            StringTokenizer stJson = new StringTokenizer(field, ".");
            int count = stJson.countTokens();
            JSONObject objecStore = new JSONObject();
            objecStore = inputJson;
            while (stJson.hasMoreTokens()) {
                String st = stJson.nextToken();
                if (count > 1) {
                    JSONObject objNode = objecStore.getJSONObject(st);
                    count--;
                    objecStore = objNode;
                } else {
                    System.out.println(st);
                    resultValue = objecStore.getString(st);
                }
            }

        } catch (JSONException e) {
        	e.printStackTrace();
        }
       
        return resultValue;
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
          }
          else {
        	  saltStr = salt.toString().concat("0");
          }
          System.out.println(saltStr);
          return saltStr;

    }
    public static long createRandomInteger(int aStart, long aEnd){
		 Random random = new Random();
	    if ( aStart > aEnd ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = aEnd - (long)aStart + 1;	
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * random.nextDouble());
	    long randomNumber =  fraction + (long)aStart;    
	    System.out.println("Generated : " + randomNumber);
		return randomNumber;

	  }
    
	/*KeyStore readStore() throws Exception {
		 KeyStore keyStore = null;
       try {
       	InputStream keyStoreStream = new FileInputStream("D:\\platform\\certs\\aeusapp-keystore.jks");
        System.out.println("Started the try block");
            keyStore = KeyStore.getInstance("PKCS12"); // or "PKCS12"
           keyStore.load(keyStoreStream, privateKey.toCharArray());
           return keyStore;
       }
       catch(Exception e){
    	   System.out.println("Error loading keystore with keystore "+ keyStorePath);
    	   System.out.println("Error loading keystore "+  e.getMessage(),  e);
       	e.printStackTrace();
       }
		return keyStore; 
   }*/
}
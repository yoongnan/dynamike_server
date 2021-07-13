package com.dynamike.pos.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.dynamike.pos.model.entities.Client;
import com.dynamike.pos.model.entities.OrderList;
import com.dynamike.pos.model.entities.Payment;
import com.dynamike.pos.model.entities.PaymentType;
import com.dynamike.pos.model.entities.ProviderSource;


public class ExcelLazadaHelper {
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "Id", "Title", "Description", "Published" };
  static String SHEET = "Tutorials";

  
  
  public static boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }
  public static List<OrderList> excelLazadaToOrderList(InputStream is, String excel) {
	    try {
	    	Sheet sheet;
	    	Workbook workbook = null;
	    	HSSFWorkbook hssfworkbook = null;
	    	if(excel.equals(".xlsx")) {
	  	      workbook = new XSSFWorkbook(is);
	  	      sheet = workbook.getSheetAt(0);	    		
	    	}else {
	    		hssfworkbook = new HSSFWorkbook(new POIFSFileSystem(is));
	    		sheet = hssfworkbook.getSheetAt(0);
	    	}
	      Iterator<Row> rows = sheet.iterator();

	      List<OrderList> orders = new ArrayList<OrderList>();

	      int rowNumber = 0;
	      while (rows.hasNext()) {
	        Row currentRow = rows.next();

	        if(!currentRow.getCell(64).getStringCellValue().toLowerCase().equals("canceled") && !currentRow.getCell(64).getStringCellValue().toLowerCase().trim().equals("")) {
		        // skip header
		        if (rowNumber == 0) {
		          rowNumber++;
		          continue;
		        }
	
		        Iterator<Cell> cellsInRow = currentRow.iterator();
	
		        OrderList order = orders.stream().filter(o -> o.getInvoiceId().equals(currentRow.getCell(12).getStringCellValue()) 
		        		&& o.getItemId().equals(currentRow.getCell(50).getStringCellValue().split("-")[0].trim())
		        		).findFirst().orElse(null);
		        if(order !=null) {
	        		order.setQuantity(order.getQuantity()+1);
		        }else {
		        	order = new OrderList();
			        order.setQuantity(1);
			        order.setInvoiceId(currentRow.getCell(12).getStringCellValue());
			        order.setItemId(currentRow.getCell(50).getStringCellValue().split("-")[0].trim());	
			        orders.add(order);
		        }
	  	  	}
	          rowNumber++;
	      }

	      if(workbook != null) {
	    	  workbook.close();   
	      }
	      if(hssfworkbook != null) {
	    	  hssfworkbook.close();   
	      }

	      return orders;
	    } catch (IOException e) {
	      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
	    }
	  }
  public static List<Client> excelLazadaToClient(InputStream is, String excel) {
	    try {
	    	Sheet sheet;
	    	Workbook workbook = null;
	    	HSSFWorkbook hssfworkbook = null;
	    	if(excel.equals(".xlsx")) {
	  	      workbook = new XSSFWorkbook(is);
	  	      sheet = workbook.getSheetAt(0);	    		
	    	}else {
	    		hssfworkbook = new HSSFWorkbook(new POIFSFileSystem(is));
	    		sheet = hssfworkbook.getSheetAt(0);
	    	}

	      Iterator<Row> rows = sheet.iterator();

	      List<Client> clients = new ArrayList<Client>();

	      int rowNumber = 0;
	      while (rows.hasNext()) {
	        Row currentRow = rows.next();

	        // skip header
	        if (rowNumber == 0) {
	          rowNumber++;
	          continue;
	        }

	        Iterator<Cell> cellsInRow = currentRow.iterator();

//	        while (cellsInRow.hasNext()) {
	        Client client = new Client();
	        String phoneno = currentRow.getCell(37).getStringCellValue();
	        if(phoneno.equals("")) {
	        	phoneno = currentRow.getCell(25).getStringCellValue();
	        }
	        client.setId(phoneno);
	        client.setContactNo(phoneno);
	        client.setName(currentRow.getCell(16).getStringCellValue() + currentRow.getCell(17).getStringCellValue());
	        client.setShippingAddress(currentRow.getCell(20).getStringCellValue());
	        client.setBillingAddress(currentRow.getCell(32).getStringCellValue());

//	        16 CLient Name
//	        17 Email
//	        19 Shipping Name
//	        20 SHipping Address
//	        21 SHipping Address2
//	        22 SHipping State
//	        23 SHipping City
//	        24 SHipping Postal Code
//	        25 phone number
//	        27 SHipping City
//	        28 SHipping Postal Code
//
//	        31 Billing Name
//	        32 Billing Address
//	        33 Billing Address2
//	        34 Billing State
//	        35 Billing City
//	        36 Billing Postal Code
//	        37 Billing Phone
//	        38 Billing City
//	        39 Billing PostCode
	        clients.add(client);
	      }

	      if(workbook != null) {
	    	  workbook.close();   
	      }
	      if(hssfworkbook != null) {
	    	  hssfworkbook.close();   
	      }
	      

	      return clients;
	    } catch (IOException e) {
	      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
	    }
	  }

  public static List<Payment> excelLazadaToAmountTransaction(InputStream is, String excel) {
	  DecimalFormat df = new DecimalFormat("0.00");
	  if(excel.equals(".csv")) {
		  String line = "";  
		  String splitBy = ",";  
		  BufferedReader br = new BufferedReader(new InputStreamReader(is));
		  try {
			  List<Payment> payments = new ArrayList<Payment>();

		      int rowNumber = 0;
			  while ((line = br.readLine()) != null) {
				  // skip header
			      if (rowNumber == 0) {
			         rowNumber++;
			         continue;
			      }
				  String[] strPayment = line.split(splitBy);
				  Payment payment = payments.stream().filter(o -> o.getOrderId().equals(strPayment[0])).findFirst().orElse(null);
				  if(strPayment.length>12 && !strPayment[13].toString().equals("")) {
			        if(payment !=null) {

			            Float paymentCredit = Float.parseFloat(payment.getPaymentCredit());
			            paymentCredit += Float.parseFloat(strPayment[3].equals("")?"0":strPayment[3]);
			    		paymentCredit += Float.parseFloat(strPayment[8].equals("")?"0":strPayment[8]);
			    		paymentCredit += Float.parseFloat(strPayment[6].equals("")?"0":strPayment[6]);
			    		paymentCredit += Float.parseFloat(strPayment[9].equals("")?"0":strPayment[9]);
			      	  	payment.setPaymentCredit(df.format(paymentCredit));

			      	  	Float paymentFees = Float.parseFloat(payment.getPaymentFees());
			      	  	paymentFees +=  Float.parseFloat(strPayment[5].equals("")?"0":strPayment[5]);
			    		paymentFees +=  Float.parseFloat(strPayment[4].equals("")?"0":strPayment[4]);
			    		
			      	  	Float shippingFees = Float.parseFloat(payment.getShippingFees());
			    		shippingFees =  Float.parseFloat(strPayment[7].equals("")?"0":strPayment[7]);
			      	  	payment.setShippingFees(df.format(shippingFees));
			      	  	
			      	  	Float otherFees = Float.parseFloat(payment.getOthersFees());
			      	  	otherFees =  Float.parseFloat(strPayment[9].equals("")?"0":strPayment[9]);
			      	  	payment.setOthersFees(df.format(otherFees));
			      	  	
			      	  	Float paymentDue = payment.getPaymentDue();
			      	  	paymentDue +=  Float.parseFloat(strPayment[10].equals("")?"0":strPayment[10]);
			      	  	payment.setPaymentDue(paymentDue);
			        }else {
			        	payment = new Payment();
			            Float paymentCredit = 0f;
			            Float paymentDue = 0f;
			            Float paymentFees = 0f;
			            Float shippingFees = 0f;
			            Float otherFees = 0f;
			    		payment.setOrderId(strPayment[0]);	    		
			    		
			    		paymentCredit = Float.parseFloat(strPayment[3].equals("")?"0":strPayment[3]);
			    		paymentCredit += Float.parseFloat(strPayment[8].equals("")?"0":strPayment[8]);
			    		paymentCredit += Float.parseFloat(strPayment[6].equals("")?"0":strPayment[6]);
			    		paymentCredit += Float.parseFloat(strPayment[9].equals("")?"0":strPayment[9]);
			    		
			    		paymentFees =  Float.parseFloat(strPayment[5].equals("")?"0":strPayment[5]);
			    		paymentFees +=  Float.parseFloat(strPayment[4].equals("")?"0":strPayment[4]);
			    		shippingFees =  Float.parseFloat(strPayment[7].equals("")?"0":strPayment[7]);
			    		otherFees =  Float.parseFloat(strPayment[9].equals("")?"0":strPayment[9]);

			    		payment.setOthersFees(df.format(otherFees));
			            payment.setPaymentFees(df.format(paymentFees));
			      	  	payment.setPaymentCredit(df.format(paymentCredit));
			      	  	payment.setShippingFees(df.format(shippingFees));
			      	  	paymentDue =  Float.parseFloat(strPayment[10].equals("")?"0":strPayment[10]);
			      	  	payment.setPaymentDue(paymentDue);
			      	  	if(strPayment.length==14) {
			      	  		payment.setStatus(strPayment[13]);	
			      	  	}
			      	  	
				  	  	
				  	  	payments.add(payment);	
			        }
				  }
				}
			  br.close();
		      return payments;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	  }else {
		  try {
		    	Sheet sheet;
		    	Workbook workbook = null;
		    	HSSFWorkbook hssfworkbook = null;
		    	if(excel.equals(".xlsx")) {
		  	      workbook = new XSSFWorkbook(is);
		  	      sheet = workbook.getSheetAt(0);	    		
		    	}else {
		    		hssfworkbook = new HSSFWorkbook(new POIFSFileSystem(is));
		    		sheet = hssfworkbook.getSheetAt(0);
		    	}
		      Iterator<Row> rows = sheet.iterator();

		      List<Payment> payments = new ArrayList<Payment>();

		      int rowNumber = 0;
		      while (rows.hasNext()) {
		    	  Row currentRow = rows.next();
		    	  // skip header
		    	  if (rowNumber == 0) {
		    		 rowNumber++;
		    		 continue;
		    	  }
		    	  
		    	  Payment payment = payments.stream().filter(o -> o.getOrderId().equals(currentRow.getCell(0).getStringCellValue())).findFirst().orElse(null);
		    	  if(!currentRow.getCell(13).getStringCellValue().equals("")) {
		    		if(payment !=null) {

		    			Float paymentCredit = Float.parseFloat(payment.getPaymentCredit());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(3).getStringCellValue().equals("")?"0":currentRow.getCell(3).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(8).getStringCellValue().equals("")?"0":currentRow.getCell(8).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(6).getStringCellValue().equals("")?"0":currentRow.getCell(6).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(9).getStringCellValue().equals("")?"0":currentRow.getCell(9).getStringCellValue());
		    			payment.setPaymentCredit(df.format(paymentCredit));

		    			Float paymentFees = Float.parseFloat(payment.getPaymentFees());
		    			paymentFees +=  Float.parseFloat(currentRow.getCell(5).getStringCellValue().equals("")?"0":currentRow.getCell(5).getStringCellValue());
		    			paymentFees +=  Float.parseFloat(currentRow.getCell(4).getStringCellValue().equals("")?"0":currentRow.getCell(4).getStringCellValue());
		    			
		    			Float shippingFees = Float.parseFloat(payment.getShippingFees());
		    			shippingFees =  Float.parseFloat(currentRow.getCell(7).getStringCellValue().equals("")?"0":currentRow.getCell(7).getStringCellValue());
		    			payment.setShippingFees(df.format(shippingFees));
		    			
		    			Float otherFees = Float.parseFloat(payment.getOthersFees());
		    			otherFees =  Float.parseFloat(currentRow.getCell(9).getStringCellValue().equals("")?"0":currentRow.getCell(9).getStringCellValue());
		    			payment.setOthersFees(df.format(otherFees));
		    			
		    			Float paymentDue = payment.getPaymentDue();
		    			paymentDue +=  Float.parseFloat(currentRow.getCell(10).getStringCellValue().equals("")?"0":currentRow.getCell(10).getStringCellValue());
		    			payment.setPaymentDue(paymentDue);
		    		}else {
		    			payment = new Payment();
		    			Float paymentCredit = 0f;
		    			Float paymentDue = 0f;
		    			Float paymentFees = 0f;
		    			Float shippingFees = 0f;
		    			Float otherFees = 0f;
		    			payment.setOrderId(currentRow.getCell(0).getStringCellValue());	    		
		    			
		    			paymentCredit = Float.parseFloat(currentRow.getCell(3).getStringCellValue().equals("")?"0":currentRow.getCell(3).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(8).getStringCellValue().equals("")?"0":currentRow.getCell(8).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(6).getStringCellValue().equals("")?"0":currentRow.getCell(6).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(9).getStringCellValue().equals("")?"0":currentRow.getCell(9).getStringCellValue());
		    			
		    			paymentFees =  Float.parseFloat(currentRow.getCell(5).getStringCellValue().equals("")?"0":currentRow.getCell(5).getStringCellValue());
		    			paymentFees +=  Float.parseFloat(currentRow.getCell(4).getStringCellValue().equals("")?"0":currentRow.getCell(4).getStringCellValue());
		    			shippingFees =  Float.parseFloat(currentRow.getCell(7).getStringCellValue().equals("")?"0":currentRow.getCell(7).getStringCellValue());
		    			otherFees =  Float.parseFloat(currentRow.getCell(9).getStringCellValue().equals("")?"0":currentRow.getCell(9).getStringCellValue());

		    			payment.setOthersFees(df.format(otherFees));
		    			payment.setPaymentFees(df.format(paymentFees));
		    			payment.setPaymentCredit(df.format(paymentCredit));
		    			payment.setShippingFees(df.format(shippingFees));
		    			paymentDue =  Float.parseFloat(currentRow.getCell(10).equals("")?"0":currentRow.getCell(10).getStringCellValue());
		    			payment.setPaymentDue(paymentDue);
		    			
		    			payment.setStatus(currentRow.getCell(13).getStringCellValue());	
		    			
		    			
		    			
		    			payments.add(payment);	
		    		}
		    	  }
		    	}

		      if(workbook != null) {
		    	  workbook.close();   
		      }
		      if(hssfworkbook != null) {
		    	  hssfworkbook.close();   
		      }

		      return payments;
		    } catch (IOException e) {
		      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		    }
	  }
	  	
	   return null;
	  
	}

  public static List<Payment> excelLazadaToTransaction(InputStream is, String excel, List<Client> clients) {
	  DecimalFormat df = new DecimalFormat("0.00");
    try {
    	Sheet sheet;
    	Workbook workbook = null;
    	HSSFWorkbook hssfworkbook = null;
    	if(excel.equals(".xlsx")) {
  	      workbook = new XSSFWorkbook(is);
  	      sheet = workbook.getSheetAt(0);	    		
    	}else {
    		hssfworkbook = new HSSFWorkbook(new POIFSFileSystem(is));
    		sheet = hssfworkbook.getSheetAt(0);
    	}
      Iterator<Row> rows = sheet.iterator();

      List<Payment> payments = new ArrayList<Payment>();

      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }


  	  	if(!currentRow.getCell(64).getStringCellValue().toLowerCase().equals("canceled") && !currentRow.getCell(64).getStringCellValue().toLowerCase().trim().equals("")) {
  	  		
	        Payment payment = payments.stream().filter(o -> o.getOrderId().equals(currentRow.getCell(12).getStringCellValue())).findFirst().orElse(null);
	        if(payment !=null) {
	
	            Float paymentCredit = Float.parseFloat(payment.getPaymentCredit());
	    		paymentCredit += Float.parseFloat(currentRow.getCell(46).getStringCellValue()) ;
	      	  	payment.setPaymentCredit(df.format(paymentCredit));
	
	      	  	Float shippingFees = Float.parseFloat(payment.getShippingFees());
	    		shippingFees =  Float.parseFloat(currentRow.getCell(48).getStringCellValue());
	      	  	payment.setShippingFees(df.format(shippingFees));
	      	  	Float paymentDue = paymentCredit;
	      	  	payment.setPaymentDue(paymentDue);
	        }else {
	        	payment = new Payment();
	            Float paymentCredit = 0f;
	            Float paymentDue = 0f;
	            Float paymentFees = 0f;
	            Float shippingFees = 0f;
	            PaymentType type = new PaymentType();
	            type.setId(4);
	            payment.setPaymentType(type);
	            ProviderSource provider = new ProviderSource();
	            provider.setId(1);
	            payment.setProvider(provider);
	            payment.setFreeShipping(false);
	            payment.setBalance("0.00");
	            payment.setDiscount("0.00");
	            payment.setPaymentFees("0.00");
	
	        	Date date = null;
	        	java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
	//        	java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("dd MMM yyyy hh:mm");
	        	String dateString = currentRow.getCell(8).getStringCellValue();
	
				date = new Date(dateString);
	//    		try {
	//    			
	//    			date = dateFormat.parse();
	//    		} catch (ParseException e) {
	//    			// TODO Auto-generated catch block
	//    			e.printStackTrace();
	//    		}
	    		payment.setDate(date);
	           
	
	    		payment.setOrderId(currentRow.getCell(12).getStringCellValue());
	    		paymentCredit = Float.parseFloat(currentRow.getCell(46).getStringCellValue());
	    		shippingFees =  Float.parseFloat(currentRow.getCell(48).getStringCellValue());
	//    		client = clients.get(rowNumber);

	    		String phoneno = currentRow.getCell(37).getStringCellValue();
		        if(phoneno.equals("")) {
		        	phoneno = currentRow.getCell(25).getStringCellValue();
		        }
	        	Client client = new Client();
	        	
	    		for(Client c : clients) {
	    			if(c.getId().equals(phoneno)) {
	    				
	    				payment.setClient(c);
	    				break;
	    			}
	    		}
	      	  	
	      	  	payment.setPaymentCredit(df.format(paymentCredit));
	      	  	payment.setShippingFees(df.format(shippingFees));
	      	  	payment.setStatus(currentRow.getCell(64).getStringCellValue());
	      	  	paymentDue = paymentCredit;
	      	  	payment.setPaymentDue(paymentDue);

	  	  	  Payment p = payments.stream().filter(o -> o.getOrderId().equals(currentRow.getCell(12).getStringCellValue())).findFirst().orElse(null);
	  	  	  if(p ==null) {
	  	  		payments.add(payment);	  
	  	  	  }
	        }
	     }
        rowNumber++;
        
	  	  
	  	
        
      }

      if(workbook != null) {
    	  workbook.close();   
      }
      if(hssfworkbook != null) {
    	  hssfworkbook.close();   
      }

      return payments;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
}
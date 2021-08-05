package com.dynamike.pos.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.google.common.collect.Maps;


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
  public static void checkCellColumn(Sheet sheet,List<String> colNames,HashMap<String,Integer> cellno) {
	  Iterator<Cell> cellsInRow = sheet.getRow(0).iterator();

      int cellIdx = 0;
      int size = colNames.size();
      while (cellsInRow.hasNext()) { 
          if(colNames.indexOf(sheet.getRow(0).getCell(cellIdx).getStringCellValue()) != -1) {
        	  cellno.put(colNames.get(colNames.indexOf(sheet.getRow(0).getCell(cellIdx).getStringCellValue())), cellIdx);
        	  size--;
        	  if(size ==0) {
        		  break;
        	  }
          }
          cellIdx++;
      }	
  }
  public static List<OrderList> excelLazadaToOrderList(InputStream is, String excel) {
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

	      List<OrderList> orders = new ArrayList<OrderList>();
	      List<String> colNames = Arrays.asList("status","orderNumber","paidPrice","itemName");
	      HashMap<String,Integer> cellno = Maps.newLinkedHashMap();

	      checkCellColumn(sheet,colNames,cellno);
	      
	      int rowNumber = 0;
	      while (rows.hasNext()) {
	        Row currentRow = rows.next();
	        
	        if(!currentRow.getCell(cellno.get("status")).getStringCellValue().toLowerCase().equals("canceled") && !currentRow.getCell(cellno.get("status")).getStringCellValue().toLowerCase().trim().equals("")) {
  		        // skip header
  		        if (rowNumber == 0) {
  		          rowNumber++;
  		          continue;
  		        }  	
  		        String itemId = currentRow.getCell(cellno.get("itemName")).getStringCellValue().split("-")[0].trim().split(" ")[0].trim();
  		        itemId = itemId.split(" ")[0].trim();
  		        OrderList order = orders.stream().filter(o -> o.getInvoiceId().equals(currentRow.getCell(cellno.get("orderNumber")).getStringCellValue()) 
  		        		&& o.getItemId().equals(currentRow.getCell(cellno.get("itemName")).getStringCellValue().split("-")[0].trim().split(" ")[0].trim())
  		        		).findFirst().orElse(null);
  		        if(order !=null) {
  			        Float paymentCredit = Float.parseFloat(order.getSellingPrice());
  			        paymentCredit += Float.parseFloat(currentRow.getCell(cellno.get("paidPrice")).getStringCellValue()) ;
  		        	order.setSellingPrice(df.format(paymentCredit));
  	        		order.setQuantity(order.getQuantity()+1);
  		        }else {
  		        	order = new OrderList();
  			        order.setQuantity(1);
  			        Float paymentCredit = Float.parseFloat(currentRow.getCell(cellno.get("paidPrice")).getStringCellValue()) ;
  			        order.setSellingPrice(df.format(paymentCredit));
  			        order.setInvoiceId(currentRow.getCell(cellno.get("orderNumber")).getStringCellValue());
  			        order.setItemId(itemId);	
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
	      


	      List<String> colNames = Arrays.asList("status","orderNumber","itemName","paidPrice",
	    		  "sellerDiscountTotal","shippingFee","billingPhone","shippingPhone","createTime","customerName",
	    		  "customerEmail","shippingAddress","billingAddr");
	      HashMap<String,Integer> cellno = Maps.newLinkedHashMap();

	      checkCellColumn(sheet,colNames,cellno);

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
	        String phoneno = currentRow.getCell(cellno.get("billingPhone")).getStringCellValue();
	        if(phoneno.equals("")) {
	        	phoneno = currentRow.getCell(cellno.get("shippingPhone")).getStringCellValue();
	        }
	        client.setId(phoneno);
	        client.setContactNo(phoneno);
	        client.setName(currentRow.getCell(cellno.get("customerName")).getStringCellValue() + currentRow.getCell(cellno.get("customerEmail")).getStringCellValue());
	        client.setShippingAddress(currentRow.getCell(cellno.get("shippingAddress")).getStringCellValue());
	        client.setBillingAddress(currentRow.getCell(cellno.get("billingAddr")).getStringCellValue());

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
			      	  	
			      	  	Float paymentDue = Float.parseFloat(payment.getPaymentDue());
			      	  	paymentDue +=  Float.parseFloat(strPayment[10].equals("")?"0":strPayment[10]);
			      	  	payment.setPaymentDue(df.format(paymentDue));
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
			      	  	payment.setPaymentDue(df.format(paymentDue));
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

		      List<String> colNames = Arrays.asList("Payout Status","Payout Amount","Other","Promotions",
		    		  "Shipping Cost","Customer Shipping Fee","Payment Fee","Commission","Unit Price","Order Status",
		    		  "Statement Numbers","Order No.");
		      HashMap<String,Integer> cellno = Maps.newLinkedHashMap();

		      checkCellColumn(sheet,colNames,cellno);
		      
		      int rowNumber = 0;
		      while (rows.hasNext()) {
		    	  Row currentRow = rows.next();
		    	  // skip header
		    	  if (rowNumber == 0) {
		    		 rowNumber++;
		    		 continue;
		    	  }
		    	  
		    	  Payment payment = payments.stream().filter(o -> o.getOrderId().equals(currentRow.getCell(cellno.get("Order No.")).getStringCellValue())).findFirst().orElse(null);
		    	  if(!currentRow.getCell(cellno.get("Payout Status")).getStringCellValue().equals("")) {
		    		if(payment !=null) {

		    			Float paymentCredit = Float.parseFloat(payment.getPaymentCredit());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(cellno.get("Unit Price")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Unit Price")).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(cellno.get("Promotions")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Promotions")).getStringCellValue());
		    			payment.setPaymentCredit(df.format(paymentCredit));
		    			
		    			Float commissionFees = Float.parseFloat(payment.getCommissionFees());
		    			commissionFees = Float.parseFloat(currentRow.getCell(cellno.get("Commission")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Commission")).getStringCellValue());
		    			payment.setCommissionFees(df.format(commissionFees));

		    			
		    			Float paymentFees = Float.parseFloat(payment.getPaymentFees());
		    			paymentFees +=  Float.parseFloat(currentRow.getCell(cellno.get("Payment Fee")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Payment Fee")).getStringCellValue());
		    			payment.setPaymentFees(df.format(paymentFees));
		    			
		    			Float shippingFees = Float.parseFloat(payment.getShippingFees());
		    			shippingFees +=  Float.parseFloat(currentRow.getCell(cellno.get("Customer Shipping Fee")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Customer Shipping Fee")).getStringCellValue());
		    			shippingFees +=  Float.parseFloat(currentRow.getCell(cellno.get("Shipping Cost")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Shipping Cost")).getStringCellValue());
		    			payment.setShippingFees(df.format(shippingFees));
		    			
		    			Float otherFees = Float.parseFloat(payment.getOthersFees());
		    			otherFees +=  Float.parseFloat(currentRow.getCell(cellno.get("Other")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Other")).getStringCellValue());
		    			payment.setOthersFees(df.format(otherFees));
		    			
		    			Float paymentDue = Float.parseFloat(payment.getPaymentDue());
		    			paymentDue +=  Float.parseFloat(currentRow.getCell(cellno.get("Payout Amount")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Payout Amount")).getStringCellValue());
		    			payment.setPaymentDue(df.format(paymentDue));
		    		}else {
		    			payment = new Payment();
		    			Float paymentCredit = 0f;
		    			Float commissionFees = 0f;
		    			Float paymentDue = 0f;
		    			Float paymentFees = 0f;
		    			Float shippingFees = 0f;
		    			Float otherFees = 0f;
		    			payment.setOrderId(currentRow.getCell(cellno.get("Order No.")).getStringCellValue());	    		
		    			
		    			paymentCredit = Float.parseFloat(currentRow.getCell(cellno.get("Unit Price")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Unit Price")).getStringCellValue());
		    			paymentCredit += Float.parseFloat(currentRow.getCell(cellno.get("Promotions")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Promotions")).getStringCellValue());
		    			commissionFees = Float.parseFloat(currentRow.getCell(cellno.get("Commission")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Commission")).getStringCellValue());
		    			
		    			paymentFees =  Float.parseFloat(currentRow.getCell(cellno.get("Payment Fee")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Payment Fee")).getStringCellValue());
		    			
		    			shippingFees =  Float.parseFloat(currentRow.getCell(cellno.get("Customer Shipping Fee")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Customer Shipping Fee")).getStringCellValue());
		    			shippingFees +=  Float.parseFloat(currentRow.getCell(cellno.get("Shipping Cost")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Shipping Cost")).getStringCellValue());
		    			
		    			otherFees =  Float.parseFloat(currentRow.getCell(cellno.get("Other")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("Other")).getStringCellValue());

		    			payment.setOthersFees(df.format(otherFees));
		    			payment.setPaymentFees(df.format(paymentFees));
		    			payment.setPaymentCredit(df.format(paymentCredit));
		    			payment.setCommissionFees(df.format(commissionFees));
		    			payment.setShippingFees(df.format(shippingFees));
		    			paymentDue =  Float.parseFloat(currentRow.getCell(cellno.get("Payout Amount")).equals("")?"0":currentRow.getCell(cellno.get("Payout Amount")).getStringCellValue());
		    			payment.setPaymentDue(df.format(paymentDue));
		    			
		    			payment.setStatus(currentRow.getCell(cellno.get("Payout Status")).getStringCellValue());	
		    			
		    			
		    			
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

      List<String> colNames = Arrays.asList("status","orderNumber","itemName","paidPrice","sellerDiscountTotal","shippingFee","billingPhone","shippingPhone","createTime");
      HashMap<String,Integer> cellno = Maps.newLinkedHashMap();

      checkCellColumn(sheet,colNames,cellno);
      int rowNumber = 0;
      
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }


  	  	if(!currentRow.getCell(cellno.get("status")).getStringCellValue().toLowerCase().equals("canceled") && !currentRow.getCell(cellno.get("status")).getStringCellValue().toLowerCase().trim().equals("")) {
  	  		
	        Payment payment = payments.stream().filter(o -> o.getOrderId().equals(currentRow.getCell(cellno.get("orderNumber")).getStringCellValue())).findFirst().orElse(null);
	        if(payment !=null) {
	
	            Float paymentCredit = Float.parseFloat(payment.getPaymentCredit());
	    		paymentCredit += Float.parseFloat(currentRow.getCell(cellno.get("paidPrice")).getStringCellValue()) ;
	      	  	payment.setPaymentCredit(df.format(paymentCredit));
	
	      	  	Float shippingFees = Float.parseFloat(payment.getShippingFees());
	      	  	shippingFees +=  Float.parseFloat(currentRow.getCell(cellno.get("shippingFee")).getStringCellValue());	
	    		
	      	  	
	      	  	Float sellerDiscountTotal = Float.parseFloat(currentRow.getCell(cellno.get("sellerDiscountTotal")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("sellerDiscountTotal")).getStringCellValue());
	    		
	      	  	payment.setShippingFees(df.format(shippingFees));
	      	  	Float paymentDue = paymentCredit;
	      	  	payment.setPaymentDue(df.format(paymentDue));
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
	        	String dateString = currentRow.getCell(cellno.get("createTime")).getStringCellValue();
	        	
	
				date = new Date(dateString);
	//    		try {
	//    			
	//    			date = dateFormat.parse();
	//    		} catch (ParseException e) {
	//    			// TODO Auto-generated catch block
	//    			e.printStackTrace();
	//    		}
	    		payment.setDate(date);
	           
	    		
	    		payment.setOrderId(currentRow.getCell(cellno.get("orderNumber")).getStringCellValue());
	    		paymentCredit = Float.parseFloat(currentRow.getCell(cellno.get("paidPrice")).getStringCellValue()) ;
	    		shippingFees =  Float.parseFloat(currentRow.getCell(cellno.get("shippingFee")).getStringCellValue());
	    		Float sellerDiscountTotal = Float.parseFloat(currentRow.getCell(cellno.get("sellerDiscountTotal")).getStringCellValue().equals("")?"0":currentRow.getCell(cellno.get("sellerDiscountTotal")).getStringCellValue());
	    		
	    		
	//    		client = clients.get(rowNumber);
	    		
	    		String phoneno = currentRow.getCell(cellno.get("billingPhone")).getStringCellValue();
		        if(phoneno.equals("")) {
		        	phoneno = currentRow.getCell(cellno.get("shippingPhone")).getStringCellValue();
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
	      	  	payment.setStatus(currentRow.getCell(cellno.get("status")).getStringCellValue());
	      	  	paymentDue = paymentCredit;
	      	  	payment.setPaymentDue(df.format(paymentDue));

	  	  	  Payment p = payments.stream().filter(o -> o.getOrderId().equals(currentRow.getCell(cellno.get("orderNumber")).getStringCellValue())).findFirst().orElse(null);
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
package com.dynamike.pos.service;
import java.io.IOException;
import java.io.InputStream;
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


public class ExcelShopeeHelper {
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "Id", "Title", "Description", "Published" };
  static String SHEET = "Tutorials";

  
  
  public static boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }
  public static List<OrderList> excelShopeeToOrderList(InputStream is, String excel) {
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
	    	boolean orderstatus = false;
	        Row currentRow = rows.next();

	        // skip header
	        if (rowNumber == 0) {
	          rowNumber++;
	          continue;
	        }

	        Iterator<Cell> cellsInRow = currentRow.iterator();

	        OrderList order = new OrderList();
	        
	        int cellIdx = 0;
	        while (cellsInRow.hasNext()) {
	          Cell currentCell = cellsInRow.next();

	          switch (cellIdx) {
	          case 0: // orderId
	        	  order.setInvoiceId(currentCell.getStringCellValue());
	            break;
	          case 1: // OrderStatus | Cancelled
	        	  if(currentCell.getStringCellValue().equals("Cancelled")) {
	        		  orderstatus= true;
	        	  }
	            break;

	          case 10: // Order Paid Time
	            break;

	          case 12: // Product Name
	        	  order.setItemId(currentCell.getStringCellValue().split("-")[0].trim());
	            break;

	          case 17: // Quantity
	        	  Integer qty = (int) currentCell.getNumericCellValue();
	        	  order.setQuantity(qty);
	            break;

	          case 26: // Seller Voucher
	            break;
	            
	          case 34: // Total Amount
	              break;
	              
	          case 35: // Buyer Paid Shipping Fee
	              break;
	              
	          case 36: // Transaction Fee
	            break;

	          case 37: // Commission Fee
	            break;
	            
	          case 38: // Service Fee
	            break;

	          case 40: // Estimated Shipping Fee
	            break;

	          case 41: // Username (Buyer)
	            break;
	            
	          case 42: // Receiver Name
	            break;
	            
	          case 43: // Phone Number
	            break;
	            
	          case 44: // Delivery Address
	            break;

	          case 51: // SG Buyer Info
	            break;
	            
	          default:
	            break;
	          }

	          cellIdx++;
	        }

	        if(!orderstatus) {
	        	orders.add(order);	
	        }
	        
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
  public static List<Client> excelShopeeToClient(InputStream is, String excel) {
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

	        Client client = new Client();
	        
	        int cellIdx = 0;
	        Boolean isSG = false;
	        while (cellsInRow.hasNext()) {
	          Cell currentCell = cellsInRow.next();

	          switch (cellIdx) {
	          case 0: // orderId
	            break;
	          case 1: // OrderStatus | Cancelled
	            break;

	          case 10: // Order Paid Time
	            break;

	          case 12: // Product Name
	            break;

	          case 17: // Quantity
	            break;

	          case 26: // Seller Voucher
	            break;
	            
	          case 34: // Total Amount
	              break;
	              
	          case 35: // Buyer Paid Shipping Fee
	              break;
	              
	          case 36: // Transaction Fee
	            break;

	          case 37: // Commission Fee
	            break;
	            
	          case 38: // Service Fee
	            break;

	          case 40: // Estimated Shipping Fee
	            break;

	          case 41: // Username (Buyer)
	        	client.setName(currentCell.getStringCellValue());
	            break;
	            
	          case 42: // Receiver Name
	        	client.setName(client.getName() + "/"+currentCell.getStringCellValue());
	            break;
	            
	          case 43: // Phone Number
	        	  client.setId(currentCell.getStringCellValue());
	        	  client.setContactNo(currentCell.getStringCellValue());
	        	  if(client.getContactNo().equals("60355453789")) {
	        		  isSG = true;
	        	  }
	            break;
	            
	          case 44: // Delivery Address
	        	  client.setBillingAddress(currentCell.getStringCellValue());
	            break;

	          case 51: // SG Buyer Info
	        	  if(isSG) {
	        		String[] BuyerInfo =  currentCell.getStringCellValue().split("]");
	        		client.setName(BuyerInfo[1].split(": ")[1]);
	        		client.setId(BuyerInfo[2].split(": ")[1]);
	        		client.setContactNo(BuyerInfo[2].split(": ")[1]);
	        	  }
	            break;
	            
	          default:
	            break;
	          }

	          cellIdx++;
	        }

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

  public static List<Payment> excelShopeeToTransaction(InputStream is, String excel, List<Client> clients) {
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

        Iterator<Cell> cellsInRow = currentRow.iterator();

        Payment payment = new Payment();

        Boolean isSG = false;
        Boolean isCancelled = false;
        Client client = new Client();
        Float paymentCredit = 0f;
        Float paymentDue = 0f;
        Float voucher = 0f;
        Float paymentFees = 0f;
        Float shippingFees = 0f;
        PaymentType type = new PaymentType();
        type.setId(4);
        payment.setPaymentType(type);
        ProviderSource provider = new ProviderSource();
        provider.setId(2);
        payment.setProvider(provider);
        payment.setFreeShipping(false);
        payment.setBalance("0.00");
        payment.setDiscount("0.00");
        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();
          
              switch (cellIdx) {
              case 0: // orderId
            	  payment.setOrderId(currentCell.getStringCellValue());
                break;
              case 1: // OrderStatus | Cancelled
            	  if(currentCell.getStringCellValue().equals("Cancelled")) {
            		  isCancelled = true;
            	  }
    	      	  payment.setStatus(currentCell.getStringCellValue());
                break;

              case 10: // Order Paid Time
            	  java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            	  String dateString = currentCell.getStringCellValue();
            	  Date date = null;
    			try {
    				date = dateFormat.parse(dateString);
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
//            	  String newDate = dateFormat.format(date);
    			if(date !=null) {
    				payment.setDate(date);
    			}
            	  
                break;

              case 12: // Product Name
                break;

              case 17: // Quantity
                break;

              case 26: // Seller Voucher
            	  voucher = Float.parseFloat(currentCell.getStringCellValue());
                break;
                
              case 34: // Total Amount
            	  paymentCredit = Float.parseFloat(currentCell.getStringCellValue());
                  break;
              case 35: // Buyer Paid Shipping Fee
                  break;
                  
              case 36: // Transaction Fee
            	  paymentFees = Float.parseFloat(currentCell.getStringCellValue());
                break;

              case 37: // Commission Fee
            	  paymentFees += Float.parseFloat(currentCell.getStringCellValue());
                break;
                
              case 38: // Service Fee
            	  paymentFees += Float.parseFloat(currentCell.getStringCellValue());
                break;

              case 40: // Estimated Shipping Fee
                break;

              case 41: // Username (Buyer)
//                tutorial.setPublished(currentCell.getBooleanCellValue());
                break;
                
              case 42: // Receiver Name
//            	  client.setBillingAddress(currentCell.getStringCellValue());
                break;
                
              case 43: // Phone Number
//            	  client.setId(currentCell.getStringCellValue());
            	  client.setContactNo(currentCell.getStringCellValue());
            	  if(client.getContactNo().equals("60355453789")) {
            		  isSG = true;
            	  }
                break;
                
              case 44: // Delivery Address
            	  client.setBillingAddress(currentCell.getStringCellValue());
                break;

              case 51: // SG Buyer Info
            	  if(isSG) {
            		String[] BuyerInfo =  currentCell.getStringCellValue().split("]");
//            		client.setName(BuyerInfo[1].split(": ")[1]);
//            		client.setId(BuyerInfo[2].split(": ")[1]);
//            		client.setContactNo(BuyerInfo[2].split(": ")[1]);

              	  	payment.setOrderId(payment.getOrderId() + "/" + BuyerInfo[3].split(": ")[1]);
            	  }
                break;
                
              default:
                break;
              }  
          
          cellIdx++;
        }

//        client = ;
        
	  	  payment.setClient(clients.get(rowNumber-1));
	  	  payment.setPaymentCredit(df.format(paymentCredit));
	  	  payment.setPaymentFees(df.format(paymentFees));
	  	  paymentDue = paymentCredit - paymentFees;
	  	  paymentDue = paymentDue -voucher;
	  	  payment.setPaymentDue(paymentDue);
	  	  
	  	  Payment p = payments.stream().filter(o -> o.getOrderId().equals(payment.getOrderId())).findFirst().orElse(null);
	  	  if(p ==null) {
	  		if(!isCancelled) {
	  			payments.add(payment);
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
package com.dynamike.pos.service;
import java.io.IOException;
import java.io.InputStream;
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
  public static List<OrderList> excelShopeeToCancelledOrderList(InputStream is, String excel) {
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
	      
	      List<String> colNames = Arrays.asList(
	      		  "Order ID","Order Status","Order Paid Time", "Phone Number","Delivery Address","Remark from buyer","Product Subtotal",
	      		  "Total Amount","Transaction Fee","Commission Fee","Service Fee","Grand Total","Product Name","Quantity");
	      HashMap<String,Integer> cellno = Maps.newLinkedHashMap();

	      checkCellColumn(sheet,colNames,cellno);

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
	        order.setInvoiceId(currentRow.getCell(cellno.get("Order ID")).getStringCellValue());
	        if(currentRow.getCell(cellno.get("Order Status")).getStringCellValue().equals("Cancelled")) {
    		  orderstatus= true;
    	  	}
	        order.setItemId(currentRow.getCell(cellno.get("Product Name")).getStringCellValue().split("-")[0].trim());
    	  	Integer qty = (int) currentRow.getCell(cellno.get("Quantity")).getNumericCellValue();
    	  	order.setQuantity(qty);

	       	 if(currentRow.getCell(cellno.get("Phone Number")).getStringCellValue().equals("60355453789")) {
	       		String[] BuyerInfo =  currentRow.getCell(cellno.get("Remark from buyer")).getStringCellValue().split("]");
	       		
	       		for(int i =0;i<BuyerInfo.length;i++) {
	    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Order SN")) {
	    				 order.setInvoiceId(order.getInvoiceId().trim() + "/" + BuyerInfo[i].split(": ")[1].toString().trim());
	    			 }
	    			 
	    		 }
	       	 }
	        if(orderstatus) {
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
  public static List<OrderList> excelShopeeToOrderList(InputStream is, String excel) {
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
	      
	      List<String> colNames = Arrays.asList(
	      		  "Order ID","Order Status","Order Paid Time", "Phone Number","Delivery Address","Remark from buyer","Product Subtotal",
	      		  "Total Amount","Transaction Fee","Commission Fee","Service Fee","Grand Total","Product Name","Quantity");
	      HashMap<String,Integer> cellno = Maps.newLinkedHashMap();

	      checkCellColumn(sheet,colNames,cellno);

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
	        order.setInvoiceId(currentRow.getCell(cellno.get("Order ID")).getStringCellValue());
	        if(currentRow.getCell(cellno.get("Order Status")).getStringCellValue().equals("Cancelled")) {
      		  orderstatus= true;
      	  	}
	        order.setItemId(currentRow.getCell(cellno.get("Product Name")).getStringCellValue().split("-")[0].trim());
      	  	Integer qty = (int) currentRow.getCell(cellno.get("Quantity")).getNumericCellValue();
      	  	order.setQuantity(qty);
    	  	Float sellingPrice = Float.parseFloat(currentRow.getCell(cellno.get("Product Subtotal")).getStringCellValue());
    	  	order.setSellingPrice(df.format(sellingPrice));
	        

	       	 if(currentRow.getCell(cellno.get("Phone Number")).getStringCellValue().equals("60355453789")) {
	       		String[] BuyerInfo =  currentRow.getCell(cellno.get("Remark from buyer")).getStringCellValue().split("]");
	       		for(int i =0;i<BuyerInfo.length;i++) {
	    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Order SN")) {
	    				 order.setInvoiceId(order.getInvoiceId().trim() + "/" + BuyerInfo[i].split(": ")[1].toString().trim());
	    			 }
	    		 }
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
	      
	      List<String> colNames = Arrays.asList("Receiver Name","Username (Buyer)",
	      		  "Order ID","Order Status","Order Paid Time", "Phone Number","Delivery Address","Remark from buyer",
	      		  "Total Amount","Transaction Fee","Commission Fee","Service Fee","Grand Total");
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

	        
	        Client client = new Client();
	        
	        int cellIdx = 0;
	        Boolean isSG = false;
	        client.setName(currentRow.getCell(cellno.get("Username (Buyer)")).getStringCellValue() +"/" + currentRow.getCell(cellno.get("Receiver Name")).getStringCellValue());
	        client.setBillingAddress(currentRow.getCell(cellno.get("Delivery Address")).getStringCellValue());
	        client.setContactNo(currentRow.getCell(cellno.get("Phone Number")).getStringCellValue());
	        if(client.getContactNo().equals("60355453789")) {
	        	isSG = true;
	        }
	        if(isSG) {
	        	String[] BuyerInfo =  currentRow.getCell(cellno.get("Remark from buyer")).getStringCellValue().split("]");
	        	for(int i =0;i<BuyerInfo.length;i++) {
	    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Buyer name")) {
	    				 client.setName(BuyerInfo[i].split(": ")[1]);		 
	    			 }
	    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Buyer phone")) {
	    				 client.setId(BuyerInfo[i].split(": ")[1]);
	    				 client.setContactNo(BuyerInfo[i].split(": ")[1]);		
	    			 }
	    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Buyer address")) {
	    				 client.setShippingAddress(BuyerInfo[i].split(": ")[1]);		 
	    			 }
	    		 }
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

      List<String> colNames = Arrays.asList(
    		  "Order ID","Order Status","Order Paid Time", "Phone Number","Delivery Address","Remark from buyer","Buyer Paid Shipping Fee",
    		  "Total Amount","Transaction Fee","Commission Fee","Service Fee","Grand Total");
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

        Payment payment = new Payment();

        Boolean isSG = false;
        Boolean isCancelled = false;
        Client client = new Client();
        Float paymentCredit = 0f;
        Float paymentDue = 0f;
        Float voucher = 0f;
        Float paymentFees = 0f;
        Float otherFees = 0f;
        Float commissionFees = 0f;
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
        payment.setOrderId(currentRow.getCell(cellno.get("Order ID")).getStringCellValue());
        if(currentRow.getCell(cellno.get("Order Status")).getStringCellValue().equals("Cancelled")) {
  		  isCancelled = true;
  	  	}
    	 payment.setStatus(currentRow.getCell(cellno.get("Order Status")).getStringCellValue());
    	 java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
    	 String dateString = currentRow.getCell(cellno.get("Order Paid Time")).getStringCellValue();
    	 Date date = null;
    	 try {
		 	date = dateFormat.parse(dateString);
    	 } catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
    	 }
    	 if(date !=null) {
			payment.setDate(date);
    	 }
    	 
    	 paymentCredit = Float.parseFloat(currentRow.getCell(cellno.get("Grand Total")).getStringCellValue());
    	 paymentFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Transaction Fee")).getStringCellValue());
    	 commissionFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Commission Fee")).getStringCellValue());
    	 shippingFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Buyer Paid Shipping Fee")).getStringCellValue());
    	 otherFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Service Fee")).getStringCellValue());
    	 client.setBillingAddress(currentRow.getCell(cellno.get("Delivery Address")).getStringCellValue());
    	 client.setContactNo(currentRow.getCell(cellno.get("Phone Number")).getStringCellValue());
    	 if(client.getContactNo().equals("60355453789")) {
    		 isSG = true;
    	 }
    	 if(isSG) {
    		 String[] BuyerInfo =  currentRow.getCell(cellno.get("Remark from buyer")).getStringCellValue().split("]");
    		 
    		 for(int i =0;i<BuyerInfo.length;i++) {
    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Order SN")) {
    				 payment.setOrderId(payment.getOrderId().trim() + "/" + BuyerInfo[i].split(": ")[1].toString().trim());		 
    			 }

    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Buyer name")) {
    				 client.setName(BuyerInfo[i].split(": ")[1]);		 
    			 }
    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Buyer phone")) {
    				 client.setId(BuyerInfo[i].split(": ")[1]);
    				 client.setContactNo(BuyerInfo[i].split(": ")[1]);		
    			 }
    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Buyer address")) {
    				 client.setShippingAddress(BuyerInfo[i].split(": ")[1]);		 
    			 }
    		 }
    	 }

	  	  payment.setClient(clients.get(rowNumber-1));
	  	  paymentCredit =  paymentCredit + shippingFees;
	  	  payment.setPaymentCredit(df.format(paymentCredit));
	  	  payment.setPaymentFees(df.format(paymentFees));
	  	  payment.setCommissionFees(df.format(commissionFees));
	  	  payment.setOthersFees(df.format(otherFees));
	  	  paymentDue = paymentCredit + paymentFees + otherFees + commissionFees;
//	  	  paymentDue = paymentDue -voucher;
	  	  payment.setPaymentDue(df.format(paymentDue));
	  	  
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
  public static List<Payment> excelShopeeToCancelledTransaction(InputStream is, String excel, List<Client> clients) {
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

      List<String> colNames = Arrays.asList(
    		  "Order ID","Order Status","Order Paid Time", "Phone Number","Delivery Address","Remark from buyer","Buyer Paid Shipping Fee",
    		  "Total Amount","Transaction Fee","Commission Fee","Service Fee","Grand Total");
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

        Payment payment = new Payment();

        Boolean isSG = false;
        Boolean isCancelled = false;
        Client client = new Client();
        Float paymentCredit = 0f;
        Float paymentDue = 0f;
        Float voucher = 0f;
        Float paymentFees = 0f;
        Float otherFees = 0f;
        Float comissionFees = 0f;        
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
        payment.setOrderId(currentRow.getCell(cellno.get("Order ID")).getStringCellValue());
        if(currentRow.getCell(cellno.get("Order Status")).getStringCellValue().equals("Cancelled")) {
  		  isCancelled = true;
  	  	}
    	 payment.setStatus(currentRow.getCell(cellno.get("Order Status")).getStringCellValue());
    	 java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
    	 String dateString = currentRow.getCell(cellno.get("Order Paid Time")).getStringCellValue();
    	 Date date = null;
    	 try {
		 	date = dateFormat.parse(dateString);
    	 } catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
    	 }
    	 if(date !=null) {
			payment.setDate(date);
    	 }
    	 
    	 paymentCredit = Float.parseFloat(currentRow.getCell(cellno.get("Grand Total")).getStringCellValue());
    	 paymentFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Transaction Fee")).getStringCellValue());
    	 comissionFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Commission Fee")).getStringCellValue());
    	 otherFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Service Fee")).getStringCellValue());
    	 shippingFees = Float.parseFloat("-"+currentRow.getCell(cellno.get("Buyer Paid Shipping Fee")).getStringCellValue());
    	 
    	 client.setBillingAddress(currentRow.getCell(cellno.get("Delivery Address")).getStringCellValue());
    	 client.setContactNo(currentRow.getCell(cellno.get("Phone Number")).getStringCellValue());
    	 if(client.getContactNo().equals("60355453789")) {
    		 isSG = true;
    	 }
    	 if(isSG) {
    		 String[] BuyerInfo =  currentRow.getCell(cellno.get("Remark from buyer")).getStringCellValue().split("]");

    		 for(int i =0;i<BuyerInfo.length;i++) {
    			 if(BuyerInfo[i].split(": ")[0].toString().trim().contains("SG Order SN")) {
    				 payment.setOrderId(payment.getOrderId().trim() + "/" + BuyerInfo[i].split(": ")[1].toString().trim());		 
    			 }
    		 }
    	 }


        
	  	  payment.setClient(clients.get(rowNumber-1));
	  	  paymentCredit =  paymentCredit + shippingFees;
	  	  payment.setPaymentCredit(df.format(paymentCredit));
	  	  payment.setPaymentFees(df.format(paymentFees));
	  	  payment.setCommissionFees(df.format(comissionFees));
	  	  payment.setOthersFees(df.format(otherFees));
	  	  paymentDue = paymentCredit + paymentFees + otherFees + comissionFees;
//	  	  paymentDue = paymentDue -voucher;
	  	  payment.setPaymentDue(df.format(paymentDue));
	  	  
	  	  Payment p = payments.stream().filter(o -> o.getOrderId().equals(payment.getOrderId())).findFirst().orElse(null);
	  	  if(p ==null) {
	  		if(isCancelled) {
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
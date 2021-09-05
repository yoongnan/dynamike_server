package com.dynamike.pos.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dynamike.pos.model.entities.Account;
import com.dynamike.pos.model.entities.CapitalReport;
import com.dynamike.pos.model.entities.CashFlowReport;
import com.dynamike.pos.model.entities.CashSales;
import com.dynamike.pos.model.entities.Client;
import com.dynamike.pos.model.entities.DetailReport;
import com.dynamike.pos.model.entities.ExpenditureReport;
import com.dynamike.pos.model.entities.ExpiredCheck;
import com.dynamike.pos.model.entities.Inventory;
import com.dynamike.pos.model.entities.InvoiceType;
import com.dynamike.pos.model.entities.Item;
import com.dynamike.pos.model.entities.OrderList;
import com.dynamike.pos.model.entities.Payment;
import com.dynamike.pos.model.entities.PaymentWithEarned;
import com.dynamike.pos.model.entities.Product;
import com.dynamike.pos.model.entities.ProductCheck;
import com.dynamike.pos.model.entities.ProductItem;
import com.dynamike.pos.model.entities.ProductType;
import com.dynamike.pos.model.entities.ProviderSource;
import com.dynamike.pos.model.entities.Purchase;
import com.dynamike.pos.model.entities.PurchaseItemList;
import com.dynamike.pos.model.entities.SimpleItem;
import com.dynamike.pos.model.entities.StockCheck;
import com.dynamike.pos.model.entities.Supplier;
import com.dynamike.pos.service.DatabaseService;
import com.dynamike.pos.service.ExcelShopeeHelper;
import com.dynamike.pos.util.EnglishNumberToWords;
import com.dynamike.pos.util.Format;
import com.dynamike.pos.util.PDFBuilder;
import com.dynamike.pos.util.PDFUtil;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;

import ch.qos.logback.classic.Logger;
import io.swagger.annotations.Api;
//@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "POS")
@RestController
@RequestMapping("/api_v1")
public class POSController {

    private final static Logger LOG = (Logger) LoggerFactory.getLogger(POSController.class);

    private final SimpleDateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateFormatMDY = new SimpleDateFormat("MM/dd/yyyy");
    private final SimpleDateFormat dateFormatYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private DatabaseService dbservice;

    @RequestMapping(method = RequestMethod.GET, path = "/paymentbyid", produces = "application/json")
    public HttpEntity<Payment> getPaymentsById(
    		@RequestParam(value = "id", required = false)   @NotNull @Valid String id
    		) throws Exception {
    	Payment payment = dbservice.getPaymentsById(id);
    	List<OrderList> orderList = dbservice.getOrderItemListsById(id);
    	payment.setOrderList(orderList);
    		return new ResponseEntity<Payment>(payment, HttpStatus.OK);    	
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/dailypaymentsummary", produces = "application/json")
    public HttpEntity<List<Object[]>> getDailyPaymentSummary(
    		@RequestParam(value = "type", required = false)  @Valid Integer provider,
    		@RequestParam(value = "month", required = false)   @Valid Integer month,
    		@RequestParam(value = "year", required = false) @NotNull  @Valid Integer year
    		) throws Exception {
    	return new ResponseEntity<List<Object[]>>(dbservice.getOrderSummaryDaily(), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/paymentsummarybyweek", produces = "application/json")
    public HttpEntity<List<Object[]>> getPaymentSummaryByWeek(
    		@RequestParam(value = "type", required = false)  @Valid Integer provider,
    		@RequestParam(value = "month", required = false)   @Valid Integer month,
    		@RequestParam(value = "year", required = false) @NotNull  @Valid Integer year
    		) throws Exception {
    	return new ResponseEntity<List<Object[]>>(dbservice.getOrderSummaryByWeek(), HttpStatus.OK);
    }
        
    @RequestMapping(path = "/paymentsByMonth", produces = "application/json")
    public HttpEntity<List<Payment>> paymentsByMonth(
    		@RequestParam(value = "month", required = false)   @Valid Integer month,
    		@RequestParam(value = "year", required = false) @NotNull  @Valid Integer year
            ) throws Exception { 
    	return new ResponseEntity<List<Payment>>(dbservice.getPaymentsWithEarnedByYearMonth(year,month),HttpStatus.CREATED);
    }
    
    @RequestMapping(path = "/paymentsByDate", produces = "application/json")
    public HttpEntity<List<Payment>> getPaymentsbyDate(
    		@RequestParam(value = "date", required = false)  @NotNull @Valid String dateString
            ) throws Exception { 
    	java.text.DateFormat dateFormat;
		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		Date nextdate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		nextdate = c.getTime();
    	return new ResponseEntity<List<Payment>>(dbservice.getPaymentsbyDate(date,nextdate),HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/payment", produces = "application/json")
    public HttpEntity
    <Page<Payment>> getPayments(
    		@RequestParam(value = "type", required = false)  @Valid Integer provider,
    		@RequestParam(value = "month", required = false)   @Valid Integer month,
    		@RequestParam(value = "year", required = false) @NotNull  @Valid Integer year,
    		@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    		) throws Exception {  		
    	return new ResponseEntity<Page<Payment>>(dbservice.getPaginationPaymentsByYearMonth(year,month,provider,page,size), HttpStatus.OK);
			
			
    }
    @RequestMapping(method = RequestMethod.GET, path = "/purchaseItems", produces = "application/json")
    public HttpEntity<List<PurchaseItemList>> getPurchasesItems(
    		@RequestParam(value = "id", required = false) @NotNull  @Valid String id
    		) throws Exception {
    	
    	return new ResponseEntity<List<PurchaseItemList>>(dbservice.getPurchaseItemListsByPurchaseId(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/orderItems", produces = "application/json")
    public HttpEntity<List<OrderList>> getOrderItems(
    		@RequestParam(value = "id", required = false) @NotNull  @Valid String id
    		) throws Exception {
    	
    	return new ResponseEntity<List<OrderList>>(dbservice.getOrderItemListsById(id), HttpStatus.OK);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, path = "/purchasebyid", produces = "application/json")
    public HttpEntity<Purchase> getPurchasesById(
    		@RequestParam(value = "id", required = false)  @Valid String id
    		) throws Exception {

	    	Purchase purchase = dbservice.getPurchasesById(id);
	    	List<PurchaseItemList> purchaseItemList = dbservice.getPurchaseItemListsByPurchaseId(purchase.getId());
	    	purchase.setPurchaseItemList(purchaseItemList);
    		return new ResponseEntity<Purchase>(purchase, HttpStatus.OK);
    	
    }
    @PostMapping(path = "/splitExpired", produces = "application/json")
    public HttpEntity splitExpired(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
    		) throws Exception {

    	DecimalFormat df = new DecimalFormat("0.00");
    	List<ExpiredCheck> items = Format.strToClassObjList(group_data, ExpiredCheck.class);
    	for(ExpiredCheck item: items) {
        	Double total = 0.0;
        	Integer qty = item.getQuantity();
        	total = Double.parseDouble(item.getUnitCost()) * qty;
        	item.setAmount(df.format(total));
    	}
    	dbservice.setPurchaseItemListsWithExpired(items);
    	return new ResponseEntity(HttpStatus.OK);
    	
    }
    
    @PostMapping(path = "/updateExpired", produces = "application/json")
    public HttpEntity updateExpired(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
    		) throws Exception {
    		
    	ExpiredCheck item = Format.strToClassObj(group_data, ExpiredCheck.class);
    	dbservice.setPurchaseItemListWithExpired(item);
    	return new ResponseEntity(HttpStatus.OK);
    	
    }
    @RequestMapping(method = RequestMethod.GET, path = "/expiredCheck", produces = "application/json")
    public HttpEntity<List<ExpiredCheck>> expiredCheck(
    		@RequestParam(value = "code", required = false)  @Valid String code
    		) throws Exception {
    	
    	return new ResponseEntity<List<ExpiredCheck>>(dbservice.getExpiredItemListsbyCode(code), HttpStatus.OK);
    	
    }
    

    @RequestMapping(method = RequestMethod.GET, path = "/expiredList", produces = "application/json")
    public HttpEntity<List<ExpiredCheck>> expiredCheck(
    		@RequestParam(value = "year", required = false)  @Valid Integer year,
    		@RequestParam(value = "month", required = false)  @Valid Integer month
    		) throws Exception {
    	
    	return new ResponseEntity<List<ExpiredCheck>>(dbservice.getExpiredItemLists(year, month), HttpStatus.OK);
    	
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/purchase", produces = "application/json")
    public HttpEntity<List<Purchase>> getPurchases(
    		@RequestParam(value = "type", required = false)  @Valid Integer type,
    		@RequestParam(value = "month", required = false)   @Valid Integer month,
    		@RequestParam(value = "year", required = false) @NotNull  @Valid Integer year
    		) throws Exception {
    	
    	
		if(month !=null) {
			if(type!=null) {
				return new ResponseEntity<List<Purchase>>(dbservice.getPurchasesByYearMonth(year,month,type), HttpStatus.OK);
			}else {
				return new ResponseEntity<List<Purchase>>(dbservice.getPurchasesByYearMonth(year,month), HttpStatus.OK);	
			}
			
    	}else {
    		return new ResponseEntity<List<Purchase>>(dbservice.getPurchasesByYear(year), HttpStatus.OK);
    	}
    }
        
    @RequestMapping(method = RequestMethod.GET, path = "/suppliers", produces = "application/json")
    public HttpEntity<List<Supplier>> getSuppliers() throws Exception {
        return new ResponseEntity<List<Supplier>>(dbservice.getSuppliers(), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/allclients", produces = "application/json")
    public HttpEntity<List<Client>> getClients() throws Exception {
        return new ResponseEntity<List<Client>>(dbservice.getClients(), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/clients", produces = "application/json")
    public HttpEntity<Page<Client>> getClients(
    		@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    		) throws Exception {
        return new ResponseEntity<Page<Client>>(dbservice.getClients(page,size), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/productById", produces = "application/json")
    public HttpEntity<List<Inventory>> getProductsById(
    		@RequestParam(value = "id", required = false)  @NotNull @Valid String id
    		) throws Exception {
    	
    		return new ResponseEntity<List<Inventory>>(dbservice.getListProductById(id), HttpStatus.OK);		
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/productByCode", produces = "application/json")
    public HttpEntity<List<Inventory>> productByCode(
    		@RequestParam(value = "id", required = false)  @NotNull @Valid String id
    		) throws Exception {
    	
    		return new ResponseEntity<List<Inventory>>(dbservice.getListProductByCode(id), HttpStatus.OK);		
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/productBySupplier", produces = "application/json")
    public HttpEntity<List<Product>> productBySupplier(
    		@RequestParam(value = "id", required = false)  @NotNull @Valid Integer id
    		) throws Exception {
    	
    		return new ResponseEntity<List<Product>>(dbservice.getListProductBySupplier(id), HttpStatus.OK);		
    }
//    public HttpEntity<List<Item>> getProductsById(
//    		@RequestParam(value = "id", required = false)  @NotNull @Valid String id
//    		) throws Exception {
//    	
//    		return new ResponseEntity<List<Item>>(dbservice.getItemProductsById(id), HttpStatus.OK);		
//    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/productByType", produces = "application/json")
    public HttpEntity
//    <Page<Item>> 
    <Page<Inventory>>
    getProductsByType(
    		@RequestParam(value = "type", required = false)  @NotNull @Valid String type,

    		@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    		) throws Exception {  		
//			return new ResponseEntity<Page<Item>>(dbservice.getItemProductsByType(type,page,size), HttpStatus.OK);
    	return new ResponseEntity<Page<Inventory>>(dbservice.getProductItemsByType(type,page,size), HttpStatus.OK);
			
			
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/products", produces = "application/json")
    public HttpEntity<List<Product>> getProducts() throws Exception  
    {
    		return new ResponseEntity<List<Product>>(dbservice.getProducts(), HttpStatus.OK);	       
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/productitems", produces = "application/json")
    public HttpEntity<List<ProductItem>> getProductItems() throws Exception 
    {    	
    		return new ResponseEntity<List<ProductItem>>(dbservice.getItemProducts(), HttpStatus.OK);	       
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/producttypes", produces = "application/json")
    public HttpEntity<List<ProductType>> getProductTypes() throws Exception {
        return new ResponseEntity<List<ProductType>>(dbservice.getProductType(), HttpStatus.OK);
    }
    
    @PostMapping(path = "/invoicetypes", produces = "application/json")
    public HttpEntity<InvoiceType> addInvoiceType(
    		@RequestParam(value = "id", required = false)  @NotNull @Valid String id,
    		@RequestParam(value = "name", required = false)  @NotNull @Valid String name
    		) throws Exception {
    	
        return new ResponseEntity<InvoiceType>(dbservice.addInvoiceTypes(id,name), HttpStatus.OK);
    }
    
    
    @DeleteMapping(path = "/producttypes", produces = "application/json")
    public HttpEntity deleteProductTypes(
    		@RequestParam(value = "id", required = false)  @NotNull @Valid String id,
    		@RequestParam(value = "name", required = false)  @NotNull @Valid String name
            ) throws Exception {
    	dbservice.deleteProductType(id,name);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/producttypes", produces = "application/json")
    public HttpEntity<ProductType> addProductTypes(
    		@RequestParam(value = "id", required = false)  @NotNull @Valid String id,
    		@RequestParam(value = "name", required = false)  @NotNull @Valid String name
    		) throws Exception {
    	
        return new ResponseEntity<ProductType>(dbservice.addProductType(id,name), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/allinvoicetypes", produces = "application/json")
    public HttpEntity<List<InvoiceType>> getAllInvoiceTypes(
    		) throws Exception {
        return new ResponseEntity<List<InvoiceType>>(dbservice.getAllInvoiceTypes(), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/otherinvoicetypes", produces = "application/json")
    public HttpEntity<List<InvoiceType>> getOtherInvoiceTypes(
    		@RequestParam(value = "types", required = false)  List<Integer> types
    		) throws Exception {
        return new ResponseEntity<List<InvoiceType>>(dbservice.getOtherInvoiceTypes(types), HttpStatus.OK);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, path = "/invoicetypes", produces = "application/json")
    public HttpEntity<List<InvoiceType>> getInvoiceTypes(
    		@RequestParam(value = "types", required = false)  List<Integer> types
    		) throws Exception {
        return new ResponseEntity<List<InvoiceType>>(dbservice.getInvoiceTypes(types), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/providers", produces = "application/json")
    public HttpEntity<List<ProviderSource>> getProviderSources() throws Exception {
        return new ResponseEntity<List<ProviderSource>>(dbservice.getProviderSources(), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/cashflowReport", produces = "application/json")
    public HttpEntity<CashFlowReport> getCashFlowReport(
    		@RequestParam(value = "years", required = false)  @NotNull @Valid Integer years
            ) throws Exception {
    	
    	
        return new ResponseEntity<CashFlowReport>(dbservice.getCashFlowReport(),HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/capitalReport", produces = "application/json")
    public HttpEntity<CapitalReport> getCapitalReport(
    		@RequestParam(value = "years", required = false)  @NotNull @Valid Integer years
            ) throws Exception {
    	
    	
        return new ResponseEntity<CapitalReport>(dbservice.getCapitalReport(years),HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/expenditureReport", produces = "application/json")
    public HttpEntity<ExpenditureReport> getExpenditureReport(
    		@RequestParam(value = "years", required = false)  @NotNull @Valid Integer years
            ) throws Exception {
    	
    	
        return new ResponseEntity<ExpenditureReport>(dbservice.getExpenditureReport(years),HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/accountYears", produces = "application/json")
    public HttpEntity<List<Integer>> getAccountYears(
            ) throws Exception {
    	
    	
        return new ResponseEntity<List<Integer>>(dbservice.getAccountYears(),HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/transactionYears", produces = "application/json")
    public HttpEntity<List<Integer>> getTransactionYears(
            ) throws Exception {
    	
    	
        return new ResponseEntity<List<Integer>>(dbservice.getTransactionYears(),HttpStatus.CREATED);
    }
    
    
    
    
    @RequestMapping(method = RequestMethod.GET, path = "/monthlyreport", produces = "application/json")
    public HttpEntity<List<DetailReport>> getMonthlyReport(
    		@RequestParam(value = "years", required = false)  @NotNull @Valid Integer years
            ) throws Exception {
    	
    	
        return new ResponseEntity<List<DetailReport>>(dbservice.getDetailReport(years),HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/summaryMonthlyreport", produces = "application/json")
    public HttpEntity<List<DetailReport>> getSummaryMonthlyReport(
    		@RequestParam(value = "years", required = false)  @NotNull @Valid Integer years
            ) throws Exception {
    	
    	
        return new ResponseEntity<List<DetailReport>>(dbservice.getSummaryDetailReport(years),HttpStatus.CREATED);
    }
    
    
    @PutMapping(path = "/purchaseItems", produces = "application/json")
    public HttpEntity updatePurchaseItems(
    		@RequestParam(value = "list_data", required = false)  @NotNull @Valid String list_data,
    		@RequestParam(value = "old_list_data", required = false) @Valid String oldlist_data
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	List<PurchaseItemList> purchaseitems = Format.strToClassObjList(list_data, PurchaseItemList.class);
    	List<PurchaseItemList> oldpurchaseitems = Format.strToClassObjList(oldlist_data, PurchaseItemList.class);
    	for(PurchaseItemList purchaseitem :oldpurchaseitems) {
        	purchaseitem.setPurchaseId(purchaseitem.getPurchaseId().toString());
        	Inventory product = dbservice.getProductByCode(purchaseitem.getProductCode());
        	if(product !=null) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
            	stock -= purchaseitem.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	dbservice.updateProduct(product);	
        	}
        	dbservice.deletePurchaseItem(purchaseitem);
        }
        for(PurchaseItemList purchaseitem :purchaseitems) {
        	purchaseitem.setPurchaseId(purchaseitem.getPurchaseId().toString());
        	Inventory product = dbservice.getProductByCode(purchaseitem.getProductCode());
        	if(product !=null) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
            	stock += purchaseitem.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	dbservice.updateProduct(product);
        		
        	}
        	dbservice.createPurchaseItem(purchaseitem);
        }
        
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    public Purchase updatePurchaseItems(
    		String group_data, Purchase purchase, String old_data
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	purchase= dbservice.createPurchase(purchase);
    	List<PurchaseItemList> purchaseitems = Format.strToClassObjList(group_data, PurchaseItemList.class);
    	List<PurchaseItemList> oldpurchaseitems = Format.strToClassObjList(old_data, PurchaseItemList.class);
    	for(PurchaseItemList purchaseitem :oldpurchaseitems) {
        	purchaseitem.setPurchaseId(purchase.getId().toString());
        	Inventory product = dbservice.getProductByCodeSupplierId(purchaseitem.getProductCode(), purchase.getSupplier().getId());
        	if(product !=null) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
            	stock -= purchaseitem.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	dbservice.updateProduct(product);
        		
        	}
        	dbservice.deletePurchaseItem(purchaseitem);
        }
        for(PurchaseItemList purchaseitem :purchaseitems) {
        	purchaseitem.setPurchaseId(purchase.getId().toString());
        	Inventory product = dbservice.getProductByCodeSupplierId(purchaseitem.getProductCode(), purchase.getSupplier().getId());
        	if(product !=null) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
            	product.setUnitCost(purchaseitem.getUnitCost());
            	stock += purchaseitem.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	dbservice.updateProduct(product);
        		
        	}
        	dbservice.createPurchaseItem(purchaseitem);
        }
        return purchase;
    }
    
    public Purchase addPurchaseItems(
    		String group_data, Purchase purchase
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	purchase= dbservice.createPurchase(purchase);
    	List<PurchaseItemList> purchaseitems = Format.strToClassObjList(group_data, PurchaseItemList.class);
        for(PurchaseItemList purchaseitem :purchaseitems) {
        	purchaseitem.setPurchaseId(purchase.getId().toString());
        	Inventory product = dbservice.getProductByCodeSupplierId(purchaseitem.getProductCode(), purchase.getSupplier().getId());
        	if(product!=null) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
            	product.setUnitCost(purchaseitem.getUnitCost());
            	stock += purchaseitem.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	dbservice.updateProduct(product);	
        	}
        	dbservice.createPurchaseItem(purchaseitem);
        }
        return purchase;
    }
    
    
    @RequestMapping(path = "/account", produces = "application/json")
    public HttpEntity<List<Purchase>> getAccountList(
    		@RequestParam(value = "month", required = false)   @Valid Integer month,
    		@RequestParam(value = "year", required = false) @NotNull  @Valid Integer year
            ) throws Exception {
    	return new ResponseEntity<List<Purchase>>(dbservice.getAccountByYearMonth(year,month),HttpStatus.CREATED);
    }
    @RequestMapping(path = "/cashflow", produces = "application/json")
    public HttpEntity<List<Purchase>> getAccountList(
            ) throws Exception {
    	return new ResponseEntity<List<Purchase>>(dbservice.getCashFlow(),HttpStatus.CREATED);
    }
    
    
    
    @RequestMapping(path = "/runcheckStock", produces = "application/json")
    public HttpEntity<List<ProductCheck>> runcheckStock(
    		@RequestParam(value = "date", required = false)  @NotNull @Valid String date
            ) throws Exception {    	
    	return new ResponseEntity<List<ProductCheck>>(dbservice.checkStockConflict(date),HttpStatus.CREATED);
    }
    
    @RequestMapping(path = "/stockcheckdate", produces = "application/json")
    public HttpEntity<List<StockCheck>> getstockCheckdate(
    		@RequestParam(value = "year", required = false)  @Valid Integer year,
    		@RequestParam(value = "month", required = false)  @Valid Integer month
            ) throws Exception {
    	return new ResponseEntity<List<StockCheck>>(dbservice.getStockCheckDate(year,month),HttpStatus.CREATED);
    }
    
    @RequestMapping(path = "/stockchecklist", produces = "application/json")
    public HttpEntity<List<StockCheck>> getstockchecklist(
    		@RequestParam(value = "date", required = false)  @NotNull @Valid String date
            ) throws Exception {  
    	return new ResponseEntity<List<StockCheck>>(dbservice.getStockCheckByDate(date),HttpStatus.CREATED);
    }
    
    
    @PostMapping(path = "/stockcheck", produces = "application/json")
    public HttpEntity stockCheck(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
            ) throws Exception {
    	List<StockCheck> stockcheck = Format.strToClassObjList(group_data, StockCheck.class);
    	dbservice.createStockCheck(stockcheck);
    	return new ResponseEntity(HttpStatus.CREATED);
    }
    @PostMapping(path = "/account", produces = "application/json")
    public HttpEntity<Account> addAccount(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
            ) throws Exception {
    	Account account = Format.strToClassObj(group_data, Account.class);
    	System.out.println("Add account");
    	return new ResponseEntity<Account>(dbservice.createAccount(account),HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/transaction", produces = "application/json")
    public HttpEntity deleteTransaction(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
            ) throws Exception {
    	Payment payment = Format.strToClassObj(group_data, Payment.class);
    	deleteTransaction(payment);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/purchase", produces = "application/json")
    public HttpEntity deletePurchase(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
            ) throws Exception {
    	Purchase purchase = Format.strToClassObj(group_data, Purchase.class);
    	deletePurchaseItems(purchase);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    public void deletePurchaseItems(
    		Purchase purchase
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	List<PurchaseItemList> purchaseitems = dbservice.getPurchaseItemListsByPurchaseId(purchase.getId());
        for(PurchaseItemList purchaseitem :purchaseitems) {
        	Inventory product = dbservice.getProductByCodeSupplierId(purchaseitem.getProductCode(), purchase.getSupplier().getId());
        	if(product!=null) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
            	stock -= purchaseitem.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	dbservice.updateProduct(product);        		
        	}
        	dbservice.deletePurchaseItem(purchaseitem);
        }
        dbservice.deletePurchase(purchase);
    }
    
    @PostMapping(path = "/paidpurchase", produces = "application/json")
    public HttpEntity<Purchase> paidPurchase(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data
            ) throws Exception {
    	Purchase purchase = Format.strToClassObj(group_data, Purchase.class);
        return new ResponseEntity<Purchase>(dbservice.createPurchase(purchase),HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/purchase", produces = "application/json")
    public HttpEntity<Purchase> addPurchase(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data,
    		@RequestParam(value = "list_data", required = false)  @NotNull @Valid String list_data
            ) throws Exception {
    	System.out.println("Add purchase");
    	Purchase purchase = Format.strToClassObj(group_data, Purchase.class);
        return new ResponseEntity<Purchase>(addPurchaseItems(list_data,purchase),HttpStatus.CREATED);
    }
    
    @PutMapping(path = "/purchase", produces = "application/json")
    public HttpEntity<Purchase> updatePurchase(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data,
    		@RequestParam(value = "list_data", required = false)  @NotNull @Valid String list_data,
    		@RequestParam(value = "old_list_data", required = false) @Valid String old_list_data
            ) throws Exception {
    	Purchase purchase = Format.strToClassObj(group_data, Purchase.class);
        return new ResponseEntity<Purchase>(updatePurchaseItems(list_data,purchase,old_list_data),HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/transaction", produces = "application/json")
    public HttpEntity<Payment> addTransaction(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data,
    		@RequestParam(value = "list_data", required = false)  @NotNull @Valid String list_data
            ) throws Exception {
    	Payment payment = Format.strToClassObj(group_data, Payment.class);
    	
        return new ResponseEntity<Payment>(addOrderItems(list_data, payment),HttpStatus.CREATED);
    }
    
    @PutMapping(path = "/transaction", produces = "application/json")
    public HttpEntity<Payment> updateTransaction(
    		@RequestParam(value = "group_data", required = false)  @NotNull @Valid String group_data,
    		@RequestParam(value = "list_data", required = false)  @NotNull @Valid String list_data,
    		@RequestParam(value = "old_list_data", required = false) @Valid String oldlist_data
            ) throws Exception {
    	Payment payment = Format.strToClassObj(group_data, Payment.class);
    	
        return new ResponseEntity<Payment>(updateOrderItems(list_data, payment,oldlist_data),HttpStatus.CREATED);
    }
    
    @PutMapping(path = "/transactionItems", produces = "application/json")
    public HttpEntity updateTransactionItems(
    		@RequestParam(value = "list_data", required = false)  @NotNull @Valid String list_data,
    		@RequestParam(value = "old_list_data", required = false) @Valid String oldlist_data
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	List<OrderList> orderitems = Format.strToClassObjList(list_data, OrderList.class);
    	List<OrderList> oldorderitems = Format.strToClassObjList(oldlist_data, OrderList.class);
    	for(OrderList orderitem :oldorderitems) {
        	Inventory product = dbservice.getProductByCode(orderitem.getItemId());
        	Double total = Double.parseDouble(product.getTotalStock());
        	Integer stock = Integer.parseInt(product.getStock());
        	stock += orderitem.getQuantity();
        	total = Double.parseDouble(product.getUnitCost()) * stock;
        	product.setStock(stock.toString());
        	product.setTotalStock(df.format(total));
        	dbservice.updateProduct(product);
        	dbservice.deleteOrderItem(orderitem);
        }
        for(OrderList orderitem :orderitems) {
        	Inventory product = dbservice.getProductByCode(orderitem.getItemId());
        	Double total = Double.parseDouble(product.getTotalStock());
        	Integer stock = Integer.parseInt(product.getStock());
        	stock -= orderitem.getQuantity();
        	total = Double.parseDouble(product.getUnitCost()) * stock;
        	product.setStock(stock.toString());
        	product.setTotalStock(df.format(total));
        	dbservice.updateProduct(product);
        	dbservice.createOrderItem(orderitem);
        }
        
        return new ResponseEntity(HttpStatus.CREATED);
    }
    public Payment updateOrderItems(
    		String group_data, Payment payment, String old_group_data
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	payment= dbservice.addTransaction(payment);
    	List<OrderList> orderitems = Format.strToClassObjList(group_data, OrderList.class);
    	List<OrderList> oldorderitems = Format.strToClassObjList(old_group_data, OrderList.class);
    	for(OrderList orderitem :oldorderitems) {
        	Inventory product = dbservice.getProductByCode(orderitem.getItemId());
        	Double total = Double.parseDouble(product.getTotalStock());
        	Integer stock = Integer.parseInt(product.getStock());
        	stock += orderitem.getQuantity();
        	total = Double.parseDouble(product.getUnitCost()) * stock;
        	product.setStock(stock.toString());
        	product.setTotalStock(df.format(total));
        	dbservice.updateProduct(product);
        	dbservice.deleteOrderItem(orderitem);
        }
        for(OrderList orderitem :orderitems) {
        	List<Inventory> products = dbservice.getListProductByCode(orderitem.getItemId());
        	Integer orderStock = orderitem.getQuantity();
        	for(Inventory product : products) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
        		if(stock>= orderStock) {
                	stock -= orderStock;
        			orderStock =0;
                	total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	dbservice.updateProduct(product);
                	break;
        		}else {
        			orderStock =orderStock - stock;
        			stock = 0;
        			total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	dbservice.updateProduct(product);
        		}
        	}
        	dbservice.createOrderItem(orderitem);
//        	Inventory product = dbservice.getProductByCode(orderitem.getItemId());
//        	System.out.println(product.getTotalStock());
//        	Double total = Double.parseDouble(product.getTotalStock());
//        	Integer stock = Integer.parseInt(product.getStock());
//        	stock -= orderitem.getQuantity();
//        	total = Double.parseDouble(product.getUnitCost()) * stock;
//        	product.setStock(stock.toString());
//        	product.setTotalStock(df.format(total));
//        	dbservice.updateProduct(product);
//        	dbservice.createOrderItem(orderitem);
        }
        return payment;
    }

    
    
    public void deleteTransaction( Payment payment
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	List<OrderList> orderitems = dbservice.getOrderItemListsById(payment.getOrderId());
        for(OrderList orderitem :orderitems) {
        	Inventory product = dbservice.getProductByCode(orderitem.getItemId());
        	Double total = Double.parseDouble(product.getTotalStock());
        	Integer stock = Integer.parseInt(product.getStock());
        	stock += orderitem.getQuantity();
        	total = Double.parseDouble(product.getUnitCost()) * stock;
        	product.setStock(stock.toString());
        	product.setTotalStock(df.format(total));
        	dbservice.updateProduct(product);
        	dbservice.deleteOrderItem(orderitem);
        }
    	dbservice.deleteTransaction(payment);
    }
    
    public Payment addOrderItems(
    		String group_data, Payment payment
            ) throws Exception {
    	DecimalFormat df = new DecimalFormat("0.00");
    	payment= dbservice.addTransaction(payment);
    	List<OrderList> orderitems = Format.strToClassObjList(group_data, OrderList.class);
        for(OrderList orderitem :orderitems) {
        	List<Inventory> products = dbservice.getListProductByCode(orderitem.getItemId());
        	Integer orderStock = orderitem.getQuantity();
        	for(Inventory product : products) {
            	Double total = Double.parseDouble(product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock());
        		if(stock>= orderStock) {
                	stock -= orderStock;
        			orderStock =0;
                	total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	dbservice.updateProduct(product);
                	break;
        		}else {
        			orderStock =orderStock - stock;
        			stock = 0;
        			total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	dbservice.updateProduct(product);
        		}
        	}
        	dbservice.createOrderItem(orderitem);
        }
        return payment;
    }
    
    
    @PostMapping(path = "/client", produces = "application/json")
    public HttpEntity<Client> addClient(
    		@RequestBody @NotNull @Valid String group_data
            ) throws Exception {
    	Client client = Format.strToClassObj(group_data, Client.class);
        
        return new ResponseEntity<Client>(dbservice.createClient(client),HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/supplier", produces = "application/json")
    public HttpEntity<Supplier> addSupplier(
    		@RequestBody @NotNull @Valid String group_data
            ) throws Exception {
    	Supplier supplier = Format.strToClassObj(group_data, Supplier.class);
        
        return new ResponseEntity<Supplier>(dbservice.createSupplier(supplier),HttpStatus.CREATED);
    }
    
    public BufferedImage rescale(BufferedImage originalImage, int fileSize)
    {
        BufferedImage resizedImage = new BufferedImage(fileSize, fileSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, fileSize, fileSize, null);
        g.dispose();
        return resizedImage;
    }
    
    @PutMapping(path = "/quickUpdateStockCheck", produces = "application/json")
    public HttpEntity<ProductCheck> addProductCheck(
    		@RequestParam(value = "group_data", required = false) String group_data
            ) throws Exception {
    	ProductCheck product = Format.strToClassObj(group_data, ProductCheck.class);
//    	SimpleItem  item = product.getItem();
//    	product.setItem(item);
//    	Item item = Format.strToClassObj(group_data, Item.class);
//    	Inventory product = item.getProduct();
//    	product = dbservice.createProduct(product);
//    	item.setProduct(product);
        return new ResponseEntity<ProductCheck>(dbservice.createProduct(product),HttpStatus.CREATED);
    }
    
    @PutMapping(path = "/quickUpdateProduct", produces = "application/json")
    public HttpEntity<Inventory> addProduct(
    		@RequestParam(value = "group_data", required = false) String group_data,
    		@RequestParam(value = "update", required = true) boolean update
            ) throws Exception {
    	Inventory product = Format.strToClassObj(group_data, Inventory.class);
    	Inventory existingproduct = dbservice.getProductById(product.getId());
    	SimpleItem  item = product.getItem();
    	if(item.getSellingPrice() == null || item.getSellingPrice().trim().equals("")) {
    		item.setSellingPrice("0.00");
    	}
    	if(!update) {
    		if(existingproduct!=null) {
    			throw new Exception("duplicate Inventory");
    		}
    	}
    	item = dbservice.createSimpleItem(item);
    	if(product.getTotalStock() == null || product.getTotalStock().trim().equals("")) {
    		product.setTotalStock("0.00");	
    	}
    	if(product.getStock() == null || product.getStock().trim().equals("")) {
    		product.setStock("0");	
    	}
    	product.setItem(item);
        return new ResponseEntity<Inventory>(dbservice.createProduct(product),HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/products", produces = "application/json")
    public HttpEntity<Inventory> addProduct(
    		@RequestParam(value = "group_data", required = false) String group_data,
    		@RequestParam(value = "update", required = false) boolean update,
    		@RequestParam(value = "file") MultipartFile files
            ) throws Exception {
    	System.out.println("ADD Product");

    	Inventory product = Format.strToClassObj(group_data, Inventory.class);
    	Inventory existingproduct = dbservice.getProductById(product.getId());
    	SimpleItem  item = product.getItem();
    	if(item.getSellingPrice() == null || item.getSellingPrice().trim().equals("")) {
    		item.setSellingPrice("0.00");
    	}
    	
    	if(!update) {
    		if(existingproduct!=null) {
    			throw new Exception("duplicate Inventory");
    		}
    	}
    	
		item = dbservice.createSimpleItem(item);	
    	
    	
//    	Item item = Format.strToClassObj(group_data, Item.class);
        if(files != null){
        	byte[] rawBytes = null;  
//        	java.nio.file.Path target = java.nio.file.Paths.get("C:\\Users\\bysadmin\\Documents\\NetBeansProjects\\pos\\original.png");
//        	java.nio.file.Path target = java.nio.file.Paths.get("C:\\Users\\Asus\\Desktop\\pos-server\\original.png");
//        	File convFile = new File("product.png");
//        	files.transferTo(myObj);
//        	System.out.println("getURI");
//        	System.out.println("ClassLoader:"+ClassLoader.getSystemResource("./application.properties").toURI());
//        	java.nio.file.Path target = java.nio.file.Paths.get(ClassLoader.getSystemResource("original.png").toURI());
        	java.nio.file.Path target =  java.nio.file.Paths.get(new File("product.png").toURI());
//        	File convFile = new File(ClassLoader.getSystemResource("original.png").getPath());
        	File convFile = target.toFile();
        	files.transferTo(convFile);
        	try{
                java.io.FileInputStream fis = new java.io.FileInputStream(convFile);
                
                int imageLength = Integer.parseInt(String.valueOf(convFile.length()));  
                rawBytes = new byte[imageLength];  
                fis.read(rawBytes, 0, imageLength);  
            }catch(Exception e){
            	e.printStackTrace();
            	return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
//        	
//        	int baseSize = 200;
//        	BufferedImage targetImg = rescale(ImageIO.read(convFile),baseSize);
//        	
//        	
//            java.nio.file.Path resizetarget = java.nio.file.Paths.get("C:\\Users\\bysadmin\\Documents\\NetBeansProjects\\pos\\resize.png");
//            ImageIO.write(targetImg,"png", resizetarget.toFile());
//            File savedFile = resizetarget.toFile();
//        	try{
//                java.io.FileInputStream fis = new java.io.FileInputStream(savedFile);
//                
//                int imageLength = Integer.parseInt(String.valueOf(savedFile.length()));  
//                rawBytes = new byte[imageLength];  
//                fis.read(rawBytes, 0, imageLength);  
//            }catch(Exception e){
//            	e.printStackTrace();
//            }
        	
        	product.setImage(rawBytes);
        }

    	if(product.getTotalStock() == null || product.getTotalStock().trim().equals("")) {
    		product.setTotalStock("0.00");	
    	}
    	if(product.getStock() == null || product.getStock().trim().equals("")) {
    		product.setStock("0");	
    	}
//    	Inventory product = item.getProduct();    	
    	product = dbservice.createProduct(product);    	
//    	item.setProduct(product);
//    	item = dbservice.createItem(item);
        return new ResponseEntity<Inventory>(product,HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/shopee", produces = "application/json")
    public HttpEntity uploadShopeeOrderTransaction(
    		@RequestParam(value = "file") MultipartFile files
            ) throws Exception {
    	dbservice.saveShopeeTransaction(files);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/lazada", produces = "application/json")
    public HttpEntity uploadLazadaOrderTransaction(
    		@RequestParam(value = "file") MultipartFile files
            ) throws Exception {
    	dbservice.saveLazadaOrderTransaction(files);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/lazadaTransaction", produces = "application/json")
    public HttpEntity uploadLazadaTransaction(
    		@RequestParam(value = "file") MultipartFile files
            ) throws Exception {
    	dbservice.saveLazadaTransaction(files);
        return new ResponseEntity(HttpStatus.CREATED);
    }
   
    
    @PostMapping("/cashsales/pdf")
    public OutputStream generateCashSales(
             @RequestBody @NotNull @Valid CashSales cashsales, BindingResult bindingResult, HttpServletResponse response) throws IOException {
//   	 	LOG.info("export to pdf");
        if (bindingResult.hasErrors()) {
            response.setStatus(400);
            response.getWriter().write("Download pdf data invalid.");
            response.getWriter().flush();
            LOG.error("Download pdf data invalid.");
            return null;
        } else {
            try (OutputStream out = response.getOutputStream()){
            	
//                Document document = new Document(PageSize.A5);
            	Document document = new Document(new RectangleReadOnly(598,792));
                document.setMargins(36f,36f,36f,55f);
                PdfWriter writer = PdfWriter.getInstance(document, out);
                PDFBuilder builder = new PDFBuilder();
        		writer.setPageEvent(builder);
                String html = dbservice.getCashSalesPdfContent(cashsales);
                PDFUtil.convertToPdf(writer, document, html);
                return out;
            } catch (IOException | DocumentException e){
                LOG.error("PDF export to response fail", e);
                return null;
            }
        }
    }
    
    @PostMapping("/invoices/pdf")
    public OutputStream generateInvoice(
             @RequestBody @NotNull @Valid CashSales cashsales, BindingResult bindingResult, HttpServletResponse response) throws IOException {
//   	 	LOG.info("export to pdf");
        if (bindingResult.hasErrors()) {
            response.setStatus(400);
            response.getWriter().write("Download pdf data invalid.");
            response.getWriter().flush();
            LOG.error("Download pdf data invalid.");
            return null;
        } else {
            try (OutputStream out = response.getOutputStream()){
            	
            	Document document = new Document(new RectangleReadOnly(598,792));
                document.setMargins(36f,36f,36f,55f);
                PdfWriter writer = PdfWriter.getInstance(document, out);
                PDFBuilder builder = new PDFBuilder();
        		writer.setPageEvent(builder);
                String html = dbservice.getInvoicePdfContent(cashsales);
                PDFUtil.convertToPdf(writer, document, html);
                return out;
            } catch (IOException | DocumentException e){
                LOG.error("PDF export to response fail", e);
                return null;
            }
        }
    }
    
}

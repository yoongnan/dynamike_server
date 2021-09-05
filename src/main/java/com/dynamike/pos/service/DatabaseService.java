package com.dynamike.pos.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dynamike.pos.model.entities.*;
import com.dynamike.pos.model.entities.repo.*;
import com.dynamike.pos.util.Constants;
import com.dynamike.pos.util.EnglishNumberToWords;
import com.dynamike.pos.util.Format;
import com.dynamike.pos.util.PDFUtil;
import com.google.common.collect.Lists;


@Service
@Transactional
public class DatabaseService {
	DecimalFormat df = new DecimalFormat("0.00");	        
	@Autowired
	ProuctCheckRepository product_check_repos;
	
	@Autowired
	StockCheckRepository stockcheck_repos;
	
	@Autowired
	private ItemRepository item_repos;
	
	@Autowired
	private SimpleItemRepository simpleitem_repos;
	
	@Autowired
	private InventoryRepository inventory_repos;
	
	@Autowired
	private ClientRepository client_repos;
	
	@Autowired
	private SupplierRepository supplier_repos;
	
	@Autowired
	private OrderRepository order_repos;
	
	@Autowired
	private OrderListRepository orderlist_repos;
	
	@Autowired
	private PaymentRepository payment_repos;
	
	@Autowired
	private PurchaseRepository purchase_repos;
	
	@Autowired
	private ProductTypeRepository producttype_repos;
	
	@Autowired
	private PurchaseItemListRepository purchaseslist_repos;
	
	@Autowired
	private ExpiredCheckRepository expiredcheck_repos;
	
	@Autowired
	private InvoiceTypeRepository invoicetype_repos;
	
	@Autowired
	private ProviderSourceRepository provider_repos;
	
	@Autowired
	private AccountRepository account_repos;
	

	public List<ProductType> getProductType(){
		return producttype_repos.getProductType();
	}
	public void deleteProductType(String id,String name){
		ProductType type = new ProductType();
		type.setId(Integer.parseInt(id));
		type.setDescription(name);
		producttype_repos.delete(type);
	}
	
	
	public ProductType addProductType(String id,String name){
		ProductType type = new ProductType();
		type.setId(Integer.parseInt(id));
		type.setDescription(name);
		return producttype_repos.save(type);
	}
	
	public void createStockCheck(List<StockCheck> stockchecks) {
		stockcheck_repos.saveAll(stockchecks);
	}
	
	public List<StockCheck> getStockCheckDate(Integer year, Integer month) throws Exception {
		return stockcheck_repos.getStockCheckDate(year,month);
	}

	public List<StockCheck> getStockCheckByDate(String strDate) throws Exception {

		return stockcheck_repos.getStockCheckByDate(strDate+"%");
	}
	
	public List<ProductCheck> checkStockConflict(String strDate) throws Exception {
		List<ProductCheck> productchecks = inventory_repos.getStock();
		java.text.DateFormat dateFormat;
		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
//    		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
//		Date date = Format.strToClassObj(strDate, Date.class);
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
//		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
//		String sDate = dateFormat.format(date);
		for(ProductCheck p :productchecks) {
			List<StockCheck> ss = stockcheck_repos.getStockCheck(p.getId());
			StockCheck s = null;
			for(StockCheck c: ss) {
				if(c.getDate().equals(strDate)) {
					s= c;
					break;
				}	
			}
			if(s != null) {
				if(s.getQuantity().equals(p.getStock())) {
					s.setStatus("Checked");
				}else {
					s.setStatus("Conflict");	
				}
			}else {
				if(p.getStock()==null||p.getStock().equals("0") || p.getStock().equals("")) {
					
				}else {
					s = new StockCheck();
					s.setDate(date);
					s.setProductId(p.getId());
					s.setQuantity("0");
					s.setTotal(0f);
					if(Double.isNaN(Float.parseFloat(p.getUnit_cost()))) {
						s.setUnitCost(0f);
					}else {
						s.setUnitCost(Float.parseFloat(p.getUnit_cost()));	
					}
					
					s.setStatus("Conflict");
				}
			}
			if(s!=null) {
				stockcheck_repos.save(s);
				p.setStockcheck(s);
			}
					
		}
		return productchecks;
	}
	
	public List<Order> getOrder(){
		return order_repos.getOrders();
	}
	
	public List<OrderList> getOrderLIst(){
		return orderlist_repos.getOrderLists();
	}
	
	public Purchase getPurchasesById(String id){
		return purchase_repos.getPurchasesById(id);
	}
	
	public List<Purchase> getPurchasesByYear(Integer year){
		return purchase_repos.getPurchasesByYear(year);
	}
	
	public List<Purchase> getCashFlow(){
		return purchase_repos.getCashFlow();
	}
	
	public List<Purchase> getPurchasesByYearMonth(Integer year, Integer month){
		return purchase_repos.getPurchasesByYearMonth(year,month);
	}
	
	
	
	public List<Purchase> getAccountByYearMonth(Integer year, Integer month){
		return purchase_repos.getAccountByYearMonth(year,month);
	}
	
	public List<Purchase> getPurchasesByYearMonth(Integer year, Integer month, Integer type){
		List<Integer> types = Lists.newArrayList(type);
		return purchase_repos.getPurchasesByYearMonth(year,month,types);
	}
	
	public List<Purchase> getPurchases(){
		return purchase_repos.getPurchases();
	}
	
	public List<PurchaseItemList> getPurchaseItemListsByPurchaseId(String id){
		return purchaseslist_repos.getPurchaseItemListsByPurchaseId(id);
	}
	
	public List<ExpiredCheck> getExpiredItemListsbyCode(String code){
		return expiredcheck_repos.getExpiredItemListsbyCode(code);
	}
	
	public List<ExpiredCheck> getExpiredItemLists(Integer year, Integer month){
		return expiredcheck_repos.getExpiredItemLists(year, month);
	}
	
	public void setPurchaseItemListsWithExpired(List<ExpiredCheck> purchaseItem){
//		purchaseslist_repos.save(purchaseItem);
		expiredcheck_repos.saveAll(purchaseItem);
	}
	
	public void setPurchaseItemListWithExpired(ExpiredCheck purchaseItem){
//		purchaseslist_repos.save(purchaseItem);
		expiredcheck_repos.save(purchaseItem);
	}
	
	
	public List<OrderList> getOrderItemListsById(String id){
		 List<OrderList> o =orderlist_repos.getOrderItemListsById(id);
		return o;
	}
	
	public List<PurchaseItemList> getPurchaseItemLists(){
		return purchaseslist_repos.getPurchaseItemLists();
	}
	
	public List<Payment> getPaymentsWithEarnedByYearMonth(Integer year, Integer month){
		List<Payment> pageItem = payment_repos.getPaymentsByYearMonth(year,month);
		List<Payment> returnspageItem = Lists.newArrayList();
		Double earnedperday = 0.0;
		Double cogsperday = 0.0;
		Double paymentdueperday = 0.0;
		Double paymentfeesperday = 0.0;
		Double paymentcreditperday = 0.0;
		Double shippingfeesperday = 0.0;
		Double commissionfeesperday = 0.0;
		Double otherfeesperday = 0.0;
		String dateString = pageItem.get(0).getDate().substring(0,10);
		int orderCount = 0;
		int index =1;
		for(Payment p : pageItem) {
			if(index == pageItem.size()) {
				java.text.DateFormat dateFormat;
				dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date = dateFormat.parse(dateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Payment rp = new Payment();
				rp.setId(dateString);
				rp.setDate(date);
				rp.setOrderCount(orderCount);
				rp.setTotalCOGS(df.format(cogsperday));
				rp.setEarned(df.format(earnedperday));
				rp.setPaymentCredit(df.format(paymentcreditperday));
				rp.setPaymentDue(df.format(paymentdueperday));
				rp.setPaymentFees(df.format(paymentdueperday));
				rp.setShippingFees(df.format(shippingfeesperday));
				rp.setCommissionFees(df.format(commissionfeesperday));
				rp.setOthersFees(df.format(otherfeesperday));
				returnspageItem.add(rp);
				dateString = p.getDate().substring(0,10);
				orderCount=0;
				earnedperday = 0.0;
				cogsperday = 0.0;
				paymentdueperday = 0.0;
				paymentfeesperday = 0.0;
				paymentcreditperday = 0.0;
				shippingfeesperday = 0.0;
				commissionfeesperday = 0.0;
				otherfeesperday = 0.0;
			}
			if(!dateString.equals("") && !dateString.equals(p.getDate().substring(0,10))) {
				java.text.DateFormat dateFormat;
				dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date = dateFormat.parse(dateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Payment rp = new Payment();
				rp.setId(dateString);
				rp.setDate(date);
				rp.setOrderCount(orderCount);
				rp.setTotalCOGS(df.format(cogsperday));
				rp.setEarned(df.format(earnedperday));
				rp.setPaymentCredit(df.format(paymentcreditperday));
				rp.setPaymentDue(df.format(paymentdueperday));
				rp.setPaymentFees(df.format(paymentdueperday));
				rp.setShippingFees(df.format(shippingfeesperday));
				rp.setCommissionFees(df.format(commissionfeesperday));
				rp.setOthersFees(df.format(otherfeesperday));
				returnspageItem.add(rp);
				dateString = p.getDate().substring(0,10);
				orderCount=0;
				earnedperday = 0.0;
				cogsperday = 0.0;
				paymentdueperday = 0.0;
				paymentfeesperday = 0.0;
				paymentcreditperday = 0.0;
				shippingfeesperday = 0.0;
				commissionfeesperday = 0.0;
				otherfeesperday = 0.0;
			}
			Float cogs  =orderlist_repos.getCOGSByInvoiceId(p.getOrderId());
			p.setTotalCOGS(df.format(cogs));
			Double adjusted  = 0.0;
			if(p.getAdjusted()!=null && !p.getAdjusted().trim().equals("")) {
				Double.parseDouble(p.getAdjusted());	
			}			
			Double earned = Double.parseDouble(p.getPaymentDue()) - Double.parseDouble(p.getTotalCOGS()) - adjusted;
			p.setEarned(df.format(earned));
			
			
			if(p.getCommissionFees()!=null && !p.getCommissionFees().trim().equals("")) {
				commissionfeesperday+= Double.parseDouble(p.getCommissionFees());	
			}
			if(p.getOthersFees()!=null && !p.getOthersFees().trim().equals("")) {
				otherfeesperday+= Double.parseDouble(p.getOthersFees());
			}
			if(p.getShippingFees()!=null && !p.getShippingFees().trim().equals("")) {
				shippingfeesperday+= Double.parseDouble(p.getShippingFees());	
			}
			if(p.getPaymentFees()!=null && !p.getPaymentFees().trim().equals("")) {
				paymentfeesperday+= Double.parseDouble(p.getPaymentFees());
			}
			
			paymentcreditperday+= Double.parseDouble(p.getPaymentCredit());
			paymentdueperday+= Double.parseDouble(p.getPaymentDue());
			cogsperday +=cogs;
			earnedperday +=earned;
			orderCount++;
			index++;
		}
		return returnspageItem;
	}
	
	
	public List<Payment> getPaymentsbyDate(Date from, Date to){
		List<Payment> pageItem = payment_repos.getPaymentsByDate(from, to);
		for(Payment p : pageItem) {
			Float cogs  =orderlist_repos.getCOGSByInvoiceId(p.getOrderId());
			p.setTotalCOGS(df.format(cogs));
			Double adjusted  = 0.0;
			if(p.getAdjusted()!=null && !p.getAdjusted().trim().equals("")) {
				Double.parseDouble(p.getAdjusted());	
			}			
			Double earned = Double.parseDouble(p.getPaymentDue()) - Double.parseDouble(p.getTotalCOGS()) - adjusted;
			p.setEarned(df.format(earned));
		}
		return pageItem;
	}
	
	public List<Payment> getPayments(){
		return payment_repos.getPayments();
	}
	public Payment getPaymentsByInvoiceNo(String id){
		return payment_repos.getPaymentsByInvoiceNo(id);
	}
	
	public Payment getPaymentsById(String id){
		Payment p = payment_repos.getPaymentsByInvoiceNo(id);
		Float cogs  =orderlist_repos.getCOGSByInvoiceId(p.getOrderId());
		p.setTotalCOGS(df.format(cogs));
		Double adjusted  = 0.0;
		if(p.getAdjusted()!=null && !p.getAdjusted().trim().equals("")) {
			adjusted = Double.parseDouble(p.getAdjusted());	
		}			
		Double earned = Double.parseDouble(p.getPaymentDue()) - Double.parseDouble(p.getTotalCOGS()) - adjusted;
		p.setEarned(df.format(earned));
		return p;
	}
	
	public List<Payment> getPaymentsByYear(Integer year){
		return payment_repos.getPaymentsByYear(year);
	}
	
	public List<Payment> getPaymentsByYearMonth(Integer year, Integer month){
		return payment_repos.getPaymentsByYearMonth(year,month);
	}
	
	public List<Payment> getPaymentsByYearMonth(Integer year, Integer month, Integer type){
		List<Integer> types = Lists.newArrayList(type);
		return payment_repos.getPaymentsByYearMonth(year,month,types);
	}
	
	public Page<Payment> getPaginationPaymentsByYearMonth(Integer year, Integer month, Integer type, Integer page, Integer size){
		Pageable pagingSort = PageRequest.of(page, size);
		Page<Payment> pageItem;
		List<Integer> types = Lists.newArrayList(type);
		pageItem = payment_repos.getPaginationPaymentsByYearMonth(year,month,types,pagingSort);
		for(Payment p : pageItem) {
			Float cogs  =orderlist_repos.getCOGSByInvoiceId(p.getOrderId());
			p.setTotalCOGS(df.format(cogs));
			Double adjusted  = 0.0;
			if(p.getAdjusted()!=null && !p.getAdjusted().trim().equals("")) {
				Double.parseDouble(p.getAdjusted());	
			}			
			Double earned = Double.parseDouble(p.getPaymentDue()) - Double.parseDouble(p.getTotalCOGS()) - adjusted;
			p.setEarned(df.format(earned));
		}
		return pageItem;
	}
	
	public List<Object[]> getOrderSummaryDaily(){
		List<Object[]> result = Lists.newArrayList();
		result = payment_repos.getOrderSummaryDaily(2021,7,null);
		return result;
	}
	
	public List<Object[]> getOrderSummaryByWeek(){
		List<Object[]> result = Lists.newArrayList();
		result = payment_repos.getOrderSummaryByWeek(2021, null);
		return result;
	}
	
	public Page<Client> getClients(Integer page, Integer size) {
		Pageable pagingSort = PageRequest.of(page, size);
		Page<Client> pageItem;
		pageItem = client_repos.getClients(pagingSort);
		return pageItem;
	}
	
	public List<Client> getClients() {
		return client_repos.getClients();
	}
	
	public List<Supplier> getSuppliers() {
		return supplier_repos.getSuppliers();
	}
	
	
	public List<Product> getProducts() { 
		return inventory_repos.getProducts();
	}
	
	public List<InvoiceType> getOtherInvoiceTypes(List<Integer> types) {
		return invoicetype_repos.getOtherInvoiceTypes(types);
	}
	
	public List<InvoiceType> getAllInvoiceTypes() {
		return invoicetype_repos.getAllInvoiceTypes();
	}
	
	public List<InvoiceType> getInvoiceTypes(List<Integer> types) {
		return invoicetype_repos.getInvoiceTypes(types);
	}
	
	public InvoiceType addInvoiceTypes(String id,String name){
		InvoiceType type = new InvoiceType();
		type.setId(Integer.parseInt(id));
		type.setDescription(name);
		return invoicetype_repos.save(type);
	}
	
	public List<ProviderSource> getProviderSources() {
		return provider_repos.getProviderSources();
	}
	
	public List<ProductItem> getItemProducts() {
		return item_repos.getItems();
	}
	
	public Page<Inventory> getProductItemsByType(String type, Integer page, Integer size){
		Pageable pagingSort = PageRequest.of(page, size);
		Page<Inventory> pageItem;
		if(type.equals("0")) {
			pageItem = inventory_repos.getProductByOtherType(pagingSort);
		}else {
			pageItem =inventory_repos.getProductByType(type,pagingSort );	
		}
		
		return pageItem;
	}
	public Page<Item> getItemProductsByType(String type, Integer page, Integer size){
		Pageable pagingSort = PageRequest.of(page, size);
		Page<Item> pageItem;
		if(type.equals("0")) {
			pageItem = item_repos.getItemsByOtherType(pagingSort);
		}else {
			pageItem =item_repos.getItemsByType(type,pagingSort );	
		}
		
		return pageItem;
	}
	
	public void saveLazadaTransaction(MultipartFile file) throws Exception {
	    try {
	      String excel = "";
	      if(file.getOriginalFilename().endsWith(".xlsx")) {
	    	  excel = ".xlsx";
	      }else if(file.getOriginalFilename().endsWith(".xls")){
	    	  excel = ".xls";
	      }else if(file.getOriginalFilename().endsWith(".csv")){
	    	  excel = ".csv";
	      }
	      List<Payment> payments = ExcelLazadaHelper.excelLazadaToAmountTransaction(file.getInputStream(),excel);
	      updatePayment(payments);
	      file.getInputStream().close();
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store excel data: " + e.getMessage());
	    }
	}
	
	public void updatePayment(List<Payment> payments) throws Exception 
	{
        for(Payment payment :payments) {
        	Payment o = payment_repos.getPaymentsByInvoiceNo(payment.getOrderId());
        	if(o != null) {
        		o.setPaymentCredit(payment.getPaymentCredit());
        		o.setShippingFees(payment.getShippingFees());
        		o.setPaymentDue(payment.getPaymentDue());
        		o.setPaymentFees(payment.getPaymentFees());
        		o.setOthersFees(payment.getOthersFees());
        		o.setStatus(payment.getStatus());
        		payment_repos.save(o);
        	}
        		
        	
        }
    }
	
	
	public void saveLazadaOrderTransaction(MultipartFile file) throws Exception {
	    try {
	      String excel = "";
	      if(file.getOriginalFilename().endsWith(".xlsx")) {
	    	  excel = ".xlsx";
	      }else if(file.getOriginalFilename().endsWith(".xls")){
	    	  excel = ".xls";
	      }
	      List<Client> clients = ExcelLazadaHelper.excelLazadaToClient(file.getInputStream(),excel);
	      List<Client> listWithoutDuplicates = Lists.newArrayList(
	    	      new HashSet<>(clients));
	      client_repos.saveAll(listWithoutDuplicates);
	      List<Payment> payments = ExcelLazadaHelper.excelLazadaToTransaction(file.getInputStream(),excel,clients);
	      saveAllPayment(payments);
	      cancelOrderItems(payments);
	      List<OrderList> orderitems = ExcelLazadaHelper.excelLazadaToOrderList(file.getInputStream(),excel);
	      addOrderItems(orderitems);
	      file.getInputStream().close();
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store excel data: " + e.getMessage());
	    }
	}
	
	public void saveShopeeTransaction(MultipartFile file) throws Exception {
	    try {
	      String excel = "";
	      if(file.getOriginalFilename().endsWith(".xlsx")) {
	    	  excel = ".xlsx";
	      }else if(file.getOriginalFilename().endsWith(".xls")){
	    	  excel = ".xls";
	      }
	      List<Client> clients = ExcelShopeeHelper.excelShopeeToClient(file.getInputStream(),excel);
	      client_repos.saveAll(clients);
	      List<Payment> payments = ExcelShopeeHelper.excelShopeeToTransaction(file.getInputStream(),excel,clients);
	      saveAllPayment(payments);
	      List<Payment> cancelledPayments = ExcelShopeeHelper.excelShopeeToCancelledTransaction(file.getInputStream(),excel,clients);
	      List<OrderList> orderitems = ExcelShopeeHelper.excelShopeeToOrderList(file.getInputStream(),excel);
	      addOrderItems(orderitems);
	      file.getInputStream().close();
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store excel data: " + e.getMessage());
	    }
	}
	
	public void saveAllPayment(List<Payment> excelPayments) {
		List<Payment> payments = Lists.newArrayList();
		for(Payment excelPayment: excelPayments) {
			Payment payment = payment_repos.getPaymentsByInvoiceNo(excelPayment.getOrderId());
			if(payment!=null) {
				excelPayment.setId(payment.getId());	
			}
			payments.add(excelPayment);
		}
		payment_repos.saveAll(payments);
		
	}
	public void cancelOrderItems(
			List<Payment> payments
            ) throws Exception {
		for(Payment payment :payments) {
        	List<OrderList> o = orderlist_repos.getOrderItemListsById(payment.getOrderId());
        	
    		for(OrderList ol : o) {
    			
    			Inventory product = getProductByCode(ol.getItemId());
    			if(product!=null) {
                	Double total = Double.parseDouble(product.getTotalStock()==null?"0.0":product.getTotalStock());
                	Integer stock = Integer.parseInt(product.getStock()==null?"0":product.getStock());
                	stock += ol.getQuantity();
                	total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	updateProduct(product);
    			}
            	deleteOrderItem(ol);
    		}
        		
        }
	}
	public void addOrderItems(
			List<OrderList> orderitems
            ) throws Exception {
        for(OrderList orderitem :orderitems) {
        	List<OrderList> o = orderlist_repos.getOrderItemListsBy2Id(orderitem.getInvoiceId(),orderitem.getItemId());
        	
    		for(OrderList ol : o) {
    			
    			Inventory product = getProductByCode(ol.getItemId());
            	Double total = Double.parseDouble(product.getTotalStock()==null?"0.0":product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock()==null?"0":product.getStock());
            	stock += ol.getQuantity();
            	total = Double.parseDouble(product.getUnitCost()) * stock;
            	product.setStock(stock.toString());
            	product.setTotalStock(df.format(total));
            	updateProduct(product);
            	deleteOrderItem(ol);
    		}
        		
    		List<Inventory> products = getListProductByCode(orderitem.getItemId());
        	Integer orderStock = orderitem.getQuantity();
        	for(Inventory product : products) {
            	Double total = Double.parseDouble(product.getTotalStock()==null?"0.0":product.getTotalStock());
            	Integer stock = Integer.parseInt(product.getStock()==null?"0":product.getStock());
        		if(stock>= orderStock) {
                	stock -= orderStock;
        			orderStock =0;
                	total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	updateProduct(product);

            		Double unitCost = Double.parseDouble(product.getUnitCost()==null?"0":product.getUnitCost());
            		Double totalCost = unitCost * orderitem.getQuantity();
            		orderitem.setUnitPrice(df.format(unitCost));
            		orderitem.setTotalPrice(df.format(totalCost));
                	break;
        		}else {
        			orderStock =orderStock - stock;
        			stock = 0;
        			total = Double.parseDouble(product.getUnitCost()) * stock;
                	product.setStock(stock.toString());
                	product.setTotalStock(df.format(total));
                	updateProduct(product);

            		Double unitCost = Double.parseDouble(product.getUnitCost()==null?"0":product.getUnitCost());
            		Double totalCost = unitCost * orderitem.getQuantity();
            		orderitem.setUnitPrice(df.format(unitCost));
            		orderitem.setTotalPrice(df.format(totalCost));
        		}
        		
        	}
        	createOrderItem(orderitem);
        }
    }
	
	public List<Item> getItemProductsById(String id){
		return item_repos.getItemProductsById(id);
	}
	
	public Inventory getProductById(String code){
		List<Inventory> invetories = inventory_repos.getListProductById(code);
		if(invetories.size()>0) {
			return invetories.get(0);	
		}else {
			return null;
		}
		
	}
	
	public Inventory getProductByCode(String code){
		List<Inventory> invetories = inventory_repos.getListProductByCode(code);
//		return inventory_repos.getProductByCode(code);
		if(invetories.size()>0) {
			return invetories.get(0);	
		}
		return null;
	}
	
	public List<Product> getListProductBySupplier(Integer supplier){
		return inventory_repos.getListProductBySupplier(supplier);
	}
	
	public List<Inventory> getListProductByCode(String code){
		return inventory_repos.getListProductByCode(code);
	}
	
	public List<Inventory> getListProductById(String code){
		return inventory_repos.getListProductById(code);
	}
	
	public Inventory getProductByCodeSupplierId(String code, Integer id){
		List<Inventory> products = inventory_repos.getProductByCodeSupplierId(code, id);
		
		if(products.size() >0) {
			return products.get(0);	
		}else {
			System.out.println("Code :" + code +", Supplier:" + id);	
		}
		
		return null;
	}
	
	public Payment addTransaction(Payment payment) {
		return payment_repos.save(payment);
	}
	
	public void deleteTransaction(Payment payment) {
		payment_repos.delete(payment);
	}
	
	public Client createClient(Client client) {
		return client_repos.save(client);
	}
	
	public PurchaseItemList createPurchaseItem(PurchaseItemList purchaseitem) {
		return purchaseslist_repos.save(purchaseitem);
	}
	
	public void deletePurchaseItem(PurchaseItemList purchaseitem) {
		purchaseslist_repos.delete(purchaseitem);
	}
	
	public OrderList createOrderItem(OrderList orderitem) {
		return orderlist_repos.save(orderitem);
	}
	
	public void deleteOrderItem(OrderList orderitem) {
		orderlist_repos.delete(orderitem);
	}
	
	public void deletePurchase(Purchase purchase) {
		 purchase_repos.delete(purchase);
	}
	
	public Purchase createPurchase(Purchase purchase) {
		return purchase_repos.save(purchase);
	}
	
	public Account createAccount(Account account) {
		return account_repos.save(account);
	}		
	
	public Supplier createSupplier(Supplier supplier) {
		return supplier_repos.save(supplier);
	}
	
	public SimpleItem createSimpleItem(SimpleItem item) {
		return simpleitem_repos.save(item);
	}
	
	public Item createItem(Item item) {
		return item_repos.save(item);
	}
	
	public ProductCheck createProduct(ProductCheck product) {
		return product_check_repos.save(product);
	}
	
	public Inventory createProduct(Inventory product) {
		return inventory_repos.save(product);
	}
	
	public Inventory updateProduct(Inventory product) {
		return inventory_repos.save(product);
	}
	
	public List<Integer> getTransactionYears(){
		return payment_repos.getTransactionYears();
	}
	
	public List<Integer> getAccountYears(){
		return purchase_repos.getAccountYears();
	}
	
	private static double roundDouble(double d, int places) {
		 
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
 
    private static float roundFloat(float f, int places) {
 
        BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }
    
    public CashFlowReport getCashFlowReport() {
    	CashFlowReport report = new CashFlowReport();
    	float cash = DatabaseService.roundFloat( purchase_repos.getShopCash() == null?0l:purchase_repos.getShopCash(), 2);
        float bankAccount  = DatabaseService.roundFloat(purchase_repos.getBankMoney() == null?0l:purchase_repos.getBankMoney(), 2);        
        float shopeeWallet  = DatabaseService.roundFloat(purchase_repos.getShopeeWallet() == null?0l:purchase_repos.getShopeeWallet(), 2);      
        float lazadaWallet  = DatabaseService.roundFloat(purchase_repos.getLazaWallet() == null?0l:purchase_repos.getLazaWallet(), 2);        
//        float insurance  = DatabaseService.roundFloat(purchase_repos.getInsurance() == null?0l:purchase_repos.getInsurance(), 2);
        float wallet  = DatabaseService.roundFloat(purchase_repos.getWallet() == null?0l:purchase_repos.getWallet(), 2);
        float total = DatabaseService.roundFloat(cash + bankAccount + shopeeWallet + lazadaWallet + wallet, 2);
    	report.setBankAccount(bankAccount);
    	report.setCash(cash);
//    	report.setInsurance(insurance);
    	report.setWallet(wallet);
    	report.setLazadaWallet(lazadaWallet);
    	report.setShopeeWallet(shopeeWallet);
    	report.setTotal(total);
		return report;
	}
    
	public CapitalReport getCapitalReport(Integer year) {
		CapitalReport report = new CapitalReport();
		
		List<Integer> years = Lists.newArrayList(year);
		float inventory = 0l;
		List<Integer> accountYears = purchase_repos.getAccountYears();
		if(year.equals(accountYears.get(0))) {
			inventory= DatabaseService.roundFloat(inventory_repos.getInventoryValue(), 2);
		}else {
			inventory = DatabaseService.roundFloat(purchase_repos.getInventoryValue(years), 2);
		}
		float bf = 0l;
	    if(accountYears.contains(year-1)) {
	    	List<Integer> previousyears = Lists.newArrayList(year-1);
	    	
	    	
	    	float bfdividend = DatabaseService.roundFloat(purchase_repos.getYearlyDividendPaidValue(previousyears), 2);
		    float bfinvestment = DatabaseService.roundFloat(purchase_repos.getYearlyInvestmentValue(previousyears), 2);
		    float bfopenAccount = DatabaseService.roundFloat(purchase_repos.getOpenBankAccountValue(previousyears), 2);    
		    float bfexpenses= DatabaseService.roundFloat(purchase_repos.getExpenditureByYear(previousyears), 2);
		    float bfsales = DatabaseService.roundFloat(payment_repos.getLatestSalesbyYear(previousyears), 2);		    
		    float bfinventorycash = 0l;
		    bfinventorycash = (bfsales + bfinvestment + bfopenAccount + bfdividend) - bfexpenses;
		    bfinventorycash = DatabaseService.roundFloat(bfinventorycash, 2);
		    	    	
		    float bfinventory = DatabaseService.roundFloat(purchase_repos.getInventoryValue(previousyears), 2);	    		
		    float bfcash = bfinventorycash - bfinventory;
		    
		    bf = DatabaseService.roundFloat(bfinventorycash, 2);
	    }
	    java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy");
        String strYear = dateFormat.format(new Date());
	    float dividend = DatabaseService.roundFloat(purchase_repos.getYearlyDividendPaidValue(years), 2);
	    float investment = DatabaseService.roundFloat(purchase_repos.getYearlyInvestmentValue(years), 2);
	    float openAccount = DatabaseService.roundFloat(purchase_repos.getOpenBankAccountValue(years), 2);    
	    float expenses= DatabaseService.roundFloat(purchase_repos.getExpenditureByYear(years), 2);
	    float sales = DatabaseService.roundFloat(payment_repos.getLatestSalesbyYear(years), 2);
	    
	    
	    Float equipment = purchase_repos.getYearlyEquipmentValue(years);
    	report.setInventory(inventory);	
    
    	report.setBringForward(bf);
    	report.setDividend(dividend);	
    
    	report.setInvestment(investment);	
    
    	report.setOpenAccount(openAccount);	
    	report.setSales(sales);
    	report.setExpenditure(expenses);
//	    float bal1 = investment + openAccount + dividend;
	    float cash = 0l;

    	if(year==Integer.parseInt(strYear)) {
    		cash = (sales + bf + investment + openAccount + dividend) - expenses ;
    	}else {
    		cash = (sales + investment + openAccount + dividend) - expenses - inventory;
    	}
	    cash = DatabaseService.roundFloat(cash, 2);
	    	    	
	    	    		
	    float inventorycash = inventory + cash;
	    
    		    
    	report.setCash(cash);	
    	
    	float earned = (inventorycash) - openAccount - investment;
    	if(year==Integer.parseInt(strYear)) {
    		earned = (inventorycash) - openAccount - investment - bf;
    	}
    	earned = DatabaseService.roundFloat(earned, 2);
    	report.setEarned(earned);
	    inventorycash = DatabaseService.roundFloat(inventorycash, 2);
	    report.setInventorycash(inventorycash);
	    float totalCapital = report.getBringForward() + report.getInvestment();
	    report.setTotalCapital(DatabaseService.roundFloat(totalCapital, 2));
		return report;
	}
	
	public ExpenditureReport getExpenditureReport(Integer year) {
		ExpenditureReport report = new ExpenditureReport();
		
		List<Integer> years = Lists.newArrayList(year);
//		float inventory =  0l;
//        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy");  
//        String strYear = dateFormat.format(new Date());
        
        Float inventory =  DatabaseService.roundFloat(purchase_repos.getYearlyStockCost(years),2);	        
	    Float packages  = DatabaseService.roundFloat(purchase_repos.getYearlyPackageValue(years),2);	    
	    Float advertise = DatabaseService.roundFloat(purchase_repos.getYearlyAdvertiseValue(years),2);	  
	    Float equipment = DatabaseService.roundFloat(purchase_repos.getYearlyEquipmentValue(years),2);
	    Float lost = DatabaseService.roundFloat(purchase_repos.getYearlyLostValue(years),2);
	    Float refund = DatabaseService.roundFloat(purchase_repos.getYearlyRefundValue(years),2);
	    Float subsidy = DatabaseService.roundFloat(purchase_repos.getYearlySubsidyValue(years),2);
	    
	    
    	report.setInventory(inventory);	
    
    	report.setAdvertise(advertise);	
    
    	report.setEquipment(equipment);	
    
    	report.setLost(lost);	
    
    	report.setPackages(packages);	
    
    	report.setRefund(refund);	
    
    	report.setSubsidy(subsidy);	
	    
		return report;
	}

	public List<DetailReport> getDetailReport(Integer year) {
		List<DetailReport> detailReports = Lists.newArrayList();

		List<Integer> years = Lists.newArrayList(year);
		List<Integer> accountYears = purchase_repos.getAccountYears();
		float bfinventorycash = 0l;
		if(accountYears.contains(year-1)) {
	    	List<Integer> previousyears = Lists.newArrayList(year-1);
	    	
	    	float bfdividend = DatabaseService.roundFloat(purchase_repos.getYearlyDividendPaidValue(previousyears), 2);
		    float bfinvestment = DatabaseService.roundFloat(purchase_repos.getYearlyInvestmentValue(previousyears), 2);
		    float bfopenAccount = DatabaseService.roundFloat(purchase_repos.getOpenBankAccountValue(previousyears), 2);    
		    float bfexpenses= DatabaseService.roundFloat(purchase_repos.getExpenditureByYear(previousyears), 2);
		    float bfsales = DatabaseService.roundFloat(payment_repos.getLatestSalesbyYear(previousyears), 2);		    
		    float bfcash = 0l;
		    bfinventorycash = (bfsales + bfinvestment + bfopenAccount + bfdividend) - bfexpenses;
		    bfinventorycash = DatabaseService.roundFloat(bfinventorycash, 2);
		    	    	
		    float bfinventory = DatabaseService.roundFloat(purchase_repos.getInventoryValue(previousyears), 2);	    		
		    bfcash = bfinventorycash - bfinventory;
		    
	    }
		float accEarned =  0l;
		for(Integer i = 1; i <=12 ; i++) {
			float earned =  0l;
			DetailReport detailReport = new DetailReport();
			float investment = DatabaseService.roundFloat(purchase_repos.getInvestmentValueByMonth(year,i),2);
			bfinventorycash +=investment;
			float sales = DatabaseService.roundFloat(payment_repos.getReportDueByMonth(year,i),2);
			float netsales = 0l;
			float expenses = DatabaseService.roundFloat(purchase_repos.getReportbyMonth(year,i),2);
			float invcash = DatabaseService.roundFloat(purchase_repos.getInventoryCashValue(year,i),2);
			Integer lazada = payment_repos.getOrderReportbyMonth(year,i,1);
			Integer shopee = payment_repos.getOrderReportbyMonth(year,i,2);
			Integer local = payment_repos.getOrderReportbyMonth(year,i,3);
			Integer orderCount = lazada + shopee +local;
			
			
			detailReport.setSale(sales);
			detailReport.setLazada(lazada);
			detailReport.setShopee(shopee);
			detailReport.setLocalShop(local);
			detailReport.setOrderCount(orderCount);
			detailReport.setExpenditure(expenses);	
			
			float expenditureTotalAmount = purchase_repos.getTotalExpenditure(year,i);
			List<Object[]> expenditures = purchase_repos.getExpenditure(year,i);
			for(Object[] expenditure : expenditures) {
				switch(Integer.parseInt(expenditure[0].toString())) {
					case 2:{
						detailReport.setPackages(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 3:{
						detailReport.setAdvertisement(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 5:{
						detailReport.setRefund(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 6:{
//						detailReport.set(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 7:{
//						detailReport.setPackages(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
				}
			}
			
			
			
			float lazadacogs = 0f;
			try {
				lazadacogs = DatabaseService.roundFloat(payment_repos.getCOGS(year,i,1),2);	
			}catch(Exception e) {
				
			}
			
			float shopeecogs = 0f;
			try {
				shopeecogs = DatabaseService.roundFloat(payment_repos.getCOGS(year,i,2),2);	
			}catch(Exception e) {
				
			}
			
			float cogs = 0f;
			try {
				cogs = DatabaseService.roundFloat(payment_repos.getCOGS(year,i,null),2);	
			}catch(Exception e) {
				
			}
			detailReport.setShopeecogs(shopeecogs);
			detailReport.setLazadacogs(lazadacogs);
			detailReport.setCogs(cogs);
			
			
	        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM");
	        String strMonth = dateFormat.format(new Date());
	        dateFormat = new java.text.SimpleDateFormat("yyyy");  
	        String strYear = dateFormat.format(new Date());

			float balance = sales - expenses;
        	if(year==Integer.parseInt(strYear)) {
		        if(i>Integer.parseInt(strMonth)) {
		        	detailReport.setEarned(DatabaseService.roundFloat(0,2));	
		        }
		        else{
	        		if(i==Integer.parseInt(strMonth)) {

		        		float bfInvcash = DatabaseService.roundFloat(purchase_repos.getInventoryCashValue(year,i-1),2);
		        		float bfstocks = DatabaseService.roundFloat(purchase_repos.getInventoryValue(year,i-1),2);
		        		float cash = (bfInvcash - bfstocks) + sales - expenses + investment;
			        	float stocks = DatabaseService.roundFloat(inventory_repos.getInventoryValue(),2);
//		        		stocks = DatabaseService.roundFloat(inventory_repos.getInventoryValue(),2);
			        	invcash = cash+stocks;
			        	detailReport.setStocks(stocks);
			        	detailReport.setInventoryCash(invcash);
		        	}else {
		        		float bfstocks = 0f;
		        		try {
		        			bfstocks = DatabaseService.roundFloat(purchase_repos.getInventoryValue(year,i),2);	
		        		}catch(Exception ex) {
		        			bfstocks = DatabaseService.roundFloat(0f,2);
		        		}
			        	detailReport.setStocks(bfstocks);
		        		
		        	}
		        	
					earned = bfinventorycash - invcash - accEarned;
//	        		earned = netsales -cogs - expenditureTotalAmount;
		        	accEarned+=earned;
		        	detailReport.setEarned(DatabaseService.roundFloat(-earned,2));
//		        	detailReport.setEarned(DatabaseService.roundFloat(earned,2));
		        }
        	}else {

	        	detailReport.setEarned(DatabaseService.roundFloat(0,2));	

//        		earned = netsales -cogs - expenditureTotalAmount;
//	        	detailReport.setEarned(DatabaseService.roundFloat(earned,2));
        	}
        	detailReport.setMonthExpenditure(expenditureTotalAmount);
			detailReport.setBalance(DatabaseService.roundFloat(balance,2));
			detailReport.setInventoryCash(invcash);
			
			detailReport.setMonth(i);
			detailReports.add(detailReport);
		}
		
		return detailReports;
	}

	public List<DetailReport> getSummaryDetailReport(Integer year) {
		List<DetailReport> detailReports = Lists.newArrayList();

		List<Integer> years = Lists.newArrayList(year);
		List<Integer> accountYears = purchase_repos.getAccountYears();
		float bfinventorycash = 0l;
		if(accountYears.contains(year-1)) {
	    	List<Integer> previousyears = Lists.newArrayList(year-1);
	    	
	    	float bfdividend = DatabaseService.roundFloat(purchase_repos.getYearlyDividendPaidValue(previousyears), 2);
		    float bfinvestment = DatabaseService.roundFloat(purchase_repos.getYearlyInvestmentValue(previousyears), 2);
		    float bfopenAccount = DatabaseService.roundFloat(purchase_repos.getOpenBankAccountValue(previousyears), 2);    
		    float bfexpenses= DatabaseService.roundFloat(purchase_repos.getExpenditureByYear(previousyears), 2);
		    float bfsales = DatabaseService.roundFloat(payment_repos.getLatestSalesbyYear(previousyears), 2);		    
		    float bfcash = 0l;
		    bfinventorycash = (bfsales + bfinvestment + bfopenAccount + bfdividend) - bfexpenses;
		    bfinventorycash = DatabaseService.roundFloat(bfinventorycash, 2);
		    	    	
		    float bfinventory = DatabaseService.roundFloat(purchase_repos.getInventoryValue(previousyears), 2);	    		
		    bfcash = bfinventorycash - bfinventory;
		    
	    }
		float accEarned =  0l;
		for(Integer i = 1; i <=12 ; i++) {
			float earned =  0l;
			DetailReport detailReport = new DetailReport();
			float investment = DatabaseService.roundFloat(purchase_repos.getInvestmentValueByMonth(year,i),2);
			bfinventorycash +=investment;
			float sales = DatabaseService.roundFloat(payment_repos.getReportByMonth(year,i),2);
			float netsales = 0l;
			float expenses = DatabaseService.roundFloat(purchase_repos.getReportbyMonth(year,i),2);
			float invcash = DatabaseService.roundFloat(purchase_repos.getInventoryCashValue(year,i),2);
			Integer lazada = payment_repos.getOrderReportbyMonth(year,i,1);
			Integer shopee = payment_repos.getOrderReportbyMonth(year,i,2);
			Integer local = payment_repos.getOrderReportbyMonth(year,i,3);
			Integer orderCount = lazada + shopee +local;
			
			
			detailReport.setSale(sales);
			detailReport.setLazada(lazada);
			detailReport.setShopee(shopee);
			detailReport.setLocalShop(local);
			detailReport.setOrderCount(orderCount);
			detailReport.setExpenditure(expenses);	
			
			float expenditureTotalAmount = purchase_repos.getTotalExpenditure(year,i);
			List<Object[]> expenditures = purchase_repos.getExpenditure(year,i);
			for(Object[] expenditure : expenditures) {
				switch(Integer.parseInt(expenditure[0].toString())) {
					case 1:{
						detailReport.setInStock(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 2:{
						detailReport.setPackages(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 3:{
						detailReport.setAdvertisement(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 5:{
						detailReport.setRefund(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 6:{
//						detailReport.set(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
					case 7:{
//						detailReport.setPackages(DatabaseService.roundFloat(Float.parseFloat(expenditure[1].toString()),2));
						break;
					}
				}
			}
			
			List<Object[]> buckets = payment_repos.getPaymentBuckets(year,i, null);

//			sum(payment_credit) as credit, 
//			sum(payment_fees) as fees, 
//			sum(commission_fees) as commission, 
//			sum(shipping_fees) as shipping, 
//			sum(other_fees) as other, 
//			sum(payment_due) as due  
			for(Object[] bucket : buckets) {				
				if(bucket[1] !=null) {
					detailReport.setPaymentFees(DatabaseService.roundFloat(Float.parseFloat(bucket[1].toString()),2));	
				}
				if(bucket[2] !=null) {
					detailReport.setCommissionFees(DatabaseService.roundFloat(Float.parseFloat(bucket[2].toString()),2));	
				}
				if(bucket[3] !=null) {
					detailReport.setShippingFees(DatabaseService.roundFloat(Float.parseFloat(bucket[3].toString()),2));	
				}
				if(bucket[4] !=null) {
					detailReport.setOtherFees(DatabaseService.roundFloat(Float.parseFloat(bucket[4].toString()),2));	
				}
				if(bucket[5] !=null) {
					detailReport.setNetsales(DatabaseService.roundFloat(Float.parseFloat(bucket[5].toString()),2));
					netsales = DatabaseService.roundFloat(Float.parseFloat(bucket[5].toString()),2);
				}
			}
			
			
			
			float lazadacogs = 0f;
			try {
				lazadacogs = DatabaseService.roundFloat(payment_repos.getCOGS(year,i,1),2);	
			}catch(Exception e) {
				
			}
			
			float shopeecogs = 0f;
			try {
				shopeecogs = DatabaseService.roundFloat(payment_repos.getCOGS(year,i,2),2);	
			}catch(Exception e) {
				
			}
			
			float cogs = 0f;
			try {
				cogs = DatabaseService.roundFloat(payment_repos.getCOGS(year,i,null),2);	
			}catch(Exception e) {
				
			}
			detailReport.setShopeecogs(shopeecogs);
			detailReport.setLazadacogs(lazadacogs);
			detailReport.setCogs(cogs);
			
			
	        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM");
	        String strMonth = dateFormat.format(new Date());
	        dateFormat = new java.text.SimpleDateFormat("yyyy");  
	        String strYear = dateFormat.format(new Date());

			float balance = sales - expenses;
        	if(year==Integer.parseInt(strYear)) {
		        if(i>Integer.parseInt(strMonth)) {
		        	detailReport.setEarned(DatabaseService.roundFloat(0,2));	
		        }
		        else{
	        		if(i==Integer.parseInt(strMonth)) {

		        		float bfInvcash = DatabaseService.roundFloat(purchase_repos.getInventoryCashValue(year,i-1),2);
		        		float bfstocks = DatabaseService.roundFloat(purchase_repos.getInventoryValue(year,i-1),2);
		        		float cash = (bfInvcash - bfstocks) + sales - expenses + investment;
			        	float stocks = DatabaseService.roundFloat(inventory_repos.getInventoryValue(),2);
//		        		stocks = DatabaseService.roundFloat(inventory_repos.getInventoryValue(),2);
			        	invcash = cash+stocks;
			        	detailReport.setStocks(stocks);
			        	detailReport.setInventoryCash(invcash);
		        	}else {
		        		float bfstocks = 0f;
		        		try {
		        			bfstocks = DatabaseService.roundFloat(purchase_repos.getInventoryValue(year,i),2);	
		        		}catch(Exception ex) {
		        			bfstocks = DatabaseService.roundFloat(0f,2);
		        		}
			        	detailReport.setStocks(bfstocks);
		        		
		        	}
		        	
//					earned = bfinventorycash - invcash - accEarned;
	        		earned = netsales -cogs - expenditureTotalAmount;
		        	accEarned+=earned;
//		        	detailReport.setEarned(DatabaseService.roundFloat(-earned,2));
		        	detailReport.setEarned(DatabaseService.roundFloat(earned,2));
		        }
        	}else {

//	        	detailReport.setEarned(DatabaseService.roundFloat(0,2));	

        		earned = netsales -cogs - expenditureTotalAmount;
	        	detailReport.setEarned(DatabaseService.roundFloat(earned,2));
        	}
        	detailReport.setMonthExpenditure(expenditureTotalAmount);
			detailReport.setBalance(DatabaseService.roundFloat(balance,2));
			detailReport.setInventoryCash(invcash);
			
			detailReport.setMonth(i);
			detailReports.add(detailReport);
		}
		
		return detailReports;
	}
	
	public static final int DEFAULT_BUFFER_SIZE = 8192;
	private static final Charset[] UTF_ENCODINGS = { Charset.forName("UTF-8"),
		      Charset.forName("UTF-16LE"), Charset.forName("UTF-16BE") };
	
	private static Charset getEncoding(InputStream in) throws IOException {
	    charsetLoop: for (Charset encodings : UTF_ENCODINGS) {
	      byte[] bom = "\uFEFF".getBytes(encodings);
	      in.mark(bom.length);
	      for (byte b : bom) {
	        if ((0xFF & b) != in.read()) {
	          in.reset();
	          continue charsetLoop;
	        }
	      }
	      return encodings;
	    }
	    return Charset.defaultCharset();
	  }
	
	private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {
		
		
//
//        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
//            int read;
//            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
//            while ((read = inputStream.read(bytes)) != -1) {
//                outputStream.write(bytes, 0, read);
//            }
//        }

		
//        FileOutputStream outputStream = new FileOutputStream(file, false);		
//        int read;
//        byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
//        while ((read = inputStream.read(bytes)) != -1) {
//            outputStream.write(bytes, 0, read);
//        }
		 BufferedReader reader = new BufferedReader(
		            new InputStreamReader(inputStream,"UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = reader.read()) != -1; ) {
            sb.append((char) ch);
        }
        
        
        // append = false
     	try (Writer write = new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8")) {
 			write.write(sb.toString());
 			write.close();
     	}
    }

    public String getCashSalesPdfContent(CashSales cashsales) {
    	File file = null;
//        String templatePath = this.getClass().getClassLoader().getResource(Constants.PDF_TEMPLATE_FOLDER).getPath();
//        String templatePath = "/C:/Users/bysadmin/Documents/GitHub/dynamike_server/target/classes/template";
//        file  = new File(templatePath);        	
//        if (!file.isDirectory()) {
//            return null;
//        }

        ClassPathResource cpr = new ClassPathResource(Constants.PDF_TEMPLATE_FOLDER+File.separator+"CashSales.ftl");
        file = new File("CashSales.ftl");
        try {
			copyInputStreamToFile(cpr.getInputStream(), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		String[] date = cashsales.getDate().split("-");
		Integer index =  payment_repos.generateCashSalesNo(Integer.parseInt(date[0].toString()),Integer.parseInt(date[1].toString()),3)==null? 1: payment_repos.generateCashSalesNo(Integer.parseInt(date[0].toString()),Integer.parseInt(date[1].toString()),3)+ 1;
		String formatted = String.format("%03d", index);
		cashsales.setCashSalesNo(date[0].toString()+date[1].toString()+formatted);
        Double total = 0.0;
        int orderIndex = 1;
        for(OrderList o :cashsales.getOrderItemList()) {
        	total += Double.parseDouble(o.getSellingPrice()==null?"0.0":o.getSellingPrice());
        	Double totalPrice = Double.parseDouble(o.getSellingPrice()==null?"0.0":o.getSellingPrice());
        	Double unitPrice = totalPrice / o.getQuantity();
        	o.setUnitPrice(df.format(unitPrice));
        	o.setTotalPrice(df.format(totalPrice));
        	o.setIndex("#"+orderIndex);
        	orderIndex++;
        }
        cashsales.setEmail("");
        cashsales.setSubTotal(df.format(total));
        cashsales.setFinalTotal(df.format(total));
        String phrase = cashsales.getFinalTotal() ;
        Float num = new Float( phrase ) ;
        int dollars = (int)Math.floor( num ) ;
        int cent = (int)Math.floor( ( num - dollars ) * 100.0f ) ;
        cashsales.setRinggit(EnglishNumberToWords.convert( dollars ).toUpperCase());
        cashsales.setCents(EnglishNumberToWords.convert( cent ).toUpperCase());
        return PDFUtil.getContent(cashsales, file.getAbsolutePath(), "CashSales.ftl");
    }
    
    public String getInvoicePdfContent(CashSales cashsales) {
    	File file = null;
//        String templatePath = this.getClass().getClassLoader().getResource(Constants.PDF_TEMPLATE_FOLDER).getPath();
//        String templatePath = "/C:/Users/bysadmin/Documents/GitHub/dynamike_server/target/classes/template";
//        file  = new File(templatePath);        	
//        if (!file.isDirectory()) {
//            return null;
//        }

        ClassPathResource cpr = new ClassPathResource(Constants.PDF_TEMPLATE_FOLDER+File.separator+"Invoice.ftl");
        file = new File("Invoice.ftl");
        try {
			copyInputStreamToFile(cpr.getInputStream(), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		String[] date = cashsales.getDate().split("-");
		Integer index =  payment_repos.generateCashSalesNo(Integer.parseInt(date[0].toString()),Integer.parseInt(date[1].toString()),3)==null? 1: payment_repos.generateCashSalesNo(Integer.parseInt(date[0].toString()),Integer.parseInt(date[1].toString()),3)+ 1;
		String formatted = String.format("%03d", index);
		cashsales.setCashSalesNo(date[0].toString()+date[1].toString()+formatted);
        Double total = 0.0;
        int orderIndex = 1;
        for(OrderList o :cashsales.getOrderItemList()) {
        	total += Double.parseDouble(o.getSellingPrice()==null?"0.0":o.getSellingPrice());
        	Double totalPrice = Double.parseDouble(o.getSellingPrice()==null?"0.0":o.getSellingPrice());
        	Double unitPrice = totalPrice / o.getQuantity();
        	o.setItemId(o.getItemId().replaceAll("\n", "<BR/>"));
        	o.setUnitPrice(df.format(unitPrice));
        	o.setTotalPrice(df.format(totalPrice));
        	o.setIndex("#"+orderIndex);
        	orderIndex++;
        }
        if(cashsales.getContactNo()==null) {
        	cashsales.setContactNo("");	
        }    
        cashsales.setAddress(cashsales.getAddress().replaceAll("\n", "<BR/>"));
        cashsales.setEmail("");
        cashsales.setSubTotal(df.format(total));
        cashsales.setFinalTotal(df.format(total));
        String phrase = cashsales.getFinalTotal() ;
        Float num = new Float( phrase ) ;
        int dollars = (int)Math.floor( num ) ;
        int cent = (int)Math.floor( ( num - dollars ) * 100.0f ) ;
        cashsales.setRinggit(EnglishNumberToWords.convert( dollars ).toUpperCase());
        cashsales.setCents(EnglishNumberToWords.convert( cent ).toUpperCase());
        return PDFUtil.getContent(cashsales, file.getAbsolutePath(), "Invoice.ftl");
    }
}

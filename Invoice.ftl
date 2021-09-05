<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
<TITLE>Cash Sales</TITLE>

<STYLE type="text/css">

.ft0{font: bold 19px 'Arial';line-height: 22px;}
.ft1{font: 11px 'Arial';line-height: 13px;}
.ft2{font: 15px 'Arial';line-height: 17px;}
.ft3{font: bold 16px 'Arial';line-height: 19px;}
.ft4{font: 15px 'Calibri';line-height: 18px;}
.ftItemName{font: 15px 'Microsoft YaHei';line-height: 18px;}
.ft5{font: 1px 'Calibri';line-height: 1px;}
.ft6{font: bold 15px 'Calibri';line-height: 18px;}
.ft7{font: bold 16px 'Calibri';color: #ff0000;line-height: 19px;}
.ft8{font: bold 15px 'Calibri';line-height: 17px;}
.ft9{font: 15px 'Microsoft YaHei';line-height: 20px;}
.ft10{font: italic 15px 'Microsoft YaHei';line-height: 20px;}
.ft11{font: italic 15px 'Calibri';line-height: 18px;text-align: center}
.ft12{font: bold 16px 'Calibri';line-height: 19px;}
.ft13{font: 12px 'Calibri';line-height: 14px;}
.ft14{font: italic bold 12px 'Calibri';line-height: 14px;}
.ft15{font: 12px 'Arial';line-height: 15px;}


body {margin-top: 0px;margin-left: 0px;}

table{
	width:100%;
	margin:0;
	min-height:80%
}

.td{
	border-top:#000 solid;
	border-bottom:#000 solid;
	border-width:3 0 1 0;
}
.thead{
	border-bottom:#000 1px solid;
}
.items{margin-top: 10px;height:350px}

 p{margin:0;white-space: pre-wrap;}
.p0{text-align: center}
.p1{text-align: center}
.p2{text-align: center}
.p3{text-align: center}
.p4{text-align: center;border-top:#000 solid;border-width:3 0 1 0;}
.p6{text-align: right}

.td0{padding: 0px;margin: 0px;vertical-align: bottom;}
.td1{padding: 0px;margin: 0px;vertical-align: bottom;}
.td2{padding: 0px;margin: 0px;vertical-align: bottom;}
.td3{padding: 0px;margin: 0px;vertical-align: bottom;}
.td4{padding: 0px;margin: 0px;vertical-align: bottom;}
.td5{padding: 0px;margin: 0px;vertical-align: bottom;}
.td6{border-bottom: #000000 1px solid;padding: 0px;margin: 0px;vertical-align: bottom;}
.td7{border-bottom: #000000 1px solid;padding: 0px;margin: 0px;vertical-align: bottom;}
.td8{border-bottom: #000000 1px solid;padding: 0px;margin: 0px;vertical-align: bottom;}
.td9{border-bottom: #000000 1px solid;padding: 0px;margin: 0px;vertical-align: bottom;}
.td10{border-bottom: #000000 1px solid;padding: 0px;margin: 0px;vertical-align: bottom;}
.td11{border-bottom: #000000 1px solid;padding: 0px;margin: 0px;vertical-align: bottom;}
.td12{padding: 0px;margin: 0px;vertical-align: bottom;}
.td13{padding: 0px;margin: 0px;vertical-align: bottom;}
.td14{padding: 0px;margin: 0px;vertical-align: bottom;}
.td15{padding: 0px;margin: 0px;vertical-align: bottom;}
.td16{padding: 0px;margin: 0px;vertical-align: bottom;}


.ctd0{padding: 0px;margin: 0px;vertical-align: bottom;width:70%}
.ctd1{padding: 0px;margin: 0px;vertical-align: bottom;width:10%;}
.ctd2{padding: 0px;margin: 0px;vertical-align: bottom;width:10%}

.otd0{width:10%}
.otd1{width:50%;}
.otd2{width:10%}
.otd3{width:10%}
.otd4{width:10%}

.t0{font: 15px 'Calibri';}
.t1{font: bold 16px 'Calibri';}
.boldborder{border-width:3 3 3 3;}
</STYLE>
</HEAD>

<BODY>
<div id="container">

<div class="title">
	<p class="p0 ft0">Dynamike Enterprise</p>
	<p class="p1 ft1"><NOBR>(IP0530890-X)</NOBR></p>
	<p class="p2 ft2">205, Jalan Perak 6, Taman Perak</p>
	<p class="p2 ft2">31900 Kampar, Perak, Malaysia</p>
	<p class="p3 ft2">Email:yoongnan.chin@gmail.com</p>
	<hr/>
	<p class="p4 ft3 thead" >Invoices</p>
</div>

<div class="clientInfo">
	<table cellpadding=0 cellspacing=0 class="t0" >
		<tr>
			<td class="tr0 ctd0"><p class="p5 ft4">Date: ${date}</p></td>
			<td class="tr0 ctd1"><p class="p6 ft6">No.</p></td>
			<td class="tr0 ctd2"><p class="p7 ft7">I${cashSalesNo}</p></td>
		</tr>
		<tr>
			<td class="tr1 ctd0 "><p class="p5 ft4">${clientName}</p></td>
			<td class="tr1 ctd1"><p class="p6 ft5">&nbsp;</p></td>
			<td class="tr1 ctd2"><p class="p6 ft5">&nbsp;</p></td>
		</tr>
		<tr>
			<td class="tr2 ctd0"><p class="p5 ft4">${address}</p></td>
			<td class="tr2 ctd1"><p class="p6 ft6">Due Date:</p></td>
			<td class="tr2 ctd2"><p class="p6 ft6">${date}</p></td>
		</tr>
		<tr>
			<td class="tr2 ctd0"><p class="p5 ft4">Email: ${email}</p></td>
			<td class="tr2 ctd1"><p class="p6 ft4"></p></td>
			<td class="tr2 ctd2"><p class="p8 ft4"></p></td>
		</tr>
	</table>
</div>
<div class="items">
	<table cellpadding=0 cellspacing=0 >
		<Thead class="thead" id="orderlist">
			<tr >
				<td class="tr3 td12 td otd0"><p class="p5 ft8">Item</p></td>
				<td class="tr3 td1 td otd1"><p class="p6 ft8">Description</p></td>
				<td class="tr3 td2 td otd2"><p class="p9 ft8">Qty</p></td>
				<td class="tr3 td3 td otd3"><p class="p7 ft8">U/Price</p></td>
				<td class="tr3 td4 td otd4"><p class="p10 ft8">Total</p></td>
			</tr>
			<tr>
				<td class="tr0 td6"><p class="p6 ft5">&nbsp;</p></td>
				<td class="tr0 td8"><p class="p6 ft5">&nbsp;</p></td>
				<td class="tr0 td9"><p class="p6 ft5">&nbsp;</p></td>
				<td class="tr0 td10"><p class="p7 ft6">USD</p></td>
				<td class="tr0 td11"><p class="p10 ft6">USD</p></td>
			</tr>
		</Thead>
		<tbody>
		<#list orderItemList as orderitem>			
			<tr>
				<td class="tr4 td12"><p class="p5 ft4">${orderitem.index}</p></td>
				<td class="tr4 td2"><p class="p6 ft4">${orderitem.itemId}</p></td>
				<td class="tr4 td2"><p class="p11 ft4">${orderitem.quantity}</p></td>
				<td class="tr4 td3"><p class="p7 ft4">${orderitem.unitPrice}</p></td>
				<td class="tr4 td4"><p class="p10 ft4">${orderitem.totalPrice}</p></td>
			</tr>
		</#list>

		</tbody>
	</table>
</div>
	
<div class="money">
<hr/>
		<table cellpadding=0 cellspacing=0 class="t1">
			<tr>
				<td class="tr0 td14"><p class="p6 ft4">US DOLLAR ${ringgit}
				<#if cents != "ZERO">
					AND CENTS ${cents}
				</#if>ONLY</p></td>
				<td class="tr0 td15"><p class="p6 ft12">Sub Total</p></td>
				<td class="tr0 td16"><p class="p13 ft6">${subTotal} USD</p></td>
			</tr>
			<tr>
				<td class="tr5 td14"><p class="p6 ft5">&nbsp;</p></td>
				<td class="tr5 td15"><p class="p6 ft12">Adj.</p></td>
				<td class="tr5 td16"><p class="p6 ft5">&nbsp;</p></td>
			</tr>
			<tr>
				<td class="tr5 td14"><p class="p6 ft5">&nbsp;</p></td>
				<td class="tr5 td15"><p class="p6 ft12">Final Total</p></td>
				<td class="boldborder tr0 td16"><p class="p13 ft6">${finalTotal} USD</p></td>
			</tr>
		</table>
	</div>
<hr/>
	<div class="footer">
		<p class="p14 ft13">Notes:</p>
		<p class="p15 ft13">1. All cheques should be crossed should made payable to</p>
		<p class="p15 ft14">DYNAMIKE ENTERPRISE</p>
		<p class="p16 ft15">2. Please Bank into CIMB Islamic Bank A/C:</p>
		<p class="p17 ft14">86-0473262-6</p>
		<p class="p18 ft15">And Email The Slip. Thank You !</p>
	</div>
</div>
</body>
</html>

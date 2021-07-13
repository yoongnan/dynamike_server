<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
    <style>
		body{
			font-family: "Helvetica Neue", Arial, sans-serif;
		}
		.lastUpdate{
			font-size: 10px;
			font-weight: 500;
			float: left;
		}
		.bold{
			font-weight: bold;
		}
		.title{
			width: 100%;
			padding: 24px 24px, 24px, 188px;
			color: #fff;
			font-size: 20px;
			font-weight: bold;
			background-color: #697a83;
			text-align: center;
			line-height: 22px;
			border:1px solid #ccc;
		}
		.supportedTitle{
			width: 100%;
			padding: 24px 24px, 24px, 188px;
			color: #fff;
			font-size: 20px;
			font-weight: bold;
			background-color: #0091DA;
			text-align: center;
			line-height: 24px;
		}
		.titleLine{
			padding-left: 50px;
		}
		.supportedTitleLine{
			padding-left: 12px;
		}
		.unSupportedTitleLine{
			padding-left: 1px;
		}
		.table{
			background-color: #fff;
			color: #575757;
			margin: 0;
			max-width: 100%;
			width: 100%;
		}
		table {     
			border:#000 solid ;border-width:.5 .5 0 .5;    
		}
		th {      
			border-right:#000 solid;
			border-bottom:#000 solid;border-width:0 0 .5 0;
		} 		 
		td {      
			border-right:#000 solid;
			border-bottom:#000 solid;border-width:0 0 .5 0;
		}
		.thead{
			color: #575757;
			font-size: 11px;
			font-weight: 600;
			background-color: #fafafa;
			vertical-align: bottom;
		}
		.column{
			vertical-align: middle;
			line-height: 13px;
			padding: 11px 12px;
			text-align: left;
			font-size: 9px;
		}
		.otherColumn{
			vertical-align: middle;
			text-align: center;
			line-height: 13px;
			padding: 11px 12px;
			font-size: 9px;
		}
		.redColor{
			color: red;
			font-weight: bold;
		}
		.violetColor{
			color: #7124ae;
			font-weight: bold;
		}
		.section{
			margin-top: 14px;
			font-size: 10px;
		}
		.section a:visited{
			color: #0077b8;
		}
		.section a{
			text-decoration: none;
		}
		.noteLable{
			color: #29414e;
			font-weight: 600;
			font-size: 12px;
			font-weight: bold;
		}
		.noteSection{
			line-height: 24px;
			margin-top: 24px;
			font-size: 9px;
		}
		.redColor{
			color:#f54f47;
			margin-right: 1rem;
		}
		.violetColor{
			color:#7124ae;
			margin-right: 1rem;
		}
		.header{
			overflow: hidden;
			width: 100%;
		}
		.tip{
			float: right;
			font-size: 10px;
		}
    </style>
</head>
<body>
<div class="container">
	<div class='header'>
		<div class="lastUpdate">
			<span>Last Updated: </span>
			<span>${lastUpdate}</span>
		</div>
		<div class="tip">
			<div><span  class="redColor">Red: </span><label>Within 6 months</label></div>
			<div><span  class="violetColor">Purple: </span><label>Reached End of General Support</label></div>
		</div>
	</div>
	<div class="supportedTitle">
	VMware Product Lifecycle Matrix
	<div class="supportedTitleLine">
	Supported Products Releases
	</div>
	</div>
	<table class="table" cellpadding="0" cellspacing="0" style="repeat-header:yes;repeat-footer:yes;">
		<thead class="thead">
			<tr>	
				<th width='40%' class="column">Product Release</th>
				<th width='11%' class="otherColumn">General Availability</th>
				<th width='11%' class="otherColumn">End of General Support</th>
				<th width='11%' class="otherColumn">End of Technical Guidance</th>
				<th width='11%' class="otherColumn">End of Availability</th>
				<th width='10%' class="otherColumn">Lifecycle Policy</th>
				<th width='6%' class="otherColumn">Notes</th>
			</tr>
		</thead>
		<tbody>
		<#list supportList as support>
			<tr class="tbodyTr">
				<td class="column">${support.name}</td>
				<td class="otherColumn">
					<#if support.gaDate?? >
						${support.gaDate}
					<#else>
						${support.gaText}
					</#if>
				</td>
				<#if support.endSupportDate?? >
					<#if support.endSupportColor?? >
						<#if support.endSupportColor=="RED" >
							<td class="otherColumn redColor">
						<#else>
							<td class="otherColumn violetColor">
						</#if>
					<#else>
						<td class="otherColumn">
					</#if>
						${support.endSupportDate}
					</td>
				<#else>
					<td class="otherColumn">
						${support.endSupportText}
					</td>
				</#if>
				<#if support.endTechGuidanceDate?? >
					<#if support.endTechnicalColor?? >
						<#if support.endTechnicalColor=="RED" >
							<td class="otherColumn redColor">
						<#else>
							<td class="otherColumn violetColor">
						</#if>
					<#else>
						<td class="otherColumn">
					</#if>
						${support.endTechGuidanceDate}
					</td>
				<#else>
					<td class="otherColumn">
						N/A
					</td>
				</#if>
				<td class="otherColumn">
					<#if support.endAvailabilityDate?? >
						<#if support.endAvailabilityLink?? >
							<a href="${support.endAvailabilityLink}">${support.endAvailabilityDate} ${support.endAvailabilityText}</a>
						<#else>
							${support.endAvailabilityDate}
						</#if>
					<#else>
						${support.endAvailabilityText}
					</#if>
				</td>
				<td class="otherColumn">
					<#if support.lifecyclePolicy?? >
						<#if support.lifecyclePolicy.url?? >
							<a href="${support.lifecyclePolicy.url}">${support.lifecyclePolicy.abbreviation}</a>
						<#else>
							 <a href="#">${support.lifecyclePolicy.abbreviation}</a>
						</#if>
					<#else>
						 
					</#if>
				</td>
				<td class="otherColumn">
					<#if support.footnotes?? >
						<#assign company=","/>
						<#list support.footnotes as footnote>
							<#if footnote_index == 0>
								${footnote.name}
							 <#else>
								${company + footnote.name}
							</#if>
						</#list>
					<#else>
						 
					</#if>
				</td>
			</tr>
		</#list>
		</tbody>
	</table>
	<div class="title" style="page-break-before: always;">
	VMware Product Lifecycle Matrix
	<div class="unSupportedTitleLine">
	Unsupported Products Releases
	</div>
	</div>
	<table class="table" cellpadding="0" cellspacing="0" style="repeat-header:yes;repeat-footer:yes;">
		<thead class="thead">
			<tr>	
				<th width='40%' class="column">Product Release</th>
				<th width='11%' class="otherColumn">General Availability</th>
				<th width='11%' class="otherColumn">End of General Support</th>
				<th width='11%' class="otherColumn">End of Technical Guidance</th>
				<th width='11%' class="otherColumn">End of Availability</th>
				<th width='10%' class="otherColumn">Lifecycle Policy</th>
				<th width='6%' class="otherColumn">Notes</th>
			</tr>
		</thead>
		<tbody>
		<#list unsupportedList as unsupported>
			<tr class="tbodyTr">
				<td class="column">${unsupported.name}</td>
				<td class="otherColumn">
					<#if unsupported.gaDate?? >
						${unsupported.gaDate}
					<#else>
						${unsupported.gaText}
					</#if>
				</td>
				<#if unsupported.endSupportDate?? >
					<#if unsupported.endSupportColor?? >
						<#if unsupported.endSupportColor=="RED" >
							<td class="otherColumn redColor">
						<#else>
							<td class="otherColumn violetColor">
						</#if>
					<#else>
						<td class="otherColumn">
					</#if>
						${unsupported.endSupportDate}
					</td>
				<#else>
					<td class="otherColumn">
						${unsupported.endSupportText}
					</td>
				</#if>
				<#if unsupported.endTechGuidanceDate?? >
					<#if unsupported.endTechnicalColor?? >
						<#if unsupported.endTechnicalColor=="RED" >
							<td class="otherColumn redColor">
						<#else>
							<td class="otherColumn violetColor">
						</#if>
					<#else>
						<td class="otherColumn">
					</#if>
						${unsupported.endTechGuidanceDate}
					</td>
				<#else>
					<td class="otherColumn">
						N/A
					</td>
				</#if>
				<td class="otherColumn">
					<#if unsupported.endAvailabilityDate?? >
						<#if unsupported.endAvailabilityLink?? >
							<a href="${unsupported.endAvailabilityLink}">${unsupported.endAvailabilityDate} ${unsupported.endAvailabilityText}</a>
						<#else>
							${unsupported.endAvailabilityDate}
						</#if>
					<#else>
						${unsupported.endAvailabilityText}
					</#if>
				</td>
				<td class="otherColumn">
					<#if unsupported.lifecyclePolicy?? >
						<#if unsupported.lifecyclePolicy.url?? >
							<a href="${unsupported.lifecyclePolicy.url}">${unsupported.lifecyclePolicy.abbreviation}</a>
						<#else>
							 <a href="#">${unsupported.lifecyclePolicy.abbreviation}</a>
						</#if>
					<#else>
						 
					</#if>
				</td>
				<td class="otherColumn">
					<#if unsupported.footnotes?? >
						<#assign company=","/>
						<#list unsupported.footnotes as footnote>
							<#if footnote_index == 0>
								${footnote.name}
							 <#else>
								${company + footnote.name}
							</#if>
						</#list>
					<#else>
						 
					</#if>
				</td>
			</tr>
		</#list>
		</tbody>
	</table>
	<#-- <br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<div class="title" style="page-break-before: always;">
	VMware Product Lifecycle Matrix
	<div class="titleLine">
	VI3P Products Releases
	</div>
	</div>
	<table class="table" cellpadding="0" cellspacing="0"  style="repeat-header:yes;repeat-footer:yes;">
		<thead class="thead">
			<tr>	
				<th width='34%' class="column">Product Release</th>
				<th width='11%' class="otherColumn">General Availability</th>
				<th width='15%' class="otherColumn">End of General Support</th>
				<th width='18%' class="otherColumn">End of Extended Support</th>
				<th width='11%' class="otherColumn">End of Technical Guidance</th>
				<th width='11%' class="otherColumn">End of Availability</th>
			</tr>
		</thead>
		<tbody>
		<#list vi3pList as v>
			<tr class="tbodyTr">
				<td class="column">${v.name}</td>
				<td class="otherColumn">
					<#if v.gaDate?? >
						${v.gaDate}
					<#else>
						${v.gaText}
					</#if>
				</td>
				<td class="otherColumn">
					<#if v.endSupportDate?? >
						${v.endSupportDate}
					<#else>
						${v.endSupportText}
					</#if>
				</td>
				<td class="otherColumn">
					<#if v.endExtendedSupportDate?? >
						${v.endExtendedSupportDate}
					<#else>
						${v.endExtendedSupportText}
					</#if>
				</td>
				<td class="otherColumn">
					<#if v.endTechGuidanceDate?? >
						${v.endTechGuidanceDate}
					<#else>
						<a href="https://www.vmware.com/support/policies/vi_faq.html">${v.endTechGuidanceText}</a>
					</#if>
				</td>
				<td class="otherColumn">
					<#if v.endAvailabilityDate?? >
						<#if v.endAvailabilityLink?? >
							<a href="${v.endAvailabilityLink}">${v.endAvailabilityDate} ${v.endAvailabilityText}</a>
						<#else>
							${v.endAvailabilityDate}
						</#if>
					<#else>
						${v.endAvailabilityText}
					</#if>
				</td>
			</tr>
		</#list>
		</tbody>
	</table>
	<p class="section">
		<span style="color: #29414e"><b>End of Extended Support: </b></span>
		<span> It will be provided for 2 years following General Support. New hardware platforms are no longer supported, new guest OS updates may or may not be applied, and bug fixes are limited to critical issues. Critical bugs are deviations from specified product functionality that cause data corruption, data loss, system crash, or significant customer application down time and there is no work-around that can be implemented.</span>
	</p>
	<p class="section">
		<span style="color: #29414e"><b>VMware Vcenter Server: </b></span>
		<span> VMware Vcenter Server Formerly Virtual Center Server</span>
	</p> -->
	<p class="section">
		<span>Dates highlighted in </span><span style="color: #f54f47">red </span>
		<span>indicate a product version is within 6 months of End of General Support or End of Technical Guidance.</span>
	</p>
	<p class="section">
		<span>Dates highlighted in </span><span style="color: #7124ae">purple </span>
		<span>indicate a product version has gone past its End of General Support.</span>
	</p>
	<p class="section">
		<span>Access VMware lifecycle policy information at 
		<b><a href="#">https://www.vmware.com/support/lifecycle-policies.html</a></b>.</span>
	</p>
	<p class="section">
		<span>You can find the most up-to-date product lifecycle information on the VMware website at 
		<b><a href="#">https://lifecycle.vmware.com</a></b>.</span>
	</p>
	<div class="noteSection">
		<#if footNotes?exists >
			<label for="" class="noteLable">*Notes:</label><br/>
			<#list footNotes?keys as key>
				${footNotes[key]}
			</#list>
		</#if>
		<#if showLifecyclePolicies?exists >
			<label for="" class="noteLable">*Lifecycle Policies:</label><br/>
			<#list showLifecyclePolicies?keys as key>
				${showLifecyclePolicies[key]}
			</#list>
		</#if>
		<span>All dates presented in this document are in the ISO developed international format.
			This format uses a numerical date system as follows: YYYY-MM-DD where YYYY is the year, 
			MM the month and DD the day. The information contained herein is believed to be accurate as of the date of publication,
			but updates and revisions may be posted periodically and without notice. VMWARE DOES NOT PROVIDE ANY WARRANTIES COVERING 
			THIS INFORMATION AND SPECIFICALLY DISCLAIMS ANY LIABILITY FOR DAMAGES, INCLUDING, WITHOUT LIMITATION, DIRECT, INDIRECT,
			CONSEQUENTIAL, INCIDENTAL, AND SPECIAL DAMAGES, IN CONNECTION WITH THE INFORMATION PRESENTED HERE.</span><br/>
		<span>VMware, Inc. 3401 Hillview Ave. Palo Alto, CA 94304 www.vmware.com Copyright © ${lastUpdate?substring(0,4)} VMware, Inc. All rights reserved.</span>
		<a href="https://docs.vmware.com/copyright-trademark.html">Copyright and trademark information.</a>
	</div>
</div>
</body>
</html>
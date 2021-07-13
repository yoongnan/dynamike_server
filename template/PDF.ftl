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
			//background-color: #697a83;
			background-color: #29414e;
			text-align: center;
			line-height: 22px;
			border:1px solid #ccc;
		}
		.allsupportedTitle{
			width: 100%;
			padding: 24px 24px, 24px, 188px;
			color: #fff;
			font-size: 20px;
			font-weight: bold;
			background-color: #0091DA;
			text-align: center;
			line-height: 24px;
		}
		.supportedTitle{
			width: 100%;
			padding: 24px 24px, 24px, 188px;
			color: #fff;
			font-size: 20px;
			font-weight: bold;
			background-color: #53AE78;
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
			//background-color: rgba(41,65,78,0.7);
			background-color: #697a83;
    		//opacity: 0.7;
			color: #fff;
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
		.tbodyTrUnsupport{
			//background-color: #D7D7D7;		
			background-color: rgb(215, 215, 215, 0.5);
		}
		.backgroundBox{
			background-color: rgb(215, 215, 215, 0.5);
		    border-radius: 1px;
		    margin-right: 10px;
		    padding: 0px 15px 0px;
		    display: inline;
		}
    </style>
</head>
<body>
<div class="container">
	<#if type!="VI3P" >
		<div class='header'>
			<div class="lastUpdate">
				<span>Last Updated: </span>
				<span>${lastUpdate}</span>
			</div>
			<#if type!="Supported" >
				<#if type!="Unsupported" >
					<div class="tip">
						<div><span  class="redColor">Red: </span><label>Within 6 months</label></div>
						<div><span  class="violetColor">Purple: </span><label>Reached End of General Support</label></div>
						<div>								
							<div class="backgroundBox">Background Color</div>
							<label>Product release with grey background indicates it is unsupported</label>
						</div> 
					</div>
				<#else>
					<div class="tip">
						<div><span  class="violetColor">Purple: </span><label>Reached End of General Support</label></div>
						<div>								
							<div class="backgroundBox">Background Color</div>
							<label>Product release with grey background indicates it is unsupported</label>
						</div> 
					</div>
				</#if>
			<#else>
				<div class="tip">
					<div><span  class="redColor">Red: </span><label>Within 6 months</label></div>
					<div><span  class="violetColor">Purple: </span><label>Reached End of General Support</label></div> 
				</div>
			</#if>
		</div>
	</#if>

	<#if type!="Supported" >
		<#if type!="Unsupported" >
			<div class="allsupportedTitle">
			VMware Product Lifecycle Matrix
				<div class="supportedTitleLine"></div>
		<#else>
			<div class="title">
			VMware Product Lifecycle Matrix
				<div class="unSupportedTitleLine">
				${type} Products Releases
			</div>
		</#if>
	<#else>
		<div class="supportedTitle">
		VMware Product Lifecycle Matrix
			<div class="supportedTitleLine">
				${type} Products Releases
			</div>
	</#if>
	</div>
	<table class="table" cellpadding="0" cellspacing="0" style="repeat-header:yes;repeat-footer:yes;">
		<thead class="thead">
			<tr>	
				<#if type!="VI3P" >
					<th width='40%' class="column">Product Release</th>
					<th width='11%' class="otherColumn">General Availability
						<#if generalAvailabilityDateRange?? >
						<br/> ${generalAvailabilityDateRange}
						</#if>
					</th>
					<th width='11%' class="otherColumn">End of General Support
						<#if endSupportDateRange?? >
						<br/> ${endSupportDateRange}
						</#if>
					</th>
					<th width='11%' class="otherColumn">End of Technical Guidance
						<#if endTechnicalDateRange?? >
						<br/> ${endTechnicalDateRange}
						</#if>
					</th>
					<th width='11%' class="otherColumn">End of Availability
						<#if endAvailabilityDateRange?? >
						<br/> ${endAvailabilityDateRange}
						</#if>
					</th>
					<th width='10%' class="otherColumn">Lifecycle Policy</th>
					<th width='6%' class="otherColumn">Notes</th>
				<#else>
					<th width='34%' class="column">Product Release</th>
					<th width='11%' class="otherColumn">General Availability</th>
					<th width='15%' class="otherColumn">End of General Support</th>
					<th width='18%' class="otherColumn">End of Extended Support</th>
					<th width='11%' class="otherColumn">End of Technical Guidance</th>
					<th width='11%' class="otherColumn">End of Availability</th>
				</#if>
			</tr>
		</thead>
		<tbody>
		<#list lifecycleMatrixList as lifecycleMatrix>			
			<#if lifecycleMatrix.supportType!="un">
				<tr class="tbodyTr">		
			<#else>
				<tr class="tbodyTrUnsupport">
			</#if>
				<td class="column">${lifecycleMatrix.name}</td>
				<td class="otherColumn">
					<#if lifecycleMatrix.gaDate?? >
						${lifecycleMatrix.gaDate}
					<#else>
						${lifecycleMatrix.gaText}
					</#if>
				</td>
				<#if type!="VI3P" >
					<#if lifecycleMatrix.endSupportDate?? >
						<#if lifecycleMatrix.endSupportColor?? >
							<#if lifecycleMatrix.endSupportColor=="RED" >
								<td class="otherColumn redColor">
							<#else>
								<td class="otherColumn violetColor">
							</#if>
						<#else>
							<td class="otherColumn">
						</#if>
							${lifecycleMatrix.endSupportDate}
						</td>
					<#else>
						<td class="otherColumn">
							${lifecycleMatrix.endSupportText}
						</td>
					</#if>
					<#if lifecycleMatrix.endTechGuidanceDate?? >
						<#if lifecycleMatrix.endTechnicalColor?? >
							<#if lifecycleMatrix.endTechnicalColor=="RED" >
								<td class="otherColumn redColor">
							<#else>
								<td class="otherColumn violetColor">
							</#if>
						<#else>
							<td class="otherColumn">
						</#if>
							${lifecycleMatrix.endTechGuidanceDate}
						</td>
					<#else>
						<td class="otherColumn">
							N/A
						</td>
					</#if>
					<td class="otherColumn">
						<#if lifecycleMatrix.endAvailabilityDate?? >
							<#if lifecycleMatrix.endAvailabilityLink?? >
								<a href="${lifecycleMatrix.endAvailabilityLink}">${lifecycleMatrix.endAvailabilityDate} ${lifecycleMatrix.endAvailabilityText}</a>
							<#else>
								${lifecycleMatrix.endAvailabilityDate}
							</#if>
						<#else>
							${lifecycleMatrix.endAvailabilityText}
						</#if>
					</td>
					<td class="otherColumn">
						<#if lifecycleMatrix.lifecyclePolicy?? >
							<#if lifecycleMatrix.lifecyclePolicy.url?? >
								<a href="${lifecycleMatrix.lifecyclePolicy.url}">${lifecycleMatrix.lifecyclePolicy.abbreviation}</a>
							<#else>
								 <a href="#">${lifecycleMatrix.lifecyclePolicy.abbreviation}</a>
							</#if>
						<#else>
							 
						</#if>
					</td>
					<td class="otherColumn">
						<#if lifecycleMatrix.footnotes?? >
							<#assign company=","/>
							<#list lifecycleMatrix.footnotes as footnote>
								<#if footnote_index == 0>
									${footnote.name}
								 <#else>
									${company + footnote.name}
								</#if>
							</#list>
						<#else>
							 
						</#if>

					</td>
				<#else>
					<td class="otherColumn">
						<#if lifecycleMatrix.endSupportDate?? >
							${lifecycleMatrix.endSupportDate}
						<#else>
							${lifecycleMatrix.endSupportText}
						</#if>
					</td>
					<td class="otherColumn">
						<#if lifecycleMatrix.endExtendedSupportDate?? >
							${lifecycleMatrix.endExtendedSupportDate}
						<#else>
							${lifecycleMatrix.endExtendedSupportText}
						</#if>
					</td>
					<td class="otherColumn">
						<#if lifecycleMatrix.endTechGuidanceDate?? >
							${lifecycleMatrix.endTechGuidanceDate}
						<#else>
							<a href="https://www.vmware.com/support/policies/vi_faq.html">${lifecycleMatrix.endTechGuidanceText}</a>
						</#if>
					</td>
					<td class="otherColumn">
						<#if lifecycleMatrix.endAvailabilityDate?? >
							<#if lifecycleMatrix.endAvailabilityLink?? >
								<a href="${lifecycleMatrix.endAvailabilityLink}">${lifecycleMatrix.endAvailabilityDate} ${lifecycleMatrix.endAvailabilityText}</a>
							<#else>
								${lifecycleMatrix.endAvailabilityDate}
							</#if>
						<#else>
							${lifecycleMatrix.endAvailabilityText}
						</#if>
					</td>
				</#if>
			</tr>
		</#list>
		</tbody>
	</table>
	<#if type=="VI3P" >
		<p class="section">
			<span style="color: #29414e"><b>End of Extended Support: </b></span>
			<span> It will be provided for 2 years following General Support. New hardware platforms are no longer supported, new guest OS updates may or may not be applied, and bug fixes are limited to critical issues. Critical bugs are deviations from specified product functionality that cause data corruption, data loss, system crash, or significant customer application down time and there is no work-around that can be implemented.</span>
		</p>
		<p class="section">
			<span style="color: #29414e"><b>VMware Vcenter Server: </b></span>
			<span> VMware Vcenter Server Formerly Virtual Center Server</span>
		</p>
	<#else>
		<#if type!="Supported" >
			<#if type!="Unsupported" >
				<p class="section">
					<span>Dates highlighted in </span><span style="color: #f54f47">red </span>
					<span>indicate a product version is within 6 months of End of General Support or End of Technical Guidance.</span>
				</p>
				<p class="section">
					<span>Dates highlighted in </span><span style="color: #7124ae">purple </span>
					<span>indicate a product version has gone past its End of General Support.</span>
				</p>
				
				<p class="section">								
					<span class="backgroundBox">Background Color</span>
					<span>Product release with grey background indicates it is unsupported</span>
				</p>
			<#else>
				<p class="section">
					<span>Dates highlighted in </span><span style="color: #7124ae">purple </span>
					<span>indicate a product version has gone past its End of General Support.</span>
				</p>
				
				<p class="section">								
					<span class="backgroundBox">Background Color</span>
					<span>Product release with grey background indicates it is unsupported</span>
				</p>
			</#if>
		<#else>
			<p class="section">
				<span>Dates highlighted in </span><span style="color: #f54f47">red </span>
				<span>indicate a product version is within 6 months of End of General Support or End of Technical Guidance.</span>
			</p>
			<p class="section">
				<span>Dates highlighted in </span><span style="color: #7124ae">purple </span>
				<span>indicate a product version has gone past its End of General Support.</span>
			</p>
		</#if>
		 
	</#if>
	<p class="section">
		<span>Access VMware lifecycle policy information at 
		<b><a href="#">https://www.vmware.com/support/lifecycle-policies.html</a></b>.</span>
	</p>
	<p class="section">
		<span>You can find the most up-to-date product lifecycle information on the VMware website at 
		<b><a href="#">https://lifecycle.vmware.com</a></b>.</span>
	</p>
	<div class="noteSection">
		<#if type!="VI3P" >
			<#if footNotes?exists >
				<label for="" class="noteLable">*Notes:</label><br/>
				<#list footNotes?keys as key>
					${footNotes[key]}
				</#list>
			</#if>
			<#if showLifecyclePolicies?exists>
				<label for="" class="noteLable">*Lifecycle Policies:</label><br/>
				<#list showLifecyclePolicies?keys as key>
					${showLifecyclePolicies[key]}
				</#list>
			</#if>
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
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page trimDirectiveWhitespaces="false" %>
<html>
   <head>
      <title>CURRENCY CONVERTER</title>
 	  <link href="/css/default.css" type="text/css" rel="stylesheet" />   
   </head>

   <body>
     <div style="text-align:center;">
      <span class="heading">CURRENCY CONVERTER</span>
     </div> 
      <br/>
      <br/>      
      <form:form id="form" action="/currencyConverter" modelAttribute="currencyConverterForm" method="post">
        <div class="form_converter">
         <table>
            <tr>
              <td colspan = "6" align="center">
                 <c:if test="${not empty result}">
                 <span class="result">
                   <fmt:formatNumber value="${result.amountToConvert}" type="number" maxFractionDigits="2" minFractionDigits="2"/> 
                     ${result.sourceCurrency} = 
                   <fmt:formatNumber value="${result.convertedAmount}" type="number" maxFractionDigits="2" minFractionDigits="2"/> 
                     ${result.targetCurrency} 
                    on <fmt:formatDate pattern="yyyy-MM-dd" value="${result.conversionDate}" />
                 </span> 
                 </c:if>
              </td>
            </tr> 
            <tr>
              <td colspan = "6" align="center">
    		    <span class="error_big">
      	    		<c:out value="${error}"/>
          		</span> 
              </td>
            </tr> 
            <tr>
              <td colspan = "6" align="center">
              	<form:errors cssClass="error_big" path="amountToConvert"/>
              </td>
            </tr>
            <tr>
              <td colspan = "6" align="center">
              	<form:errors cssClass="error_big" path="conversionDate"/>
              </td>
            </tr>                         	 
            <tr>
               <td><form:input cssClass="input" path="amountToConvert" name="amountToConvert" size="10" maxlength="14"/></td>
               <td><form:select cssClass="input" path="sourceCurrency" items="${supportedCurrencies}"/></td>
               <td><span class="message">=></span></td>
               <td><form:select cssClass="input" path="targetCurrency" items="${supportedCurrencies}"/></td>
               <td><span class="message">as on</span> </td>
               <td><form:input cssClass="input" path="conversionDate" name="conversionDate" maxlength="10"/></td>
            </tr>         
            <tr>
               <td colspan = "6" align="center">
                  <input type="submit" name="convert" value="Convert" 
                         onclick="this.disabled=true;this.value='Convert';this.form.submit();"/>
               </td>
            </tr>           
         </table>
        <pre><c:out value="${queryHistory}">
        </c:out></pre>        
       </div>
      </form:form>
      
      <c:if test="${pageContext.request.userPrincipal.name != null}">        
        <form id="logoutForm" method="POST" action="/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <div class="footer">
        	<span class = "footer_text">
        		Logged in as ${pageContext.request.userPrincipal.name} | <a href="#" onclick="document.forms['logoutForm'].submit()">Logout</a>     	
        	</span>       	
        </div> 
      </c:if>
   </body>
   
</html>
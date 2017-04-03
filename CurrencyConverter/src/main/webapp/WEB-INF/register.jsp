<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<html>
   <head>
        <title>REGISTRATION</title>
		<link href="/css/default.css" type="text/css" rel="stylesheet"/>	      
   </head>
   <body>
     <!--div id="form_background"-->
      <form:form id="form" action="/register" modelAttribute="registrationForm" method="post">
        <div class="form_register">
         <table>              
            <tr>
            	<td colspan="2" align="center">
            	    <span class="heading">Registration</span>
            	</td>
            	<td/>
            </tr>
            <tr>
            	<td colspan="3" align="center">
            	    <span class="error">${error}</span>
            	</td>
            </tr>
            <tr>
            	<td colspan="3" align="center">
            	    <span class="message">${message}</span>
            	</td>
            </tr>                          
            <tr>
               <td><label class="message">Username</label></td>
               <td><form:input cssClass="input" path="username" name="username" maxlength="12"/></td>
               <td><form:errors cssClass="error" path="username"/></td>
            </tr>           
            <tr>
               <td><label class="message">Password</label></td>
               <td><form:input cssClass="input" path="password" type="password" maxlength="18" name="password"/></td>
               <td><form:errors cssClass="error" path="password"/></td>               
            </tr>           
            <tr>
               <td><label class="message">Re-Password</label></td>
               <td><form:input cssClass="input" path="confirmPassword" type="password" maxlength="18" name="confirmPassword"/></td>
               <td><form:errors cssClass="error" path="confirmPassword"/></td> 
            </tr>
            <tr>
               <td><label class="message">First Name</label></td>
               <td><form:input cssClass="input" path="firstName" name="firstName" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="firstName"/></td>                              
            </tr>
            <tr>
               <td><label class="message">Last Name</label></td>
               <td><form:input cssClass="input" path="lastName" name="lastName" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="lastName"/></td>                                            
            </tr>      
             <tr>
               <td><label class="message">Email</label></td>
               <td><form:input cssClass="input" path="emailId" name="emailId" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="emailId"/></td>                                                         
            </tr>
            <tr>
               <td><label class="message">Date of Birth</label></td>
               <td><form:input cssClass="input" path="dateOfBirth" placeholder="yyyy-MM-dd" maxlength="10" name="dateOfBirth"/></td>
               <td><form:errors cssClass="error" path="dateOfBirth"/></td>                                                         
            </tr>            
         </table>
          <table>
           	<tr>
               <td><label class="message">Address Line1</label></td>
               <td><form:input cssClass="input" path="address.addressLine1" placeholder="Appt#, Building" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="address.addressLine1"/></td>
           </tr>
           	<tr>
               <td><label class="message">Street</label></td>
               <td><form:input cssClass="input" path="address.street" placeholder="Street/Locality" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="address.street"/></td>
           </tr>          
           	<tr>
               <td><label class="message">City</label></td>
               <td><form:input cssClass="input" path="address.city" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="address.city"/></td>
           </tr> 
           	<tr>
               <td><label class="message">Zip/Postal Code</label></td>
               <td><form:input cssClass="input" path="address.zipCode" maxlength="15"/></td>
               <td><form:errors cssClass="error" path="address.zipCode"/></td>
           </tr>
           	<tr>
               <td><label class="message">State/Province</label></td>
               <td><form:input cssClass="input" path="address.state" maxlength="30"/></td>
               <td><form:errors cssClass="error" path="address.state"/></td>
           </tr>		   
           	<tr>
               <td><label class="message">Country</label></td>
               <td><form:select cssClass="input" path="address.country" items="${countryList}"/></td>
               <td><form:errors cssClass="error" path="address.country"/></td>
           </tr>
             <tr>
               <td colspan="2" align="center">
                  <input id="submit_button" type="submit" name="register" value="Register" 
                         onclick="this.disabled=true;this.form.submit();"/>
               </td>
            </tr>                                            
         </table>
        </div>
      </form:form>
     <!--/div-->
   </body>
</html>
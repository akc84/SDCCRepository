<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>LOGIN</title>
<link href="/css/default.css" type="text/css" rel="stylesheet" />
</head>

<body>
	<!-- div id="form_background"-->
		<form id="form" action="/login" method="post">
			<div class="form_login">
				<br />
				<table>
					<tr>
						<td colspan="2" align="center"><span class="heading">Login</span>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center"><c:if
								test="${param.error != null}">
								<span class="error_big"><c:out
										value="Invalid username/password" /></span>
							</c:if></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><span class="message"><c:out
									value="${message}" /></span></td>
					</tr>
					<tr>
						<td><label class="message">Username</label></td>
						<td><input class="input" name="username" /></td>
					</tr>
					<tr>
						<td><label class="message">Password</label></td>
						<td><input class="input" type="password" name="password" /></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
						  <input id="submit_button" type="submit" name="submit" value="Submit"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center"><a class="message"
							href="/register">New User? Register here</a></td>
					</tr>
				</table>
			</div>
		</form>
	<!--/div-->
</body>

</html>
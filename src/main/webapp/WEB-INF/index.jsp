<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Moniteur serie</title>
		<link rel="stylesheet" href="css/style.css">
</head>
<body>
	
	<div class="content">
		<header>
			<h1>Moniteur serie</h1>
			<form action="" method="post">
				<select>
					<option></option>
				</select>
				
				<button type="submit">Valider</button>
			</form>
		</header>
		
		<section>
			<table>
				<thead>
					<tr>
						<th>N°</th>
						<th>Date et heure</th>
						<th>Valeur</th>
						<th>Derniere modif</th>
						<th>Option</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach items="${requestScope.events}" var="event" varStatus="status">
					<tr>
						<td><c:out value="${(status.index + requestScope.startIndex + 1)}"/></td>
						<td><c:out value="${event.formatRecordDate}"/></td>
						<td><c:out value="${event.distance}"/></td>
						<td></td>
						<td></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</section>
		
		<footer>
			<a class="btn" href="
			<c:url value="/index">
				<c:param name="page" value="${(requestScope.startIndex / 20 ) - 1 }"/>
			</c:url>">Precedant</a>
			<a class="btn" href="
			<c:url value="/index">
				<c:param name="page" value="${(requestScope.startIndex / 20) + 1 }"/>
			</c:url>
			">Suivant</a>
		</footer>
	</div>
	
</body>
</html>

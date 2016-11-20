<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<title>BENGATI & BARKOUKI - Projet NoSQL - Films</title>
	<style>
		div.scroll {
			height: 400px;
			overflow: scroll;
		}
	</style>
</head>
<body>
<center><h1>
	<c:choose>
	    <c:when test="${userId==null}">
	        Tous les films
	    </c:when>    
	    <c:otherwise>
	        Films de l'utilisateur ${userId}
	    </c:otherwise>
	</c:choose>
</h1></center>
<div class="col-md-12">
	<div class="col-md-4">
		<center><h3><img width="80px" src="http://idelways.developpez.com/news/images/mongodb.png" alt="MongoDB"></h3></center>
	</div>
	<div class="col-md-4">
		<center><h3><img width="80px" src="https://upload.wikimedia.org/wikipedia/fr/thumb/6/62/MySQL.svg/1280px-MySQL.svg.png" alt="MySQL"></h3></center>
	</div>
	<div class="col-md-4">
		<center><h3><img width="80px" src="http://info.neo4j.com/rs/773-GON-065/images/neo4j_logo.png" alt="Neo4j"></h3></center>
	</div>
</div>
<div class="col-md-12">
<div class="col-md-4 scroll">

	<ul>
		<c:forEach items="${moviesMongoDB}" var="movie">
			<li>
					${movie.title}
				<ul>
					<c:forEach items="${movie.genres}" var="genre">
						<li>
								${genre.name}
						</li>
					</c:forEach>
				</ul>
			</li>
		</c:forEach>
	</ul>
</div>
	<div class="col-md-4 scroll">
		<ul>
			<c:forEach items="${moviesMySQL}" var="movie">
				<li>
						${movie.title}
					<ul>
						<c:forEach items="${movie.genres}" var="genre">
							<li>
									${genre.name}
							</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="col-md-4 scroll">
		<ul>
			<c:forEach items="${moviesNeo4J}" var="movie">
				<li>
						${movie.title}
					<ul>
						<c:forEach items="${movie.genres}" var="genre">
							<li>
									${genre.name}
							</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
</body>
</html>
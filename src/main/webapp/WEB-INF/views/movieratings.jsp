<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"

         pageEncoding="ISO-8859-1"%>
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
            height: 250px;
            overflow: scroll;
        }
        .border {
            border: 2px solid #a1a1a1;
            padding: 10px 40px;
            background: #dddddd;
            width: 300px;
            border-radius: 25px;
        }
    </style>
</head>
<body>



<div class="col-md-12">
    <center><h1>
        Vos films notés
    </h1></center>
    <div class="col-md-6">

        <center><h3><img width="80px" src="http://idelways.developpez.com/news/images/mongodb.png" alt="MongoDB"><span class="badge">${ratingsMongoDBNbr}</span></h3></center>
        <div class="col-md-12 scroll">
            <c:forEach items="${ratingsMongoDB}" var="rating">
                <center><p class="col-md-12 border">
                        ${rating.getMovie().getTitle()} - ${rating.score}
                </p></center>
            </c:forEach>
        </div>
    </div>
    <div class="col-md-6">
        <center><h3><img width="80px" src="http://info.neo4j.com/rs/773-GON-065/images/neo4j_logo.png" alt="Neo4j"><span class="badge">${ratingsNeo4jNbr}</span></h3></center>
        <div class="col-md-12 scroll">
            <c:forEach items="${ratingsNeo4j}" var="rating">
                <center><p class="col-md-12 border">
                        ${rating.getMovie().getTitle()} - ${rating.score}
                </p></center>
            </c:forEach>
        </div>
    </div>
    </div>
<div class="col-md-12">
    <center><h1>
        Ajouter une note
    </h1></center>

    <div class="col-md-6">

        <center><h3><img width="80px" src="http://idelways.developpez.com/news/images/mongodb.png" alt="MongoDB"><span class="badge">${roRateMongoDBNb}</span></h3></center>
        <div class="col-md-12 scroll">
            <c:forEach items="${roRateMongoDB}" var="movie">
                <c:set var="score" value="0" />
                <c:forEach items="${ratings}" var="rating">
                    <c:if test="${rating.getMovie().id eq movie.id}">
                        <c:set var="score" value="${rating.score}" />
                    </c:if>
                </c:forEach>
                    <form method="post" action="/movieratings/mongodb" class="border">
                    ${movie.title}
                        <input type="number" name="userId" value="${userId}" hidden="hidden">
                        <input type="number" name="movieId" value="${movie.id}" hidden="hidden">
                        <input type="radio" name="score" onclick="this.form.submit();" value="1" <c:if test="${score eq 1}">checked</c:if>>
                        <input type="radio" name="score" onclick="this.form.submit();" value="2" <c:if test="${score eq 2}">checked</c:if>>
                        <input type="radio" name="score" onclick="this.form.submit();" value="3" <c:if test="${score eq 3}">checked</c:if>>
                        <input type="radio" name="score" onclick="this.form.submit();" value="4" <c:if test="${score eq 4}">checked</c:if>>
                        <input type="radio" name="score" onclick="this.form.submit();" value="5" <c:if test="${score eq 5}">checked</c:if>>
                    </form>
            </c:forEach>
        </div>
    </div>
    <div class="col-md-6">
        <center><h3><img width="80px" src="http://info.neo4j.com/rs/773-GON-065/images/neo4j_logo.png" alt="Neo4j"><span class="badge">${roRateNeo4jNb}</span></h3></center>
        <div class="col-md-12 scroll">
            <c:forEach items="${roRateNeo4j}" var="movie">
                <c:set var="score" value="0" />
                <c:forEach items="${ratings}" var="rating">
                    <c:if test="${rating.getMovie().id eq movie.id}">
                        <c:set var="score" value="${rating.score}" />
                    </c:if>
                </c:forEach>
                        <form method="post" action="/movieratings/neo4j" class="border">
                             ${movie.title}
                            <input type="number" name="userId" value="${userId}" hidden="hidden">
                            <input type="number" name="movieId" value="${movie.id}" hidden="hidden">
                            <input type="radio" name="score" onclick="this.form.submit();" value="1" <c:if test="${score eq 1}">checked</c:if>>
                            <input type="radio" name="score" onclick="this.form.submit();" value="2" <c:if test="${score eq 2}">checked</c:if>>
                            <input type="radio" name="score" onclick="this.form.submit();" value="3" <c:if test="${score eq 3}">checked</c:if>>
                            <input type="radio" name="score" onclick="this.form.submit();" value="4" <c:if test="${score eq 4}">checked</c:if>>
                            <input type="radio" name="score" onclick="this.form.submit();" value="5" <c:if test="${score eq 5}">checked</c:if>>
                        </form>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
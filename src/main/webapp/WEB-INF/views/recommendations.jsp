<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
</head>
<body>

<div class="container col-md-6">
    <h1>
        Vos recommandations Neo4J
    </h1>
    <ul>
        <c:forEach items="${recommendationsNeo4j}" var="recommendation">
            <li>
                    ${recommendation.getMovie().title}
            </li>
        </c:forEach>
    </ul>

</div>
<div class="container col-md-6">
    <h1>
    Vos recommandations MongoDB
</h1>
<ul>
    <c:forEach items="${recommendationsNeo4j}" var="recommendation">
        <li>
                ${recommendation.getMovie().title}
        </li>
    </c:forEach>
</ul>


</div>


</body>
</html>
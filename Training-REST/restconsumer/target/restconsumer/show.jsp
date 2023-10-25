<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Show people</title>
    </head>
    <body>
        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>City</th>
                    <th>State</th>
                    <th>Postcode</th>
                    <th>Email</th>
                    <th>Gender</th>
                </tr>
            </thead>
            <tbody>

                <c:forEach var="people" items="${people}">
                    <tr>
                       <td><c:out value='${people.name.title}'/></td>
                        <td><c:out value="${people.name.first}"/></td>
                        <td><c:out value="${people.name.last}"/></td>
                        <td><c:out value="${people.location.city}"/></td>
                        <td><c:out value="${people.location.state}"/></td>
                        <td><c:out value="${people.location.postcode}"/></td>
                        <td><c:out value="${people.email}"/></td>
                        <td><c:out value="${people.gender}"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
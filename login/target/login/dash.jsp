<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/css/bootstrap.min.css"
        integrity="sha384-DhY6onE6f3zzKbjUPRc2hOzGAdEf4/Dz+WJwBvEYL/lkkIsI3ihufq9hk9K4lVoK" crossorigin="anonymous">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/style.css">

    <!-- Fontawesome CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

    <!-- Optional JavaScript; choose one of the two! -->

    <!-- Option 1: Bootstrap Bundle with Popper.js -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/js/bootstrap.bundle.min.js"
        integrity="sha384-BOsAfwzjNJHrJ8cZidOg56tcQWfp6y72vEJ8xQ9w6Quywb24iOsW913URv1IS4GD" crossorigin="anonymous">
    </script>

    <!-- Option 2: Separate Popper.js and Bootstrap JS
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/js/bootstrap.min.js" integrity="sha384-5h4UG+6GOuV9qXh6HqOLwZMY4mnLPraeTrjT5v07o347pj6IkfuoASuGBhfDsp3d" crossorigin="anonymous"></script>
    -->

    <title>DashBoard - Employees</title>
</head>

<body class="login">
    <!-- nav start -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="dash.jsp">Hi, <%=request.getAttribute("username")%></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="employees-list.jsp"><%=request.getAttribute("attendance")%></a>
                    </li>
                    <% if(request.getAttribute("leaves"))!= null
                    { %>
                    <li class="nav-item">
                        <a class="nav-link" href="jobs-list.jsp"><%=request.getAttribute("leaves")%></a>
                    </li>
                    <%
                    }
                    %>
                    <li class="nav-item">
                        <a class="nav-link" href="jobs-list.jsp"><%=request.getAttribute("myprofile")%></a>
                    </li>

                </ul>
                <form class="d-flex">
                    <div class="input-group">
                        <input type="search" class="form-control form-control-sm" placeholder="Search.."
                            aria-label="Search.." aria-describedby="button-addon2">
                        <button class="btn btn-sm btn-success" type="button" id="button-addon2"><i
                                class="fa fa-search"></i></button>
                    </div>
                </form>

                <a href="#" class="btn btn-sm btn-warning ml-3">Log Out</a>
            </div>
        </div>
    </nav>
    <!-- nav end -->

    <!-- dashboard contents start -->
    <div class="container-fluid">
        <div class="row mt-3">
            <div class="col-lg-3 col-md-3">
                <div class="card card-border">
                    <div class="card-body">
                        <h4 class="card-title">245 <small class="text-muted">Employees</small></h4>
                    </div>
                    <div class="list-group list-group-flush">
                        <a href="employees-list.jsp" class="list-group-item list-group-item-primary">View All</a>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-md-3">
                <div class="card card-border">
                    <div class="card-body">
                        <h4 class="card-title">25 <small class="text-muted">Jobs</small></h4>
                    </div>
                    <div class="list-group list-group-flush">
                        <a href="jobs-list.jsp" class="list-group-item list-group-item-primary">View All</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- dashboard contents end -->
</body>

</html>
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

    <title>Employees List</title>
</head>

<body class="login">
    <!-- nav start -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="dash.jsp">Hi, Raja</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="employees-list.html">Employees</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="jobs-list.html">Jobs</a>
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
                <div class="list-group small">
                    <div class="list-group-item active">Employee Data</div>
                    <a href="#" class="list-group-item" data-toggle="modal" data-target="#add_employee">Add Employee</a>
                    <a href="employees-list.html" class="list-group-item">View All Employee</a>
                </div>
            </div>
            <div class="col-lg-9 col-md-9">
                <table class="table table-striped table-hover bg-light small">
                    <tr class="table-dark">
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email ID</th>
                        <th>Details</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Nay Lin</td>
                        <td>naylin@gmail.com</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info" data-toggle="modal"
                                data-target="#employeeDetails">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning" data-toggle="modal"
                                data-target="#employeeEditDetails">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger"
                                onclick="confirm('Are you sure you want to delete \'Nay Lin\'?')">Delete</a></td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Nay Lin</td>
                        <td>naylin@gmail.com</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger">Delete</a></td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Nay Lin</td>
                        <td>naylin@gmail.com</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger">Delete</a></td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Nay Lin</td>
                        <td>naylin@gmail.com</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger">Delete</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <!-- dashboard contents end -->

    <!-- add_employee Modal start -->
    <div class="modal fade" id="add_employee" tabindex="-1" aria-labelledby="add_employeeLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="add_employeeLabel">Add Employee Details</h5>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <input type="date" class="form-control form-control-sm" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control form-control-sm" placeholder="Employee Name"
                                required>
                        </div>
                        <div class="mb-3">
                            <input type="email" class="form-control form-control-sm" placeholder="Employee Email Id"
                                required>
                        </div>
                        <div class="mb-3">
                            <input type="tel" class="form-control form-control-sm" placeholder="Employee Phone Number"
                                required>
                        </div>
                        <div class="mb-3">
                            <select class="form-control form-control-sm">
                                <option value="Graphic Designer">Graphic Designer</option>
                                <option value="Web Designer">Web Designer</option>
                                <option value="Web Developer">Web Developer</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-sm btn-success btn-block">Add New Employee</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- add_employee Modal end -->

    <!-- Details Modal start -->
    <div class="modal fade" id="employeeDetails" tabindex="-1" aria-labelledby="employeeDetailsLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="employeeDetailsLabel">Employee Details</h5>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered">
                        <tr>
                            <th>ID</th>
                            <td>1</td>
                        </tr>
                        <tr>
                            <th>Joining Date</th>
                            <td>25 Oct 2020</td>
                        </tr>
                        <tr>
                            <th>Name</th>
                            <td>Nay Lin</td>
                        </tr>
                        <tr>
                            <th>Email ID</th>
                            <td>naylin@gmail.com</td>
                        </tr>
                        <tr>
                            <th>Phone</th>
                            <td>+959791546974</td>
                        </tr>
                        <tr>
                            <th>Job</th>
                            <td>Web Developer</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- Details Modal end -->

    <!-- Edit Details Modal start -->
    <div class="modal fade" id="employeeEditDetails" tabindex="-1" aria-labelledby="employeeEditDetailsLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="employeeEditDetailsLabel">Employee Details</h5>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <input type="date" class="form-control form-control-sm" value="2020-10-25" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control form-control-sm" value="Nay Lin"
                                placeholder="Employee Name" required>
                        </div>
                        <div class="mb-3">
                            <input type="email" class="form-control form-control-sm" value="naylin@gmail.com"
                                placeholder="Employee Email Id" required>
                        </div>
                        <div class="mb-3">
                            <input type="tel" class="form-control form-control-sm" value="+959791546974"
                                placeholder="Employee Phone Number" required>
                        </div>
                        <div class="mb-3">
                            <select class="form-control form-control-sm">
                                <option value="Graphic Designer">Graphic Designer</option>
                                <option value="Web Designer">Web Designer</option>
                                <option value="Web Developer" selected>Web Developer</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-sm btn-success btn-block">Update Employee
                                Details</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- Edit Details Modal end -->
</body>

</html>
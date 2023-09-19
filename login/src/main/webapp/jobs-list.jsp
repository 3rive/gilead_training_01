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

    <title>Jobs List</title>
</head>

<body class="login">
    <!-- nav start -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="dash.jsp">Hi, NayLin</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="employees-list.jsp">Employees</a>
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
                    <div class="list-group-item active">Job Data</div>
                    <a href="#" class="list-group-item" data-toggle="modal" data-target="#add_job">Add New Job</a>
                    <a href="jobs-list.html" class="list-group-item">View All Jobs</a>
                </div>
            </div>
            <div class="col-lg-9 col-md-9">
                <table class="table table-striped table-hover bg-light small">
                    <tr class="table-dark">
                        <th>ID</th>
                        <th>Job Name</th>
                        <th>Details</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Graphic Designer</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info" data-toggle="modal"
                                data-target="#jobDetails">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning" data-toggle="modal"
                                data-target="#jobEditDetails">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger"
                                onclick="confirm('Are you sure you want to delete \'Graphic Designer\'?')">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Web Designer</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger">Delete</a></td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Web Developer</td>
                        <td><a href="#" class="btn btn-sm btn-block btn-info">Details</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-warning">Edit</a></td>
                        <td><a href="#" class="btn btn-sm btn-block btn-danger">Delete</a></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <!-- dashboard contents end -->

    <!-- add_job Modal start -->
    <div class="modal fade" id="add_job" tabindex="-1" aria-labelledby="add_jobLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="add_jobLabel">Add Job Details</h5>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <input type="date" class="form-control form-control-sm" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control form-control-sm" placeholder="Job Name" required>
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-sm btn-success btn-block">Add New Job</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- add_job Modal end -->

    <!-- Details Modal start -->
    <div class="modal fade" id="jobDetails" tabindex="-1" aria-labelledby="jobDetailsLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="jobDetailsLabel">Job Details</h5>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table class="table table-bordered">
                        <tr>
                            <th>ID</th>
                            <td>1</td>
                        </tr>
                        <tr>
                            <th>Job Added Date</th>
                            <td>25 Oct 2020</td>
                        </tr>
                        <tr>
                            <th>Name</th>
                            <td>Graphic Designer</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <!-- Details Modal end -->

    <!-- Edit Details Modal start -->
    <div class="modal fade" id="jobEditDetails" tabindex="-1" aria-labelledby="jobEditDetailsLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="jobEditDetailsLabel">Job Details</h5>
                    <button type="button" class="btn-close" data-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="mb-3">
                            <input type="date" class="form-control form-control-sm" value="2020-10-25" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control form-control-sm" value="Graphic Designer"
                                placeholder="Job Name" required>
                        </div>
                        <div class="mb-3">
                            <button type="submit" class="btn btn-sm btn-success btn-block">Update Job Details</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- Edit Details Modal end -->
</body>

</html>
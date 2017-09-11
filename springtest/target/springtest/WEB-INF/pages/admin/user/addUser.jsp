<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title> 添加用户</title>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <h1> 添加用户</h1>
    <hr/>
    <form:form action="/admin/users/addP" method="post" commandName="user" role="form">

        <div class="form-group">
            <label for="ID">ID:</label>
            <input type="text" class="form-control" id="ID" name="ID" placeholder="Enter ID:"/>
        </div>
        <div class="form-group">
            <label for="name">name</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Enter name:"/>
        </div>
        <div class="form-group">
            <label for="identificationType">identificationType:</label>
            <input type="text" class="form-control" id="identificationType" name="identificationType" placeholder="Enter identificationType:"/>
        </div>
        <div class="form-group">
            <label for="identification">identification:</label>
            <input type="text" class="form-control" id="identification" name="identification" placeholder="Enter identification:"/>
        </div>
        <div class="form-group">
            <label for="sex">sex:</label>
            <input type="text" class="form-control" id="sex" name="sex" placeholder="Enter sex:"/>
        </div>
        <div class="form-group">
            <label for="birthday">birthday:</label>
            <input type="text" class="form-control" id="birthday" name="birthday" placeholder="Enter birthday:"/>
        </div>
        <div class="form-group">
            <label for="bankCard">bankCard:</label>
            <input type="text" class="form-control" id="bankCard" name="bankCard" placeholder="Enter bankCard:"/>
        </div>
        <div class="form-group">
            <label for="phoneNumber">phoneNumber:</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" placeholder="Enter phoneNumber:"/>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-sm btn-success">提交</button>
        </div>
    </form:form>
</div>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>
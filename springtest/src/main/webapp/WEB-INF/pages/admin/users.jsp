<%--
  Created by IntelliJ IDEA.
  User: 张鹏
  Date: 2017/8/30
  Time: 13:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>用户管理</title>

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
    <h1>用户管理</h1>
    <hr/>

    <h3>所有用户 <a href="/admin/users/add" type="button" class="btn btn-primary btn-sm">添加</a> <a href="/admin/users/install" type="button" class="btn btn-primary btn-sm">Install</a></h3>

    <!-- 如果用户列表为空 -->
    <c:if test="${empty userList}">
        <div class="alert alert-warning" role="alert">
            <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>User表为空，请<a href="/admin/users/add" type="button" class="btn btn-primary btn-sm">添加</a>
        </div>
    </c:if>


    <table class="table table-bordered table-striped">
        <tr>
            <th>ID</th>
            <th>名字</th>
            <th>证件类型</th>
            <th>证件号码</th>
            <th>性别</th>
            <th>生日</th>
            <th>银行卡号</th>
            <th>手机号</th>
            <th>操作</th>


        </tr>

        <c:forEach items="${userList}" var="user">
            <tr>
                <td>${user.ID}</td>
                <td>${user.name}</td>
                <td>${user.identificationType}</td>
                <td>${user.identification}</td>
                <td>${user.sex}</td>
                <td>${user.birthday}</td>
                <td>${user.bankCard}</td>
                <td>${user.phoneNumber}</td>
                <td>
                    <a href="/admin/users/show/${user.id}" type="button" class="btn btn-sm btn-success">详情</a>
                    <a href="/admin/users/update/${user.id}" type="button" class="btn btn-sm btn-warning">修改</a>
                    <a href="/admin/users/delete/${user.id}" type="button" class="btn btn-sm btn-danger">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>

</div>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>
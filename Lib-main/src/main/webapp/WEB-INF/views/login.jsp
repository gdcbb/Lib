<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>登录 - 图书管理系统</title>
  <style>
    body { margin: 0; font-family: "Microsoft YaHei", Arial, sans-serif; background: #f2f4f7; }
    .card { width: 320px; margin: 120px auto; padding: 32px; background: #fff;
            border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,.08); }
    h2 { margin: 0 0 24px; text-align: center; color: #333; font-size: 20px; }
    label { display: block; margin-bottom: 6px; color: #555; font-size: 14px; }
    input[type=text], input[type=password] { width: 100%; box-sizing: border-box;
            padding: 9px 10px; margin-bottom: 16px; border: 1px solid #d0d5dd;
            border-radius: 4px; font-size: 14px; }
    button { width: 100%; padding: 10px; border: 0; border-radius: 4px;
            background: #2563eb; color: #fff; font-size: 15px; cursor: pointer; }
    button:hover { background: #1d4ed8; }
    .error { margin-bottom: 16px; padding: 8px 10px; border-radius: 4px;
            background: #fef2f2; color: #b91c1c; font-size: 13px; }
    .hint { margin-top: 16px; text-align: center; color: #999; font-size: 12px; }
    .hint a { color: #2563eb; text-decoration: none; }
    .success { margin-bottom: 16px; padding: 8px 10px; border-radius: 4px;
            background: #f0fdf4; color: #15803d; font-size: 13px; }
  </style>
</head>
<body>
<div class="card">
  <h2>图书管理系统</h2>

  <c:if test="${not empty error}">
    <div class="error"><c:out value="${error}"/></div>
  </c:if>
  <c:if test="${not empty message}">
    <div class="success"><c:out value="${message}"/></div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/login">
    <label for="username">用户名</label>
    <input type="text" id="username" name="username"
           value="<c:out value='${param.username}'/>" autocomplete="username">

    <label for="password">密码</label>
    <input type="password" id="password" name="password"
           autocomplete="current-password">

    <button type="submit">登 录</button>
  </form>

  <div class="hint">
    <a href="${pageContext.request.contextPath}/register">注册新账号</a>
  </div>
</div>
</body>
</html>

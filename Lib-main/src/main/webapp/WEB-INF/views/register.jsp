<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>注册 - 图书管理系统</title>
  <style>
    body { margin: 0; font-family: "Microsoft YaHei", Arial, sans-serif; background: #f2f4f7; }
    .card { width: 360px; margin: 90px auto; padding: 32px; background: #fff;
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
    .login { margin-top: 16px; text-align: center; font-size: 13px; color: #666; }
    .login a { color: #2563eb; text-decoration: none; }
  </style>
</head>
<body>
<div class="card">
  <h2>注册账号</h2>

  <c:if test="${not empty error}">
    <div class="error"><c:out value="${error}"/></div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/register">
    <label for="username">用户名（3-20 位）</label>
    <input type="text" id="username" name="username" maxlength="20"
           value="<c:out value='${username}'/>" required>

    <label for="password">密码（不少于 6 位）</label>
    <input type="password" id="password" name="password" required>

    <label for="confirmPassword">确认密码</label>
    <input type="password" id="confirmPassword" name="confirmPassword" required>

    <button type="submit">注 册</button>
  </form>

  <div class="login">已有账号？
    <a href="${pageContext.request.contextPath}/login">返回登录</a>
  </div>
</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>新增图书 - 图书管理系统</title>
  <style>
    body { margin: 0; font-family: "Microsoft YaHei", Arial, sans-serif; background: #f2f4f7; color: #333; }
    .header { display: flex; justify-content: space-between; align-items: center;
              padding: 14px 24px; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.06); }
    .header h1 { margin: 0; font-size: 18px; }
    .header a { color: #2563eb; text-decoration: none; }
    .card { width: 520px; margin: 32px auto; padding: 28px 32px; background: #fff;
            border-radius: 8px; box-shadow: 0 1px 6px rgba(0,0,0,.06); }
    .row { display: flex; gap: 16px; }
    .field { flex: 1; margin-bottom: 14px; }
    label { display: block; margin-bottom: 6px; font-size: 14px; color: #555; }
    input { width: 100%; box-sizing: border-box; padding: 8px 10px;
            border: 1px solid #d0d5dd; border-radius: 4px; font-size: 14px; }
    .actions { display: flex; gap: 10px; margin-top: 8px; }
    .btn { padding: 9px 18px; border: 0; border-radius: 4px; background: #2563eb;
           color: #fff; font-size: 14px; cursor: pointer; text-decoration: none; }
    .btn-plain { background: #fff; color: #555; border: 1px solid #d0d5dd; }
  </style>
</head>
<body>
<div class="header">
  <h1>新增图书</h1>
  <a href="${pageContext.request.contextPath}/books">返回列表</a>
</div>

<div class="card">
  <form method="post" action="${pageContext.request.contextPath}/books/add">
    <div class="field">
      <label for="isbn">ISBN</label>
      <input type="text" id="isbn" name="isbn" required>
    </div>

    <div class="field">
      <label for="title">书名</label>
      <input type="text" id="title" name="title" required>
    </div>

    <div class="row">
      <div class="field">
        <label for="author">作者</label>
        <input type="text" id="author" name="author" required>
      </div>
      <div class="field">
        <label for="publisher">出版社</label>
        <input type="text" id="publisher" name="publisher" required>
      </div>
    </div>

    <div class="row">
      <div class="field">
        <label for="category">分类</label>
        <input type="text" id="category" name="category" required>
      </div>
      <div class="field">
        <label for="stock">库存</label>
        <input type="number" id="stock" name="stock" min="0" required>
      </div>
    </div>

    <div class="actions">
      <button class="btn" type="submit">保存</button>
      <a class="btn btn-plain" href="${pageContext.request.contextPath}/books">取消</a>
    </div>
  </form>
</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta charset="utf-8"/>
	<title>Блок чейн (владельцы блоков)</title>
</head>
<body>

<h3>Блок-чейн 1</h3>
Варианты стратегий (пропорции):
<div>S1 - 10% : 15% : 15% : 60%</div>
<div>S2 - 30% : 30% : 30% :  10%</div>
<div>S3 - 60% : 10% : 10% :  20%</div>
<div>S4 - 35% : 35% : 15% : 15%</div>

<table>
	<th></th><th>Создан</th><th>Владелец</th><th>Валюта 1</th><th>Валюта 2</th><th>Валюта 3</th><th>Валюта 4</th><th>Стратегия</th>
<#list model["chain"] as block>
	<tr>
		<td>${block?counter}</td><td>${block.ts}</td><td>${block.data.name}</td>
		<td>${block.data.currency1}</td>
		<td>${block.data.currency2}</td>
		<td>${block.data.currency3}</td>
		<td>${block.data.currency4}</td>
		<td>${block.data.strategy!}</td>
	</tr>
</#list>
</table>

<h3>Блок-чейн 2</h3>
Варианты стратегий (пропорции):
<div>S1 - 20% : 15% : 25% : 40%</div>
<div>S2 - 30% : 25% : 15% :  30%</div>
<div>S3 - 50% : 15% : 25% :  10%</div>
<div>S4 - 35% : 25% : 20% : 20%</div>

<table>
	<th></th><th>Создан</th><th>Владелец</th><th>Валюта 1</th><th>Валюта 2</th><th>Валюта 3</th><th>Валюта 4</th><th>Стратегия</th>
	<#list model["chain2"] as block>
	<tr>
		<td>${block?counter}</td><td>${block.ts}</td><td>${block.data.name}</td>
		<td>${block.data.currency1}</td>
		<td>${block.data.currency2}</td>
		<td>${block.data.currency3}</td>
		<td>${block.data.currency4}</td>
		<td>${block.data.strategy!}</td>
	</tr>
</#list>
</table>
</body>
</html>
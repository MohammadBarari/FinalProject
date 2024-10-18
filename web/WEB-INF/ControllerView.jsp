<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 10/18/2024
  Time: 2:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Charge Credit</title>
</head>
<body>
<h1>Charge Credit</h1>
<form id="chargeForm" action="/customerChargeCredit" method="post">
    <input type="hidden" id="customerId" name="customerId" value="${customerId}">

    <label for="amount">Amount:</label>
    <input type="number" step="0.01" id="amount" name="amount" required><br>

    <label for="cartNumber">Cart Number:</label>
    <input type="text" id="cartNumber" name="cartNumber" pattern="\\d{11}" required><br>

    <label for="cvv2">CVV2:</label>
    <input type="text" id="cvv2" name="cvv2" pattern="\\d{4}" required><br>

    <label for="expiresDate">Expiration Date:</label>
    <input type="date" id="expiresDate" name="expiresDate" required><br>

    <button type="submit">Submit</button>
</form>

<script>
    document.getElementById('chargeForm').onsubmit = function(event) {
        event.preventDefault();
        const formData = {
            customerId: document.getElementById('customerId').value,
            amount: document.getElementById('amount').value,
            cartNumber: document.getElementById('cartNumber').value,
            cvv2: document.getElementById('cvv2').value,
            expiresDate: document.getElementById('expiresDate').value
        };

        fetch('/customerChargeCredit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json())
            .then(data => {
                alert('Success: ' + data);
            })
            .catch(error => {
                alert('Error: ' + error);
            });
    };
</script>
</body>
</html>

const remainingTime = 600;
let timer;

document.getElementById('submitButton').addEventListener('click', handleChargeCredit);
document.getElementById('refreshCaptchaButton').addEventListener('click', fetchCaptcha);

window.onload = function() {
    fetchCaptcha();
    startTimer();
};



function startTimer() {
    let timeLeft = remainingTime;
    timer = setInterval(() => {
        if (timeLeft <= 0) {
            clearInterval(timer);
            showNotification("Time is up! The site will now close.");
            window.close();
        } else {
            timeLeft--;
            updateTimerDisplay(timeLeft);
        }
    }, 1000);
}

function updateTimerDisplay(timeLeft) {
    const minutes = Math.floor(timeLeft / 60);
    const seconds = timeLeft % 60;
    document.getElementById('timer').textContent = `Remaining Time: ${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

let currentCaptchaID = null;
async function fetchCaptcha() {
    try {
        const response = await fetch("http://localhost:8080/customer/captcha/generate", {
            method: "POST"
        });
        const captcha = await response.json();
        currentCaptchaID = captcha.id;
        document.getElementById('captchaDisplay').textContent = captcha.captcha;
    } catch (error) {
        showNotification("Error fetching CAPTCHA: " + error.message);
    }
}

async function handleChargeCredit() {
    const customerId = document.getElementById('customerId').value;
    const amount = parseFloat(document.getElementById('amount').value);
    const cartNumber = document.getElementById('cartNumber').value;
    const cvv2 = document.getElementById('cvv2').value;
    const expiresDate = document.getElementById('expiresDate').value;
    const captchaAnswer = document.getElementById('captcha').value;

    if (!customerId || isNaN(amount) || !cartNumber || !cvv2 || !expiresDate || !captchaAnswer) {
        showNotification("Please fill in all fields correctly.");
        return;
    }

    const dto = {
        customerId: parseInt(customerId),
        amount: amount,
        cartNumber: cartNumber,
        cvv2: cvv2,
        expiresDate: expiresDate,
        captchaAnswer : captchaAnswer,
        captchaId : currentCaptchaID,
    };

    try {
        const response = await fetch(`http://localhost:8080/customer/chargeCredit`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dto)
        });

        const result = await response.text();
        showNotification("Response: " + result);

        if (result.toLowerCase() === "successful") {
            window.location.href = 'http://localhost:8080/customer/charge/success';
        } else {
            showNotification("Charge was not successful: " + result);
        }
    } catch (error) {
        showNotification("Error: " + error.message);
    }
}

function showNotification(message) {
    const notificationDiv = document.getElementById('notification');
    notificationDiv.textContent = message;
    setTimeout(() => notificationDiv.textContent = '', 3000);
}

import { httpRequest } from './http.js';

const signupButton = document.getElementById("signup-btn");
if (signupButton) {
    signupButton.addEventListener("click", (event) => {
        let body = JSON.stringify({
            email: document.getElementById("email").value,
            password1: document.getElementById("password1").value,
            password2: document.getElementById("password2").value,
            nickname: document.getElementById("nickname").value,
        });
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("회원가입 완료되었습니다.");
            location.replace("/login");
        }

        httpRequest("POST", "/signup", csrfToken, body, success);
    });
}

const logoutButton = document.getElementById("logout-btn");
if (logoutButton) {
    logoutButton.addEventListener("click", (event) => {
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            location.reload();
        }
        httpRequest("POST", "/logout", csrfToken, null, success);
    });
}
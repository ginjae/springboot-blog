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

        function fail() {
        }

        httpRequest("POST", "/signup", csrfToken, body, success, fail);
    });
}

const logoutButton = document.getElementById("logout-btn");
if (logoutButton) {
    logoutButton.addEventListener("click", (event) => {
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            if (location.pathname.includes("/articles"))
                location.reload();
            else
                location.href = "/articles";
        }

        function fail() {
        }

        httpRequest("POST", "/logout", csrfToken, null, success, fail);
    });
}

const updateButton = document.getElementById("update-btn");
if (updateButton) {
    updateButton.addEventListener("click", (event) => {
        let body = JSON.stringify({
            nickname: document.getElementById("nickname").value,
        });
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("닉네임이 변경되었습니다.");
            location.reload();
        }

        function fail() {
        }

        httpRequest("PUT", "/userinfo", csrfToken, body, success, fail);
    })
}

const userAdminButton = document.querySelectorAll(".user-admin-btn");
userAdminButton.forEach((button) => {
    button.addEventListener("click", function() {
        let email = this.getAttribute("data-email");
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("관리자 권한이 부여되었습니다.");
            location.reload();
        }

        function fail() {
        }

        httpRequest("PUT", "/userinfo/" + email, csrfToken, null, success, fail);
    });
});

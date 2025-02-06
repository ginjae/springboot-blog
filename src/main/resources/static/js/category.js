import { httpRequest } from './http.js';

const categoryCreateButton = document.getElementById("category-create-btn");
if (categoryCreateButton) {
    categoryCreateButton.addEventListener("click", (event) => {
        let body = JSON.stringify({
            name: document.getElementById("categoryName").value,
            allowedRole: document.getElementById("categoryAllowedRole").value,
        });
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("카테고리가 추가되었습니다.");
            location.reload();
        }

        function fail() {
        }

        httpRequest("POST", "/api/categories", csrfToken, body, success, fail);
    });

}
const categoryDeleteButton = document.querySelectorAll(".category-delete-btn");
categoryDeleteButton.forEach((button) => {
    button.addEventListener("click", function() {
        let categoryName = this.getAttribute("data-name");
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("삭제 완료되었습니다.");
            location.reload();
        }

        function fail() {
        }

        httpRequest("DELETE", "/api/categories/" + categoryName, csrfToken, null, success, fail);
    });
});

const categoryUpdateButton = document.querySelectorAll(".category-update-btn");
categoryUpdateButton.forEach((button) => {
    button.addEventListener("click", function() {
        let categoryName = this.getAttribute("data-name");
        let allowedRole = this.previousElementSibling;
        let body = JSON.stringify({
            name: allowedRole.previousElementSibling.value,
            allowedRole: allowedRole.value,
        })
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("수정 완료되었습니다.");
            location.reload();
        }

        function fail() {
        }

        httpRequest("PUT", "/api/categories/" + categoryName, csrfToken, body, success, fail);
    });
});

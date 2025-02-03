import { httpRequest } from './http.js';

const categoryCreateButton = document.getElementById("category-create-btn");
if (categoryCreateButton) {
    categoryCreateButton.addEventListener("click", (event) => {
        let body = JSON.stringify({
            name: document.getElementById("categoryName").value,
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

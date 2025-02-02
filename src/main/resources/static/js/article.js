import { httpRequest } from './http.js';

const createButton = document.getElementById("create-btn");
if (createButton) {
    createButton.addEventListener("click", (event) => {
        let body = JSON.stringify({
            title: document.getElementById("title").value,
            content: simpleMDE.value(),
        });
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
        }

        httpRequest("POST", "/api/articles", csrfToken, body, success, fail);
    });
}

const deleteButton = document.getElementById("delete-btn");
if (deleteButton) {
    deleteButton.addEventListener("click", (event) => {
        let id = document.getElementById("article-id").value;
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("삭제 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
        }

        httpRequest("DELETE", "/api/articles/" + id, csrfToken, null, success, fail);
    });
}

const modifyButton = document.getElementById("modify-btn");
if (modifyButton) {
    modifyButton.addEventListener("click", (event) => {
        let params = new URLSearchParams(location.search);
        let id = params.get("id");
        let csrfToken = document.getElementById("csrf").value;
        let body = JSON.stringify({
            title: document.getElementById("title").value,
            content: simpleMDE.value(),
        });

        function success() {
            alert("수정 완료되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            location.replace("/articles/" + id);
        }

        httpRequest("PUT", "/api/articles/" + id, csrfToken, body, success, fail);
    });
}

const commentCreateButton = document.getElementById("comment-create-btn");
if (commentCreateButton) {
    commentCreateButton.addEventListener("click", (event) => {
        let id = document.getElementById("article-id").value;
        let csrfToken = document.getElementById("csrf").value;
        let body = JSON.stringify({
            articleId: id,
            content: document.getElementById("content").value,
        });

        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            location.replace("/articles" + id);
        }

        httpRequest("POST", "/api/comments", csrfToken, body, success, fail);
    });
}

const commentDeleteButton = document.querySelectorAll(".comment-delete-btn");
commentDeleteButton.forEach((button) => {
    button.addEventListener("click", function() {
        let id = document.getElementById("article-id").value;
        let commentId = this.getAttribute("data-id");
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("삭제 완료되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            location.replace("/articles/" + id);
        }

        httpRequest("DELETE", "/api/comments/" + commentId, csrfToken, null, success, fail);
    });
});

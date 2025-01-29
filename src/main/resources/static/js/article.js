const createButton = document.getElementById('create-btn');
if (createButton) {
    createButton.addEventListener("click", (event) => {
        let body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("POST", "/api/articles", csrfToken, body, success);
    });
}

const deleteButton = document.getElementById('delete-btn');
if (deleteButton) {
    deleteButton.addEventListener("click", (event) => {
        let id = document.getElementById('article-id').value;
        let csrfToken = document.getElementById("csrf").value;

        function success() {
            alert("삭제 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("삭제 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("DELETE", "/api/articles/" + id, csrfToken, null, success);
    });
}

const modifyButton = document.getElementById('modify-btn');
if (modifyButton) {
    modifyButton.addEventListener("click", (event) => {
        let params = new URLSearchParams(location.search);
        let id = params.get("id");
        let csrfToken = document.getElementById("csrf").value;
        let body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });

        function success() {
            alert("수정 완료되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            alert("수정 실패했습니다.");
            location.replace("/articles/" + id);
        }

        httpRequest("PUT", "/api/articles/" + id, csrfToken, body, success);
    });
}

function httpRequest(method, url, csrfToken, body, success) {
    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN" : csrfToken
        },
        body: body,
    }).then((response) => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
            return response.json().then((error) => {
                alert(error.code + ": " + error.message);
            });
        }
    });
}

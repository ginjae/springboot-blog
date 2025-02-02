export function httpRequest(method, url, csrfToken, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN" : csrfToken,
        },
        body: body,
    }).then((response) => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
            return response.json().then((error) => {
                alert(error.message);
                return fail(error);
            });
        }
    });
}

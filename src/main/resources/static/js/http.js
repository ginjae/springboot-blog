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
            return success(response);
        } else {
            return response.json().then((error) => {
                alert(error.message);
                return fail(error);
            });
        }
    });
}

export function changeParam(key, value) {
    const url = new URL(window.location.href);
    if (key === "sort")
        url.searchParams.set("page", "1");
    url.searchParams.set(key, value);
    window.location.href = url.toString();
}

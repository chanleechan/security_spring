$(() => {
    $("#joinBtn").click(() => {
        joinLocation();
    });

    $("#join").click(() => {
        join();
    });

    $("#logoutBtn").click(() => {
        logout();
    });

});


function joinLocation() {
    location.href = "/user/join";
}


function join() {
    if (joinValidationCheck()) {
        $.ajax({
            type: "post",
            url: "/login/join",
            data: {
                userId: $("#userId").val(),
                password: $("#password").val()
            },
            success: ((data) => {
                console.log(data);
            }), error(e) {
                console.log(e);
            }
        })
    } else {
        alert("조건이 맞지 않습니다.");
    }

}


function joinValidationCheck() {
    let check;
    const id = $("#userId").val();
    const pass = $("#password").val();
    const repass = $("#rePass").val();

    check = (id ?? true) !== "" && (pass ?? true) !== "" && (repass ?? true) !== "";
    if (check === false) return check;
    return pass === repass;
}

function logout() {
    location.href = "/user/logout";
}

// @author Ben Schäfer
//makes an ajax to the API-Server running on localhost with the requested url
class ApiHandler {
    constructor() {
        this.suffix = "http://localhost:4567/";
    }
    promised_ajax(request) {
        return new Promise((resolve, reject) => {
            $.ajax({
                method: "GET",
                dataType: "json",
                url: this.suffix + request,
                success: function (data) {
                    resolve(data);
                },
                error: function (data) {
                    console.log("Ungültige Anfrage")
                    reject(data);
                }
            })
        })
    }
}


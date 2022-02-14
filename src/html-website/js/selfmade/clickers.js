
// Creates DashBoard if searched input equals a fraction-name
function search_for_fraction(input) {
    if (["SPD", "AFD", "DIE_LINKE", "FDP", "BUENDNIS_90_DIE_GRUENEN", "CDU_CSU"].includes(input)) {
        console.log("gefunden")
        showTemplate(input);
    }
    else {
        alert("Der Suche entspricht leider keine Fraktion");
    }
}

// Creates from Template a board that fits to a party name
function showTemplate(name) {
    var t = document.getElementsByTagName("template")[0];
    var clone = t.content.cloneNode(true);
    document.getElementById("SearchDashboard").appendChild(clone);

    var new_board = [...document.querySelectorAll("#SearchDashboard > div")].pop();
    new_board.id = name;

    // Board nach Suche benennen
    new_board.querySelector('div > div.card-header.py-3 > h6').innerHTML = name;



    // add Delete-Button
    var btn = document.createElement("button")
    btn.innerHTML = "Board löschen";
    btn.addEventListener("click", function(e) {
        e.preventDefault();
        const dash_query = document.querySelector("#SearchDashboard");
        dash_query.removeChild(document.querySelector('#' + name));
    });
    new_board.appendChild(btn);

    // add all boards
    build_board(new_board.querySelector("div[class='card-body']"), name);

}


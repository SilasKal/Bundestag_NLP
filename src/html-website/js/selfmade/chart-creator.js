
// Eine Funktion die ein Chart eines wählbaren Typs erstellt
function chartCreator(writeTo, type) {
    var theChart = new Chart(writeTo, {
        type: type,
        data: {
            labels: ["Bitte", "etwas", "eingeben"],
            datasets: [{
                data: [33, 33, 33],
                backgroundColor: ['#dfd84e', '#1c39c8', '#36cc77'],
                hoverBackgroundColor: ['#6b5877', '#ffbe89', '#6baf2c'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }],
        },
        options: {
            maintainAspectRatio: false,
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                caretPadding: 10,
            },
            legend: {
                display: false
            },
            cutoutPercentage: 80,
        },
    });

    return theChart;
}
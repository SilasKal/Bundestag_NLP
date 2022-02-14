
// Enables you to search by clicking "ENTER"
var input = document.getElementById("SearchVal");
input.addEventListener("keydown", function (e) {

    if (e.key === "Enter") {
        e.preventDefault();
        search_for_fraction(document.getElementById("SearchVal").value);
    }
})


// creates a chart of desired type at desired location,fitting to one of the categories demanded in 3b)
class ChangeableChart {
    constructor(name, chart_type, parent, fraction) {
        this.name = name;
        console.log(fraction)
        this.fraction = fraction;
        this.frame = this.build_html(name, chart_type);
        parent.appendChild(this.frame);
    }
    // get frame for chart from template and write it to the actual html
    build_html(name, chart_type) {
        const chart_temp = document.getElementsByTagName("template")[1];
        const clone = chart_temp.content.cloneNode(true);
        clone.querySelector("h6[name='title']").innerHTML = name;
        if (name == "all") {
            clone.querySelector("h6[name='title']").innerHTML = "Übersicht";
        }

        // adds a listener for changes in the date frame
        clone.querySelector("form[name='DateBoard']").addEventListener("change", function (e) {
            e.preventDefault();
            var time_dict = Array.from(e.target.parentNode.querySelectorAll("input"))
                .reduce((acc, input) => ({
                ...acc, [input.name] : new Date(input.value)}), {});
            if (time_dict["SearchFrom"].getTime() < time_dict["SearchTo"].getTime()) {
                console.log("Jetzt nur noch Daten suchen...");
            }
            else {
                window.alert("keine valide Datumsangabe");
            }
        });
        this.chart = chartCreator(clone.querySelector("canvas"), chart_type);
        return clone;
    }

    // load data from API and write it to the chart for token, pos and sentiments
    async update_info(item) {
        console.log("waiting")
        var data = await new ApiHandler().promised_ajax(this.name + "/" + this.fraction);
        console.log(data.result)
        var count_list =  data.result.map(function(json) {return json.count})
        var type_list = data.result.map(function(json) {return json[item]})

        // Switch rows if sentiment is called
        if (this.name == "sentiment") {
            var filler = count_list;
            count_list = type_list;
            type_list = filler;
        }

        // Update chart data and revisualize it
        this.chart.data.labels = type_list;
        this.chart.data.datasets[0].data = count_list;
        this.chart.update();

        // Control-Prints
        console.log("Done")
        console.log(count_list)
        console.log(type_list);
    }

    // Update the info manually (necessary for named entities and speakers)
    update_only_info(types, counts) {
        this.chart.data.labels = types;
        this.chart.data.datasets[0].data = counts;
        this.chart.update();
        console.log("Updated")
        console.log(counts)
        console.log(types);

    }

}

//wandelt in europäisches Datum
function make_eu(date) {
    console.log(date);
    var eu_date = date.slice(-2) + "." + date.slice(-5,-3) + "." + date.slice(0,4)
    return eu_date;
}


// creates for every category a Chart
async function build_board(parent, fraction) {
    var type_list = ["line", "bar", "radar", "line", "bar"]
    var name_list = ["token", "pos", "sentiment", "ne", "speaker"]
    var chart_list = [];
    for (let i = 0; i < 5; i++) {

        //Special-Case NamedEntities: Create multiple line-Charts
        if (name_list[i] == "ne") {
            console.log(name_list[i])

            // load named entity data once
            var data = await new ApiHandler().promised_ajax("ne/" + fraction);
            console.log(data.result)
            var entities = ["MISC", "ORG", "PER", "LOC"]
            // build the multiple line-charts for every category
            for(let j=0; j<4; j++) {
                console.log("Das ausgewählte Paket: ")
                console.log(data.result[i][entities[i]]);
                var words = data.result[i][entities[i]].map(function(json) {return json.element});
                var counts = data.result[i][entities[i]].map(function(json) {return json.count});
                new ChangeableChart(entities[j], type_list[i], parent, fraction).update_only_info(words, counts);
            }

        }
        // Special-Case Speakers: Create Speaker Chart with extra pictures
        else if (name_list[i] == "speaker") {
            console.log(name_list[i]);
            var data = await new ApiHandler().promised_ajax("speaker/" + fraction);
            var speaker_names = data.result.map(function(json) {return json.name})
            var speech_count = data.result.map(function(json) {return json.count});
            var pictures = data.result.map(function(json) {return json.url});
            new ChangeableChart("speaker", type_list[i], parent, fraction).update_only_info(speaker_names, speech_count);
        }
        // build normal chart, when there is no special-case
        else {
            chart_list.push(new ChangeableChart(name_list[i], type_list[i], parent, fraction));
            await chart_list[i].update_info(name_list[i]);
        }
    }


}

// Enables you to update the DB-Progress-Bar
async function show_db_progress() {
    console.log("checking db...")
    var html_bar = document.getElementById("DbProgress");
    // html_bar.parentNode
    var result = await new ApiHandler().promised_ajax("speeches/count");
    console.log(result)
    var css_width = "width: " + Math.round((result.count / 26112) * 100) + "%";
    console.log(css_width);
    html_bar.setAttribute("style", css_width);
}

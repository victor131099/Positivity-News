fetch("/api/countries/selectedCountries").then(function(response) { return response.json() }).then(function (data){
                    localStorage.setItem('selectedLocation', JSON.stringify(data));
})

function getColor(countryname) {
    var selectedLocation=JSON.parse(localStorage.getItem("selectedLocation") || "[]");
    var indexcountry= -1
    for (var i =0; i < selectedLocation.length; i++){
        if (selectedLocation[i] === countryname){
            indexcountry= i
            break
        }
    }

    if (indexcountry === -1) {
        return "#0077b6"
    }

    return "#FF0000";
}

function style(feature) {
    return {
        fillColor: getColor(feature.properties.country),
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
    };
}

var countriesjson = '/api/countries/get';
var mymap = L.map('map').setView([0, 0], 2);

L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoidmNodTM4MDYiLCJhIjoiY2t1ODJqa2h4MDY4YjJvcnVyb3RmYzQ1NiJ9.ep94Zcw1b_MD7ikJTpBfuQ', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: 'your.mapbox.access.token'
}).addTo(mymap);
 fetch(countriesjson).then(function(response) { return response.json() }).then(function (data){
    var geojson = L.geoJSON(data, {
    style : style,
   onEachFeature: function (feature, layer) {
     layer.on('mouseover', function () {
        info.update(layer.feature.properties);
     });

     layer.on('mouseout', function (e) {
        info.update();
     });

     layer.on('click', function (e) {
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/countries?country=" + feature.properties.country);
        xhr.send();

        var selectedLocation=JSON.parse(localStorage.getItem("selectedLocation") || "[]");
        if (selectedLocation !== null){
            var indexcountry= -1
            for (var i =0; i < selectedLocation.length; i++){
                if (selectedLocation[i] === feature.properties.country){
                    indexcountry= i
                    break
                }
            }
            if (indexcountry === -1) {
               this.setStyle({
                'fillColor': "#FF0000"

               });
               selectedLocation.push(feature.properties.country)

            }else{
               this.setStyle({
                   'fillColor': "#0077b6"

                  });
                 selectedLocation.splice(selectedLocation.indexOf( feature.properties.country),1)
            }
         }else{
            selectedLocation=[]
            this.setStyle({
                'fillColor': "#FF0000"
               });
         }
        localStorage.setItem('selectedLocation', JSON.stringify(selectedLocation ));
      });
   }
 }).addTo(mymap);
});

var info = L.control();

info.onAdd = function (map) {
    this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
    this.update();
    return this._div;
};

// method that we will use to update the control based on feature properties passed
info.update = function (props) {
    this._div.innerHTML = '<h4>World Map</h4>'+  (props ?  '<br />'+ '<b>' +'Country' +'</b>' + ': '+ props.country +'<br/>' :"");
    this._div.style= "padding: 6px 8px; background: white; box-shadow: 0 0 15px rgba(0,0,0,0.2); border-radius: 5px;"
};

info.addTo(mymap);

var legend = L.control({position: 'bottomright'});

legend.onAdd = function (map) {
    var div = L.DomUtil.create('div', 'info legend')
    div.style = "padding: 6px 8px; backgroundx: white; box-shadow: 0 0 15px rgba(0,0,0,0.2); border-radius: 5px; height:100px; width:100px"

    // loop through our density intervals and generate a label with a colored square for each interval
    div.innerHTML +='<i style="background: #0077b6; width: 12px; height: 12px;float: left; margin-left:0px; margin-right: 8px; opacity: 0.7;"></i> Unselected Country <br> </br>'
    div.innerHTML +='<i style="background: #FF0000; width: 12px; height: 12px;float: left; margin-left:0px; margin-right: 8px; opacity: 0.7;"></i> Selected Country'

    return div;
};

legend.addTo(mymap);
function tableCreate(){
    var selectedLocation=JSON.parse(localStorage.getItem("selectedLocation") || "[]");
    var body = document.getElementById("selected-table")
    var checktbl = document.getElementById("countrytable")
    if (checktbl !== null){
        checktbl.remove();
    }
    var tbl  = document.createElement('table');
    tbl.id= "countrytable";
    tbl.class= "styled-table"
    tbl.classList.add("mx-auto")

    for(var i = 0; i < selectedLocation.length+1; i++){

        if (i==0){
            var tr = tbl.insertRow();
            tr.class = "table-head"
            var td1 = tr.insertCell();
            var td2 = tr.insertCell();
            td1.appendChild(document.createTextNode('Country'));
            td2.appendChild(document.createTextNode('Value'));

        }else{
            var tr = tbl.insertRow();
            var td1 = tr.insertCell();
            var td2 = tr.insertCell();
            td1.appendChild(document.createTextNode(selectedLocation[i-1].country));
            td1.style.border = '1px solid black';
            td2.appendChild(document.createTextNode(selectedLocation[i-1].value));
            td2.style.border = '1px solid black';
        }
    }

    body.appendChild(tbl);
}

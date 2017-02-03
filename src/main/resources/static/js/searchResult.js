var myMap = L.map('map');
// create the tile layer with correct attribution
/*
 * var osmUrl = 'http://{s}.tile.openstreetmap.de/tiles/osmde/{z}/{x}/{y}.png';
 * var osmAttrib = 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a>
 * contributors'; var osm = new L.TileLayer(osmUrl, { minZoom : 8, maxZoom : 18,
 * attribution : osmAttrib});
 */

myMap.setView([ 51.3391827, 12.3810549 ], 12);
// myMap.addLayer(osm);
myMap.addLayer(hybridBingLayer);

var table = $('#resultTable').DataTable({
	"paging" : false,
	"order" : [[ 2, "asc" ]]
});

$('#resultTable tbody tr').each(function() {
	var lat = $(this).attr("latitude");
	var lon = $(this).attr("longitude");

	if (lat == 0 && lon == 0)
		return;

	var ol = $(this).children("td:nth-child(2)").html();
	var p = "<p>" + $(this).children("td:nth-child(3)").html() + " €</p>";

	var circle = new L.circleMarker([ lat, lon ], {
		color : '#79b7e7',
		fillColor : '#79b7e7',
		fillOpacity : 0.5,
		radius : 10
	}).addTo(myMap);
	circle.bindTooltip(ol + p);
	var row = $(this);
	circle.on('mouseover', function() {
		circle.setStyle({
			color : '#2e6e9e',
			fillColor : '#2e6e9e'
		});
		row.addClass("highlight");
	});
	circle.on('mouseout', function() {
		circle.setStyle({
			color : '#79b7e7',
			fillColor : '#79b7e7'
		});
		row.removeClass("highlight");
	});

	$(this).hover(function() {
		$(this).addClass("highlight");
		circle.setStyle({
			color : '#2e6e9e'
		});
	}, function() {
		$(this).removeClass("highlight");
		circle.setStyle({
			color : '#79b7e7'
		});
	});
});
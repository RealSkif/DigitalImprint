document.addEventListener('coordinatesUpdated', function (event) {
    ymaps.ready(function () {
        var latitude = event.detail.latitude;
        var longitude = event.detail.longitude;

        var map = new ymaps.Map('map', {
            center: [latitude, longitude],
            zoom: 10 // Set the desired zoom level
        });

        var marker = new ymaps.Placemark([latitude, longitude], {
            hintContent: 'Marker'
        });

        map.geoObjects.add(marker);
    });
});

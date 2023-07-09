
    function getLocation() {
    if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition(
    function (position) {
    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    sendDataToServer(latitude, longitude);
},
    function (error) {
    sendDataToServer(null, null);
    console.log("Error: " + error.message);
}
    );
} else {
    console.log("Geolocation is not supported");
}
}

    function sendDataToServer(latitude, longitude) {
    var button = document.getElementById("01");
    var data = {
    latitude: latitude,
    longitude: longitude,
};

    fetch('/home', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(data)
})
    .then(response => {
    if (response.ok) {
    button.innerText = 'Успех!';
    setTimeout(() => {
    button.innerText = 'Отправить местоположение';
}, 3000);
    console.log('Location data sent successfully');
} else {
    button.innerText = 'Ошибка';
    setTimeout(() => {
    button.innerText = 'Отправить местоположение';
}, 3000);
    console.log('Error sending location data');
}
})
    .catch(error => {
    button.innerText = 'Ошибка';
    setTimeout(() => {
    button.innerText = 'Отправить местоположение';
}, 3000);
    console.log('Error sending location data:', error);
});
}


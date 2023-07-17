function getLocation() {
    var button = document.getElementById("01");
    var defaultText = button.innerText;
    var loadingText = "Отправляем...";
    var gpsNotEnabledText = "GPS не включен!";

    button.innerText = loadingText;
    button.disabled = true;

    checkGPSAvailability()
        .then(function (isGPSAvailable) {
            if (isGPSAvailable) {
                navigator.geolocation.getCurrentPosition(
                    function (position) {
                        var latitude = position.coords.latitude;
                        var longitude = position.coords.longitude;

                        sendDataToServer(latitude, longitude, button, defaultText);
                    },
                    function (error) {
                        button.innerText = gpsNotEnabledText;
                        setTimeout(function () {
                            button.innerText = defaultText;
                            button.disabled = false;
                        }, 5000);
                    }
                );
            }
        });
}

function checkGPSAvailability() {
    return new Promise(function (resolve, reject) {
        if ("geolocation" in navigator) {
            resolve(true);
        } else {
            reject(new Error("Geolocation is not supported"));
        }
    });
}

function sendDataToServer(latitude, longitude, button, defaultText) {
    var data = {
        latitude: latitude,
        longitude: longitude,
    };

    fetch('/index', {
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
                    button.innerText = defaultText;
                    button.disabled = false;
                }, 5000);
                console.log('Location data sent successfully');
            } else {
                button.innerText = 'Ошибка';
                setTimeout(() => {
                    button.innerText = defaultText;
                    button.disabled = false;
                }, 5000);
                console.log('Error sending location data');
            }
        })
        .catch(error => {
            button.innerText = 'Ошибка';
            setTimeout(() => {
                button.innerText = defaultText;
                button.disabled = false;
            }, 5000);
            console.log('Error sending location data:', error);
        });
}

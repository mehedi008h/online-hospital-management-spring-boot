function loadPost() {
    fetch('https://disease.sh/v3/covid-19/countries/bangladesh')
        .then(response => response.json())
        .then(data => displayResult(data))
}

loadPost();

function displayResult(data) {
    document.getElementById('confrim').innerText = data.cases;
    document.getElementById('death').innerText = data.deaths;
    document.getElementById('recover').innerText = data.recovered;
    document.getElementById('active').innerText = data.active;
}
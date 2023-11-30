const ROBOT_URI = "http://192.168.43.1:2713";
const telemetryForm = document.getElementById("telemetry-form");
const inputForm = document.getElementById("input-form");
const submitBr = document.getElementById("submit-br");
let pollingRate = document.getElementById("pollrate").value;

const fetchTelemetry = () => {
  fetch(ROBOT_URI + '/telemetry')
    .then(response => response.json())
    .then(data => {
      for (const key in data) {
        if (document.getElementById("tele-" + key)) {
          document.getElementById("tele-" + key).value = data[key];
          return;
        }

        const tr = document.createElement("tr");

        const labelTd = document.createElement("td");
        labelTd.innerText = key;
        tr.appendChild(labelTd);

        const valueTd = document.createElement("td");
        valueTd.setAttribute("id", "tele-" + key);
        valueTd.setAttribute("name", "tele-" + key);
        valueTd.innerText = data[key];
        tr.appendChild(valueTd);
        telemetryForm.appendChild(tr);
      }
    })
    .catch(err => console.error(err));
};

const fetchInputs = () => {
  fetch(ROBOT_URI + '/inputs')
    .then(response => response.json())
    .then(data => {
      for (const key in data) {
        if (!document.getElementById(key)) {
          const label = document.createElement("label");
          const labelText = document.createTextNode(key);
          label.className = "input-label";
          label.setAttribute("for", key);
          label.appendChild(labelText);
          inputForm.insertBefore(label, submitBr);

          const input = document.createElement("input");
          input.setAttribute("id", key);
          input.setAttribute("name", key);
          input.setAttribute("type", "number");
          input.setAttribute("value", data[key].value);
          input.setAttribute("min", data[key].min);
          input.setAttribute("max", data[key].max);
          input.classList.add("number-input");
          inputForm.insertBefore(input, submitBr);          
        }
      }
    })
    .catch(err => console.error(err));
};

fetchTelemetry()
fetchInputs();

let poller = setInterval(() => {
  fetchTelemetry()
  fetchInputs();
}, pollingRate);

document.getElementById("pollrate").addEventListener("change", (e) => {
  console.log(e.target.value);
  pollingRate = e.target.value;
  clearInterval(poller);
  poller = setInterval(() => {
    fetchTelemetry()
    fetchInputs();
  }, pollingRate);
});
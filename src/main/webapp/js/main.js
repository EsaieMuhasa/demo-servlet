/**
 * callback de reception du message
 * @param {MessageEvent} message message recue depui le serveur
 */
function onMessage (message) {
    var data = message.data;

    var json = JSON.parse(data);
    if(json.ports){
        updateSelecte(json);
    } else {
        console.log(json);
    }
} 

/**
 * lors de l'ouverture d'une connexion
 * @param {Event} event 
 */
function onOpen (event) {
    console.log(event);
}

/**
 * lors de la fermeture de la connection du WebSocket
 * @param {Event} event 
 */
function onClose (event) {
    console.log(event);
}

function onError (error) {
    console.log(error);
}

/**
 * mise en jours du select contenant les ports serie (COM port) connecter sur le serveur
 * @param {Object} data 
 * @param {string} data.currentPort le port sur lequel le serveur est actuelement connectee
 * @param {string[]} data.ports collection des ports disponible sur le serveur
 */
function updateSelecte (data) {
    var ports = data.ports;
    var selected = data.currentPort ==  ''? null : data.currentPort;

    var select = document.getElementsByTagName('select')[0];
    select.innerHTML = '<option value="none">--Aucun port--</option>';

    for (let i = 0; i < ports.length; i++) {
        const portName = ports[i];
        
        var option= document.createElement('option');
        option.setAttribute('value', portName);
        option.appendChild(document.createTextNode(portName));

        select.appendChild(option);

        if(selected == null){
            continue;
        }

        if (selected == portName) {
            option.setAttribute('selected', 'selected');
        }
    }
}


function main () {
    var host = window.location.hostname;
    var port = window.location.port;
    var url = 'ws://'+host+":"+port+"/demo-servlet/ws";

    console.log(url);

    var ws = new WebSocket(url);
    ws.onopen = event => onOpen(event);
    ws.onclose = event => onClose(event);
    ws.onmessage = message => onMessage(message);
    ws.onerror = err => onError(err);

    // var form = document.getElementsByTagName('form')[0];
    // form.addEventListener('submit', event => {
    //     event.preventDefault();

    //     var formData = new  FormData(document.getElementsByTagName('form')[0]);
    //     var port  = form.getElementsByTagName('select')[0].value;
    //     console.log(port);
    //     console.log(formData);

    //     var xhr = new  XMLHttpRequest();
    //     xhr.onload = (e) => {
    //         console.log(e);
    //     }

    //     xhr.open("POST", form.getAttribute('action'));
    //     xhr.send(formData);
    // });

    var form = document.getElementsByTagName('form')[0];
    form.addEventListener('submit', event => {
	
		event.defaultPrevented();
        
        $.ajax({
            method: "POST",
            url: form.getAttribute('action'),
            data: $(form).serialize()
        }) .done(function(msg) {
                console.log(msg)
            });
    });
    
}

(() => {
    window.addEventListener('load', () => main());
}) ();


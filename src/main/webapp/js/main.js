/**
 * callback de reception du message
 * @param {MessageEvent} message message recue depui le serveur
 */
function onMessage (message) {
    var data = message.data;

    var json = JSON.parse(data);
    updateSelecte(json);
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
 * @param {string[]} data.port collection des ports disponible sur le serveur
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

(function main () {
    var host = window.location.hostname;
    var port = window.location.port;
    var url = 'ws://'+host+":"+port+"/demo-servlet/ws";

    console.log(url);

    var ws = new WebSocket(url);
    ws.onopen = event => onOpen(event);
    ws.onclose = event => onClose(event);
    ws.onmessage = message => onMessage(message);
    ws.onerror = err => onError(err);
    
}) ();


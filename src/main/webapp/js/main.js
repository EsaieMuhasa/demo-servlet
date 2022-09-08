/**
 * callback de reception du message
 * @param {string} message message recue depui le serveur
 */
function onMessage (message) {
    console.log(message);
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


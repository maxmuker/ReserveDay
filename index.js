const functions = require('firebase-functions');
const admin = require('firebase-admin');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
exports.createUser = functions.firestore
  .document('usuario/{usuario}')
  .onUpdate(event => {
    // Get an object representing the document
    // e.g. {'name': 'Marie', 'age': 66}
    var newValue = event.data.data();
	var id = event.data.id;

    // access a particular field as you would any JS property
    var contatos = newValue.contatos;
	var contatosEncontrados = [];
	
	var db = admin.firestore();
	
	for(var i = 0, l = contatos.length; i < l; ++i){
			var refUsuarios = db.collection('usuario').doc(contatos[i]);
			if(refUsuarios.get().exists){
				contatosEncontrados.push(contatos[i]);
			}

	}
	var telefonesCadastrados = db.collection('usuario').doc(id).update({contatos:contatosEncontrados}	);
	

    // perform desired operations ...
});
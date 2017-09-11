// express inicializa la app
var app = require('express')();

// la servimos como un servicio http
var http = require('http').Server(app);

// inicio socket.io con el servicio http
var io = require('socket.io')(http);

var temperatura_actual=0;
var ritmo_cardiaco=0;
var alerta_cardiaca=0;

var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mongoClient = new MongoClient(new MongoServer('localhost' , 27017));
// definimos una ruta que sirva como bienvenida a la pagina index.html
app.get('/temperatura', function(req, res){
  	res.sendFile(__dirname + '/Temperatura.html');
});

app.get('/pulsera', function(req, res){
    res.sendFile(__dirname + '/Pulsera.html');
});

app.get('/luz', function(req, res){
    res.sendFile(__dirname + '/Luminosidad.html');
});

app.get('/', function(req, res){
    res.sendFile(__dirname + '/index.html');
});

app.get('/ac', function(req, res){
    res.sendFile(__dirname + '/AA.html');
});

app.get('/usuario', function(req,res){
    res.sendFile(__dirname + '/Usuario.html');
});

app.get('/persianas', function(req,res){
    res.sendFile(__dirname + '/Persianas.html');
});

function controlarPersianas(estado) {
	io.emit('controlar_persianas', estado);
}

function controlarac(estado) {
	io.emit('controlar_ac', estado);
}

function agente_temperatura(temperatura){
  if(temperatura>30){
    io.emit('controlar_persianas', "Cerradas");
    io.emit('log_alerta_cliente', "Temperatura alta cerrando persianas");
  }
  if(temperatura<15){
    io.emit('log_alerta_cliente', "Temperatura baja " + temperatura);
  }
}

function agente_luminosidad(luminosidad){
  if(luminosidad>7){
    io.emit('controlar_persianas', "Cerradas");
    io.emit('log_alerta_cliente', "Luminosidad alta cerrando persianas");
    alerta_cardiaca=1;
  }
  if(luminosidad<3){
    io.emit('log_alerta_cliente', "Luminosidad baja " + luminosidad);
  }
}

function agente_reglas_cardiacas(){
  if(ritmo_cardiaco>70 && temperatura_actual>24){
    io.emit('controlar_ac', "Encendido");
    io.emit('controlar_persianas', "Cerradas");
    io.emit('log_alerta_cliente', "Temperatura y ritmo cardiaco altos");
    alerta_cardiaca=1;
  }
  if(ritmo_cardiaco==0){
    io.emit('controlar_ac', "Apagado");
    io.emit('controlar_persianas', "Cerradas");
    io.emit('log_alerta_cliente', "Persona no presente cerrando persianas y apagando ac");
  }
}

function agente_alerta_cardiaca(){
  if(alerta_cardiaca==1 && temperatura_actual<=22){
    io.emit('controlar_ac', "Apagado");
    io.emit('controlar_persianas', "Abiertas");
    io.emit('log_alerta_cliente', "Temperatura regulada ");
    alerta_cardiaca=0;
  }
}

// escucho el evento connection para conexiones entrantes y las muestro por consola
mongoClient.connect("mongodb://localhost:27017/mibd", function(err,db){

	db.createCollection("valores_actuales", function(err, res) {
    if (err) throw err;
    console.log("Collection created!");
    //db.close();
  	});
  	db.createCollection("valores_registro", function(err, res) {
    if (err) throw err;
    console.log("Collection created!");
    //db.close();
  	});
  	var dato = {tipo: "temperatura" , valor: "25"};
  	db.collection("valores_actuales").insertOne(dato,function(err,res){
  	if(err) throw err;
  	console.log("temperatura registrada");
  	//db.close();
  	});
  	var dato2 = {tipo: "luminosidad" , valor: "media"};
  	db.collection("valores_actuales").insertOne(dato2,function(err,res){
  	if(err) throw err;
  	console.log("luminosidad registrada");
  	//db.close();
  	});

	io.on('connection', function(socket){
  		console.log('a user connected');

  		// si se desconecta lo mostramos en la consola
  		socket.on('disconnect', function(){
    		console.log('user disconnected');
  		});

  		
  		socket.on('nueva_temperatura', function(msg){
  			var datoA1 = {tipo: "temperatura"};
  			var datoN1 = {tipo: "temperatura" , valor: msg};
        io.emit('log_temperatura_cliente', msg);

  			db.collection("valores_actuales").updateOne(datoA1,datoN1,function(err,res){
  				if(err) throw err;
  				console.log("temperatura actualizada");
  			});
        temperatura_actual=msg;

  			db.collection("valores_registro").insertOne(datoN1,function(err,res){
  			if(err) throw err;
  			});
  			console.log('nueva temperatura: ' + msg);
        agente_temperatura(msg);
        agente_reglas_cardiacas();
        agente_alerta_cardiaca();
  		});

  		socket.on('nueva_luminosidad', function(msg){
  			//io.emit('nueva_luminosidad', msg);
  			var datoA2 = {tipo: "luminosidad"};
  			var datoN2 = {tipo: "luminosidad" , valor: msg};

        io.emit('log_luz_cliente', msg);

  			db.collection("valores_actuales").updateOne(datoA2,datoN2,function(err,res){
  				if(err) throw err;
  				console.log("luminosidad actualizada");

  				//db.close();
  			});
  			db.collection("valores_registro").insertOne(datoN2,function(err,res){
  			if(err) throw err;
  			});
    		console.log('nueva luminosidad: ' + msg);
        agente_luminosidad(msg);

  		});

      socket.on('frecuencia_cardiaca', function(msg){
        
        ritmo_cardiaco=msg;
        agente_reglas_cardiacas();
        agente_alerta_cardiaca();

        console.log('frecuencia_cardiaca a : ' + msg);
      });

      socket.on('estado_ac', function(msg){
        io.emit('controlar_ac', msg);

        console.log('estado del ac: ' + msg);
      });

      socket.on('estado_persianas', function(msg){
        io.emit('controlar_persianas', msg);

        console.log('estado de persianas: ' + msg);
      });

	});

});

//----------------------------------

// ponemos el servicio a la escucha en el puerto 3000
http.listen(3000, function(){
  	console.log('listening on *:3000');
});
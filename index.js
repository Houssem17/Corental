var crypto = require('crypto');
var uuid = require ('uuid');
var express = require ('express');
var mysql =require ('mysql');
var bodyParser = require ('body-parser');




//hello

var app = express();
var multer, storage, path, crypto;
multer = require('multer')
path = require('path');
var ima = "";

// connect to mysql

var con = mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database: 'renthouse'
});

con.connect((err)=> {
    if(!err)
        console.log('Connection Established Successfully');
    else
        console.log('Connection Failed!'+ JSON.stringify(err,undefined,2));
});

//PASSWORD CRYPT
var genRandomString = function (length) {
    return crypto.randomBytes(Math.ceil(length/2))
        .toString('hex') //Convert to hexa format
        .slice(0,length);
    
};
var sha512 = function (password,salt) {
    var hash = crypto.createHmac('sha512',salt) ; //Use SHA512
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt:salt,
        passwordHash:value
    };
    
};
function saltHashPassword(userPassword) {
    var salt = genRandomString(16); //Gen Random string with 16 charachters
    var passwordData = sha512(userPassword,salt) ;
    return passwordData;
    
}
function checkHashPassword(userPassword,salt) {
    var passwordData = sha512(userPassword,salt);
    return passwordData;
    
}

var app = express();
app.use(bodyParser.json()); // Accept JSON params
app.use(bodyParser.urlencoded({extended:true})); //Accept UrlEncoded params

app.post('/register/',(req,res,next)=>{
    var post_data = req.body;  //Get POST params
    var uid = uuid.v4();   //Get  UUID V4
    var plaint_password = post_data.password ;  //Get password from post params
    var hash_data = saltHashPassword(plaint_password);
    var password = hash_data.passwordHash;  //Get Hash value
    var salt = hash_data.salt; //Get salt

    var name = post_data.name;
    var email = post_data.email;
    var prenom = post_data.prenom;
	var tel_user = post_data.tel_user;

    con.query('SELECT * FROM user where email=?',[email],function (err,result,fields) {
        con.on('error',function (err) {
            console.log('[MYSQL ERROR]',err);
            
        });
        if (result && result.length)
            res.json('Utilisateur déjà inscrit!!!');
        else {
            con.query('INSERT INTO `user`(`unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`, `prenom`, `tel_user`,`image_user`) ' +
                'VALUES (?,?,?,?,?,NOW(),NOW(),?,?,?)',[uid,name,email,password,salt,prenom,tel_user,ima],function (err,result,fields) {
                if (err) throw err;

                res.json('Inscrire avec succés !');

            })
        }
    });

})
app.post('/login/',(req,res,next)=>{
    var post_data = req.body;

    //Extract email and password from request
    var user_password = post_data.password;
    var email = post_data.email;

    con.query('SELECT * FROM user where email=?',[email],function (err,result,fields) {
        con.on('error', function (err) {
            console.log('[MYSQL ERROR]', err);
        });
        if (result && result.length)

        {
            var salt = result[0].salt;
            var encrypted_password = result[0].encrypted_password;
            var hashed_password = checkHashPassword(user_password, salt).passwordHash;
        if (encrypted_password == hashed_password)
            res.end(JSON.stringify(result[0]))
        else
            res.end(JSON.stringify('Mot De Passe Incorrecte'))


        }

        else {

                res.json('Utilisateur n"existe pas!!');

            }

    });


})




var form = "<!DOCTYPE HTML><html><body>" +
"<form method='post' action='/upload' enctype='multipart/form-data'>" +
"<input type='file' name='upload'/>" +
"<input type='submit' /></form>" +
"</body></html>";

app.get('/', function (req, res){
  res.writeHead(200, {'Content-Type': 'text/html' });
  res.end(form);

});

// Include the node file module
var fs = require('fs');

storage = multer.diskStorage({
    destination: './uploads/',
    filename: function(req, file, cb) {
      return crypto.pseudoRandomBytes(16, function(err, raw) {
        if (err) {
          return cb(err);
        }
        return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
      });
    }
  });



// Post files
app.post(
  "/upload",
  multer({
    storage: storage
  }).single('upload'), function(req, res) {
    console.log(req.file);
    console.log(req.body);
    res.redirect("/uploads/" + req.file.filename);
    console.log(req.file.filename);
	ima = req.file.filename;
	console.log(photo_evenement);
    return res.status(200).end();
  });



//GET CLIENT:BEGIN
app.get('/user/:email', (req, res, next) => {
    con.query('SELECT * FROM user where email=?', [req.params.email], function (err, result, fields) {
        con.on('error', function (err) {
            console.log('[MYSQL ERROR]', err);
        });
        if (result && result.length) {
            res.end(JSON.stringify(result[0]));
        }
        else {
            res.end(JSON.stringify("error"));
        }

    });
	})

	//GET list evenements
app.get('/GetEvents/', (req, res, next) => {
    con.query('SELECT * FROM evenement', function (err, result, fields) {
        con.on('error', function (err) {
            console.log('[MYSQL ERROR]', err);
        });
        if (result && result.length) {
            res.end(JSON.stringify(result));
        }
        else {
            res.end(JSON.stringify("nothing was found"));
        }

    });


})


	

	//GET list articles
	app.get('/GetArticle/', (req, res, next) => {
		con.query('SELECT * FROM article', function (err, result, fields) {
			con.on('error', function (err) {
				console.log('[MYSQL ERROR]', err);
			});
			if (result && result.length) {
				res.end(JSON.stringify(result));
			}
			else {
				res.end(JSON.stringify("nothing was found"));
			}
	
		});
	
	
	})



  app.post('/evenement/add', function(req,res,next){
    var post_data = req.body;  //Get POST params
    var nom_evenement = post_data.nom_evenement;
    var type_evenement = post_data.type_evenement;
    var date_debut_evenement = post_data.date_debut_evenement;
    var date_fin_evenement = post_data.date_fin_evenement;
    var distance_evenement = post_data.distance_evenement;
    var lieux_evenement = post_data.lieux_evenement;
	var infoline = post_data.infoline;
	var difficulte_evenement = post_data.difficulte_evenement;
	var prix_evenement = post_data.prix_evenement;
	var id_user = post_data.id_user;


 
    con.query('INSERT INTO `evenement`(`nom_evenement`, `type_evenement`, `date_debut_evenement`, `date_fin_evenement`, `distance_evenement`, `lieux_evenement`, `infoline`, `difficulte_evenement`, `prix_evenement`, `id_user`, `photo_evenement`) ' +
        'VALUES (?,?,?,?,?,?,?,?,?,?,?)',[nom_evenement,type_evenement,date_debut_evenement,date_fin_evenement,distance_evenement,lieux_evenement,infoline,difficulte_evenement,prix_evenement,id_user,ima],function (err,result,fields) {
                if (err) throw err;

                res.json('Evenement ajouté avec succés');

            });

    })

app.get('/uploads/:upload', function (req, res){
  file = req.params.upload;
  console.log(req.params.upload);
  var img = fs.readFileSync(__dirname + "/uploads/" + file);
  res.writeHead(200, {'Content-Type': 'image/png' });
  res.end(img, 'binary');

});



//GET Events User
app.get('/GetEvenementUser/:id_user', (req, res, next) => {
    con.query('SELECT * FROM evenement e where  e.id_user=?', [req.params.id_user], function (err, result, fields) {
        con.on('error', function (err) {
            console.log('[MYSQL ERROR]', err);
        });
        if (result && result.length) {
            res.end(JSON.stringify(result));
        }
        else {
            res.end(JSON.stringify("error"));
        }

})});


/* ADD ARTICLE */
app.post('/article/add',(req,res,next)=>{
    var post_data = req.body;  //Get POST params
    var titre_article = post_data.titre_article;
    var description_article = post_data.description_article;
    var location_article = post_data.location_article;
   // var date_article = post_data.date_article;
    //var categorie_article = post_data.categorie_article;
    var prix_article = post_data.prix_article;
    var image_article = post_data.image_article;
    var user_id = post_data.user_id;
	var vendre_article = post_data.vendre_article;
	var louer_article = post_data.louer_article;

let currentDate = Date.now();
    let date_ob = new Date(currentDate);
    let date = date_ob.getDate();
    let month = date_ob.getMonth() + 1;
    let year = date_ob.getFullYear();
    let dateFinal = year + "-" + month + "-" + date;






    con.query('INSERT INTO `article`( `titre_article`, `description_article`, `location_article`, `date_article`, `prix_article`,`image_article`,`user_id`,`vendre_article`,`louer_article`) ' +
        'VALUES (?,?,?,?,?,?,?,?,?)',[titre_article,description_article,location_article,dateFinal,prix_article,image_article,user_id,vendre_article,louer_article],function (err,result,fields) {
        if (err) throw err;

        res.json('Article ajouté avec succés');

    });

})


//app.get("/",(req,res,next)=>{
//	console.log('Password: 123456');
//	var encrypt = saltHashPassword("123456");
//	console.log('Encrypt: '+encrypt.passwordHash);
//	console.log('Salt: '+encrypt.salt);
//})

//start server

app.listen(3000,()=>{
	console.log('EDMTDev Restful running on port 3000');
})
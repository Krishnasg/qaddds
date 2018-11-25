const express = require('express')
const multer = require('multer')
var bodyParser = require('body-parser');
const path = require('path')
const app = express()
app.use('/static',express.static('public/images'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const port = 3008

  var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, __dirname + '/public/images/')
    },
    filename: function (req, file, cb) {
      cb(null, file.fieldname + '-' + Date.now()+path.extname(file.originalname))
    }
  })
  
const uploader = multer({storage});

var postContent = {
    advertiseContent: {
        contentType: '',
        contentText: '',
        imgPath: ''
    }
}

app.get('/', function (req, res) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.status('root')
})

app.post('/post/add', uploader.any(), function (req, res) {
    if ( !req.files.length ) {
        if (req.body.postData.charAt(0) === '"' && req.body.postData.charAt(req.body.postData.length -1) === '"') {
            req.body.postData = req.body.postData.substr(1,req.body.postData.length -2);
        }
        postContent.advertiseContent.contentText = req.body.postData
        postContent.advertiseContent.contentType = "text"
        postContent.advertiseContent.imgPath = ""
    } else {
        postContent.advertiseContent.imgPath = req.protocol+"://"+req.headers.host+"/static/"+req.files[0].filename
        postContent.advertiseContent.contentType = "image"
        postContent.advertiseContent.contentText = ""
    }
    console.log(req.protocol)
    res.status(200).json(postContent)
})

app.get('/post/get', function (req, res) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.status(200).json(postContent)
})
            
app.listen(port, () => console.log(`Example app listening on port ${port}!`))
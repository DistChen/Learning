const http = require('http');

http.createServer((req,res)=>{
    const params = req.url.substring(2).split("&");
    let paraMap = new Map();
    
    params.forEach(item => {
        let keyValuePair = item.split("=");
        paraMap.set(keyValuePair[0],keyValuePair[1]);
    });
    let callbackFunc = paraMap.get("callback");
    if(callbackFunc){
        res.writeHead(200);
        res.end(`${callbackFunc}(${JSON.stringify({
            data:`Hello World`
        })})`);
    }else{
        res.writeHead(200,{
            "Content-Type":"application/json;charset=utf-8",
            "Access-Control-Allow-Origin":"*"
        });
        res.end(JSON.stringify({
            data:`Hello World`
        }));
    }
}).listen(8080,'127.0.0.1',()=>{
    console.log(`访问地址：http://127.0.0.1:8080`);
});
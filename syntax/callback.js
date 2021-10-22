// 평소 함수를 어떻게 정의 하는가?

// function a(){
//     console.log("a");
// }

var a = function() {
console.log("a");
console.log(i);
}

 a();

function slowfunc(callback){
    callback();
}

slowfunc(a);

// 비동기 처리는 왜 필요한가? > 주어진 요청을 동기적으로 처리하면 들어가는 리소스가 많아지기 때문이다.
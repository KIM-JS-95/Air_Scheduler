var fs = require('fs');



/*

// 동기
//readFileSync

console.log('A');
var result = fs.readFileSync('./sample.txt', 'utf8');
console.log(result);
console.log('C');


// A -> B -> C
*/



// 비동기
console.log('A');

fs.readFile('./sample.txt', 'utf8', function(err, result){
    console.log(result);
});


console.log("C");

// A -> C -> B
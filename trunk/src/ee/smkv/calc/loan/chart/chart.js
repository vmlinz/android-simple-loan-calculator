function drawPipe(percent , name1 , name2){
  var canvas = document.getElementById("pipe");
  var y = canvas.height/2;
  var x = y;
    var ctx = canvas.getContext("2d");
    ctx.strokeStyle = '#777';
    ctx.fillStyle = '#AFA';
    ctx.lineWidth = 2;

    ctx.beginPath();
    ctx.arc(x ,y , y *0.9 ,  0 , Math.PI*2 , false);
    ctx.stroke();

    ctx.lineWidth = 1;

    ctx.beginPath();
    ctx.moveTo(x ,y);
    ctx.arc(x ,y , y *0.85 ,  0 , Math.PI*(2 * ((percent)/100)) , true);
    ctx.fill();

    ctx.fillStyle = '#F00';
    ctx.beginPath();
    ctx.moveTo(x ,y);
    ctx.arc(x ,y, y *0.85  ,  0 ,  Math.PI*(2 * ((percent)/100)) , false);
    ctx.fill();

    ctx.strokeStyle = '#000000';
    ctx.beginPath();
    ctx.moveTo(x ,y);
    ctx.arc(x ,y, y *0.85  ,  0 ,  Math.PI*(2 * ((percent)/100)) , false);
    ctx.stroke();


    ctx.fillStyle = '#F33';
    ctx.strokeStyle = '#F33';
    ctx.lineWidth = 0.75;
    ctx.strokeText(name1 + ' ' + percent + '%' , x + y , y *1.25  );

    ctx.beginPath();
    ctx.lineWidth = 2;
    ctx.moveTo(x + y * 0.85 -1 ,y*1.05);
    ctx.lineTo(x + y -5 , y *1.25 + 5);
    ctx.lineTo(x + y + 120 , y *1.25 + 5);
    ctx.stroke();

    ctx.fillStyle = '#AFA';
    ctx.strokeStyle = '#AFA';
    ctx.lineWidth = 0.75;
    ctx.strokeText(name2 + ' ' + (100 - percent) + '%' , x + y , y *0.7  );

    ctx.beginPath();
    ctx.lineWidth = 2;
    ctx.moveTo(x + y * 0.85 -1  ,y*0.95);
    ctx.lineTo(x + y -5 , y *0.75 + 5);
    ctx.lineTo(x + y + 120 , y *0.75 + 5);
    ctx.stroke();
}

function drawLines(interest , principal, amount){
  var canvas = document.getElementById("line");
  var x = canvas.width;
  var y = canvas.height;
  var ctx = canvas.getContext("2d");
  ctx.strokeStyle = '#FFF';
  ctx.fillStyle = '#FFF';
  ctx.lineWidth = 0.5;
  var m = 15;
  ctx.strokeRect(m ,m , x-m-m , y-m-m);
  var stepx = (x-m-m) / (amount.length - 1);

  ctx.beginPath();
  var max = 0;
  for (var i = 0; i < amount.length; i++){
    max = Math.max(max , amount[i] );
    if(i > 0){
      ctx.moveTo(m + (stepx*i), m );
      ctx.lineTo(m + (stepx*i), y-m);
    }
  }
  ctx.stroke();

  ctx.lineWidth = 1;

  ctx.strokeStyle = '#F33';
  drawLine(ctx , max , interest , x,y,m );
  ctx.strokeText('Interest' , m , y-2 );


  ctx.strokeStyle = '#AFA';
  drawLine(ctx , max , principal , x,y,m );
  ctx.strokeText('Principal' , m + 50 , y-2 );

  ctx.strokeStyle = '#AAF';
  drawLine(ctx , max , amount , x,y,m );
  ctx.strokeText('Payment' , m + 100, y-2 );
}

function drawLine(ctx , max , line , x , y , m ){
  var s = ctx.lineWidth;
  ctx.lineWidth = 3;
  var h = y -(m*2); var w = x -(m*2); var step = w / (line.length - 1);
  ctx.beginPath();
  ctx.moveTo(m, h - (line[0]*100/max) + m);
  for (var i = 1; i < line.length; i++){
    ctx.lineTo(step * i + m, h - (line[i]*100/max) + m);
  }
  ctx.stroke();
  ctx.lineWidth = s;
}


//  --------------
function myDrawPie( title , amount , amountLbl , interest , interestLbl){
    var pie = new RGraph.Pie('pie', [amount ,interest]); // Create the pie object
    pie.Set('chart.labels', [amountLbl, interestLbl]);
    pie.Set('chart.gutter', 45);
    pie.Set('chart.highlight.style', '3d'); // Defaults to 3d anyway; can be 2d or 3d
    pie.Set('chart.linewidth', 2);
    pie.Set('chart.labels.sticks', true);
    pie.Set('chart.strokestyle', '#CCC');
    pie.Set('chart.colors', ['#009933','#ff3300']);
    pie.Set('chart.text.color', '#F26A00');
    pie.Set('chart.title.color', '#F26A00');
    pie.Set('chart.title',title);
    pie.Draw();
}

function myDrawLine(principalData , interestData , paymentData ,xLabels){
    var line = new RGraph.Line("line", [principalData,interestData,paymentData]);
    line.Set('chart.background.barcolor1', 'black');
    line.Set('chart.background.barcolor2', 'black');
    line.Set('chart.background.grid.color', '#777');
    line.Set('chart.background.grid.width', 0.5);
    line.Set('chart.colors', ['rgba(255,0,0,1)','rgba(0,255,0,1)','rgba(0,0,255,1)']);
    line.Set('chart.linewidth', 3);
    //line.Set('chart.filled', true);
    line.Set('chart.hmargin', 5);
    line.Set('chart.labels', xLabels);
    line.Set('chart.gutter', 20);
    line.Set('chart.text.size', 8);
    line.Set('chart.text.color','#F26A00');
    line.Draw();
}

function myDrayAll(){
    try{
        log('3');
        myDrawPie(
            window.schedule.getPieTitle(),
            window.schedule.getLoanAmount(),
            window.schedule.getLoanAmountLabel(),
            window.schedule.getLoanInterest(),
            window.schedule.getLoanInterestLabel()
        );
        log('4');
        /*myDrawLine(
            window.schedule.getPrincipalPointsData(),
            window.schedule.getInterestPointsData(),
            window.schedule.getPaymentPointsData(),
            window.schedule.getXLabels()
        ); */
    }catch( err ){
        log(err.description);
    }

}

window.onload = function (){
  log('2');
 myDrayAll();
};



function log(s){
 console.info(s);
}




//  --------------
function myDrawPie( title , amount , amountLbl , interest , interestLbl){
    var pie = new RGraph.Pie('pie', [amount ,interest]);
    pie.Set('chart.key', [amountLbl, interestLbl]);
    pie.Set('chart.key.shadow', true);
    pie.Set('chart.gutter', 15);
    pie.Set('chart.highlight.style', '3d');
    pie.Set('chart.linewidth', 2);
    pie.Set('chart.shadow', true);
    pie.Set('chart.strokestyle', '#FFF');
    pie.Set('chart.colors', ['#009933','#ff3300']);
    pie.Set('chart.title',title);
    pie.Draw();
}

function myDrawLine(principalData , interestData , paymentData ,xLabels , legend){
    var line = new RGraph.Line("line", [principalData,interestData,paymentData]);
    line.Set('chart.background.grid.width', 0.5);
    line.Set('chart.colors', ['rgba(0,255,0,1)','rgba(255,0,0,1)','rgba(0,0,255,1)']);
    line.Set('chart.linewidth', 3);
    line.Set('chart.hmargin', 2);
    line.Set('chart.labels', xLabels);
    line.Set('chart.gutter', 40);
    line.Set('chart.text.size', 8);
    line.Set('chart.key', legend);
    line.Set('chart.key.shadow', true);
    line.Draw();
}


function myDrayAll(){
    try{
        myDrawPie(
            window.schedule.getPieTitle(),
            window.schedule.getLoanAmount(),
            window.schedule.getLoanAmountLabel(),
            window.schedule.getLoanInterest(),
            window.schedule.getLoanInterestLabel()
        );
        myDrawLine(
            JSON.parse(window.schedule.getPrincipalPointsData()),
            JSON.parse(window.schedule.getInterestPointsData()),
            JSON.parse(window.schedule.getPaymentPointsData()),
            JSON.parse(window.schedule.getXLabels()),
            JSON.parse(window.schedule.getLegend())
        );
    }catch( err ){
    }

}

window.onload = function (){
 myDrayAll();
};




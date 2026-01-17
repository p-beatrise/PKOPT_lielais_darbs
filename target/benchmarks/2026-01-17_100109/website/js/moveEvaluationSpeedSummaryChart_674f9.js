
var chart_moveEvaluationSpeedSummaryChart_674f9 = new Chart(document.getElementById('chart_moveEvaluationSpeedSummaryChart_674f9'), {
    type: 'line',
    data: {
        datasets: [
            {
                  label: 'Late Acceptance',
                    borderWidth: 1
                  ,
                  data: [
                    {x: 6290730, y: 31546}, {x: 97577250, y: 29721}, {x: 356783701, y: 28933}
                  ]
                }, 
{
                  label: 'Tabu Search (favorite)',
                    borderWidth: 4
,
                  data: [
                    {x: 6290730, y: 25947}, {x: 97577250, y: 29608}, {x: 356783701, y: 27586}
                  ]
                }
        ]
    },
    options: {
        animation: false,
        responsive: true,
        maintainAspectRatio: false,
        spanGaps: true,
        plugins: {
            title: {
                display: true,
                text: 'Move evaluation speed summary (higher is better)'
            },
            tooltip: {
                callbacks: {
                }
            }
        },
        scales: {
            x: {
                title: {
                    display: true,
                    text: 'Problem scale'
                },
                ticks: {
                        stepSize: 10000000
                        
                },
                suggestedMin: 0,
                suggestedMax: 356783701,
                type: 'linear',
                display: true
            },
            y: {
                title: {
                    display: true,
                    text: 'Move evaluation speed per second'
                },
                ticks: {
                        stepSize: 100
                        
                },
                type: 'linear',
                display: true
            }
        },
watermark: {
    image: "website/img/timefold-logo-stacked-positive.svg",
    x: 15,
    y: 15,
    width: 48,
    height: 50,
    opacity: 0.1,
    alignX: "right",
    alignY: "bottom",
    alignToChartArea: true,
    position: "front",
}    },
plugins: [{ 
    id: 'customPlugin',
    beforeDraw: (chart, args, options) => {
          const ctx = chart.canvas.getContext('2d');
          ctx.save();
          ctx.globalCompositeOperation = 'destination-over';
          ctx.fillStyle = 'white';
          ctx.fillRect(0, 0, chart.canvas.width, chart.canvas.height);
          ctx.restore();
    }
}]
});

window.addEventListener('beforeprint', () => {
  chart_moveEvaluationSpeedSummaryChart_674f9.resize(1280, 720);
});
window.addEventListener('afterprint', () => {
  chart_moveEvaluationSpeedSummaryChart_674f9.resize();
});

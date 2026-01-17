
var chart_bestScoreProblemStatisticChart1_9f8f7 = new Chart(document.getElementById('chart_bestScoreProblemStatisticChart1_9f8f7'), {
    type: 'line',
    data: {
        datasets: [
            {
                  label: 'Late Acceptance',
                        stepped: true,
                        pointRadius: 0,
                    borderWidth: 1
                  ,
                  data: [
                    {x: 574, y: 175.13152}, {x: 897, y: 175.34145}, {x: 1046, y: 176.93027}, {x: 1120, y: 177.09333}, {x: 1356, y: 177.23967}, {x: 1383, y: 177.38924}, {x: 1384, y: 177.54228}, {x: 1707, y: 177.89432}, {x: 1876, y: 177.92225}, {x: 2748, y: 178.95003}, {x: 4918, y: 180.27328}, {x: 8155, y: 180.43363}, {x: 10158, y: 180.70274}, {x: 10977, y: 180.98217}, {x: 11990, y: 181.41501}, {x: 12112, y: 181.53229}, {x: 13064, y: 182.83729}, {x: 17055, y: 184.04701}, {x: 18020, y: 183.67834}, {x: 19338, y: 184.14493}, {x: 19344, y: 184.40381}, {x: 21910, y: 184.53661}, {x: 21912, y: 184.67183}, {x: 21913, y: 184.95003}, {x: 24205, y: 185.09333}, {x: 25328, y: 185.23967}, {x: 25332, y: 185.54228}, {x: 28247, y: 185.85979}, {x: 28899, y: 186.02487}, {x: 30005, y: 186.02487}
                  ]
                }, 
{
                  label: 'Tabu Search (favorite)',
                        stepped: true,
                        pointRadius: 0,
                    borderWidth: 4
,
                  data: [
                    {x: 596, y: 175.13152}, {x: 1252, y: 175.34145}, {x: 1617, y: 175.56129}, {x: 1972, y: 175.79261}, {x: 2473, y: 177.79261}, {x: 2929, y: 178.03741}, {x: 3099, y: 178.29836}, {x: 3145, y: 179.79261}, {x: 4820, y: 180.03741}, {x: 7466, y: 180.29836}, {x: 9342, y: 180.57916}, {x: 10019, y: 182.29836}, {x: 10068, y: 182.57916}, {x: 28726, y: 182.97506}, {x: 28761, y: 183.39023}, {x: 28796, y: 183.84669}, {x: 28871, y: 184.09488}, {x: 28950, y: 184.35995}, {x: 28989, y: 184.6459}, {x: 30004, y: 184.6459}
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
                text: 'Problem_2 best soft score statistic'
            },
            tooltip: {
                callbacks: {
                        title: function(context) {
                            return humanizeTime(context[0].parsed.x);
                        }
                        
                }
            }
        },
        scales: {
            x: {
                title: {
                    display: true,
                    text: 'Time spent'
                },
                ticks: {
                        stepSize: 100
                        ,
                        callback: function(value, index) {
                            return humanizeTime(value);
                        }
                },
                suggestedMin: 0,
                suggestedMax: 30005,
                type: 'linear',
                display: true
            },
            y: {
                title: {
                    display: true,
                    text: 'Best soft score'
                },
                ticks: {
                        stepSize: 1
                        
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
  chart_bestScoreProblemStatisticChart1_9f8f7.resize(1280, 720);
});
window.addEventListener('afterprint', () => {
  chart_bestScoreProblemStatisticChart1_9f8f7.resize();
});

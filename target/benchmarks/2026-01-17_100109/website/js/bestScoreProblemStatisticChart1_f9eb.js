
var chart_bestScoreProblemStatisticChart1_f9eb = new Chart(document.getElementById('chart_bestScoreProblemStatisticChart1_f9eb'), {
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
                    {x: 116, y: 43.36529}, {x: 137, y: 43.54564}, {x: 147, y: 43.73217}, {x: 169, y: 45.36529}, {x: 235, y: 46.08986}, {x: 271, y: 46.23613}, {x: 296, y: 45.19052}, {x: 426, y: -34.78519}, {x: 429, y: -32.88686}, {x: 536, y: -32.85812}, {x: 639, y: -5.61438}, {x: 1095, y: -2.98749}, {x: 1096, y: -2.57862}, {x: 1097, y: 2.1824}, {x: 1375, y: 13.94663}, {x: 1738, y: -22.97913}, {x: 1776, y: -21.61438}, {x: 1777, y: -20.46659}, {x: 1952, y: -0.05337}, {x: 1956, y: 1.80625}, {x: 1961, y: 3.66856}, {x: 2570, y: 16.33631}, {x: 2573, y: 17.73217}, {x: 2597, y: -15.30476}, {x: 4037, y: -8.33144}, {x: 4464, y: 22.1824}, {x: 5804, y: 22.64835}, {x: 11117, y: 23.40066}, {x: 11831, y: 23.95843}, {x: 11833, y: 25.73987}, {x: 12825, y: 27.27019}, {x: 12826, y: 27.40066}, {x: 13221, y: 27.45365}, {x: 13757, y: 29.53341}, {x: 14532, y: 29.95843}, {x: 14533, y: 31.95843}, {x: 15000, y: 32.06971}, {x: 16314, y: 32.89136}, {x: 18649, y: 34.89136}, {x: 18662, y: 33.80625}, {x: 18685, y: 33.94663}, {x: 18780, y: 35.40066}, {x: 18907, y: 37.14188}, {x: 19274, y: 34.69524}, {x: 19275, y: 34.8559}, {x: 19405, y: 38.64835}, {x: 19406, y: 38.76896}, {x: 19444, y: 40.52946}, {x: 23516, y: 41.53341}, {x: 26428, y: 42.29655}, {x: 29040, y: 42.64835}, {x: 29287, y: 44.1824}, {x: 30004, y: 44.1824}
                  ]
                }, 
{
                  label: 'Tabu Search (favorite)',
                        stepped: true,
                        pointRadius: 0,
                    borderWidth: 4
,
                  data: [
                    {x: 75, y: 43.36529}, {x: 175, y: 43.54564}, {x: 267, y: 45.19052}, {x: 365, y: 45.19052}, {x: 462, y: 45.36529}, {x: 556, y: 41.36529}, {x: 750, y: 41.54564}, {x: 905, y: 41.73217}, {x: 952, y: 43.36529}, {x: 1977, y: 43.54564}, {x: 2023, y: 43.73217}, {x: 2453, y: 45.36529}, {x: 2738, y: 45.54564}, {x: 2822, y: 45.73217}, {x: 4030, y: 47.73217}, {x: 4115, y: 48.1266}, {x: 4692, y: 44.1266}, {x: 4750, y: 45.73217}, {x: 4898, y: 47.73217}, {x: 10033, y: 49.36529}, {x: 11527, y: 49.54564}, {x: 11608, y: 50.8559}, {x: 30005, y: 50.8559}
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
                text: 'Problem_1 best soft score statistic'
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
  chart_bestScoreProblemStatisticChart1_f9eb.resize(1280, 720);
});
window.addEventListener('afterprint', () => {
  chart_bestScoreProblemStatisticChart1_f9eb.resize();
});

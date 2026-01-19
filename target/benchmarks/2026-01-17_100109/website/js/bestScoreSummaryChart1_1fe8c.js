
var chart_bestScoreSummaryChart1_1fe8c = new Chart(document.getElementById('chart_bestScoreSummaryChart1_1fe8c'), {
    type: 'bar',
    data: {
        labels: [
            'Problem_0', 'Problem_1', 'Problem_2'
        ],
        datasets: [
            {
                  label: 'Late Acceptance',
                  grouped: true,
                    borderWidth: 1
                  ,
                  data: [
                    2.133975, 44.76673, 185.99485
                  ]
                }, 
{
                  label: 'Tabu Search (favorite)',
                  grouped: true,
                    borderWidth: 4
,
                  data: [
                    2.133975, 50.8559, 185.31256
                  ]
                }
        ]
    },
    options: {
        animation: false,
        responsive: true,
        maintainAspectRatio: false,
        resizeDelay: 100,
        spanGaps: true,
        plugins: {
            title: {
                display: true,
                text: 'Best soft score summary (higher is better)'
            }
        },
        scales: {
            x: {
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
  chart_bestScoreSummaryChart1_1fe8c.resize(1280, 720);
});
window.addEventListener('afterprint', () => {
  chart_bestScoreSummaryChart1_1fe8c.resize();
});